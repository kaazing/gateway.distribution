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

package org.kaazing.gateway.transport.wsn.httpx;

import static org.kaazing.test.util.ITUtil.createRuleChain;

import java.net.URI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.kaazing.gateway.server.test.GatewayRule;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.builder.GatewayConfigurationBuilder;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class ExtendedHandshakeIT {

    private final K3poRule robot = new K3poRule().setScriptRoot("org/kaazing/specification/httpx/extended");

    public GatewayRule gateway = new GatewayRule() {
        {

            GatewayConfiguration configuration = new GatewayConfigurationBuilder()
                    .service()
                        .accept(URI.create("ws://localhost:8000/echo"))
                        .type("echo")
                        .crossOrigin()
                            .allowOrigin("*")
                        .done()
                    .done()
                    .service()
                        .accept(URI.create("ws://localhost:8000/echoauth"))
                        .type("echo")
                        .realmName("demo1")
                        .authorization()
                            .requireRole("AUTHORIZED")
                        .done()
                        .crossOrigin()
                            .allowOrigin("*")
                        .done()
                    .done()
                    .security()
                        .realm()
                            .name("demo1")
                            .description("Kaazing Gateway Demo")
                            .httpChallengeScheme("Application Basic")
                            .authorizationMode("challenge")
                            .loginModule()
                                 .type("file")
                                 .success("required")
                                 .option("file", "src/test/resources/gateway/conf/jaas-config.xml")
                            .done()
                        .done()
                    .done()
                .done();
            init(configuration);
        }
    };

    @Rule
    public TestRule chain = createRuleChain(gateway, robot);

    @Test
    @Specification("connection.established.with.authorization/request")
    public void shouldEstablishingConnectionWithAuthorization() throws Exception {
        robot.finish();
    }

    @Test
    @Specification("connection.established/request")
    public void shouldEstablishConnection() throws Exception {
        robot.finish();
    }

    @Test
    @Specification("client.sends.message.between.opening.and.extended.handshake/request")
    public void shouldFailWhenClientSendsMessageBetweenOpeningAndExtendedHandshake() throws Exception {
        robot.finish();
    }

    @Test
    @Specification("extended.handshake.response.code.101/request")
    public void shouldPassWhenWebSocketProtocolGets101StatusCode() throws Exception {
        robot.finish();
    }

    @Test
    @Specification("extended.handshake.response.code.401/request")
    public void shouldPassWhenWebSocketProtocolGets401StatusCode() throws Exception {
        robot.finish();
    }

}
