package com.example.viktor.yandextranslate.models;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Viktor on 28.04.2017.
 */

public class TranslatedEntity implements Serializable {
    private UUID id;
    private String dir;
    private String realText;
    private String translatedText;
    private static final long serialVersionUID = 29238982928391L;

    public TranslatedEntity(UUID id, String dir, String realText, String translatedText) {
        this.id = id;
        this.dir = dir;
        this.realText = realText;
        this.translatedText = translatedText;
    }

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

    public String getRealText() {
        return realText;
    }

    public void setRealText(String realText) {
        this.realText = realText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslatedEntity entity = (TranslatedEntity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (!dir.equals(entity.dir)) return false;
        if (!realText.equals(entity.realText)) return false;
        return translatedText.equals(entity.translatedText);

    }

    @Override
    public int hashCode() {
        int result = dir.hashCode();
        result = 31 * result + realText.hashCode();
        result = 31 * result + translatedText.hashCode();
        return result;
    }
}
