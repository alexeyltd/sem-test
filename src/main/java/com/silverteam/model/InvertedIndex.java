package com.silverteam.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InvertedIndex {

    private Map<String, Set<Long>> wordToFiles;

    public InvertedIndex() {
        wordToFiles = new HashMap<>();
    }

    public Set<Long> findFiles(Set<String> words) {
        Set<Set<Long>> filesWhichContainWord = words.stream()
                .filter(wordToFiles::containsKey)
                .map(wordToFiles::get)
                .collect(Collectors.toSet());
        Set<Long> result = filesWhichContainWord.size() > 0 ? filesWhichContainWord.iterator().next() : new HashSet<>();
        filesWhichContainWord.forEach(result::retainAll);
        return result;
    }

    public void addWord(String word, Long file) {
        Set<Long> files = wordToFiles.get(word);
        if (null != files) {
            files.add(file);
        } else {
            files = new HashSet<>();
            files.add(file);
            wordToFiles.put(word, files);
        }
    }

}
