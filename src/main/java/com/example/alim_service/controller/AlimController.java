package com.example.alim_service.controller;

import com.example.alim_service.service.AlimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/alim")
@RequiredArgsConstructor
@Slf4j
public class AlimController {
    private final AlimService alimService;

    @GetMapping("/message")
    public SseEmitter message(@RequestParam String email){
        log.info("[alim-service] client email : {}",email);
        return alimService.createEmitter(email);
    }
}
