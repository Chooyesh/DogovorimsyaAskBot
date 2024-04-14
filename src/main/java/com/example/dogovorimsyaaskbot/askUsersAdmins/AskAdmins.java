package com.example.dogovorimsyaaskbot.askUsersAdmins;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity ( name = "AskAdmins")
public class AskAdmins {
    @Id
    private String userName;
    private String status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getUserName() + " - " + getStatus();
    }

}
