package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.telegram_bot.command.CommandName.*;

public class HelpCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final JobConnect jobConnect;

    public final static String HELP_MESSAGE = String.format("Доступные команды:" +
                    "\n\n" +
                    "▶ %s - начать работу бота" +
                    "\n" +
                    "\uD83C\uDFB5 %s - получить песни по настроению" +
                    "\n" +
                    "\uD83C\uDD98 %s - все команды бота" +
                    "\n",
            START.getCommandName(), GETSONG.getCommandName(), HELP.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService, JobConnect jobConnect) {
        this.sendBotMessageService = sendBotMessageService;
        this.jobConnect = jobConnect;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }

}
