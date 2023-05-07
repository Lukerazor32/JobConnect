package com.example.telegram_bot.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VacancyRequestArgs {
    private String keyWords;
    private String location;
    private int qualification;
}
