package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import kong.unirest.HttpResponse;

public interface SuperJobUserService {
    HttpResponse<ResumeData> getResumes(String authToken);
}
