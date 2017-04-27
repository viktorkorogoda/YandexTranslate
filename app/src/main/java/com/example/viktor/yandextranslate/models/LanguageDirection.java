package com.example.viktor.yandextranslate.models;


/**
 * Created by Viktor on 27.04.2017.
 */

public class LanguageDirection {
    private String lngFrom;
    private int lngFromInd;
    private String lngTo;
    private int lngToInd;

    public String getLngFrom() {
        return lngFrom;
    }

    public void setLngFrom(String lngFrom) {
        this.lngFrom = lngFrom;
    }

    public int getLngFromInd() {
        return lngFromInd;
    }

    public void setLngFromInd(int lngFromInd) {
        this.lngFromInd = lngFromInd;
    }

    public String getLngTo() {
        return lngTo;
    }

    public void setLngTo(String lngTo) {
        this.lngTo = lngTo;
    }

    public int getLngToInd() {
        return lngToInd;
    }

    public void setLngToInd(int lngToInd) {
        this.lngToInd = lngToInd;
    }
}
