package com.semihkurucay.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPageableRequest {

    private int page = 0;
    private int size = 10;
    private String columnName;
    private boolean asc;
}
