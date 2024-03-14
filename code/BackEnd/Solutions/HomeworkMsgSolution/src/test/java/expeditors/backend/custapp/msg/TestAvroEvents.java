package expeditors.backend.custapp.msg;

import expeditors.backend.avro.SimpleCustomerMessage;
import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.commonconfig.msg.MessageSender;
import expeditors.backend.commonconfig.msg.listeners.SBFKafkaAvroListenerHolder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A way to test whether a message got to its destination.  We have to somehow
 * wait for the Listener to get the message before doing our assertions.  A
 * standard trick is to use a CountDownLatch, as below.  Problem is that is
 * putting testing code in your application. Generally not good.
 * We make a compromise here, and create a local Message Listener, where
 * we do the trick with the CountDownLatch.
 * @author whynot
 */
@SpringBootTest(properties = {"spring.kafka.consumer.auto-offset-reset=earliest",
        "ttl.kafka.customer.topic = custapp-testtopic"
})
@EmbeddedKafka(partitions = 1, topics = {"custapp-testtopic"},
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=19092" })
//@ActiveProfiles(profiles = {"embeddedkafka", "kkavroevents", "prod", "mysql" })
//@EnabledIf(expression = "#{environment.matchesProfiles('embeddedkafka')}")
//@EnabledIf(value = "#{'${ttl.kafka.embedded}' != null && ${ttl.kafka.embedded} == 'true'}", loadContext = true)
//@EnabledIf(value = "#{'${ttl.kafka.embedded}' == 'true'}", loadContext = true)
//@EnabledIf(value = "#{environment.matchesProfiles('kkavroevents') && '${ttl.kafka.embedded}' == 'true'}", loadContext = true)
@EnabledIf(value = "#{environment.matchesProfiles('kkavroevents') && environment.getProperty('EMBEDDED_KAFKA') != null}", loadContext = true)
//@EnabledIf(value = "#{environment.getProperty('EMBEDDED_KAFKA') != null}", loadContext = true)
public class TestAvroEvents {


    private static CountDownLatch latch = new CountDownLatch(1);

    Environment env;
    @TestConfiguration
    public static class TestKafkaListener {
        private SimpleCustomerMessage result;
        @KafkaListener(groupId = "custapp-avro-test-group", topics = "custapp-testtopic")
        public void listen(ConsumerRecord<String, Object> consumerRecord, SimpleCustomerMessage payload) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.err.println("########################################\n"
                    + "TestListenerInternal: Name: " + payload.getName()
//                + ", Phone: " + payload.getPhoneNumber()
                    + ", Dob: " + payload.getDob()
                    + ", Status: " + payload.getStatus()
                    + ", TS: " + payload.getTimeStamp());
            result = payload;

            latch.countDown();

            result = payload;
        }
    }

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private SBFKafkaAvroListenerHolder messageListener;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private TestKafkaListener testConfig;

    static private Customer customer = new Customer("Test Avro Customer", LocalDate.of(1960, 10, 5), Customer.Status.NORMAL);


    @Test
    public void testSendToEmbeddedBroker()
            throws Exception {


        messageSender.sendMessage(customer, "custapp-testtopic");

        System.err.println("########## Sent Message########## ");

        latch.await(1, TimeUnit.SECONDS);

        assertNotNull(testConfig.result);
        assertEquals(customer.getName(), testConfig.result.getName());
    }
}