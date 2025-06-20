package com.example.alim_service.service;

import com.example.alim_service.event.message.AlimMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class AlimService {
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String email){
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        emitterMap.put(email, emitter);

        try {
            emitter.send(SseEmitter.event().name("INIT").data("SSE 연결됨"));
        }
        catch (IOException e){
            emitter.completeWithError(e);
        }

        emitter.onCompletion(() -> emitterMap.remove(email));
        emitter.onTimeout(() -> emitterMap.remove(email));
        return emitter;
    }

    public void sendToClient (String email, AlimMessage alim){
        SseEmitter emitter = emitterMap.get(email);
        if(emitter != null){
            try {
                emitter.send(SseEmitter.event().data(alim));
            }
            catch(IOException e){
                emitter.completeWithError(e);
            }
        }
    }
}
