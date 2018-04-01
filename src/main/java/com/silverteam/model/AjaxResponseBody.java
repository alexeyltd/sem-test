package com.silverteam.model;

import lombok.Data;

import java.util.List;

@Data
public class AjaxResponseBody {
    String msg;
//    List<Document> documentList;
    List<byte[]> bytes;
}
