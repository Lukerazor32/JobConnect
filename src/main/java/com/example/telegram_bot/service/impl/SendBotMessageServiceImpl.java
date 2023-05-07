package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.service.SendBotMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final JobConnect jobConnect;

    private SendMessage sendMessage = new SendMessage();

    @Autowired
    public SendBotMessageServiceImpl(JobConnect jobConnect) {
        this.jobConnect = jobConnect;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);
        sendMessage.setText(message);


        try {
            jobConnect.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        finally {
            sendMessage = new SendMessage();
        }
    }

    @Override
    public void setReplyMarkup(InlineKeyboardMarkup markup) {
        sendMessage.setReplyMarkup(markup);
    }

    @Override
    public void setReplyMarkup(ReplyKeyboardMarkup markup) {
        sendMessage.setReplyMarkup(markup);
    }
}
