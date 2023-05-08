package com.example.telegram_bot.dto.specialization;

import lombok.Data;

@Data
public class Children {
    private int id;
    private int parent_id;
    private String title;
    private String translation;
}
