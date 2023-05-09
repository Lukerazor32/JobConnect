package com.example.telegram_bot.dto.superjob;

import lombok.Getter;

import java.util.List;

@Getter
public class Vacancy {
    private int id;
    private int id_client;
    private int payment_from;
    private int payment_to;
    private long date_pub_to;
    private long date_archived;
    private long date_published;
    private String address;
    private String payment;
    private String profession;
    private String work;
    private List<Metro> metro;
    private String currency;
    private boolean moveable;
    private boolean agreement;
    private PlaceOfWork place_of_work;
    private Education education;
    private Experience experience;
    private MaritalStatus maritalstatus;
    private Children children;
    private boolean already_sent_on_vacancy;
    private List<String> languages;
    private List<String> driving_licence;
    private List<Catalogue> catalogues;
    private Agency agency;
    private Town town;
    private String client_logo;
    private int age_from;
    private int age_to;
    private Gender gender;
    private String firm_name;
    private String firm_activity;
    private String link;
}
