package com.silverteam.tst;

public class TSTNode {
    char data;
    boolean isEnd;
    TSTNode left, middle, right;

    public TSTNode(char data) {
        this.data = data;
        this.isEnd = false;
        this.left = null;
        this.middle = null;
        this.right = null;
    }
}
