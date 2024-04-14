package com.example.dogovorimsyaaskbot.configuration;

import com.example.dogovorimsyaaskbot.bot.AskBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class AskBotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi (AskBot askBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(askBot);
        return api;
    }
}
