package com.semihkurucay.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exception <T> {

    private String path;
    private LocalDateTime dateTime;
    private T exceptionMessage;
}
