package ai.api.sample;

/***********************************************************************************************************************
 *
 * API.AI Android SDK -  API.AI libraries usage example
 * =================================================
 *
 * Copyright (C) 2015 by Speaktoit, Inc. (https://www.speaktoit.com)
 * https://www.api.ai
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************/

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import ai.api.android.AIConfiguration;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.sample.model.CalendarBuilderObject;
import ai.api.ui.AIButton;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements AIButton.AIButtonListener {

    public static final String TAG = MainActivity.class.getName();

    private AIButton aiButton;
    private TextView resultTextView;
    private Gson gson = GsonFactory.getGson();
    private AudioManager audioManager;
    private ImageView animImageView;
    private AnimationDrawable frameAnimation;
    int minute, hour, day;
    Calendar cal;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_background);
        //relativeLayout.setBackgroundResource(R.drawable.molly_background2);

        aiButton = (AIButton) findViewById(R.id.micButton);
        //resultTextView = (TextView) findViewById(R.id.resultTextView);
        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));
        aiButton.initialize(config);
        aiButton.setResultsListener(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        animImageView = (ImageView) findViewById(R.id.imageView);
        animImageView.setBackgroundResource(R.drawable.molly_anim);
        animImageView.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation =
                        (AnimationDrawable) animImageView.getBackground();
                frameAnimation.start();
            }
        });
        TTS.init(getApplicationContext());
        // TTS.speak("Hi! I am Molly, your personal assistant for real estate practice. How can I help you?");

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAudioRecordPermission();
        TTS.speak("Hi! I am Molly, your personal assistant for real estate practice. How can I help you?");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(AISettingsActivity.class);
            return true;
        }else if(id == R.id.textBased){
        Intent i = new Intent(getBaseContext(), AITextSampleActivity.class);
        startActivity(i);
    }
        return super.onOptionsItemSelected(item);
    }

    public void serviceSampleClick(final View view) {
        startActivity(AIServiceSampleActivity.class);
    }

    public void buttonSampleClick(final View view) {
        startActivity(AIButtonSampleActivity.class);
    }

    public void dialogSampleClick(final View view) {
        startActivity(AIDialogSampleActivity.class);
    }

    public void textSampleClick(final View view) {
        startActivity(AITextSampleActivity.class);
    }

    private void startActivity(Class<?> cls) {
        final Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");
                //resultTextView.setText("");
            }
        });
    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");
                //resultTextView.setText(error.toString());
            }
        });
    }

    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "onResult");

                //resultTextView.setText(gson.toJson(response.getResult()));

                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                //resultTextView.setBackgroundResource(R.drawable.background_border);
                //resultTextView.setText("You said: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());
                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);
                TTS.speak(speech, aiButton);
                if (speech.contains("remind") || speech.contains("schedule a meeting") || speech.contains("set up a meeting") || speech.contains("meeting") || speech.contains("appointment")) {
                    //createCalendarEvent();
                    CalendarBuilderObject calendarObj = null;
                    try {
                        calendarObj=   buildCalendarEvent(result.getParameters());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    createCalendar(calendarObj);

                    //setAlarm();
                }
                //aiButton.onClick();
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
        // aiButton.onClick();
        // startActivity(AIButtonSampleActivity.class);

    }

    public CalendarBuilderObject buildCalendarEvent(HashMap<String, JsonElement> inputMap) throws ParseException{
        CalendarBuilderObject requestObj = new CalendarBuilderObject();
        if(null == inputMap){

        }else {
            if(null != inputMap.get("task-title")) {
                requestObj.setTaskTitle(inputMap.get("task-title").getAsString());
            }else{
                requestObj.setTaskTitle("");
            }
            String date=null;
            if(null != inputMap.get("date")) {
                date = inputMap.get("date").getAsString();
                String[] dateArray = date.split("-");
                requestObj.setYear(Long.parseLong(dateArray[0]));
                requestObj.setMonth(Long.parseLong(dateArray[1]));
                requestObj.setDay(Long.parseLong(dateArray[2]));

            }else{
                Calendar temp = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String tempDate = dateFormat.format(temp.getTime());
                //Log.i(tempDate+" ",tempDate);
                String[] dateArray = tempDate.split("-");
                requestObj.setYear(Long.parseLong(dateArray[0]));
                requestObj.setMonth(Long.parseLong(dateArray[1]));
                requestObj.setDay(Long.parseLong(dateArray[2]));

            }
            if(null!=inputMap.get("time")) {
                String time = inputMap.get("time").getAsString();
                String [] timeArray = time.split(":");
                requestObj.setHour(Long.parseLong(timeArray[0]));
                requestObj.setMinute(Long.parseLong(timeArray[1]));
            }else{
                requestObj.setHour(8L);
                requestObj.setMinute(00L);
            }


        }
        return requestObj;
    }

    public synchronized void createCalendarEvent() {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 4, 6, 10, 15);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 4, 6, 10, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Reminder")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "15.amangupta@gmail.com");
        startActivity(intent);

    }

    public synchronized void createCalendar(CalendarBuilderObject calendarObj) {
        Calendar startTimeMillis = Calendar.getInstance();
        Calendar endTimeMillis = Calendar.getInstance();
        startTimeMillis.set(calendarObj.getYear().intValue(), calendarObj.getMonth().intValue()-1, calendarObj.getDay().intValue(), calendarObj.getHour().intValue(), calendarObj.getMinute().intValue());
        endTimeMillis.set(calendarObj.getYear().intValue(), calendarObj.getMonth().intValue()-1, calendarObj.getDay().intValue(), calendarObj.getHour().intValue(), calendarObj.getMinute().intValue()+30);
        final ContentValues event = new ContentValues();
        //event.put(CalendarContract.Reminders.DTSTART, startTimeMillis.getTimeInMillis());
        event.put(CalendarContract.Events.CALENDAR_ID, 1);
        event.put(CalendarContract.Events.TITLE, calendarObj.getTaskTitle());
        event.put(CalendarContract.Events.DESCRIPTION, calendarObj.getTaskTodo());
        event.put(CalendarContract.Events.EVENT_LOCATION, "");
        event.put(CalendarContract.Events.DTSTART, startTimeMillis.getTimeInMillis());
        event.put(CalendarContract.Events.DTEND, endTimeMillis.getTimeInMillis());
        event.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true
        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

        Uri baseUri;
        baseUri = Uri.parse("content://com.android.calendar/events");
        //getApplicationContext();
        baseUri = getApplicationContext().getContentResolver().insert(baseUri, event);


        long eventID = Long.parseLong(baseUri.getLastPathSegment());
        ContentValues reminders = new ContentValues();
        reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        reminders.put(CalendarContract.Reminders.MINUTES, 10);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri2 = getApplicationContext().getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminders);


    }
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        System.exit(0);
    }
    public synchronized void setAlarm(){
        cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        day = cal.get(Calendar.DAY_OF_WEEK);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_HOUR, hour +1);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute + 5);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(i);
    }

}
