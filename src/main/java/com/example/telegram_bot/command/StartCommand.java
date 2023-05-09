package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import com.example.telegram_bot.repository.entity.TelegramUser;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import kong.unirest.HttpResponse;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class StartCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final SuperJobAuth superJobAuth;
    private final SuperJobUserService service;

    private boolean isAuth = false;

    public final static String START_MESSAGE = "Приветствую!\nЭто телеграм-бот для поиска работы! Для начала пройди авторизацию: \n%s";

    public StartCommand(SendBotMessageService sendBotMessageService,
                        TelegramUserService telegramUserService,
                        SuperJobAuth superJobAuth,
                        SuperJobUserService service) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.superJobAuth = superJobAuth;
        this.service = service;
    }

    @Override
    public void startState(Update update, User user) {
//        ReplyKeyboardMarkup markupInline = new ReplyKeyboardMarkup();
//        markupInline.setResizeKeyboard(true);
//        List<KeyboardRow> keyboardRows = new ArrayList<>();
//
//        KeyboardRow row1 = new KeyboardRow();
//        KeyboardButton getSong = new KeyboardButton();
//        getSong.setText(BUTTONGETSONG.getCommandName());
//        row1.add(getSong);
//        keyboardRows.add(row1);
//
//        if (user.getChatId().equals(adminID)) {
//            KeyboardRow folderRow = new KeyboardRow();
//            KeyboardRow songRow = new KeyboardRow();
//
//            KeyboardButton addFolder = new KeyboardButton();
//            addFolder.setText(ADDFOLDER.getCommandName());
//
//            KeyboardButton updFolder = new KeyboardButton();
//            updFolder.setText(UPDATEFOLDER.getCommandName());
//
//            KeyboardButton deleteFolder = new KeyboardButton();
//            deleteFolder.setText(DELETEFOLDER.getCommandName());
//
//            folderRow.add(addFolder);
//            folderRow.add(updFolder);
//            folderRow.add(deleteFolder);
//
//            KeyboardButton addSong = new KeyboardButton();
//            addSong.setText(ADDSONG.getCommandName());
//
//            KeyboardButton deleteSong = new KeyboardButton();
//            deleteSong.setText(DELETESONG.getCommandName());
//
//            songRow.add(addSong);
//            songRow.add(deleteSong);
//
//            keyboardRows.add(folderRow);
//            keyboardRows.add(songRow);
//        }
//
//        markupInline.setKeyboard(keyboardRows);
//        sendBotMessageService.setReplyMarkup(markupInline);
//
//        sendBotMessageService.sendMessage(update.getMessage().getChatId(), START_MESSAGE);
//        HttpResponse<JsonNode> jsonResponse1
//                = Unirest.get("https://career.habr.com/api/frontend/vacancies")
//                .queryString("q", "python")
//                .queryString("sort", "relevance")
//                .queryString("type", "all")
//                .queryString("currency", "RUR")
//                .asJson();
//        JsonNode jsonNode1 = jsonResponse1.getBody();
//        JSONArray jsonArray = jsonNode1.getObject().getJSONArray("list");
////        Gson gson = new Gson();
////        JobListing[] vacancies = gson.fromJson(jsonArray.toString(), JobListing[].class);
////        for (JobListing obj : vacancies) {
////            System.out.println(obj.getHref());
////        }
//        System.out.println(jsonNode1 + " \n" + jsonArray);

//        Vacancies jsonResponse2
//                = Unirest.get("https://career.habr.com/api/frontend/vacancies")
//                .queryString("q", "python")
//                .queryString("sort", "relevance")
//                .queryString("type", "all")
//                .queryString("currency", "RUR")
//                .asObject(new GenericType<Vacancies>() {
//                })
//                .getBody();
//        String url = jsonResponse2.getList().get(0).getHref();
//        for (JobListing vacancy : jsonResponse2.getList()) {
//            System.out.println(vacancy.getId() + " " + vacancy.getHref() + " " + vacancy.getTitle());
//        }
//
//        try {
//            Document doc = Jsoup.connect("https://career.habr.com" + url).get();
//            Element info = doc.select(".basic-section--appearance-vacancy-description").first();
//            Elements descript = info.select(".style-ugc > *");
//            System.out.println(descript);
//            String response = "";
//            for (Element el : descript) {
//                String str = el.html();
//                if (str.contains("<li>")) {
//                    str = str.replace("<li>", "\n\uD83D\uDE1D");
//                    str = str.replace("</li>", " ");
//                    System.out.println(str);
//                    response += str;
//                } else {
//                    response += el.text();
//                }
//            }
//            sendBotMessageService.sendMessage(user.getChatId(), response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        JobListing jobListing = Unirest.get("https://api.superjob.ru/2.0/vacancies/")
//                .header("X-Api-App-Id", "v3.r.137533868.0c505521a6bdae5fb42658f110bba226456273aa.13e634d8d3301ee3d4d2b40b40c914cf58d29237")
//                .asObject(new GenericType<JobListing>() {
//                })
//                .getBody();
        telegramUserService.findByChatId(user.getChatId()).ifPresent(
            telegramUser -> {
                HttpResponse<ResumeData> resumeData = service.getResumes(telegramUser.getAccessToken());
                System.out.println("Успешно!\n" + resumeData.getStatus() + "\n" + resumeData.getBody().getObjects()[0].toString());
            }
        );

        if (update.hasMessage() && update.getMessage().hasText()) {
            telegramUserService.findByChatId(user.getChatId()).ifPresent(
                    oldUser -> {
                        if(oldUser.getAccessToken() != null && oldUser.getRefreshToken() != null) {
                            isAuth = true;
                        }
                    }
            );
            if (!isAuth) {
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowCheck = new ArrayList<>();
                InlineKeyboardButton checkutton = new InlineKeyboardButton();
                checkutton.setText("Проверить авторизацию");
                checkutton.setCallbackData("checkAuth");
                rowCheck.add(checkutton);
                rowsInline.add(rowCheck);
                markupInline.setKeyboard(rowsInline);

                sendBotMessageService.setReplyMarkup(markupInline);
                sendBotMessageService.sendMessage(user.getChatId(), String.format(START_MESSAGE, superJobAuth.getAuthURL(user.getChatId().toString())));
            }
        } else if (update.hasCallbackQuery() || isAuth) {
            execute(update, user);
        }
    }

    @Override
    public void execute(Update update, User user) {
        telegramUserService.findByChatId(user.getChatId()).ifPresentOrElse(
                oldUser -> {
                    if (update.hasCallbackQuery() && oldUser.getAccessToken() == null) {
                        String accessToken;
                        try {
                            accessToken = superJobAuth.getTokens().get(oldUser.getChatId())[0];
                        } catch (NullPointerException e) {
                            return;
                        }
                        String refreshToken = superJobAuth.getTokens().get(oldUser.getChatId())[1];
                        oldUser.setAccessToken(accessToken);
                        oldUser.setRefreshToken(refreshToken);
                        telegramUserService.save(oldUser);
                        sendBotMessageService.sendMessage(user.getChatId(), "Авторизация прошла успешно");
                        user.setState(new NoCommand(sendBotMessageService));
                    } else {
                        user.setState(new NoCommand(sendBotMessageService));
                    }
                },
                () -> {
                    TelegramUser newUser = new TelegramUser();
                    newUser.setUsername(update.getMessage().getFrom().getUserName());
                    newUser.setChatId(user.getChatId());
                    newUser.setFirstName(update.getMessage().getFrom().getFirstName());
                    newUser.setLastName(update.getMessage().getFrom().getLastName());
                    telegramUserService.save(newUser);
                }
        );
    }

    @Override
    public void undo() {

    }
}
