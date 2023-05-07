package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.Specialization;
import com.example.telegram_bot.repository.entity.TelegramUser;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final Long adminID = Long.valueOf(1395425257);

    public final static String START_MESSAGE = "Приветствую!\nЭто телеграм-бот для поиска работы!";

    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
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
////        Vacancy[] vacancies = gson.fromJson(jsonArray.toString(), Vacancy[].class);
////        for (Vacancy obj : vacancies) {
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
//        for (Vacancy vacancy : jsonResponse2.getList()) {
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
        try {
            // Получаем HTML страницу
            Document doc = Jsoup.connect("https://career.habr.com/vacancies").get();

            // Находим скрипты внутри HTML
            Element scriptElement = doc.select("script[data-ssr-state=true]").first();

            // Извлекаем содержимое скрипта
            String script = scriptElement.html();
            System.out.println(script);

            // Ищем конкретные JSON объекты в скрипте
            JSONObject jsonObject = new JSONObject(script);
            JSONArray jsonArray = jsonObject.getJSONObject("search").getJSONArray("groups");

            Gson gson = new Gson();
            Specialization[] specialization = gson.fromJson(jsonArray.toString(), Specialization[].class);

            for (Specialization sp : specialization)
                System.out.println(sp.getId() + " " + sp.getTitle());
//
//            System.out.println(dataObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        execute(update, user);
    }

    @Override
    public void execute(Update update, User user) {
        telegramUserService.findByChatId(user.getChatId()).ifPresentOrElse(
                Olduser -> {},
                () -> {
                    TelegramUser newUser = new TelegramUser();
                    newUser.setChatId(user.getChatId());
                    newUser.setUsername(update.getMessage().getFrom().getUserName());
                    newUser.setFirstName(update.getMessage().getFrom().getFirstName());
                    newUser.setLastName(update.getMessage().getFrom().getLastName());
                    telegramUserService.save(newUser);
                }
        );
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void undo() {

    }

}
