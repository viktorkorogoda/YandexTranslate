package com.example.viktor.yandextranslate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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
import com.example.viktor.yandextranslate.models.Languages;
import com.example.viktor.yandextranslate.nettwork.CustomJSONObjectRequest;
import com.example.viktor.yandextranslate.nettwork.CustomVolleyRequestQueue;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class TranslateActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
//    private final String url = "https://translate.yandex.net/api/v1.5/tr.json";

    private TextView mTextMessage;

    private EditText editText;
    private boolean userIsInteracting;
    private boolean visibleSpinner = false;

    private final String defaultLng = "ru";

    private RequestQueue mQueue;

    private String spinnerName = "";

    private Spinner spinnerLeft;
    private Spinner spinnerRight;
    private Button buttonSwapLng;
    public static final String REQUEST_TAG = "TranslateActivity";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        editText = (EditText) findViewById(R.id.editText);
        spinnerLeft = (Spinner) findViewById(R.id.spinnerLeft);
        spinnerRight = (Spinner) findViewById(R.id.spinnerRight);
        buttonSwapLng = (Button) findViewById(R.id.buttonSwapLng);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        editText.setOnKeyListener(onKeyListener);
        editText.setOnLongClickListener(onLongClickListener);
        spinnerLeft.setOnItemSelectedListener(selectLeft);
        spinnerRight.setOnItemSelectedListener(selectRight);
        getLaguages(defaultLng);
    }

    private EditText.OnKeyListener onKeyListener = new EditText.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP) {

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


    private EditText.OnLongClickListener onLongClickListener = new EditText.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            return true;
//            if (((EditText) v.findViewById(R.id.editText)).getText().length() != 0) {
//                if (!visibleSpinner) {
//                    spinnerLeft.setVisibility(View.VISIBLE);
//                    spinnerRight.setVisibility(View.VISIBLE);
//                    buttonSwapLng.setVisibility(View.VISIBLE);
//                    visibleSpinner = true;
//                    return true;
//                }
//            } else {
//                visibleSpinner = false;
//                spinnerLeft.setVisibility(View.INVISIBLE);
//                spinnerRight.setVisibility(View.INVISIBLE);
//                buttonSwapLng.setVisibility(View.INVISIBLE);
//            }
//            return false;
        }
    };

    private Spinner.OnItemSelectedListener selectLeft = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userIsInteracting) {
                spinnerName = "spinnerLeft";
                getLaguages("ru");
                userIsInteracting = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private Spinner.OnItemSelectedListener selectRight = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (userIsInteracting) {
                spinnerName = "spinnerRight";
                getLaguages("ru");
                userIsInteracting = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


//        @Override
//        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            KeyEvent event1 = event;
//            if (event1.getAction()==KeyEvent.ACTION_UP) {
//
//                if (((EditText)v.findViewById(R.id.editText)).getText().length() != 0) {
//                    if(!visibleSpinner) {
//                        spinnerLeft.setVisibility(View.VISIBLE);
//                        spinnerRight.setVisibility(View.VISIBLE);
//                        buttonSwapLng.setVisibility(View.VISIBLE);
//                        visibleSpinner = true;
//                    }
//                } else {
//                    visibleSpinner = false;
//                    spinnerLeft.setVisibility(View.INVISIBLE);
//                    spinnerRight.setVisibility(View.INVISIBLE);
//                    buttonSwapLng.setVisibility(View.INVISIBLE);
//                }
//                return  true;
//            }
//            return false;
//        }
//    };


    private void fillSpinner(Spinner spinner, Languages languages) {

        ArrayList<String> spinnerInfo = new ArrayList<>();

        ArrayList<String> dirList = languages.getDirs();
        HashMap<String, String> languageNames = languages.getLangs();

        ArrayList<String> langList = new ArrayList<>();

        for (String lng : languageNames.keySet()) {
            langList.add(languageNames.get(lng));
        }


//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, dirList);
        ArrayAdapter<String> viewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, langList);

        viewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(viewAdapter);

    }

    private void getLaguages(String lng) {

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

        if (spinnerName.length() == 0) {
            fillSpinner(spinnerLeft, languages);
            fillSpinner(spinnerRight, languages);
        } else {
            switch (spinnerName) {
                case "spinnerRight": {
                    fillSpinner(spinnerLeft, languages);
                    break;
                }
                case "spinnerLeft": {
                    fillSpinner(spinnerRight, languages);
                    break;
                }
            }
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    public void onResponse(Object response) {
        parseLanguages(response.toString());
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

//    private Languages getLanguagesToTranslate(String lng) throws IOException, JSONException {
//        HttpsURLConnection connection = null;
//        BufferedReader reader = null;
//        String key = "trnsl.1.1.20170424T122712Z.30bb3eb21a38e99d.82d54b665ec3a394c5d8c82b49c5457f1be77d60";
//
//        String urlGet = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?ui="+lng+"&key="+key;
//
//        URL url = new URL(urlGet);
//        connection = (HttpsURLConnection) url.openConnection();
//        connection.connect();
//        InputStream stream = connection.getInputStream();
//        reader = new BufferedReader(new InputStreamReader(stream));
//        StringBuffer buffer = new StringBuffer();
//        String line ="";
//        while ((line = reader.readLine()) != null){
//            buffer.append(line);
//        }
//
//        String finalJson = buffer.toString();
//
//
//    }
}
