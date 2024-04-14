package com.example.dogovorimsyaaskbot.modelData;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;


@Entity(name = "userAskBotTable")
public class User {
    @Id
    private long chatId;
    private String userName;
    private String chatMode;
    private String firstName;
    private LocalDate date;
    private int countMsgPerDay;

    private String userStatus;

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCountMsgPerDay() {
        return countMsgPerDay;
    }

    public void setCountMsgPerDay(int countMsgPerDay) {
        this.countMsgPerDay = countMsgPerDay;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChatMode() {
        return chatMode;
    }

    public void setChatMode(String chatMode) {
        this.chatMode = chatMode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "User{" + "chatId=" + chatId + ", userName='" + userName + ", userId=" + firstName + ", chatMode='" + chatMode + '}';
    }
}
