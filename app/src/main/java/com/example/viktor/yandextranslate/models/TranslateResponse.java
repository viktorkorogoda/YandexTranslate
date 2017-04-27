package com.example.viktor.yandextranslate.models;

import java.util.List;

/**
 * Created by Viktor on 27.04.2017.
 */

public class TranslateResponse {
    private int code;
    private String lang;
    private List<String> text;

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
