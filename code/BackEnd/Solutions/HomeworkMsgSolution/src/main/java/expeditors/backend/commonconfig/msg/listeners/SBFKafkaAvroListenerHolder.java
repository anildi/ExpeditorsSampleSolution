package expeditors.backend.commonconfig.msg.listeners;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.domain.PetType;
import expeditors.backend.avro.FullAdopterMessage;
import expeditors.backend.avro.SimpleCustomerMessage;
import expeditors.backend.custapp.domain.Customer;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("kkavroevents")
public class SBFKafkaAvroListenerHolder {

    public SBFKafkaAvroListenerHolder(@Value("${ttl.kafka.adopter.topic}") String tp) {
        int stop = 0;
    }

    @KafkaListener(groupId = "adoptapp-avro-consumer-group", topics = "${ttl.kafka.adopter.topic}")
    public void listen(ConsumerRecord<String, Object> consumerRecord, Object payload) {
        System.err.println("########### KafkaAvroListener ##########################\n");
        System.err.println("payload: " + payload);

        var obj = consumerRecord.value();
        System.out.println(STR."KafkaList, obj class: \{obj.getClass()}, obj: \{obj}");


        switch (obj) {
            case SimpleCustomerMessage scm -> {
                Customer receivedCustomer = new Customer(scm.getName(),
                      LocalDate.parse(scm.getDob()),
                      Customer.Status.valueOf(scm.getStatus()));
            }
            case FullAdopterMessage avroAdopter -> {
                Adopter adopter = new Adopter(avroAdopter.getName(), avroAdopter.getPhoneNumber(),
                      avroAdopter
                            .getPets().stream()
                            .map(ap -> Pet.builder(PetType.valueOf(ap.getPetType()))
                                  .name(ap.getPetName())
                                  .breed(ap.getPetBreed())
                                  .adoptionDate(ap.getAdoptionDate())
                                  .build()
                            ).collect(Collectors.toSet())
                );

                System.out.println("Avro Listener got Adopter: " + adopter);
            }
            default -> {
            }
        }
    }
}
