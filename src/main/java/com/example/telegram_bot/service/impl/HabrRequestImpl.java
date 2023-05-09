package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.Location;
import com.example.telegram_bot.dto.LocationList;
import com.example.telegram_bot.dto.Skill;
import com.example.telegram_bot.dto.VacancyRequestArgs;
import com.example.telegram_bot.dto.specialization.Specialization;
import com.example.telegram_bot.dto.vacancy.Vacancies;
import com.example.telegram_bot.service.HabrRequest;
import com.google.gson.Gson;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class HabrRequestImpl implements HabrRequest {
    private final String habrAPIPath;
    private final String habrPath;

    public HabrRequestImpl(@Value("${habr.api.path}") String habrAPIPath,
                           @Value("${habr.path}") String habrPath) {
        this.habrAPIPath = habrAPIPath;
        this.habrPath = habrPath;
    }

    @Override
    public List<Vacancies> getVacancies(VacancyRequestArgs vacancyRequestArgs) {
        return null;
    }

    @Override
    public List<Location> getLocations(String city) {
        LocationList locationList = Unirest.get(String.format("%s/suggestions/locations", habrAPIPath))
                .queryString("term", city)
                .asObject(new GenericType<LocationList>() {
                })
                .getBody();
        return locationList.getList();
    }

    @Override
    public List<Specialization> getSpecializations() {
        List<Specialization> specializations = null;
        try {
            // Получаем HTML страницу
            Document doc = Jsoup.connect(habrPath).get();

            // Находим скрипты внутри HTML
            Element scriptElement = doc.select("script[data-ssr-state=true]").first();

            // Извлекаем содержимое скрипта
            String script = scriptElement.html();

            // Ищем конкретные JSON объекты в скрипте
            JSONObject jsonObject = new JSONObject(script);
            JSONArray jsonArray = jsonObject.getJSONObject("search").getJSONArray("groups");

            Gson gson = new Gson();
            Specialization[] specArr = gson.fromJson(jsonArray.toString(), Specialization[].class);
            specializations = Arrays.asList(specArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return specializations;
    }

    @Override
    public List<Skill> getSkills() {
        return null;
    }

    @Override
    public List<Skill> getSkills(String skillName) {
        return null;
    }
}
