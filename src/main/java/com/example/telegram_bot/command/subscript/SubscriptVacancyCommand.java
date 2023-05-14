package com.example.telegram_bot.command.subscript;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.SubscriptionArgs;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.SuperJobUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SubscriptVacancyCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final SuperJobUserService superJobUserService;
    private SubscriptionArgs subscriptionArgs;

    public SubscriptVacancyCommand(SendBotMessageService sendBotMessageService, SuperJobUserService superJobUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.superJobUserService = superJobUserService;
    }

    @Override
    public void startState(Update update, User user) {

    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }
}
