package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.Vacancies;

import java.util.List;

public interface SuperJobRequest {
    /** Get all Vacancies filtered by VacancyRequestArgs*/
    List<Vacancies> getVacancies();


}
