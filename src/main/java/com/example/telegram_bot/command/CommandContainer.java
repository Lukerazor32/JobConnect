package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.command.admin.addfolder.AddFolderNameCommand;
import com.example.telegram_bot.command.admin.addsong.AddSongCommand;
import com.example.telegram_bot.command.admin.deletefolder.DeleteFolderCommand;
import com.example.telegram_bot.command.admin.deletesong.DeleteSongCommand;
import com.example.telegram_bot.command.admin.help.AdminHelpCommand;
import com.example.telegram_bot.command.admin.updatefolder.ChooseFolderCommand;
import com.example.telegram_bot.command.getsong.GetMoodCommand;
import com.example.telegram_bot.command.settings.LocationCommand;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import com.google.common.collect.ImmutableMap;

import static com.example.telegram_bot.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<String, State> commandMap;
    private final ImmutableMap<String, State> commandAdminMap;
    private final State unknownCommand;
    private final Long adminID = Long.valueOf(1395425257);

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            TelegramUserService telegramUserService,
                            JobConnect jobConnect,
                            MoodFolderService moodFolderService,
                            TelegramMusicService telegramMusicService,
                            HabrRequest habrRequest) {
        commandMap = ImmutableMap.<String, State>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService, habrRequest))
                .put(GETSONG.getCommandName(), new GetMoodCommand(sendBotMessageService, moodFolderService, telegramMusicService, jobConnect))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService, jobConnect))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(SETLOCATION.getCommandName(), new LocationCommand(sendBotMessageService, telegramUserService, habrRequest))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService, jobConnect);

        commandAdminMap = ImmutableMap.<String, State>builder()
                .build();
    }

    public State retrieveCommand(String commandIdentifier, User user) {
        if (commandAdminMap.containsKey(commandIdentifier) && (user.getChatId().equals(adminID))) return commandAdminMap.get(commandIdentifier);
        else return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
