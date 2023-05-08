package com.example.telegram_bot.dto.specialization;

import lombok.Data;
import org.json.JSONObject;

import java.util.List;

@Data
public class Specialization {
    private int id;
    private String title;
    private String translation;
    private JSONObject socialLinks;
    private List<Children> children;
}
