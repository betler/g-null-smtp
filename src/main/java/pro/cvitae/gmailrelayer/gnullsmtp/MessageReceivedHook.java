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

import java.util.stream.Collectors;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.james.core.MailAddress;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.MessageHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author betler
 *
 */
public class MessageReceivedHook implements MessageHook {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Long delay;

    public MessageReceivedHook(final Long delay) {
        this.delay = delay;
    }

    @Override
    public void init(final Configuration config) throws ConfigurationException {
        // Do nothing. It is not invoked, anyway
    }

    @Override
    public void destroy() {
        // Do nothing
    }

    /**
     * Implements the {@link MessageHook#onMessage(SMTPSession, MailEnvelope)}
     * method. Parses incoming message and relays to the configured server.
     */
    @Override
    public HookResult onMessage(final SMTPSession session, final MailEnvelope mail) {

        if (this.delay > 0) {
            try {
                Thread.sleep(this.delay);
            } catch (final InterruptedException e) {
                this.logger.error("Could not set delay", e);
                Thread.currentThread().interrupt();
            }
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Received message from {} to {}", mail.getMaybeSender().asPrettyString(),
                    mail.getRecipients().stream().map(MailAddress::asPrettyString).collect(Collectors.joining(", ")));
        }
        // Everything OK
        return HookResult.OK;
    }

}
