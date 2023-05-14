package com.example.telegram_bot.command.subscript;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.Catalogues;
import com.example.telegram_bot.dto.superjob.Positions;
import com.example.telegram_bot.dto.superjob.SubscriptionArgs;
import com.example.telegram_bot.dto.superjob.Town;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.SuperJobUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final SuperJobUserService superJobUserService;
    private SubscriptionArgs.SubscriptionArgsBuilder subscriptionArgs;

    private Message message;
    private EditMessageText editMessageText;
    private int startIndex;
    private int endIndex;
    private int page;

    private List<Catalogues> catalogues;
    private List<Catalogues> selectedCatalogues;

    private final static String CATEGORYTEXT = "Теперь выберите категорию работы";

    public ChooseCategoryCommand(SendBotMessageService sendBotMessageService, SuperJobUserService superJobUserService, SubscriptionArgs.SubscriptionArgsBuilder subscriptionArgs) {
        this.sendBotMessageService = sendBotMessageService;
        this.superJobUserService = superJobUserService;
        this.subscriptionArgs = subscriptionArgs;
        editMessageText = new EditMessageText();
        selectedCatalogues = new ArrayList<>();
        startIndex = 0;
        endIndex = 5;
        page = 1;
    }

    @Override
    public void startState(Update update, User user) {
        editMessageText.setChatId(String.valueOf(user.getChatId()));
        catalogues = superJobUserService.getCategories();
        setMarkup(catalogues, page);
        message = sendBotMessageService.sendMessage(user.getChatId(), CATEGORYTEXT);
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            if (data.contains("catalogueId")) {
                int catalogueIndex = Integer.parseInt(data.split(" ")[1]);
                selectedCatalogues.add(catalogues.get(catalogueIndex));
            } else if (data.contains("deleteCatalogue")) {
                int catalogueIndex = Integer.parseInt(data.split(" ")[1]);
                selectedCatalogues.remove(catalogues.get(catalogueIndex));
            } else if (data.contains("plus") && page+1 <= 3) {
                page++;
            } else if (data.contains("minus") && page-1 > 0) {
                page--;
            } else if (data.contains("acceptCatalogue")) {
                if (selectedCatalogues.size() > 0) {
                    subscriptionArgs.catalogues(selectedCatalogues);
                    sendBotMessageService.sendMessage(user.getChatId(), "Категория выбрана!");
                    return;
                } else {
                    sendBotMessageService.sendMessage(user.getChatId(), "Выберите категорию");
                }

            }
            setMarkup(catalogues, page);
        }
    }

    @Override
    public void undo() {

    }

    private void setMarkup(List<Catalogues> catalogues, int page) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        int startIndex = (page - 1) * 5;
        int endIndex = Math.min(startIndex + 5, catalogues.size());
        int catalogueId = startIndex;

        List<Catalogues> cataloguePage = catalogues.subList(startIndex, endIndex);
        for (Catalogues catalogue : cataloguePage) {
            String catalogueTitle = catalogue.getTitle();
            if (selectedCatalogues.size() > 0 && selectedCatalogues.contains(catalogue)) {
                catalogueTitle += " ✅";
                rowsInline.add(sendBotMessageService.createRow(catalogueTitle, "deleteCatalogue " + catalogueId));
            } else {
                rowsInline.add(sendBotMessageService.createRow(catalogueTitle, "catalogueId " + catalogueId));
            }
            catalogueId++;
        }
        List<InlineKeyboardButton> rowNextBack = new ArrayList<>();
        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("➡");
        nextButton.setCallbackData("plus");
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("⬅");
        backButton.setCallbackData("minus");
        rowNextBack.add(backButton);
        rowNextBack.add(nextButton);

        rowsInline.add(rowNextBack);
        rowsInline.add(sendBotMessageService.createRow("Отправить", "acceptCatalogue"));
        markupInline.setKeyboard(rowsInline);
        if (message != null) {
            editMessageText.setReplyMarkup(markupInline);
            editMessageText.setText(CATEGORYTEXT);
            editMessageText.setMessageId(message.getMessageId());
            sendBotMessageService.updateMessage(editMessageText);
        } else {
            sendBotMessageService.setReplyMarkup(markupInline);
        }
    }
}
