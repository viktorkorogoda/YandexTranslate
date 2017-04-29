package com.example.viktor.yandextranslate.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.viktor.yandextranslate.models.TranslatedEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Viktor on 29.04.2017.
 */

public class BookmarkUtils {

    public static Set<TranslatedEntity> readBookmark(Context context) {
        Set<TranslatedEntity> returnList = new HashSet<>();
        FileInputStream fis;
        try {
            if (fileExists(context, "bookmarks")) {
                fis = context.openFileInput("bookmarks");

                ObjectInputStream ois = new ObjectInputStream(fis);
                TranslatedEntity entity;
                while ((entity = (TranslatedEntity) ois.readObject()) != null) {
                    returnList.add(entity);
                }
                ois.close();
            } else returnList = new HashSet<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    private static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public static void saveBookmark(Context context, Set<TranslatedEntity> bookmarks) {

        FileOutputStream fos = null;
        try {
            context.deleteFile("bookmarks");
            fos = context.openFileOutput("bookmarks", Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (TranslatedEntity entity : bookmarks) {
                oos.writeObject(entity);
            }
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fillBookmarks(LinearLayout frameLayout, Context context, Set<TranslatedEntity> bookmarks) {

        for (TranslatedEntity entity : bookmarks) {

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);


            LinearLayout frameLayoutText = new LinearLayout(context);
            frameLayoutText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 5F));
            frameLayoutText.setOrientation(LinearLayout.VERTICAL);

            TextView translatedTextView = new TextView(context);
            translatedTextView.setText(entity.getTranslatedText());
            translatedTextView.setTextSize(18);
            LinearLayout.LayoutParams translatedTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            translatedTextParams.setMargins(15, 0, 0, 0);
            translatedTextView.setLayoutParams(translatedTextParams);


            TextView realTextView = new TextView(context);
            realTextView.setText(entity.getRealText());
            realTextView.setTextSize(16);
            realTextView.setTypeface(null, Typeface.BOLD);
            LinearLayout.LayoutParams realTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            realTextParams.setMargins(15, 15, 0, 0);
            realTextView.setLayoutParams(realTextParams);


            TextView dirTextView = new TextView(context);
            dirTextView.setText(entity.getDir());
            dirTextView.setTextSize(20);
            dirTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1F));


            frameLayoutText.addView(realTextView);
            frameLayoutText.addView(translatedTextView);
            linearLayout.addView(frameLayoutText);
            linearLayout.addView(dirTextView);

            frameLayout.addView(linearLayout);
        }
    }
}
