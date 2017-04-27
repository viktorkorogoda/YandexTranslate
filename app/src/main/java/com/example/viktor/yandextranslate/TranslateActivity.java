package com.example.viktor.yandextranslate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.viktor.yandextranslate.models.LanguageDirection;
import com.example.viktor.yandextranslate.models.Languages;
import com.example.viktor.yandextranslate.models.TranslateResponse;
import com.example.viktor.yandextranslate.nettwork.CustomJSONObjectRequest;
import com.example.viktor.yandextranslate.nettwork.CustomVolleyRequestQueue;
import com.example.viktor.yandextranslate.utils.EditTextWatcher;
import com.example.viktor.yandextranslate.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class TranslateActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    private EditText editText;
    private boolean userIsInteracting;
    private boolean visibleSpinner = false;

    private final String defaultLng = "ru";

    private RequestQueue mQueue;

    private boolean parseLng = false;
    private boolean translate = false;

    private Spinner spinnerLeft;
    private Spinner spinnerRight;
    private AppCompatImageButton buttonSwapLng;
    private Button buttonTranslate;
    public static final String REQUEST_TAG = "TranslateActivity";

    private LanguageDirection languageDirection;
    private String lngFrom = "";
    private String lngTo = "";
    private HashMap<Integer, HashMap<String, String>> lngCollections;
    private TextView translatedText;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_translate:
                    return true;
                case R.id.navigation_bookmark:
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languageDirection = new LanguageDirection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        editText = (EditText) findViewById(R.id.editText);
        spinnerLeft = (Spinner) findViewById(R.id.spinnerLeft);
        spinnerRight = (Spinner) findViewById(R.id.spinnerRight);
        buttonSwapLng = (AppCompatImageButton) findViewById(R.id.buttonSwapLng);
        buttonTranslate = (Button) findViewById(R.id.buttonTranslate);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        editText.setOnKeyListener(onKeyListener);
        translatedText = (TextView) findViewById(R.id.translatedText);

        spinnerLeft.setOnItemSelectedListener(selectLeft);
        spinnerRight.setOnItemSelectedListener(selectRight);
        buttonTranslate.setOnClickListener(onClickListener);
        buttonSwapLng.setOnClickListener(onClickListenerSwapLng);
        getLanguages(defaultLng);
        editText.addTextChangedListener(new EditTextWatcher(spinnerLeft,spinnerRight,buttonSwapLng,editText));
    }

    private EditText.OnKeyListener onKeyListener = new EditText.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (event.getAction() == KeyEvent.FLAG_SOFT_KEYBOARD) {

                if (editText.getText().length() != 0) {
                    if (!visibleSpinner) {
                        spinnerLeft.setVisibility(View.VISIBLE);
                        spinnerRight.setVisibility(View.VISIBLE);
                        buttonSwapLng.setVisibility(View.VISIBLE);
                        visibleSpinner = true;
                    }
                } else {
                    visibleSpinner = false;
                    spinnerLeft.setVisibility(View.INVISIBLE);
                    spinnerRight.setVisibility(View.INVISIBLE);
                    buttonSwapLng.setVisibility(View.INVISIBLE);
                }
                return true;
            }
            return false;
        }
    };


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


    private void addToLangueageCollections(HashMap<String, String> languageNames) {
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
            url = "https://translate.yandex.net/api/v1.5/tr.json/translate?text=" + URLEncoder.encode(text,"UTF-8") + "&lang=" + lang + "&key=" + key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
//        try {
//            jsonRequest.getHeaders().put("Content-type","application/x-www-form-urlencoded");
//        } catch (AuthFailureError authFailureError) {
//            authFailureError.printStackTrace();
//        }

        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }


    private void getLanguages(String lng) {
        this.parseLng = true;
        String key = "trnsl.1.1.20170424T122712Z.30bb3eb21a38e99d.82d54b665ec3a394c5d8c82b49c5457f1be77d60";
        String url = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?ui=" + lng + "&key=" + key;
        // Instantiate the RequestQueue.
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
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
        addToLangueageCollections(lanuagesMap);

        fillSpinner(spinnerLeft, lanuagesMap);
        fillSpinner(spinnerRight, lanuagesMap);
        /*else {
            switch (spinnerName) {
                case "spinnerRight": {
                    fillSpinner(spinnerLeft, lanuagesMap);
                    break;
                }
                case "spinnerLeft": {
                    fillSpinner(spinnerRight, lanuagesMap);
                    break;
                }
            }
        }*/
    }

    private void showTranslatedText(String text) {

        Gson gson = new Gson();
        TranslateResponse response = gson.fromJson(text, TranslateResponse.class);

        this.translatedText.setText(response.getText().get(0));
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
