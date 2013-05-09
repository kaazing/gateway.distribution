/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.mina.core.session;

import java.util.concurrent.Executor;

import org.apache.mina.core.session.IoSession;

import com.kaazing.mina.core.service.IoServiceEx;

/**
 * Extended version of IoSession to add support for thread alignment.
*/
public interface IoSessionEx extends IoSession, IoAlignment {

    Executor IMMEDIATE_EXECUTOR = new Executor() {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    };

    /**
     * Returns the IO worker thread in which all filters on the filter chain for this session will be executed
     */
    Thread getIoThread();

    /**
     * Returns an Executor which can be used to execute tasks in the IO worker thread that owns this session.
     * This executor delegates to the executeInIoThread method of the corresponding Netty NioWorker, so when invoked
     * from that same thread, the Executor will immediately execute the task, and when invoked from a different thread,
     * the task will be queued for asynchronous (but quasi-immediate) execution in the worker thread.
     */
    Executor getIoExecutor();

    @Override
    IoSessionConfigEx getConfig();

    @Override
    IoServiceEx getService();

    @Override
    boolean isIoAligned();
}
