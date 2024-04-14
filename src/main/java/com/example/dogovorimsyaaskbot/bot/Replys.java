package com.example.dogovorimsyaaskbot.bot;

public class Replys {
    public static final String MSG_SENDING_MODE = "\uD83E\uDD16Отправь сообщение с вопросом";
    public static final String MSG_SENDING_DONE = "\uD83E\uDD16Твой вопрос отправлен команде";
    public static final String MSG_HELP = """
            \uD83E\uDD16Я бот команды "Договоримся"
            С моей помощью ты можешь задать интересующий тебя вопрос моим коллегам
            для этого используй команду /sendmyask,
            если вопросов несколько, то отправляй их по-очереди
            
            Для завершения режима отправки жми команду /cancel
            
            /edit - а это что за команда?
            """;
    public static final String MSG_ADMINS_EDITOR = """
            \uD83E\uDD16Режим управленя админами
            Отправь сообщение:
            Чтобы удалить: '-remove '
            Чтобы добавить: '-add '
            Пример '-add user'
            /getAdmins выдаст список администраторов""";
}
