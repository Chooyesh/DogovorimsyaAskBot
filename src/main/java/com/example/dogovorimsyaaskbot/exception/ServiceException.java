package com.example.dogovorimsyaaskbot.exception;

public class ServiceException extends Exception{
    public ServiceException (String message, Throwable cause){
        super(message, cause);
    }
}
