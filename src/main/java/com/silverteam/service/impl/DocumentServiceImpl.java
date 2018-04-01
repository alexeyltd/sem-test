package com.silverteam.service.impl;

import com.silverteam.dao.DocumentDao;
import com.silverteam.model.Document;
import com.silverteam.model.InvertedIndex;
import com.silverteam.service.DocumentService;
import com.silverteam.service.ResponseMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentDao documentDao;
    private final InvertedIndex invertedIndex = new InvertedIndex();

    @Value("${project.folderName}")
    private String folderName;

    private byte[] readAllBytes(Document document) {
        try {
            return Files.readAllBytes(Paths.get(document.getDocNameOnDisk()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private Document apply(Long file) {
        return documentDao.findById(file).get();
    }

    private void saveToInvertedIndex(MultipartFile file, Document doc) throws IOException {
        String stringFromFile = new String(file.getBytes());
        Matcher matcher = getMatcher(stringFromFile);
        while (matcher.find()) {
            invertedIndex.addWord(matcher.group(), doc.getId());
        }
    }

    private void deleteFilesIfExists() throws IOException {
        if (Files.exists(Paths.get(folderName))) {
            Files.walkFileTree(Paths.get(folderName), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void saveFileToDisk(MultipartFile file, Document doc) throws IOException {
        log.info("Deleting fileFolder if exists");
        deleteFilesIfExists();
        Files.createDirectory(Paths.get(folderName));
        try {
            log.info("Generating unique name for a file: " + file.getOriginalFilename());
            UUID uuid = UUID.randomUUID();
            String filename = uuid.toString();
            log.info("Creating file on Disk: " + file.getOriginalFilename());
            Path path = Files.createFile(Paths.get(folderName, filename));
            doc.setDocNameOnDisk(path.toString());
            Files.write(path, file.getBytes());
        }
        catch (Exception e) {
            e.getMessage();
        }
    }

    private Matcher getMatcher(String words) {
        Pattern pattern = Pattern.compile("\\w+");
        return pattern.matcher(words);
    }

    @PreDestroy
    private void deleteFileFromDisk() throws IOException {
        deleteFilesIfExists();
    }

    @Override
    @Transactional
    public ResponseMetadata save(MultipartFile file) throws IOException {
        Document doc = new Document();
        doc.setDocName(file.getOriginalFilename());
        log.info("Saving file to a disk: " + file.getOriginalFilename());
        saveFileToDisk(file, doc);
        log.info("Saving metaDate to database : " + file.getOriginalFilename());
        documentDao.save(doc);
        log.info("Saving file to a invertedIndex: " + file.getOriginalFilename());
        saveToInvertedIndex(file, doc);
        ResponseMetadata metadata = new ResponseMetadata();
        metadata.setMessage("success");
        metadata.setStatus(200);
        return metadata;
    }

    @Override
    public byte[] getDocumentFile(Long id) {
        log.info("Getting file by id");
        Document document = documentDao.findById(id).get();
        String docNameOnDisk = document.getDocNameOnDisk();
        Path path = Paths.get(docNameOnDisk);
        byte[] bytes = new byte[0];
        try {
            log.info("Reading bytes from filePath: " + path.toString());
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public List<Document> findAll() {
        log.info("Finding all");
        return (List<Document>) documentDao.findAll();
    }

    @Override
    public List<byte[]> getDocumentByWord(String words) {
        log.info("Getting document By Word");
        Set<String> wordsSet = new HashSet<>();
        List<byte[]> bytes;
        Matcher matcher = getMatcher(words);
        while (matcher.find()) {
            wordsSet.add(matcher.group());
        }
        Set<Long> files = invertedIndex.findFiles(wordsSet);
        bytes = files.stream()
                .map(this::apply)
                .map(this::readAllBytes)
                .collect(Collectors.toList());
        return bytes;
    }
}
