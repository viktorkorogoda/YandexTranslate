package com.example.viktor.yandextranslate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.viktor.yandextranslate.models.LanguageDirection;
import com.example.viktor.yandextranslate.models.Languages;
import com.example.viktor.yandextranslate.models.TranslateResponse;
import com.example.viktor.yandextranslate.models.TranslatedEntity;
import com.example.viktor.yandextranslate.nettwork.CustomJSONObjectRequest;
import com.example.viktor.yandextranslate.nettwork.CustomVolleyRequestQueue;
import com.example.viktor.yandextranslate.utils.BookmarkUtils;
import com.example.viktor.yandextranslate.utils.EditTextWatcher;
import com.example.viktor.yandextranslate.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.example.viktor.yandextranslate.utils.RowsCreator.addTranslatedTextRow;


public class TranslateActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    private EditText editText;
    private boolean userIsInteracting;
    private final String defaultLng = "ru";
    private boolean parseLng = false;
    private boolean translate = false;
    private Spinner spinnerLeft;
    private Spinner spinnerRight;
    private static final String REQUEST_TAG = "TranslateActivity";
    private LanguageDirection languageDirection;
    private String lngFrom = "";
    private String lngTo = "";
    private HashMap<Integer, HashMap<String, String>> lngCollections;
    private FrameLayout translateFrameLayout;
    private LinearLayout bookmarkFrameLayout;
    private FrameLayout settingsFrameLayout;
    private Set<TranslatedEntity> bookmarks;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_translate:
                    settingsFrameLayout.setVisibility(View.INVISIBLE);
                    bookmarkFrameLayout.setVisibility(View.INVISIBLE);
                    translateFrameLayout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_bookmark:
                    settingsFrameLayout.setVisibility(View.INVISIBLE);
                    translateFrameLayout.setVisibility(View.INVISIBLE);
                    bookmarkFrameLayout.setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.bookmark)).removeAllViews();
                    BookmarkUtils.fillBookmarks((LinearLayout) findViewById(R.id.bookmark), getApplicationContext(), bookmarks);
                    return true;
                case R.id.navigation_settings:
                    settingsFrameLayout.setVisibility(View.VISIBLE);
                    bookmarkFrameLayout.setVisibility(View.INVISIBLE);
                    translateFrameLayout.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bookmarks = BookmarkUtils.readBookmark(this);
        languageDirection = new LanguageDirection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        editText = (EditText) findViewById(R.id.editText);
        spinnerLeft = (Spinner) findViewById(R.id.spinnerLeft);
        spinnerRight = (Spinner) findViewById(R.id.spinnerRight);
        AppCompatImageButton buttonSwapLng = (AppCompatImageButton) findViewById(R.id.buttonSwapLng);
        Button buttonTranslate = (Button) findViewById(R.id.buttonTranslate);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        translateFrameLayout = (FrameLayout) findViewById(R.id.translate);
        bookmarkFrameLayout = (LinearLayout) findViewById(R.id.bookmark);
        settingsFrameLayout = (FrameLayout) findViewById(R.id.settings);

        spinnerLeft.setOnItemSelectedListener(selectLeft);
        spinnerRight.setOnItemSelectedListener(selectRight);
        buttonTranslate.setOnClickListener(onClickListener);
        buttonSwapLng.setOnClickListener(onClickListenerSwapLng);
        getLanguages(defaultLng);
        editText.addTextChangedListener(new EditTextWatcher(spinnerLeft, spinnerRight, buttonSwapLng, editText));
    }

    private Button.OnClickListener onClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            sendToTranslate(editText.getText().toString(), lngFrom + "-" + lngTo);
            translate = true;
        }
    };

    private Button.OnClickListener onClickListenerSwapLng = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            String lngFrom = languageDirection.getLngFrom();
            int lngFromInd = languageDirection.getLngFromInd();

            languageDirection.setLngFrom(languageDirection.getLngTo());
            languageDirection.setLngFromInd(languageDirection.getLngToInd());
            languageDirection.setLngTo(lngFrom);
            languageDirection.setLngToInd(lngFromInd);

            spinnerLeft.setSelection(languageDirection.getLngFromInd());
            spinnerRight.setSelection(languageDirection.getLngToInd());
        }
    };

    private Spinner.OnItemSelectedListener selectLeft = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userIsInteracting) {
                lngFrom = lngCollections.get(position).keySet().iterator().next();

                languageDirection.setLngFrom(lngCollections.get(position).keySet().iterator().next());
                languageDirection.setLngFromInd(position);
                userIsInteracting = false;
            }
            if (lngFrom.length() == 0)
                lngFrom = lngCollections.get(position).keySet().iterator().next();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private Spinner.OnItemSelectedListener selectRight = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userIsInteracting) {
                lngTo = lngCollections.get(position).keySet().iterator().next();
                languageDirection.setLngTo(lngCollections.get(position).keySet().iterator().next());
                languageDirection.setLngToInd(position);
                userIsInteracting = false;
            }
            if (lngTo.length() == 0)
                lngTo = lngCollections.get(position).keySet().iterator().next();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void fillSpinner(Spinner spinner, HashMap<String, String> languageNames) {
        ArrayAdapter<String> viewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(languageNames.values()));
        viewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(viewAdapter);
    }


    private void addToLanguageCollections(HashMap<String, String> languageNames) {
        if (this.lngCollections == null) {
            this.lngCollections = new HashMap<>();
            int i = 0;
            for (String key : languageNames.keySet()) {
                HashMap<String, String> map = new HashMap<>();
                map.put(key, languageNames.get(key));
                this.lngCollections.put(i, map);
                i++;
            }
        }
    }

    private void sendToTranslate(String text, String lang) {
        translate = true;
        String key = "trnsl.1.1.20170424T122712Z.30bb3eb21a38e99d.82d54b665ec3a394c5d8c82b49c5457f1be77d60";
        String url = null;
        try {
            url = "https://translate.yandex.net/api/v1.5/tr.json/translate?text="
                    + URLEncoder.encode(text, "UTF-8")
                    + "&lang="
                    + lang
                    + "&key="
                    + key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        addRequestToQueue(url);
    }


    private void getLanguages(String lng) {
        this.parseLng = true;
        String key = "trnsl.1.1.20170424T122712Z.30bb3eb21a38e99d.82d54b665ec3a394c5d8c82b49c5457f1be77d60";
        String url = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?ui="
                + lng
                + "&key="
                + key;
        addRequestToQueue(url);
    }


    private void addRequestToQueue(String url){
        //полуение очередеи для http запросо и помещение запроса в очередь
        RequestQueue mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    public void parseLanguages(String response) {
        Gson gson = new Gson();
        Languages languages = gson.fromJson(response, Languages.class);

        HashMap<String, String> lanuagesMap = Utils.sortByValue(languages.getLangs());
        addToLanguageCollections(lanuagesMap);

        fillSpinner(spinnerLeft, lanuagesMap);
        fillSpinner(spinnerRight, lanuagesMap);
    }

    private void showTranslatedText(String text) {
        Gson gson = new Gson();
        TranslateResponse response = gson.fromJson(text, TranslateResponse.class);
        response.setRealText(editText.getText().toString());
        TableLayout tableLayout = (TableLayout) findViewById(R.id.translateTable);
        addTranslatedTextRow(tableLayout, this, response, bookmarks);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    public void onResponse(Object response) {
        if (parseLng) {
            parseLanguages(response.toString());
            parseLng = false;
        }
        if (translate) {
            showTranslatedText(response.toString());
            translate = false;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
