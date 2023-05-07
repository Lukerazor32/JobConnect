package com.example.telegram_bot.dto;

import lombok.Data;
import org.json.JSONArray;
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
