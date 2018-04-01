package com.silverteam.service;

import lombok.Data;

@Data
public class ResponseMetadata {

    private int status;
    private String message;
    private Object data;

}
