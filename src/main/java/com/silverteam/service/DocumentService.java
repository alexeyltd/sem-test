package com.silverteam.service;

import com.silverteam.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    ResponseMetadata save(MultipartFile multipartFile) throws IOException;

    byte[] getDocumentFile(Long id);

    List<Document> findAll();

    List<byte[]> getDocumentByWord(String word);
}
