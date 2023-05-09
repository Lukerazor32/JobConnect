package com.example.telegram_bot.command.settings;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.dto.Location;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.HabrRequest;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class LocationCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final HabrRequest habrRequest;

    public LocationCommand(SendBotMessageService sendBotMessageService,
                           TelegramUserService telegramUserService,
                           HabrRequest habrRequest) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.habrRequest = habrRequest;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(user.getChatId(), "Напиши город и выбери его из списка");
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String city = update.getMessage().getText().trim();
            List<Location> locations = habrRequest.getLocations(city);
            int locationsSize = 5;

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

            if (locations != null && locations.size() != 0) {
                if (locations.size() < 5) {
                    locationsSize = locations.size();
                }
                for (int i = 0; i < locationsSize; i++) {
                    List<InlineKeyboardButton> rowLocation = new ArrayList<>();
                    InlineKeyboardButton inlineButtonAccept = new InlineKeyboardButton();
                    inlineButtonAccept.setText(locations.get(i).getTitle());
                    inlineButtonAccept.setCallbackData(locations.get(i).getValue());
                    rowLocation.add(inlineButtonAccept);
                    rowsInline.add(rowLocation);
                }
                markupInline.setKeyboard(rowsInline);

                sendBotMessageService.setReplyMarkup(markupInline);
                sendBotMessageService.sendMessage(user.getChatId(), "Выберите город:");
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), "Город не найден");
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery response = update.getCallbackQuery();

            String locationId = response.getData();

            telegramUserService.findByChatId(user.getChatId()).ifPresent(
                    telegramUser -> {

                    }
            );
        }
    }

    @Override
    public void undo() {

    }
}
