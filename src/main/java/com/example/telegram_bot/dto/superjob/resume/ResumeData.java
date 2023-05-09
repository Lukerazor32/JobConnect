package com.example.telegram_bot.dto.superjob.resume;

import lombok.Data;

@Data
public class ResumeData {
    private ResumeObject[] objects;
    private boolean more;
    private int total;
}
