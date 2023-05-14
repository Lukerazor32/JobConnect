package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.Vacancy;
import com.example.telegram_bot.dto.superjob.VacancyObject;
import com.example.telegram_bot.dto.superjob.VacancyResponse;
import com.example.telegram_bot.dto.superjob.VacancyResponseObject;
import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import com.example.telegram_bot.dto.superjob.resume.Resume;
import com.example.telegram_bot.service.ResumeService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.service.VacancyService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class VacancyCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    private ResumeData resumeData;
    private Resume selectedResume;
    private List<Vacancy> vacancy;
    private int indexVacancy;

    private Message message;

    private final static String SELECT_RESUME = "Выберите резюме по параметрам которого вы хотите искать вакансии\n%s";

    public VacancyCommand(SendBotMessageService sendBotMessageService,
                          TelegramUserService telegramUserService,
                          ResumeService resumeService,
                          VacancyService vacancyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.resumeService = resumeService;
        this.vacancyService = vacancyService;
        indexVacancy = 0;
    }

    @Override
    public void startState(Update update, User user) {
        if (!update.hasCallbackQuery()) {
            resumeData = resumeService.getResumes(user.getToken());
            if (resumeData.getTotal() > 0) {
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                Resume[] resumes = resumeData.getObjects();
                for (int i = 0; i < resumes.length; i++) {
                    Resume resume = resumes[i];
                    String resumeTitle = resume.getId() + " " + resume.getProfession() + " " + String.format("(%s)", resume.getPublished().getTitle());
                    rowsInline.add(sendBotMessageService.createRow(resumeTitle, "resumeId " + i));
                }
                markupInline.setKeyboard(rowsInline);

                sendBotMessageService.setReplyMarkup(markupInline);
                sendBotMessageService.sendMessage(user.getChatId(), String.format(SELECT_RESUME, "Список резюме:"));
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), String.format(SELECT_RESUME, "Резюме не найдено"));
            }
        } else {
            execute(update, user);
        }
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();

            if (callbackData.contains("resumeId")) {
                try {
                    int resumeId = Integer.parseInt(callbackData.split(" ")[1]);
                    selectedResume = resumeData.getObjects()[resumeId];
                    if (selectedResume.getPublished().getId() == 1) {
                        VacancyObject vacancies = vacancyService.getVacancies(user.getToken(), selectedResume.getId());
                        vacancy = vacancies.getObjects();
                        if (vacancy != null && vacancy.size() > 1) {
                            sendBotMessageService.setReplyMarkup(getResumeReplyMarkup(user.getToken()));
                            message = sendBotMessageService.sendMessage(user.getChatId(), vacancy.get(indexVacancy).toString());
                        } else {
                            sendBotMessageService.sendMessage(user.getChatId(), "Вакансий не найдено");
                        }
                        return;
                    }
                    sendBotMessageService.sendMessage(user.getChatId(), "Состояние резюме - " + selectedResume.getPublished().getTitle());
                    return;
                } catch (Exception e) {}
            }

            if (callbackData.equals("next") && indexVacancy+1 < vacancy.size()) {
                indexVacancy++;
            } else if (callbackData.equals("back") && indexVacancy-1 >= 0) {
                indexVacancy--;
            } else if (callbackData.equals("vacancyResponseFalse")) {
                vacancyService.sendResponseToVacancy(user.getToken(), selectedResume.getId(), vacancy.get(indexVacancy).getId());
            }

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setReplyMarkup(getResumeReplyMarkup(user.getToken()));
            editMessageText.setChatId(String.valueOf(user.getChatId()));
            editMessageText.setText(vacancy.get(indexVacancy).toString());
            sendBotMessageService.updateMessage(editMessageText);
        }
    }

    private InlineKeyboardMarkup getResumeReplyMarkup(String token) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> nextBackButtons = new ArrayList<>();
        nextBackButtons.add(sendBotMessageService.createRow("⬅", "back").get(0));
        nextBackButtons.add(sendBotMessageService.createRow("➡", "next").get(0));
        rowsInline.add(nextBackButtons);

        VacancyResponseObject vacancyResponseObj = resumeService.getVacancyResponse(token, selectedResume.getId());

        for (VacancyResponse vacancyResp : vacancyResponseObj.getObjects()) {
            if (vacancyResp.getId_vacancy() == vacancy.get(indexVacancy).getId()) {
                rowsInline.add(sendBotMessageService.createRow("Отклик отправлен", "vacancyResponseTrue"));
                break;
            }
        }
        if (rowsInline.size() < 2) {
            rowsInline.add(sendBotMessageService.createRow("Отправить отклик на вакансию", "vacancyResponseFalse"));
        }

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public void undo() {

    }
}