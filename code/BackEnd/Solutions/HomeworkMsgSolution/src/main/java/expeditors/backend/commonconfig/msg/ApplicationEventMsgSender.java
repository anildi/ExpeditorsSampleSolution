package expeditors.backend.commonconfig.msg;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author whynot
 */

@Component
@Profile({"springevents"})
public class ApplicationEventMsgSender implements MessageSender{

    private final ApplicationEventPublisher publisher;

    public ApplicationEventMsgSender(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void sendMessage(Object message) {
        publisher.publishEvent(new CustomerCreatedEvent(message));
    }
}
