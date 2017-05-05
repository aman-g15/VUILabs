package ai.api.sample;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;

import java.util.Locale;
import java.util.Set;

import ai.api.ui.AIButton;

/***********************************************************************************************************************
 *
 * API.AI Android SDK - client-side libraries for API.AI
 * =================================================
 *
 * Copyright (C) 2016 by Speaktoit, Inc. (https://www.speaktoit.com)
 * https://www.api.ai
 *
 * *********************************************************************************************************************
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

public class TTS {

    private static TextToSpeech textToSpeech;

    public static void init(final Context context) {
       // audioManager.setSpeakerphoneOn(false);
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {

                }
            },"networkTts");
        }
    }

    public static void speak(final String text, AIButton aiButton){
        speak(text);
        aiButton.onClick();
    }
    public static void speak(final String text) {

            Set<Voice> voices = textToSpeech.getVoices();
            Voice voice = null;

            voice = new Voice("en_GB-locale-default", Locale.ENGLISH, 900, 100, true, null);

            //Log.i("Voice is: ", v1.toString());

            textToSpeech.setVoice(voice);
            //textToSpeech.setLanguage(Locale.ENGLISH);
            //Voice v = new Voice("en_US-locale", Locale.ENGLISH,500, 400, true,null);
            // textToSpeech.setVoice(v);
            int res = textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,"1");
            //aiButton.onClick();
            //textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);

    }

    public static void stop(){
        textToSpeech.stop();
    }
    /*private static Voice getUSEnglish(Set<Voice> voices){
        Voice voice=null;
        for(Voice v1 : voices){
           // Log.i("Voice is ~~~~~~~~~", v1.toString());
            if((v1.getName()).equals("ja_JP-locale")){
                //Voice[Name: en_GB-locale, locale: en_GB, quality: 500, latency: 400, requiresNetwork: true, features: [LegacySetLanguageVoice, networkTts, embeddedTts]]
                Log.e("Success! Voice is: ", v1.toString());
                voice = v1;
            }else{
            voice = new Voice("",Locale.ENGLISH,400,500,true,null);
            }
            //Log.i("Voice is: ", v1.toString());
        }
return voice;

    }*/
    public static boolean isSpeaking(){
        return textToSpeech.isSpeaking();
    }
}
