/**
 * Copyright [2020] [https://github.com/betler]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author betler
 *
 */
package pro.cvitae.gmailrelayer.gnullsmtp;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.Validate;
import org.apache.james.protocols.api.handler.ProtocolHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author betler
 *
 */
@Configuration
public class SmtpServerConfiguration {

    @Value("${relayer.smtp.server.port}")
    private Integer port;

    @Value("${relayer.smtp.server.delay}")
    private Long delay;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SmtpServer smtpServer() {

        Validate.inclusiveBetween(1L, 65535L, this.port);

        final Collection<ProtocolHandler> handlers = new ArrayList<>();
        handlers.add(new MessageReceivedHook(this.delay));
        return new SmtpServer(this.port, handlers);
    }

}
