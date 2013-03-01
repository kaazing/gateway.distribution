/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.mina.core.service;

import static org.junit.Assert.*;

import java.util.concurrent.Executor;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import com.kaazing.mina.core.session.IoSessionEx;

public class AbstractIoProcessorTest {
    private static final Thread TEST_THREAD = new Thread();

    @Test
    public void add_shouldExecuteImmediatelyIfInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(Thread.currentThread()));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.add(session);
        context.assertIsSatisfied();
        assertEquals("add0", ((TestIoProcessor<?>)processor).called);
    }
    
    @Test
    public void add_shouldExecuteAsynchronouslyIfNotInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        final Executor executor = context.mock(Executor.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(TEST_THREAD));
            oneOf(session).getIoExecutor(); will(returnValue(executor));
            oneOf(executor).execute(with(any(Runnable.class)));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.add(session);
        context.assertIsSatisfied();
        assertNull(((TestIoProcessor<?>)processor).called);
    }

    @Test
    public void flush_shouldExecuteImmediatelyIfInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(Thread.currentThread()));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.flush(session);
        context.assertIsSatisfied();
        assertEquals("flush0", ((TestIoProcessor<?>)processor).called);
    }
    
    @Test
    public void flush_shouldExecuteAsynchronouslyIfNotInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        final Executor executor = context.mock(Executor.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(TEST_THREAD));
            oneOf(session).getIoExecutor(); will(returnValue(executor));
            oneOf(executor).execute(with(any(Runnable.class)));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.flush(session);
        context.assertIsSatisfied();
        assertNull(((TestIoProcessor<?>)processor).called);
    }

    @Test
    public void remove_shouldExecuteImmediatelyIfInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(Thread.currentThread()));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.remove(session);
        context.assertIsSatisfied();
        assertEquals("remove0", ((TestIoProcessor<?>)processor).called);
    }
    
    @Test
    public void remove_shouldExecuteAsynchronouslyIfNotInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        final Executor executor = context.mock(Executor.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(TEST_THREAD));
            oneOf(session).getIoExecutor(); will(returnValue(executor));
            oneOf(executor).execute(with(any(Runnable.class)));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.remove(session);
        context.assertIsSatisfied();
        assertNull(((TestIoProcessor<?>)processor).called);
    }

    @Test
    public void updateTrafficControl_shouldExecuteImmediatelyIfInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(Thread.currentThread()));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.updateTrafficControl(session);
        context.assertIsSatisfied();
        assertEquals("updateTrafficControl0", ((TestIoProcessor<?>)processor).called);
    }
    
    @Test
    public void updateTrafficControl_shouldExecuteAsynchronouslyIfNotInIoThread() throws Exception{
        Mockery context = new Mockery();
        final IoSessionEx session = context.mock(IoSessionEx.class);
        final Executor executor = context.mock(Executor.class);
        
        context.checking(new Expectations() {{
            oneOf(session).getIoThread(); will(returnValue(TEST_THREAD));
            oneOf(session).getIoExecutor(); will(returnValue(executor));
            oneOf(executor).execute(with(any(Runnable.class)));
        }});
        
        AbstractIoProcessor<IoSessionEx> processor = new TestIoProcessor<IoSessionEx>();
        processor.updateTrafficControl(session);
        context.assertIsSatisfied();
        assertNull(((TestIoProcessor<?>)processor).called);
    }

    private static class TestIoProcessor<T extends IoSessionEx> extends AbstractIoProcessor<IoSessionEx> {
        String called = null;

        @Override
        public boolean isDisposing() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isDisposed() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void dispose() {
            // TODO Auto-generated method stub
            
        }

        @Override
        protected void add0(IoSessionEx session) {
            called = "add0";
        }

        @Override
        protected void flush0(IoSessionEx session) {
            called = "flush0";
        }

        @Override
        protected void updateTrafficControl0(IoSessionEx session) {
            called = "updateTrafficControl0";            
        }

        @Override
        protected void remove0(IoSessionEx session) {
            called = "remove0";         
        }
        
    }

}
