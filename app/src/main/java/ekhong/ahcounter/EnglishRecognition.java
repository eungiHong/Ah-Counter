package ekhong.ahcounter;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ekhong on 2015-11-20.
 */
public class EnglishRecognition extends AppCompatActivity {
    SpeechRecognizer mRecognizer; // SpeechRecognizer
    Intent i;
    TextView resultString;
    ArrayList<String> mResults;

    private SQLiteDatabase stringDB;  // Database
    public static final String AH_TABLE = "ah";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_STRING = "string";
    public static final String DATABASE_NAME = "ah_count.db";

    final CharSequence wordList[] = {"actually", "ah", "basically", "like", "um", "yeah"};  // LIst of Ah words
    boolean bl[] = new boolean[wordList.length];
    ArrayList<Integer> selectedWords = new ArrayList<Integer>();
    ArrayList<String> wordSetting = new ArrayList<String>();

    Chronometer mChronometer;  // Stop Watch


    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.english_recognition);
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener); // Set RecognitionListener
        resultString = (TextView)findViewById(R.id.resultString);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");  // Set English language
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);


        mChronometer = (Chronometer) findViewById(R.id.chronometer1);

        Button button1 = (Button) findViewById(R.id.bVoice);  // The button for starting speech recognition
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer.startListening(i); // Start speech recognition
                mChronometer.setBase(SystemClock.elapsedRealtime());  // Initialize the stop watch
                mChronometer.start();//Start the stop watch
            }
        });

        Button button2 = (Button) findViewById(R.id.bStop);  // The button for stopping speech recognition
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer.cancel(); // Stop speech recognition
                mChronometer.stop();//Stop the stop watch
            }
        });

        Button button4 = (Button) findViewById(R.id.bText);  // The button for moving to AhCountingResult.class
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AhCountingResult.class);
                try {
                    i.putExtra("wordSet1", wordSetting);  // Pass selected ah words to AhCountingResult.class
                    startActivity(i);
                } catch (ArrayIndexOutOfBoundsException e) {
                    startActivity(i);
                }
            }
        });

        stringDB = openOrCreateDatabase(DATABASE_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READWRITE, null);
        stringDB.execSQL("CREATE TABLE IF NOT EXISTS " + AH_TABLE + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_STRING + " VARCHAR)");
        // Create DB
    }

    public RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {  // Restart speech recognition when an error occurs
            if (error == 3 || error == 6 || error == 7) { // 3: ERROR_AUDIO, 6: ERROR_SPEECH_TIMEOUT, 7: ERROR_NO_MATCH,  Number 6 is the most important- it makes mRecognizer restart speech recognition when there is no input.
                mRecognizer.startListening(i);
                mChronometer.start();
            }
        }

        @Override
        public void onResults(Bundle results) {
            mResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            resultString.append(mResults.get(0) + " ");  // Add recognized speech to resultString.

            ContentValues values = new ContentValues();
            values.put(COLUMN_STRING, resultString.getText().toString());
            stringDB.insert(AH_TABLE, null, values);
            // Add to DB
            mRecognizer.startListening(i);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    public void ondialogbtnClicked1(View v) { // Set list of Ah words
        AlertDialog.Builder checkBox = new AlertDialog.Builder(this);
        checkBox.setTitle("Select Ah words you want to count");
        checkBox.setMultiChoiceItems(wordList, bl, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedWords.add(which);
                } else if (selectedWords.contains(which)) {
                    selectedWords.remove(Integer.valueOf(which));
                }
            }
        });
        checkBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selectedWords.size(); i++) {
                    String str = (String) wordList[selectedWords.get(i)];
                    wordSetting.add(str);
                }
            }
        });
        checkBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        checkBox.show();
    }

    public void ondialogbtnClicked2(View v) {  // User Guide
        AlertDialog.Builder userGuide = new AlertDialog.Builder(this);
        userGuide.setTitle("User Guide");
        userGuide.setMessage("Ah Counter App records your speech, and it counts 'Ah words', the unnecessary words in a speech. With this App, you can examine your speech habits, and improve your speech skill. \n" +
                "\nHere is a sequence of steps you may take to use this app.\n" +
                "\n1. Click \"Set Ah Words\" to select ah words which you want to count in your speech.\n" +
                "\n2. Click \"Recognize Speech\" to start your speech. The application recognizes your speech and stores it in a database while writing it down on the blank below the buttons.\n" +
                "\n3. Click \"Stop Recognizing\" to finish speech recognition.\n" +
                "\n4. Click \"Show Result\" to check out the result of Ah counting. Click \"Show Result\" again, then you will find out how many times you said those Ah words during your speech.\n" +
                "\n5. If you want to clear your database and store another speech, click \"Initialize the Note\". It will initialize your database.");
        userGuide.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        userGuide.show();
    }

    @Override
    public void finish() {
        if (mRecognizer != null) mRecognizer.stopListening();
        if(stringDB.isOpen())
            stringDB.close();
        super.finish();
    }
}

