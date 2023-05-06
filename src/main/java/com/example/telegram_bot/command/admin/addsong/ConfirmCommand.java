package com.example.telegram_bot.command.admin.addsong;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.repository.entity.TelegramSong;
import com.example.telegram_bot.service.impl.DownloadSongRequest;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramMusicService;
import com.example.telegram_bot.state.State;
import com.example.telegram_bot.thread.DownloadSongsScraping;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfirmCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final List<MoodFolder> moodFolders;
    private final List<String> similarSongs;
    private final TelegramMusicService telegramMusicService;
    private final JobConnect jobConnect;
    private int count;

    public ConfirmCommand(
            SendBotMessageService sendBotMessageService,
            TelegramMusicService telegramMusicService,
            List<MoodFolder> moodFolder,
            List<String> similarSongs,
            JobConnect jobConnect) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramMusicService = telegramMusicService;
        this.moodFolders = moodFolder;
        this.similarSongs = similarSongs;
        this.jobConnect = jobConnect;
        count = 0;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(user.getChatId(), "Идет обработка...");
        execute(update, user);
    }

    @Override
    public void execute(Update update, User user) {
        DownloadSongRequest songRequest = new DownloadSongRequest();

        ExecutorService executor = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            executor.submit(new DownloadSongsScraping(songRequest, similarSongs));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {}


        List<TelegramSong> newSongs = songRequest.getTelegramSongs();
        List<TelegramSong> telegramSongs = telegramMusicService.findAll();

        Iterator<TelegramSong> iterator = newSongs.iterator();

        while (iterator.hasNext()) {
            TelegramSong newSong = iterator.next();
            String title = newSong.getTitle().toLowerCase();
            String artist = newSong.getArtist().toLowerCase();
            for (TelegramSong ts : telegramSongs) {
                if (ts.getTitle().toLowerCase().contains(title) && ts.getArtist().toLowerCase().contains(artist)) {
                    iterator.remove();
                    break;
                }
            }
        }

        telegramMusicService.save(moodFolders, newSongs);
        sendBotMessageService.sendMessage(user.getChatId(), "Успешно добавлено!");
    }

    @Override
    public void undo() {

    }
}
