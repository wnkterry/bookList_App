package com.example.kathy.booklist;

/**
 * Created by Kathy on 11/22/2017.
 */

public class bookInfo {

    private String title;
    private String author;
    private String url;

    public bookInfo(String title, String author){
        this.title =title;
        this.author = author;
    }

    public bookInfo(String title, String author, String url){
        this.title =title;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}


