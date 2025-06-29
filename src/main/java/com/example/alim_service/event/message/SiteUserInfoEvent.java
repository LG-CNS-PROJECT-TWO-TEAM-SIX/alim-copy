package com.example.alim_service.event.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SiteUserInfoEvent {
    public static final String Topic = "user";

    private String action;
    private String email;
    private String name;
    private LocalDateTime eventTime;
}

