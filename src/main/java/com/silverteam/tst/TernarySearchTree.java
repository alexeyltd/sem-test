package com.silverteam.tst;

import java.util.ArrayList;
import java.util.List;

public class TernarySearchTree {

    private TSTNode root;

    private List<String> al;

    /**
     * Constructor
     **/
    public TernarySearchTree() {
        root = null;
    }

    /**
     * function to check if empty
     **/
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * function to clear
     **/
    public void makeEmpty() {
        root = null;
    }

    /**
     * function to insert for a word
     **/
    public void insert(String word) {
        root = insert(root, word.toCharArray(), 0);
    }

    /**
     * function to insert for a word
     **/
    public TSTNode insert(TSTNode r, char[] word, int ptr) {
        if (r == null)
            r = new TSTNode(word[ptr]);

        if (word[ptr] < r.data)
            r.left = insert(r.left, word, ptr);
        else if (word[ptr] > r.data)
            r.right = insert(r.right, word, ptr);
        else {
            if (ptr + 1 < word.length)
                r.middle = insert(r.middle, word, ptr + 1);
            else
                r.isEnd = true;
        }
        return r;
    }

    /**
     * function to delete a word
     **/
    public void delete(String word) {
        delete(root, word.toCharArray(), 0);
    }

    /**
     * function to delete a word
     **/
    private void delete(TSTNode r, char[] word, int ptr) {
        if (r == null)
            return;

        if (word[ptr] < r.data)
            delete(r.left, word, ptr);
        else if (word[ptr] > r.data)
            delete(r.right, word, ptr);
        else {
            /** to delete a word just make isEnd false **/
            if (r.isEnd && ptr == word.length - 1)
                r.isEnd = false;

            else if (ptr + 1 < word.length)
                delete(r.middle, word, ptr + 1);
        }
    }

    /**
     * function to search for a word
     **/
    public boolean search(String word) {
        return search(root, word.toCharArray(), 0);
    }

    /**
     * function to search for a word
     **/
    private boolean search(TSTNode r, char[] word, int ptr) {
        if (r == null)
            return false;
        if (word[ptr] < r.data)
            return search(r.left, word, ptr);
        else if (word[ptr] > r.data)
            return search(r.right, word, ptr);
        else {
            return r.isEnd && ptr == word.length - 1 || ptr != word.length - 1 && search(r.middle, word, ptr + 1);
        }
    }

    /**
     * function to print tree
     **/
    public String toString() {
        al = new ArrayList<>();
        traverse(root, "");
        return "\nTernary Search Tree : " + al;
    }

    /**
     * function to traverse tree
     **/
    private void traverse(TSTNode r, String str) {
        if (r != null) {
            traverse(r.left, str);

            str = str + r.data;
            if (r.isEnd)
                al.add(str);

            traverse(r.middle, str);
            str = str.substring(0, str.length() - 1);

            traverse(r.right, str);
        }
    }
}
