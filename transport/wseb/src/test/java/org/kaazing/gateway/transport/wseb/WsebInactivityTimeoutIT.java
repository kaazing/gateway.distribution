/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.gateway.transport.wseb;

import static org.kaazing.test.util.ITUtil.createRuleChain;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.kaazing.gateway.server.test.GatewayRule;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.builder.GatewayConfigurationBuilder;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class WsebInactivityTimeoutIT {

    private final K3poRule k3po = new K3poRule();

    private final GatewayRule gateway = new GatewayRule() {
        {
            // @formatter:off
            GatewayConfiguration configuration =
                    new GatewayConfigurationBuilder()
                        .webRootDirectory(new File("src/test/webapp"))
                        .tempDirectory(new File("src/test/temp"))
                        .service()
                            .accept("wse://localhost:8123/echo")
                            .acceptOption("ws.inactivity.timeout", "2sec")
                            .type("echo")
                        .done()
                    .done();
            // @formatter:on
            init(configuration);
        }
    };

    @Rule
    public TestRule chain = createRuleChain(gateway, k3po);

    @Specification("echo.inactivity.timeout.should.close")
    @Test
    public void testEchoInactiveTimeoutShouldCloseConnection() throws Exception {
        k3po.finish();
    }

    @Specification("echo.inactivity.timeout.should.not.ping.old.client")
    @Test
    public void testEchoInactiveTimeoutShouldNotPingOldClient() throws Exception {
        k3po.finish();
    }

    @Test
    @Specification("ping.on.longpolling.request")
    public void shouldReceivePingOnLongPollingRequest() throws Exception {
        k3po.finish();
    }

}
