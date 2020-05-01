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

import java.net.InetSocketAddress;
import java.util.Collection;

import org.apache.james.metrics.api.NoopMetricFactory;
import org.apache.james.protocols.api.Protocol;
import org.apache.james.protocols.api.handler.ProtocolHandler;
import org.apache.james.protocols.netty.NettyServer;
import org.apache.james.protocols.smtp.SMTPConfigurationImpl;
import org.apache.james.protocols.smtp.SMTPProtocol;
import org.apache.james.protocols.smtp.SMTPProtocolHandlerChain;
import org.jboss.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author betler
 *
 */
public class SmtpServer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Collection<ProtocolHandler> handlers;

	private NettyServer server;

	/**
	 * Listening port.
	 */
	private final Integer port;

	public SmtpServer(final Integer port, final Collection<ProtocolHandler> handlers) {
		this.handlers = handlers;
		this.port = port;
	}

	public void start() throws Exception {
		final SMTPConfigurationImpl smtpConfiguration = new SMTPConfigurationImpl();
		smtpConfiguration.setSoftwareName("Spring Boot g-mail-relayer SMTP Server");
		smtpConfiguration.setUseAddressBracketsEnforcement(false);

		final SMTPProtocolHandlerChain chain = new SMTPProtocolHandlerChain(new NoopMetricFactory());
		chain.addAll(this.handlers);
		chain.wireExtensibleHandlers();

		final Protocol protocol = new SMTPProtocol(chain, smtpConfiguration);

		this.server = new NettyServer.Factory(new HashedWheelTimer()).protocol(protocol).build();
		this.server.setListenAddresses(new InetSocketAddress(this.port));
		this.server.setTimeout(120);
		this.server.bind();
		this.logger.info("SMTP Server started on port {}", this.port);
	}

	public void stop() {
		this.server.unbind();
		this.logger.info("SMTP Server stopped");
	}
}
