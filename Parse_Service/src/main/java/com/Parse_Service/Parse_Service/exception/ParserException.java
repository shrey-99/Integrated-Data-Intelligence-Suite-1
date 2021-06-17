package com.Parse_Service.Parse_Service.exception;

public class ParserException extends Exception {
    public ParserException(String message){
        super(message);
    }

    public ParserException(Throwable cause){
        super(cause);
    }

    public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
