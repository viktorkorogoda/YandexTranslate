package com.example.viktor.yandextranslate.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viktor.yandextranslate.R;
import com.example.viktor.yandextranslate.models.TranslateResponse;
import com.example.viktor.yandextranslate.models.TranslatedEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Viktor on 29.04.2017.
 */

public class RowsCreator {
    public static void addTranslatedTextRow(TableLayout tableLayout, final Context context, final TranslateResponse response, Set<TranslatedEntity> bookmarks) {
        tableLayout.removeAllViews();

        for (final String text : response.getText()) {

            TableRow tr = new TableRow(context);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            final Set<TranslatedEntity> finalBookmarks = bookmarks;
            ImageView.OnClickListener clickListener = new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TranslatedEntity entity = new TranslatedEntity(
                            UUID.randomUUID(),
                            response.getLang(),
                            response.getRealText(),
                            text
                    );
                    finalBookmarks.add(entity);
                    BookmarkUtils.saveBookmark(context, finalBookmarks);
                    Toast toast = Toast.makeText(context,
                            "Сохраненов в закладки", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 3);
                    toast.show();
                }
            };

            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.ic_add_bookmark);
            LinearLayout.LayoutParams layoutParamsIV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsIV.setMargins(0, 8, 40, 0);
            iv.setLayoutParams(layoutParamsIV);
            iv.setClickable(true);
            iv.setOnClickListener(clickListener);



            TextView textView = new TextView(context);
            textView.setTag(UUID.randomUUID().toString());
            textView.setText(text);
            textView.setTextSize(25);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5f);
            layoutParams.setMargins(25, 20, 0, 0);
            textView.setLayoutParams(layoutParams);


            linearLayout.addView(textView);
            linearLayout.addView(iv);

            tr.addView(linearLayout);
            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }
    }
}
