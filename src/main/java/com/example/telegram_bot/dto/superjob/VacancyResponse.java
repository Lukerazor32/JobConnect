package com.example.telegram_bot.dto.superjob;

import lombok.Getter;

@Getter
public class VacancyResponse {
    private int id_vacancy;
    private Vacancy vacancy;
    private int id_resume;
    private String firm_name;
    private String contact_face;
    private int date_sent;
    private String position_name;
    private String title;
    private String body;
    private boolean archive;
    private boolean storage;
    private String resume_additional_info;
    private String status_text;
    private int status;
}
