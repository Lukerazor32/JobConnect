package com.example.telegram_bot.command;

public enum CommandName {

    START("/start"),
    GETSONG("/getsong"),
    HELP("/help"),
    NO("");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
