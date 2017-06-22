package ai.api.sample;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

/**
 * Created by alexey on 07/12/16.
 */
public class AITextSampleActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static final String TAG = AITextSampleActivity.class.getName();

    private Gson gson = GsonFactory.getGson();

    private TextView resultTextView;
    private TextView requestTextView;
    //private EditText contextEditText;
    private EditText queryEditText;
    private CheckBox eventCheckBox;

    private Spinner eventSpinner;
    private ImageView animImageView;
    private AnimationDrawable frameAnimation;
    private LinearLayout requestLinearLayout;
    private LinearLayout inputTextLayout;
    private static List<String> requestList;
    private ImageView convImage;
    private AIDataService aiDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //convImage = new ImageView(getApplicationContext());

        //animImageView = (ImageView) findViewById(R.id.imageView);
        setContentView(R.layout.activity_aitext_sample);
        convImage = (ImageView) findViewById(R.id.imageViewMolly);
        requestLinearLayout = (LinearLayout) findViewById(R.id.requestLinearLayout);
        //requestList = new LinkedList<>();
       // reqLayout = (RelativeLayout) findViewById(R.id.requestLinearLayout);

        requestTextView = (TextView) findViewById(R.id.textViewRequest);

        resultTextView = (TextView) findViewById(R.id.textViewResponse);
        inputTextLayout = (LinearLayout) findViewById(R.id.linearLayout4);
       // resLayout = (RelativeLayout) findViewById(R.id.responseLinearLayout);

       // contextEditText = (EditText) findViewById(R.id.contextEditText);
        queryEditText = (EditText) findViewById(R.id.textQuery);
        findViewById(R.id.buttonSend).setOnClickListener(this);
        /*textView1 = new TextView(this);
        requestLinearLayout.addView(textView6);*/

        //findViewById(R.id.buttonClear).setOnClickListener(this);
        /*animImageView = (ImageView) findViewById(R.id.imageView3);
        animImageView.setBackgroundResource(R.drawable.molly_anim);
        animImageView.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation =
                        (AnimationDrawable) animImageView.getBackground();
                frameAnimation.start();
            }
        });
        */

       /* eventSpinner = (Spinner) findViewById(R.id.selectEventSpinner);
        final ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Config.events);
        eventSpinner.setAdapter(eventAdapter); */

        /*eventCheckBox = (CheckBox) findViewById(R.id.eventsCheckBox);
        checkBoxClicked();
        eventCheckBox.setOnClickListener(this);*/

        /*Spinner spinner = (Spinner) findViewById(R.id.selectLanguageSpinner);
        final ArrayAdapter<LanguageConfig> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Config.languages);
        spinner.setAdapter(languagesAdapter);
        spinner.setOnItemSelectedListener(this);*/
        initService(new LanguageConfig("en",Config.ACCESS_TOKEN));

    }

    private void initService(final LanguageConfig selectedLanguage) {
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(selectedLanguage.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(this, config);
    }


    private void clearEditText() {
        queryEditText.setText("");
    }

    /*
    * AIRequest should have query OR event
    */
    private void sendRequest() {

        final String queryString = String.valueOf(queryEditText.getText());
        //final String eventString = String.valueOf(String.valueOf(eventSpinner.getSelectedItem()));
        //final String contextString = String.valueOf(contextEditText.getText());

        if (TextUtils.isEmpty(queryString) ) {
            onError(new AIError(getString(R.string.non_empty_query)));
            return;
        }

        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];
             //   String event = params[1];

                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);
                /*if (!TextUtils.isEmpty(event))
                    request.setEvent(new AIEvent(event));*/
               // final String contextString = params[2];
                RequestExtras requestExtras = null;
                /*if (!TextUtils.isEmpty(contextString)) {
                    final List<AIContext> contexts = Collections.singletonList(new AIContext(contextString));
                    requestExtras = new RequestExtras(contexts, null);
                }*/

                try {
                    return aiDataService.request(request, requestExtras);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResult(response);
                } else {
                    onError(aiError);
                }
            }
        };

        task.execute(queryString);
    }

    public void checkBoxClicked() {
        eventSpinner.setEnabled(eventCheckBox.isChecked());
        queryEditText.setVisibility(!eventCheckBox.isChecked() ? View.VISIBLE : View.GONE);
    }


    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");




                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
                //reqLayout.setBackgroundResource(R.color.resTextView_color);
                convImage.setImageResource(R.drawable.molly_button);
                //inputTextLayout.addView(convImage);



                requestTextView.setText(result.getResolvedQuery());
                requestTextView.setGravity(Gravity.LEFT);
                requestTextView.setBackgroundResource(R.drawable.req_backcolor);


                queryEditText.setText("");
                Log.i(TAG, "Action: " + result.getAction());

                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);

               // TTS.speak(speech);
                //resultTextView.setBackgroundResource(R.drawable.message_response);
               // resLayout.setBackgroundResource(R.color.reqTextView_color);

                resultTextView.setText(gson.toJson(response.getResult().getFulfillment().getSpeech().toString()));
                resultTextView.setGravity(Gravity.LEFT);
                resultTextView.setBackgroundResource(R.drawable.res_backcolor);


                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }
                }
            }

        });
    }

    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error.toString());
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final LanguageConfig selectedLanguage = (LanguageConfig) parent.getItemAtPosition(position);
        initService(selectedLanguage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        sendRequest();/*
        switch (v.getId()) {
            case R.id.buttonClear:
                clearEditText();
                break;
            case R.id.buttonSend:
                sendRequest();
                break;
     }*/
    }
}
