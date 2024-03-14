package expeditors.backend.commonconfig.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author whynot
 */

@Component
//@Profile("kafkaevents & jsonmsg")
@Profile("kkjsonevents")
public class KafkaJsonMsgSender implements MessageSender{

    @Value("${ttl.kafka.adopter.topic}")
    private String topic;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, Object> template;

    public KafkaJsonMsgSender(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public void sendMessage(Object message) {
        sendMessage(message, topic);
    }


    @Override
    public void sendMessage(Object message, String resolvedTopic) {
//        var rrMsg = RestResultGeneric.ofValue(message);
        //var result = template.send(topic, message);
        System.err.println("KafkaJson Sending to topic: " + resolvedTopic + ", Our topic: " + topic);
        var result = template.send(resolvedTopic, message);
        result.whenComplete((sr, ex) -> {
           if(ex != null) {
              logger.warn("Send Kafka Message failed with ex: {}", ex.getMessage());
           }
        });
    }
}