package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.superjob.Vacancy;
import com.example.telegram_bot.dto.superjob.VacancyObject;
import kong.unirest.HttpResponse;

import java.util.List;

public interface VacancyService {
    List<Vacancy> getVacancies(String authToken, int idResume, int page);

    List<Vacancy> getVacanciesOrderByDate(String authToken, int idResume);

    boolean sendResponseToVacancy(String authToken, int idResume, int idVacancy);
}
