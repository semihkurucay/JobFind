package com.semihkurucay.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private String message;
    private ErrorType errorType;

    public String prepareErrorMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(errorType.getMessage());

        if(message != null) {
            builder.append(" : " + message);
        }

        return builder.toString();
    }
}
