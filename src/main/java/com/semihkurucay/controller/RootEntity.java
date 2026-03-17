package com.semihkurucay.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RootEntity <T> {

    private Integer status;
    private T data;

    public static <T> RootEntity<T> ok(T data) {
        RootEntity<T> root = new RootEntity<T>();
        root.status = HttpStatus.OK.value();
        root.data = data;
        return root;
    }
}
