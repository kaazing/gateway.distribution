/**
 * Copyright (c) 2007-2013, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.mina.netty.socket.nio;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.socket.DatagramChannelConfig;
import org.jboss.netty.channel.socket.nio.NioDatagramChannel;
import org.jboss.netty.channel.socket.nio.NioDatagramWorker;

import com.kaazing.mina.core.service.IoProcessorEx;
import com.kaazing.mina.netty.ChannelIoService;
import com.kaazing.mina.netty.ChannelIoSession;
import com.kaazing.mina.netty.socket.DefaultDatagramChannelIoSessionConfig;
import com.kaazing.mina.netty.util.threadlocal.VicariousThreadLocal;

/**
 * This session is always used in conjunction with an NioDatagramChannel, which necessarily has an associated worker.
 * It forces all operations of the session to be done in the worker thread (using worker.executeIntoThread if a call
 * is made in another thread).
 */
public class NioDatagramChannelIoSession extends ChannelIoSession<DatagramChannelConfig> {

    private static final ThreadLocal<WorkerExecutor> WORKER_EXECUTOR = new VicariousThreadLocal<WorkerExecutor>();

    public NioDatagramChannelIoSession(ChannelIoService service,
            IoProcessorEx<ChannelIoSession<? extends ChannelConfig>> processor, NioDatagramChannel channel) {
        super(service, processor, channel, new DefaultDatagramChannelIoSessionConfig(),
                currentThread(), asExecutor(channel.getWorker()));
    }

    private static Executor asExecutor(NioDatagramWorker worker) {
        assert isInIoThread(worker) : "Session created from non-I/O thread";
        WorkerExecutor executor = WORKER_EXECUTOR.get();
        if (executor == null) {
            executor = new WorkerExecutor(worker);
            WORKER_EXECUTOR.set(executor);
        }
        assert executor.worker == worker : "Worker does not match I/O thread";
        return executor;
    }

    private static boolean isInIoThread(NioDatagramWorker worker) {
        final Thread[] ioThread = new Thread[]{null};
        worker.executeInIoThread(new Runnable() {
            @Override
            public void run() {
                ioThread[0] = currentThread();
            }
        });
        boolean aligned = ioThread[0] == currentThread();
        assert aligned : format("Current thread %s does not match I/O thread %s", currentThread(), ioThread[0]);
        return aligned;
    }

    private static final class WorkerExecutor implements Executor {
        private final NioDatagramWorker worker;

        WorkerExecutor(NioDatagramWorker worker) {
            this.worker = worker;
        }

        @Override
        public void execute(Runnable command) {
            worker.executeInIoThread(command, /* alwaysAsync */ true);
        }
    }

}
