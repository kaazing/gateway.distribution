/**
 * Copyright (c) 2007-2012, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.mina.netty.bootstrap;

class ConnectionlessServerBootstrapFactory implements ServerBootstrapFactory {

    @Override
    public ConnectionlessServerBootstrap createBootstrap() {
        return new ConnectionlessServerBootstrap();
    }

}
