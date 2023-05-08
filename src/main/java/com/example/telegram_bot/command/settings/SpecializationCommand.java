package com.example.telegram_bot.command.settings;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.specialization.Specialization;
import com.example.telegram_bot.service.HabrRequest;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SpecializationCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final HabrRequest habrRequest;
    private Message message;

    public SpecializationCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService, HabrRequest habrRequest) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.habrRequest = habrRequest;
    }

    @Override
    public void startState(Update update, User user) {
        List<Specialization> specializations = habrRequest.getSpecializations();

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Specialization spec : specializations) {
            List<InlineKeyboardButton> rowLocation = new ArrayList<>();
            InlineKeyboardButton inlineButtonAccept = new InlineKeyboardButton();
            inlineButtonAccept.setText(spec.getTitle());
            inlineButtonAccept.setCallbackData(String.valueOf(spec.getId()));
            rowLocation.add(inlineButtonAccept);
            rowsInline.add(rowLocation);
        }

        markupInline.setKeyboard(rowsInline);
        sendBotMessageService.setReplyMarkup(markupInline);
        sendBotMessageService.sendMessage(user.getChatId(),"Выбери специализацию из списка");
    }

    @Override
    public void execute(Update update, User user) {
        if(update.hasCallbackQuery()) {

        }
    }

    @Override
    public void undo() {

    }
}
