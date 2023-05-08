package com.example.telegram_bot.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VacancyRequestArgs {
    private String keyWords;
    private String[] locations;
    private int qualification;
    private String sort;
    private int[] specialization;
    private int[] skills;
}
