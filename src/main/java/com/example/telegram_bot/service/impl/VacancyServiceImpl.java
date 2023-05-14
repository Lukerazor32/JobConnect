package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.superjob.Vacancy;
import com.example.telegram_bot.dto.superjob.VacancyObject;
import com.example.telegram_bot.service.VacancyService;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final String superJobAPIPath;
    private final String secretKey;

    public VacancyServiceImpl(@Value("${superjob.api.path}") String superJobAPIPath,
                              @Value("${superjob.api.secret-key}") String secretKey) {
        this.superJobAPIPath = superJobAPIPath;
        this.secretKey = secretKey;
    }

    @Override
    public VacancyObject getVacancies(String authToken, int idResume) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        HttpResponse<VacancyObject> response = Unirest.get(String.format("%s/vacancies/", superJobAPIPath))
                .headers(headerProp)
                .queryString("id_resume", idResume)
                .asObject(VacancyObject.class);
        VacancyObject vacancies = null;
        if (response.getStatus() == 200) {
            vacancies = response.getBody();
        }
        return vacancies;
    }

    @Override
    public boolean sendResponseToVacancy(String authToken, int idResume, int idVacancy) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        HttpResponse<JsonResponse> response =
                Unirest.post(String.format("%s/send_cv_on_vacancy/", superJobAPIPath))
                        .headers(headerProp)
                        .queryString("id_cv", idResume)
                        .queryString("id_vacancy", idVacancy)
                        .asObject(JsonResponse.class);
        if (response.getStatus() == 200) {
            return true;
        }
        return false;
    }


}
