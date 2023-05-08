package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.Location;
import com.example.telegram_bot.dto.Skill;
import com.example.telegram_bot.dto.VacancyRequestArgs;
import com.example.telegram_bot.dto.specialization.Specialization;
import com.example.telegram_bot.dto.vacancy.Vacancies;

import java.util.List;

public interface HabrRequest {
    /** Get all Vacancies filtered by VacancyRequestArgs
     * @param vacancyRequestArgs*/
    List<Vacancies> getVacancies(VacancyRequestArgs vacancyRequestArgs);

    List<Location> getLocations(String city);

    List<Specialization> getSpecializations();

    List<Skill> getSkills();

    List<Skill> getSkills(String skillName);
}
