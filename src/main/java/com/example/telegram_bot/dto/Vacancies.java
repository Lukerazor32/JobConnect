package com.example.telegram_bot.dto;

import lombok.Data;

import java.util.List;

@Data
public class Vacancies {
    private List<Vacancy> list;
    private Meta meta;
}
