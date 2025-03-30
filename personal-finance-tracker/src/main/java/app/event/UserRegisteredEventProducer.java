package app.event;

import app.event.payload.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisteredEventProducer {
    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    @Autowired
    public UserRegisteredEventProducer(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserRegisteredEvent event) {
        kafkaTemplate.send("user-registered-event.v1", event);
        log.info("Successfully published user registered event for user with id - [%s]".formatted(event.getId()));
    }
}
