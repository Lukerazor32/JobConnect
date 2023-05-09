package com.example.telegram_bot.dto.superjob;

import lombok.Data;

import java.util.List;

@Data
public class JobListing {
    private List<Vacancy> objects;
    private int total;
    private String corrected_keyword;
    private boolean more;
}
