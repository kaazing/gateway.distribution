/**
 * Copyright (c) 2007-2014, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.mina.core.future;

import java.util.Arrays;

import org.apache.mina.core.future.CompositeIoFuture;
import org.apache.mina.core.future.DefaultIoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;


public class DefaultUnbindFuture extends DefaultIoFuture implements UnbindFuture {
    private static final UnbindFuture SUCCEEDED_FUTURE;

    static {
        SUCCEEDED_FUTURE = new DefaultUnbindFuture();
        SUCCEEDED_FUTURE.setUnbound();
    }

    public DefaultUnbindFuture() {
        super(null);
    }

    public static UnbindFuture succeededFuture() {
        return SUCCEEDED_FUTURE;
    }

    /** Combine futures in a way that minimizes cost(no object creation) for the common case where
     * both have already been fulfilled.
     */
    public static UnbindFuture combineFutures(UnbindFuture future1, UnbindFuture future2) {
        if (future1 == null || future1.isUnbound()) {
            return future2;
        }
        else if (future2 == null || future2.isUnbound()) {
            return future1;
        }
        else {
            return new CompositeUnbindFuture(Arrays.asList(future1, future2));
        }
    }

    @Override
    public boolean isUnbound() {
        if (isDone() && getException() == null) {
            return ((Boolean) getValue()).booleanValue();
        } else {
            return false;
        }
    }

    @Override
    public Throwable getException() {
        return getValueAsThrowable(getValue());
    }

    @Override
    public void setUnbound() {
        setValue(Boolean.TRUE);
    }

    @Override
    public void setException(Throwable exception) {
        if (exception == null) {
            throw new NullPointerException("exception");
        }
        setValue(exception);
    }

    @Override
    public IoSession getSession() {
        // There is no session associated with an unbind operation
        throw new UnsupportedOperationException("UnbindFuture.getSession");
    }

    @Override
    public UnbindFuture await() throws InterruptedException {
        return (UnbindFuture) super.await();
    }

    @Override
    public UnbindFuture awaitUninterruptibly() {
        return (UnbindFuture) super.awaitUninterruptibly();
    }

    @Override
    public UnbindFuture addListener(IoFutureListener<?> listener) {
        return (UnbindFuture) super.addListener(listener);
    }

    @Override
    public UnbindFuture removeListener(IoFutureListener<?> listener) {
        return (UnbindFuture) super.removeListener(listener);
    }

    private static Throwable getValueAsThrowable(Object value) {
        if (value != null && value instanceof Throwable) {
            return (Throwable) value;
        }
        else {
            return null;
        }
    }

    private static class CompositeUnbindFuture extends CompositeIoFuture<UnbindFuture> implements UnbindFuture {

        public CompositeUnbindFuture(Iterable<UnbindFuture> children) {
            super(children);
        }

        @Override
        public boolean isUnbound() {
            if (isDone() && getException() == null) {
                return ((Boolean) getValue()).booleanValue();
            } else {
                return false;
            }
        }

        @Override
        public Throwable getException() {
            return getValueAsThrowable(getValue());
        }

        @Override
        public void setUnbound() {
            setValue(Boolean.TRUE);
        }

        @Override
        public void setException(Throwable exception) {
            if (exception == null) {
                throw new NullPointerException("exception");
            }
            setValue(exception);
        }

    }

}
