package com.example.viktor.yandextranslate.models;

import java.util.List;
import java.util.UUID;

/**
 * Created by Viktor on 27.04.2017.
 */

public class TranslateResponse {
    private UUID id;


    private int code;
    private String lang;
    private List<String> text;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }
}
