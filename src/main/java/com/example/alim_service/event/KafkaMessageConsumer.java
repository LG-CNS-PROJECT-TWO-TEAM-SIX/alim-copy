package com.example.alim_service.event;

import com.example.alim_service.event.message.AlimMessage;
import com.example.alim_service.event.message.SiteUserInfoEvent;
import com.example.alim_service.service.AlimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageConsumer {
    private final AlimService alimService;

    @KafkaListener(
            topics = SiteUserInfoEvent.Topic,
            groupId = "alim-group",
            properties = {
                    JsonDeserializer.VALUE_DEFAULT_TYPE + ":com.example.alim_service.event.message.SiteUserInfoEvent"
            }
    )
    public void handleUserEvent(SiteUserInfoEvent event, Acknowledgment ack){
        log.info("유저 이벤트 수신: action={}, email={}, name={}", event.getAction(), event.getEmail(), event.getName());
        String message = switch (event.getAction()) {
            case "SIGNUP" -> event.getName() + "님, 회원가입을 환영합니다";
            case "LOGIN" -> "로그인 되었습니다: " + event.getName();
            case "WITHDRAWAL" -> "회원탈퇴가 완료되었습니다.";
            default -> "[알림]";
        };

        AlimMessage alim = new AlimMessage(event.getAction(), message, event.getEventTime());
        alimService.sendToClient(event.getEmail(), alim);
        ack.acknowledge();
    }
}
