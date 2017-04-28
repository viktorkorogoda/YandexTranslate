package com.example.viktor.yandextranslate.models;

import java.util.UUID;

/**
 * Created by Viktor on 28.04.2017.
 */

public class TranslatedEntity {
    private UUID id;
    private String dir;
    private String text;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
