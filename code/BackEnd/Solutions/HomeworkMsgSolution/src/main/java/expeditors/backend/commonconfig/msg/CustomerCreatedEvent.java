package expeditors.backend.commonconfig.msg;

import org.springframework.context.ApplicationEvent;

/**
 * @author whynot
 */
public class CustomerCreatedEvent extends ApplicationEvent {

    public CustomerCreatedEvent(Object source) {
        super(source);
    }
}
