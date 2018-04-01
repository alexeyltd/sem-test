package com.silverteam.controller;

import com.silverteam.model.AjaxResponseBody;
import com.silverteam.model.Document;
import com.silverteam.model.SearchCriteria;
import com.silverteam.service.DocumentService;
import com.silverteam.service.ResponseMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/doc")
public class DocumentController extends AdviceController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload")
    public @ResponseBody
    ResponseMetadata handleFileUpload(@RequestParam(value="file") MultipartFile file) throws IOException {
        log.info("saving file: " + file.getOriginalFilename());
        return documentService.save(file);
    }

    @GetMapping(value = "/{id}")
    public HttpEntity<byte[]> getDocument(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("text", "plain", Charset.forName("UTF-8")));
        return new ResponseEntity<>(documentService.getDocumentFile(id), httpHeaders, HttpStatus.OK);
    }

    @GetMapping
    public @ResponseBody
    List<Document> getDocument() {
        return documentService.findAll();
    }

    @PostMapping(value = "/words")
    public ResponseEntity<?> getDocumentByWords(@RequestBody SearchCriteria search, Errors errors) {
        AjaxResponseBody result = new AjaxResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {
            result.setMsg(errors.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        List<byte[]> documentByWord = documentService.getDocumentByWord(search.getWords());
        if (documentByWord.isEmpty()) {
            result.setMsg("no document found!");
        } else {
            result.setMsg("success");
        }
        result.setBytes(documentByWord);
        return ResponseEntity.ok(result);
    }

}
