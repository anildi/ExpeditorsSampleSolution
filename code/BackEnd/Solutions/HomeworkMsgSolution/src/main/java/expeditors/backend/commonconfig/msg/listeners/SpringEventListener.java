package expeditors.backend.commonconfig.msg.listeners;

import expeditors.backend.commonconfig.msg.CustomerCreatedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("springevents")
public class SpringEventListener
{
    @EventListener
//    @Async
    public void handleCustomerCreatedEvent(CustomerCreatedEvent event) {
        System.out.println("Handle Customer Created: " + event + " in " + Thread.currentThread());
    }
}
