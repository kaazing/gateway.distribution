/**
 * Copyright (c) 2007-2013, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.mina.core.filterchain;

import java.util.concurrent.Executor;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.kaazing.mina.core.session.AbstractIoSessionEx;

/**
 * Extended version of DefaultIoFilterChain to add support for thread alignment. Every method is
 * executed explicitly on the IoSession's I/O worker thread that is not the current thread.
 */
public class DefaultIoFilterChainEx extends DefaultIoFilterChain {

    private final Thread ioThread;
    private final Executor ioExecutor;

    public DefaultIoFilterChainEx(AbstractIoSessionEx session) {
        super(session);
        ioThread = session.getIoThread();
        ioExecutor = session.getIoExecutor();

        // conditionally add alignment checking filter if assert is enabled
        if (AssertAlignedFilter.isAssertEnabled()) {
            addFirst("assert thread aligned", new AssertAlignedFilter(session));
        }
    }

    @Override
    protected final void callNextSessionCreated(final Entry entry, final IoSession session) {
        if (aligned()) {
            super.callNextSessionCreated(entry, session);
        }
        else {
            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callNextSessionCreated(entry, session);
                }
            });
        }
    }

    @Override
    protected final void callNextSessionOpened(final Entry entry, final IoSession session) {
        if (aligned()) {
            super.callNextSessionOpened(entry, session);
        }
        else {
            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callNextSessionOpened(entry, session);
                }
            });
        }
    }

    @Override
    protected final void callNextSessionClosed(final Entry entry, final IoSession session) {
        if (aligned()) {
            super.callNextSessionClosed(entry, session);
        }
        else {
            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callNextSessionClosed(entry, session);
                }
            });
        }
    }

    @Override
    protected final void callNextSessionIdle(final Entry entry, final IoSession session, final IdleStatus status) {
        if (aligned()) {
            super.callNextSessionIdle(entry, session, status);
        }
        else {
            execute(new CallNextSessionIdleCommand(status, entry, session));
        }
    }

    @Override
    protected final void callNextMessageReceived(final Entry entry, final IoSession session, final Object message) {
        if (aligned()) {
            // Note: no suspendRead / resumeRead coordination necessary when thread-aligned
            super.callNextMessageReceived(entry, session, message);
        }
        else {
            // Note: reads will be resumed after completion of scheduled callNextMessageReceived
            session.suspendRead();

            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callNextMessageReceived(entry, session, message);

                    // Note: reads were suspended before scheduling callNextMessageReceived
                    //       if suspendRead was called during callNextNessageReceived
                    //       then calling resumeRead below will not actually resume reads
                    //       due to internal read suspend counter
                    session.resumeRead();
                }
            });
        }
    }

    @Override
    protected final void callNextMessageSent(
            final Entry entry, final IoSession session, final WriteRequest writeRequest) {
        if (aligned()) {
            super.callNextMessageSent(entry, session, writeRequest);
        }
        else {
            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callNextMessageSent(entry, session, writeRequest);
                }
            });
        }
    }

    @Override
    protected final void callNextExceptionCaught(final Entry entry, final IoSession session, final Throwable cause) {
        if (aligned()) {
            super.callNextExceptionCaught(entry, session, cause);
        }
        else {
            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callNextExceptionCaught(entry, session, cause);
                }
            });
        }
    }

    @Override
    protected final void callPreviousFilterWrite(
            final Entry entry, final IoSession session, final WriteRequest writeRequest) {
        if (aligned()) {
            super.callPreviousFilterWrite(entry, session, writeRequest);
        }
        else {
            final Entry entry0 = entry;
            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callPreviousFilterWrite(entry0, session, writeRequest);
                }
            });
        }
    }

    @Override
    protected final void callPreviousFilterClose(final Entry entry, final IoSession session) {
        if (aligned()) {
            super.callPreviousFilterClose(entry, session);
        }
        else {
            execute(new Runnable() {
                @Override
                public void run() {
                    DefaultIoFilterChainEx.super.callPreviousFilterClose(entry, session);
                }
            });
        }
    }

    private boolean aligned() {
        return Thread.currentThread() == ioThread;
    }

    private void execute(Runnable command) {
        ioExecutor.execute(command);
    }

    // allow detection of legitimate non-IoThread commands
    public final class CallNextSessionIdleCommand implements Runnable {
        private final IdleStatus status;
        private final Entry entry;
        private final IoSession session;

        public CallNextSessionIdleCommand(IdleStatus status, Entry entry,
                IoSession session) {
            this.status = status;
            this.entry = entry;
            this.session = session;
        }

        @Override
        public void run() {
            DefaultIoFilterChainEx.super.callNextSessionIdle(entry, session, status);
        }
    }

}
