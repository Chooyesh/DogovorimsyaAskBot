package com.example.dogovorimsyaaskbot.bot;


import com.example.dogovorimsyaaskbot.askUsersAdmins.AskAdmins;
import com.example.dogovorimsyaaskbot.askUsersAdmins.AskAdminsRepository;
import com.example.dogovorimsyaaskbot.asksDataHouse.Ask;
import com.example.dogovorimsyaaskbot.asksDataHouse.AskRepository;
import com.example.dogovorimsyaaskbot.dataBaseParser.DataBaseExporter;
import com.example.dogovorimsyaaskbot.modelData.User;
import com.example.dogovorimsyaaskbot.modelData.UserRepostitory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AskBot extends TelegramLongPollingBot {



    //final private String BOT_TOKEN = ""; //тест токен
    final private String BOT_TOKEN = ""; //основной токен
    final private String BOT_NAME = "";
    private static final Logger LOG = LoggerFactory.getLogger(AskBot.class);

    @Autowired
    private UserRepostitory userRepostitory;

    @Autowired
    private AskRepository askRepository;


    @Override
    public void onUpdateReceived(Update update) {//main method
        if (update.hasMessage() || update.getMessage().hasText()) {
            var messageText = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            Message message = update.getMessage();
            registerUser(update.getMessage());
            String mode = getUserModeStr(chatId);
            if (mode.equals(Commands.MODE_COMMAND)) {
                switch (messageText) {
                    case Commands.CMD_START -> {
                        registerUser(update.getMessage());
                        sendStartMessage(message);
                    }
                    case Commands.CMD_SEND_ASK -> {
                        setUserChatMode(message, Commands.MODE_SENDING);
                        sendMessage(chatId, Replys.MSG_SENDING_MODE, UserKeyboards.getCancellTrayKeyboard());
                    }
                    case Commands.CMD_HELP -> {
                        sendMessage(chatId, Replys.MSG_HELP, getMainKeyBoardAdminOrUser(message));
                    }
                    case Commands.CMD_EDIT -> {
                        sendMessage(chatId, "\uD83E\uDD16Эта функция находится в разработке", getMainKeyBoardAdminOrUser(message));
                    }
                    case Commands.CMD_EDIT_ADMINS -> {
                        if (isHeAdmin(message)){
                            sendMessage(chatId, Replys.MSG_ADMINS_EDITOR, UserKeyboards.getCancellTrayKeyboard());
                            setUserChatMode(message,Commands.MODE_EDITOR_ADMINS);
                        }
                        else sendMessage(chatId, "\uD83E\uDD16Как ты узнал об этом, пользователь??");

                    }
                    case Commands.CMD_GET_MESSAGES -> {
                        if (isHeAdmin(message)) {
                            //todo
                            try {
                                sendMessageAndDocument(chatId, getMainKeyBoardAdminOrUser(message));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    case Commands.CMD_CANCEL -> {
                        sendCancellMessage(message, chatId, getMainKeyBoardAdminOrUser(message));
                    }
                    default -> {
                        unknownCommand(message);
                    }
                }
            }
            if (mode.equals(Commands.MODE_SENDING)) {
                switch (messageText) {
                    case Commands.CMD_CANCEL -> {
                        sendCancellMessage(message, chatId, getMainKeyBoardAdminOrUser(message));
                    }
                    default -> sendingMode(message, chatId, messageText);
                }
            }
            if (mode.equals(Commands.MODE_EDITOR_ADMINS) && isHeAdmin(message)) {
                switch (messageText) {
                    case Commands.CMD_CANCEL -> {
                        sendCancellMessage(message, chatId, getMainKeyBoardAdminOrUser(message));
                    }
                    case Commands.CMD_GET_ADMINS -> {getAdminList(message);}
                    default -> adminsEditingMode(message, messageText);
                }
            }


        }
    }

    private void getAdminList(Message msg){
        String text = "\uD83E\uDD16"+ "\n";
        long size = adminsRepository.count();
        Iterable<AskAdmins> findallitter = adminsRepository.findAll();
        List<AskAdmins> adminsList = new ArrayList<>();
        for (AskAdmins askAdmins : findallitter){
            adminsList.add(askAdmins);
        }
        for (int i = 0; i< adminsList.size();i++){
            text= text+ adminsList.get(i)+ "\n";
        }

        sendMessage(msg.getChatId(),text, UserKeyboards.getCancellTrayKeyboard());
    }

    private void adminsEditingMode(Message msg, String textMessage){ //-remove user //-add user
        var chat = msg.getChat();
        if(textMessage.contains("remove") || textMessage.contains("add")){
            String[] mssStr = textMessage.split(" ");
            String user = mssStr[1];
            if(textMessage.contains("-remove")){
                if (adminsRepository.findById(user).isPresent()){
                    if(!user.equals("chooyesh")){
                        adminsRepository.deleteById(user);
                        sendMessage(msg.getChatId(), "\uD83E\uDD16Пользователь " + user + " удален!");
                    }
                    else {
                            sendMessage(msg.getChatId(), "\uD83E\uDD16ВЫ СОВЕРШИЛИ ОШИБКУ, БОГА УДАЛИТЬ НЕВОЗМОЖНО!");
                    }
                }
            }
            if (textMessage.contains("-add")){
                if(adminsRepository.findById(user).isPresent()){
                    sendMessage(msg.getChatId(), "\uD83E\uDD16Пользователь " + user + " уже является администратором!");
                }
                if (!adminsRepository.findById(user).isPresent()){
                    AskAdmins newAdmin = new AskAdmins();
                    newAdmin.setUserName(user);
                    newAdmin.setStatus(Commands.STATUS_ADMIN);
                    if(user.equals("chooyesh")){
                        newAdmin.setStatus("THE CREATOR");
                    }
                    adminsRepository.save(newAdmin);
                    sendMessage(msg.getChatId(), "\uD83E\uDD16Пользователь " + user + " добавлен!");
                }

            }
        }
        else sendMessage(msg.getChatId(), "\uD83E\uDD16Команда не распознана");

    }


    /*private void registerUser(Message msg) {
        if (userRepostitory.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
            User user = new User();
            user.setChatId(chatId);
            if(!chat.getUserName().isEmpty()){
                user.setUserName(chat.getUserName());
            } else if(chat.getUserName().isEmpty()){user.setUserName("UnknownUser");}
            user.setChatMode(Commands.MODE_COMMAND);
            user.setUserStatus(Commands.STATUS_USER);

            if (msg.getChat().getUserName().equals("thealexash") || msg.getChat().getUserName().equals("chooyesh")){
                user.setUserStatus(Commands.STATUS_ADMIN);
            }
            user.setDate(LocalDate.now());
            user.setCountMsgPerDay(3);
            user.setFirstName(msg.getChat().getFirstName());
            if (msg.getChat().getFirstName().isEmpty()) {
                user.setFirstName(msg.getChat().getUserName());
                if(chat.getUserName().isEmpty()){user.setUserName("UnknownUser");}
            }
            userRepostitory.save(user);
            LOG.info("User saved" + user);
        }
    }*/ //регистрация пользователя, не регает пользователя без юзернейма

    private void registerUser(Message msg) {
        if (userRepostitory.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setDate(LocalDate.now());
            user.setCountMsgPerDay(3);
            user.setChatMode(Commands.MODE_COMMAND);
            user.setUserStatus(Commands.STATUS_USER);


            if (!msg.getChat().getFirstName().isBlank()) {
                user.setFirstName(msg.getChat().getFirstName());
            }
            if (!msg.getChat().getFirstName().isEmpty()) {
                user.setUserName(msg.getChat().getUserName());
            }
            if (msg.getChat().getFirstName().isEmpty() && msg.getChat().getUserName().isEmpty()) {
                user.setUserName("Пользователь");
            }

            /*if (null != chat.getUserName()) {
                if (chat.getUserName().equals("thealexash") || chat.getUserName().equals("chooyesh")) {
                    user.setUserStatus(Commands.STATUS_ADMIN);
                }
            }*/ //закоментирована с учетом появления бд админов
            userRepostitory.save(user);
            LOG.info("User saved" + user);
        }
    }

    private void setUserChatMode(Message msg, String mode) {
        if (userRepostitory.findById(msg.getChatId()).isPresent()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setUserStatus(Commands.STATUS_USER);
            user.setDate(LocalDate.now());
            user.setCountMsgPerDay(3);
            user.setChatMode(mode);
            if (!msg.getChat().getFirstName().isEmpty()) {
                user.setFirstName(msg.getChat().getFirstName());
                ;
            }
            if (!msg.getChat().getFirstName().isEmpty()) {
                user.setUserName(msg.getChat().getUserName());
            }
            if (msg.getChat().getFirstName().isEmpty() && msg.getChat().getUserName().isEmpty()) {
                user.setUserName("Пользователь");
            }

            userRepostitory.save(user);
            LOG.info("User edited" + user);
        }
    } // метод изменения состояния чата

    @Autowired
    AskAdminsRepository adminsRepository;

    private boolean isHeAdmin(Message msg) {
        boolean adminBool = false;
        var chat = msg.getChat();
        if (chat.getUserName() != null) {
            String user = chat.getUserName();
            if (adminsRepository.findById(user).isPresent()){
                adminBool = true;
            }
        }
        return adminBool;
    }

    public void addNewAskToRepository(Message message, String askText) {//метод занесения вопроса в БД
        long size = askRepository.count();
        Ask ask = new Ask();
        ask.setAsk(askText);
        if (message.getChat().getUserName()!=null) {
            ask.setUser(message.getChat().getUserName());
        }
        if (message.getChat().getUserName()==null) {
            ask.setUser("Неизвестный пользователь");
        }

        ask.setNumAsk(size + 1);
        askRepository.save(ask);
        LOG.info("Question saved" + ask);
    }

    private void sendMessage(Long chatID, String text) {
        var chatIDstr = String.valueOf(chatID);
        var sendMessage = new SendMessage(chatIDstr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Send message error", e);
        }
    } // базовый метод отправки сообщения

    private void sendMessage(Long chatID, String text, ReplyKeyboardMarkup keyboardMarkup) {
        var chatIDstr = String.valueOf(chatID);
        var sendMessage = new SendMessage(chatIDstr, text);
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Send message error", e);
        }
    } //перегрузка базового с клавиатурой

    private void sendCancellMessage(Message msg, Long chatID, ReplyKeyboardMarkup keyboardMarkup) { // отправить только клавиатуру
        var chatIDstr = String.valueOf(chatID);
        var sendMessage = new SendMessage(chatIDstr, "\uD83E\uDD16Вы вернулись в главное меню");
        sendMessage.setReplyMarkup(keyboardMarkup);
        setUserChatMode(msg, Commands.MODE_COMMAND);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Send message error", e);
        }
    } //сообщение об отмне возврат в командный режим


    private ReplyKeyboardMarkup getMainKeyBoardAdminOrUser(Message msg) {
        if (isHeAdmin(msg)) {
            return UserKeyboards.getMainAdminKeyboard();
        }
        return UserKeyboards.getMainUserKeyboard();
    } //выбор клавиатуры в зависимости от статуса юзера



    public String getUserModeStr(Long chatid) {
        Optional<User> user = userRepostitory.findById(chatid);
        String usermode = user.get().getChatMode();
        //System.out.println(usermode);
        return usermode;
    } //получить режим чата из БД


    public void sendingMode(Message msg, Long chatId, String readedStr) {
        addNewAskToRepository(msg, readedStr);
        sendMessage(chatId, Replys.MSG_SENDING_DONE, UserKeyboards.getCancellTrayKeyboard());
    }//метод отправки вопроса[состояние чата]

    public void sendStartMessage(Message message) {
        String user = "";
        if (!message.getChat().getFirstName().isEmpty()) {
            user = message.getChat().getFirstName();
        } else if (message.getChat().getFirstName().isEmpty()) {
            user = message.getChat().getUserName();
        } else if (message.getChat().getFirstName().isEmpty() && message.getChat().getUserName().isEmpty()) {
            user = "Пользователь";
        }
        String text = """
                \uD83E\uDD16Привет,\t""" + user + """
                \nЭто бот команды "Договоримся"
                Отправь мне свой вопрос, а я передам его своей команде
                Нажав кнопку "/help" ты получишь более подробную инструкцию
                """;

        sendMessage(message.getChatId(), text, getMainKeyBoardAdminOrUser(message));
    } //метод команды старт

    private void unknownCommand(Message msg) {
        var text = "\uD83E\uDD16Не удалось распознать команду!";
        sendMessage(msg.getChatId(), text, getMainKeyBoardAdminOrUser(msg));
    } //если команда не распознана

    @Autowired
    DataBaseExporter dataBaseExporter; //Метод ниже вотправляет файл в чат

    public void sendMessageAndDocument(Long chatID, ReplyKeyboardMarkup keyboardMarkup) throws FileNotFoundException {
        SendDocument sendDocument = new SendDocument();
        InputFile file = new InputFile();

        file.setMedia(dataBaseExporter.createFile());

        sendDocument.setDocument(file);
        sendDocument.setChatId(chatID);
        sendDocument.setCaption("\uD83E\uDD16Вот твой файл с вопросами");
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            LOG.error("Send message error", e);
        }
    }


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


}
