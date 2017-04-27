package com.example.viktor.yandextranslate.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Viktor on 25.04.2017.
 */
public class Languages {
    private ArrayList<String> dirs;
    private HashMap<String, String> langs;

    public ArrayList<String> getDirs() {
        return dirs;
    }

    public void setDirs(ArrayList<String> dirs) {
        this.dirs = dirs;
    }

    public HashMap<String, String> getLangs() {
        return langs;
    }

    public void setLangs(HashMap<String, String> langs) {
        this.langs = langs;
    }
}
