package expeditors.backend.commonconfig.msg;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.avro.AvroPet;
import expeditors.backend.avro.FullAdopterMessage;
import expeditors.backend.avro.SimpleCustomerMessage;
import expeditors.backend.custapp.domain.Customer;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author whynot
 */

@Component
//@Profile("kafkaevents & avromsg")
@Profile("kkavroevents")
public class KafkaAvroMsgSender implements MessageSender {

    @Value("${ttl.kafka.adopter.topic}")
    private String topic;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, Object> template;

    private DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public KafkaAvroMsgSender(KafkaTemplate<String, Object> template) {
        this.template = template;
    }


    @Override
    public void sendMessage(Object message) {
        sendMessage(message, topic);
    }
    @Override
    public void sendMessage(Object message, String resolvedTopic) {
        if (message instanceof Customer customer) {
            var dob = customer.getDob() != null ? customer.getDob().toString() : "";
            SimpleCustomerMessage ssm = SimpleCustomerMessage.newBuilder()
                    .setName(customer.getName())
//                    .setPhoneNumber(customer.getPhoneNumber())
                    .setDob(dob)
                    .setStatus(customer.getStatus().toString())
                    .setTimeStamp(LocalDateTime.now().format(dtFormatter))
                    .build();


            System.err.println("KafkaAvroSender Sending Customer to topic: " + resolvedTopic + ", Our topic: " + topic);
            var result = template.send(resolvedTopic, ssm);
        }
        else if(message instanceof Adopter adopter) {
            FullAdopterMessage fam = FullAdopterMessage.newBuilder()
                  .setName(adopter.getName())
                  .setPhoneNumber(adopter.getPhoneNumber())
                  .setPets(adopter.getPets()
                        .stream()
                        .map(pet -> AvroPet.newBuilder()
                              .setPetType(pet.getType().name())
                              .setPetName(pet.getName())
                              .setPetBreed(pet.getBreed())
                              .setAdoptionDate(pet.getAdoptionDate().format(dtFormatter))
                              .build()
                        ).toList()
                    )
                  .setTimeStamp(LocalDateTime.now().toString())
                  .build();

            System.err.println("KafkaAvroSender Sending Adopter to topic: " + resolvedTopic + ", Our topic: " + topic);
            var result = template.send(resolvedTopic, fam);
        }
    }
}