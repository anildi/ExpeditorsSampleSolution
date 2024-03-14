package expeditors.backend.commonconfig.msg.listeners;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author whynot
 */
@Component
@Profile("kkjsonevents")
public class SBFKafkaJsonListenerHolder {
    @KafkaListener(id = "adoptapp-json-consumer-group", topics = "${ttl.kafka.adopter.topic}")
    public void listen(ConsumerRecord<String, Object> message,
                       @Payload Object payload) {
        System.err.println("Kafka Json Listener:" + payload);
    }
}
