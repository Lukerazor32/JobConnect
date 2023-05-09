package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import com.example.telegram_bot.service.SuperJobUserService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SuperJobUserServiceImpl implements SuperJobUserService {
    private final String superJobAPIPath;

    private final String secretKey;
    private final String client_id;

    private Map<String, String> headerProp;

    public SuperJobUserServiceImpl(@Value("${superjob.api.path}") String superJobAPIPath,
                            @Value("${superjob.api.secret-key}") String secretKey,
                            @Value("${superjob.api.id}") String client_id) {
        this.superJobAPIPath = superJobAPIPath;
        this.secretKey = secretKey;
        this.client_id = client_id;

        headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
    }
    @Override
    public HttpResponse<ResumeData> getResumes(String authToken) {
        System.out.println(String.format("Bearer %s", authToken));
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        return Unirest.get(String.format("%s/user_cvs/", superJobAPIPath))
                .headers(headerProp)
                .asObject(ResumeData.class);
    }
}
