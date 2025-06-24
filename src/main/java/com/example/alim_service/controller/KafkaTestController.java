package com.example.alim_service.controller;

import com.example.alim_service.event.message.SiteUserInfoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka/test")
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaTemplate<String, SiteUserInfoEvent> kafkaTemplate;

    @PostMapping
    public String sendTest(@RequestBody Map<String, String> req) {
        SiteUserInfoEvent event = new SiteUserInfoEvent();
        event.setAction(req.get("action"));  // 예: "SIGNUP"
        event.setEmail(req.get("email"));
        event.setName(req.get("name"));
        event.setEventTime(LocalDateTime.now());

        kafkaTemplate.send(SiteUserInfoEvent.Topic, event);
        return "테스트 메시지 전송 완료";
    }
}