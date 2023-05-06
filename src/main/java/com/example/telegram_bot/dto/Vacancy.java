package com.example.telegram_bot.dto;

import lombok.Data;

import java.util.List;

@Data
public class Vacancy {
    private int id;
    private String href;
    private String title;
    private boolean isMarked;
    private boolean remoteWork;
    private PublishedDate publishedDate;
    private Company company;
    private List<Division> divisions;
    private List<Skill> skills;
    private boolean favorite;
    private boolean archived;
    private boolean hidden;
}
