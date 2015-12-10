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
public class KoreanRecognition extends AppCompatActivity {
    SpeechRecognizer mRecognizer;  // SpeechRecognizer
    Intent i;
    TextView resultString;
    ArrayList<String> mResults;

    private SQLiteDatabase stringDB;  // 데이터베이스
    public static final String AH_TABLE = "ah";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_STRING = "string";
    public static final String DATABASE_NAME = "ah_count.db";

    final CharSequence wordList[] = {"그", "아", "어", "이제", "인제"};  // 췌언 목록
    boolean bl[] = new boolean[wordList.length];
    ArrayList<Integer> selectedWords = new ArrayList<Integer>();
    ArrayList<String> wordSetting = new ArrayList<String>();

    Chronometer mChronometer;     //스톱워치

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.korean_recognition);
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);  // RecognitionListener 세팅
        resultString = (TextView)findViewById(R.id.resultString);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "kr-KR"); // 한국어 설정
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        mChronometer = (Chronometer) findViewById(R.id.chronometer1);

        Button button1 = (Button) findViewById(R.id.bVoice);  // 음성인식 시작버튼
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer.startListening(i); // 버튼 누르면 음성인식 시작
                mChronometer.setBase(SystemClock.elapsedRealtime());  //버튼 누르면 스톱워치 초기화
                mChronometer.start();//버튼 누르면 스톱워치 시작
            }
        });

        Button button2 = (Button) findViewById(R.id.bStop);  // 음성인식 종료버튼
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer.cancel();  // 버튼 누르면 음성인식 정지
                // 음성인식 취소 메소드, mRecognizer.stopListening()도 있고, mRecognizer.destroy()도 있음. destroy()는 SpeechRecognizer 객체를 파괴하기 때문에 다시 음성인식 시작 버튼 눌렀을 시 실행되지 않음
                mChronometer.stop();// 버튼 누르면 스톱워치 정지
            }

        });

        Button button4 = (Button) findViewById(R.id.bText);  // 텍스트보기 이동버튼
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AhCountingResult.class);
                try {
                    i.putExtra("wordSet1", wordSetting);  // 설정된 Ah Word를 AhCountingResult.class로 전달
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
        // DB 생성
    }

    public RecognitionListener listener = new RecognitionListener() {  // 음성인식 메소드 조작
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
        public void onError(int error) {  // 에러 발생시 음성인식 재시작
            if (error == 3 || error == 6 || error == 7) {// 3: ERROR_AUDIO, 6: ERROR_SPEECH_TIMEOUT, 7: ERROR_NO_MATCH, 6번이 제일중요 (입력이 없어도 다시 음성인식을 시작하게 해줌)
                mRecognizer.startListening(i);
                mChronometer.start();
            }
        }

        @Override
        public void onResults(Bundle results) {
            mResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            resultString.append(mResults.get(0) + " ");  // 입력받은 문자열을 추가하는 부분

            ContentValues values = new ContentValues();
            values.put(COLUMN_STRING, resultString.getText().toString());
            stringDB.insert(AH_TABLE, null, values);
            // DB에 추가

            mRecognizer.startListening(i);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };
    public void ondialogbtnClicked1(View v) {  // 췌언목록 설정
        AlertDialog.Builder checkBox = new AlertDialog.Builder(this);
        checkBox.setTitle("설정하고 싶은 췌언을 선택하세요");
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

    public void ondialogbtnClicked2(View v) {  // 사용설명
        AlertDialog.Builder userGuide = new AlertDialog.Builder(this);
        userGuide.setTitle("사용설명");
        userGuide.setMessage("아 카운터 앱은 발화를 기록해 주고 발화 중에 발생한 불필요한 Ah word, 즉 췌언의 숫자를 세어 줍니다. 이 앱을 통해서 자신의 발화습관을 점검하고 발화능력을 향상시킬 수 있습니다.\n" +
                "\n다음과 같은 절차를 통해서 아 카운터 앱을 이용하실 수 있습니다.\n" +
                "\n1. \'췌언목록 설정\'을 눌러 발화 중 발견하고 싶은 췌언을 선택하세요.\n" +
                "\n2. \'음성인식 시작하기\'를 누른 뒤 발화를 시작하세요. 아 카운터 앱이 발화를 인식하여 데이터베이스에 저장함과 동시에 하단의 빈칸에 발화를 받아쓰기 합니다.\n" +
                "\n3. \'음성인식 종료\'를 눌러서 인식을 종료합니다.\n" +
                "\n4. \'텍스트 보기\'를 눌러서 아 카운팅 결과를 확인하세요. 전환된 화면에서 다시 \'아 카운팅 결과보기\'를 누르면 발화 중에 등장한 췌언의 횟수를 확인할 수 있습니다.\n" +
                "\n5. 새로운 발화를 저장하고 싶다면 \'메모장 초기화\'를 누르세요. 이전에 저장되었던 발화가 삭제됩니다.");
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

