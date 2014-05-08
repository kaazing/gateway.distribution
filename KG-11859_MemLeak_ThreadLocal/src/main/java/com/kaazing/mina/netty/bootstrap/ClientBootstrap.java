/**
 * Copyright (c) 2007-2013, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.mina.netty.bootstrap;

import java.net.SocketAddress;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;

public interface ClientBootstrap extends Bootstrap {

    void setFactory(ChannelFactory factory);
    ChannelFactory getFactory();

    void setPipeline(ChannelPipeline pipeline);
    ChannelPipeline getPipeline();

    ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress);
}
