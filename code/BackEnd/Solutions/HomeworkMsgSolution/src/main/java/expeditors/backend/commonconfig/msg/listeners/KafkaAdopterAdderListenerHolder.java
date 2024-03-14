package expeditors.backend.commonconfig.msg.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.service.AdopterRepoService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author whynot
 */
@Component
@Profile("kkjsonevents | kkavroevents")
public class KafkaAdopterAdderListenerHolder {
    @Autowired
    private AdopterRepoService adopterService;

    @Autowired
    private ObjectMapper mapper;

    @KafkaListener(id = "adoptapp-adopter-adder", topics = "adopter-adder-topic")
    public void listen(ConsumerRecord<String, Object> message,
                       @Payload Object payload) throws JsonProcessingException {
        var obj = message.value();
        switch (obj) {
            case Adopter adopter -> {
                Adopter newAdopter = adopterService.addAdopter(adopter);
                System.err.println(STR."Kafka Adopter Added from Object:\{newAdopter}");
            }
            case String jsonMsg -> {
                Adopter adopter = mapper.readValue(jsonMsg, Adopter.class);
                Adopter newAdopter = adopterService.addAdopter(adopter);
                System.err.println(STR."Kafka Adopter Added from Converted Json:\{newAdopter}");
            }
            default -> {
                System.err.println("Kafka Adopter Adder Listener got something else:"
                      + payload + ", class: " + obj.getClass());
            }
        }
    }
}
