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
        SseEmitter emitter = new SseEmitter(60 * 1000L * 30);
        emitterMap.put(email, emitter);

        try {
            log.info("create emitter success!!");
            emitter.send(SseEmitter.event().name("INIT").data("SSE 연결됨"));
        }
        catch (IOException e){
            log.error("emitter send error {}",e);
            emitter.completeWithError(e);
        }

        emitter.onCompletion(() -> emitterMap.remove(email));
        emitter.onTimeout(() -> emitterMap.remove(email));
        return emitter;
    }

    public void sendToClient (String email, AlimMessage alim){

        log.info("[sendToClient] email :{} ",email);

        SseEmitter emitter = emitterMap.get(email);
        if(emitter != null){
            try {
                log.info("[sendToClient] action : {} , message : {}, time : {}",alim.getAction(),alim.getMessage(),alim.getTime());
                emitter.send(SseEmitter.event().data(alim));
            }
            catch(IOException e){
                log.error("[sendToClient] Error : {}",e);
                emitter.completeWithError(e);
            }
        }
    }
}
