package com.example.dogovorimsyaaskbot.asksDataHouse;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity ( name = "AsksRepository")
public class Ask {
    @Id
    private long numAsk;
    private String user;
    private String ask;

    public long getNumAsk() {
        return numAsk;
    }

    public void setNumAsk(long numAsk) {
        this.numAsk = numAsk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }
}
