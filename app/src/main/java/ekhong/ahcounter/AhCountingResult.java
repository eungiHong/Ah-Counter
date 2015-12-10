package ekhong.ahcounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ekhong on 2015-11-21.
 */
public class AhCountingResult extends AppCompatActivity {  // 아 카운팅 결과 확인 액티비티 | Activity for finding out the result of Ah counting
    private SQLiteDatabase stringDB;
    public static final String AH_TABLE = "ah";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_STRING = "string";
    public static final String DATABASE_NAME = "ah_count.db";
    public String currentString;

    ArrayList<String> wordSetting = new ArrayList<String>();


    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.ah_counting_result);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        wordSetting = i.getStringArrayListExtra("wordSet1");
        // KoreanRecognition.class 또는 EnglishRecognition.class로부터 Ah word 목록 수신 | Receive the list of Ah words from KoreanRecognition.class or EnglishRecognition.class

        Button moveToKr = (Button) findViewById(R.id.moveToKr);
        moveToKr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), KoreanRecognition.class);
                startActivity(i);
            }
        });

        Button moveToEn = (Button) findViewById((R.id.moveToEn));
        moveToEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EnglishRecognition.class);
                startActivity(i);
            }
        });
    }

    public void clickShowDB(View v) {
        stringDB = openOrCreateDatabase(DATABASE_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READWRITE, null);
        stringDB.execSQL("CREATE TABLE IF NOT EXISTS " + AH_TABLE + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_STRING + " VARCHAR)");

        Cursor c = stringDB.query(AH_TABLE, new String[]{COLUMN_STRING}, null, null, null, null,
                COLUMN_STRING);
        c.moveToLast();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("결과(Result)");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        if (c.getCount() == 0) {
            alert.setMessage("입력된 발화가 존재하지 않습니다. \nThere is no recognized speech.");
            alert.show();
        } else {
            // 췌언 개수 집계 | Count Ah words
            String testString = c.getString(0);
            Scanner s = new Scanner(testString).useDelimiter(" ");

            if (wordSetting.size() == 1) {
                int count1 = 0;
                while (s.hasNext()) {
                    currentString = s.next();
                    if (currentString.equals(wordSetting.get(0))) {
                        count1++;
                    }
                }
                alert.setMessage(c.getString(0) + "\n" + wordSetting.get(0) + ": " + count1 + "번");
                c.close();
                if (stringDB.isOpen())
                    stringDB.close();
                alert.show();
            }
            else if (wordSetting.size() == 2) {
                int count1 = 0;
                int count2 = 0;
                while (s.hasNext()) {
                    currentString = s.next();
                    if (currentString.equals(wordSetting.get(0))) {
                        count1++;
                    }
                    if (currentString.equals(wordSetting.get(1))) {
                        count2++;
                    }
                }
                alert.setMessage(c.getString(0) + "\n" + wordSetting.get(0) + ": " + count1 + "번\n" + wordSetting.get(1) + ": " + count2 + "번");
                c.close();
                if (stringDB.isOpen())
                    stringDB.close();
                alert.show();
            }
            else if (wordSetting.size() == 3) {
                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                while (s.hasNext()) {
                    currentString = s.next();
                    if (currentString.equals(wordSetting.get(0))) {
                        count1++;
                    }
                    if (currentString.equals(wordSetting.get(1))) {
                        count2++;
                    }
                    if (currentString.equals(wordSetting.get(2))) {
                        count3++;
                    }
                }
                alert.setMessage(c.getString(0) + "\n" + wordSetting.get(0) + ": " + count1 + "번\n" + wordSetting.get(1) + ": " + count2 + "번\n" + wordSetting.get(2) + ": " + count3 + "번");
                c.close();
                if (stringDB.isOpen())
                    stringDB.close();
                alert.show();
            }
            else if (wordSetting.size() == 4) {
                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                int count4 = 0;
                while (s.hasNext()) {
                    currentString = s.next();
                    if (currentString.equals(wordSetting.get(0))) {
                        count1++;
                    }
                    if (currentString.equals(wordSetting.get(1))) {
                        count2++;
                    }
                    if (currentString.equals(wordSetting.get(2))) {
                        count3++;
                    }
                    if (currentString.equals(wordSetting.get(3))) {
                        count4++;
                    }
                }
                alert.setMessage(c.getString(0) + "\n" + wordSetting.get(0) + ": " + count1 + "번\n" + wordSetting.get(1) + ": " + count2 + "번\n" + wordSetting.get(2) + ": " + count3 + "번\n" +
                        wordSetting.get(3) + ": " + count4 + "번");
                c.close();
                if (stringDB.isOpen())
                    stringDB.close();
                alert.show();
            }
            else if (wordSetting.size() == 5) {
                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                int count4 = 0;
                int count5 = 0;
                while (s.hasNext()) {
                    currentString = s.next();
                    if (currentString.equals(wordSetting.get(0))) {
                        count1++;
                    }
                    if (currentString.equals(wordSetting.get(1))) {
                        count2++;
                    }
                    if (currentString.equals(wordSetting.get(2))) {
                        count3++;
                    }
                    if (currentString.equals(wordSetting.get(3))) {
                        count4++;
                    }
                    if (currentString.equals(wordSetting.get(4))) {
                        count5++;
                    }
                }
                alert.setMessage(c.getString(0) + "\n" + wordSetting.get(0) + ": " + count1 + "번\n" + wordSetting.get(1) + ": " + count2 + "번\n" + wordSetting.get(2) + ": " + count3 + "번\n" +
                        wordSetting.get(3) + ": " + count4 + "번\n" + wordSetting.get(4) + ": " + count5 + "번");
                c.close();
                if (stringDB.isOpen())
                    stringDB.close();
                alert.show();
            }
            else if (wordSetting.size() == 6) {
                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                int count4 = 0;
                int count5 = 0;
                int count6 = 0;
                while (s.hasNext()) {
                    currentString = s.next();
                    if (currentString.equals(wordSetting.get(0))) {
                        count1++;
                    }
                    if (currentString.equals(wordSetting.get(1))) {
                        count2++;
                    }
                    if (currentString.equals(wordSetting.get(2))) {
                        count3++;
                    }
                    if (currentString.equals(wordSetting.get(3))) {
                        count4++;
                    }
                    if (currentString.equals(wordSetting.get(4))) {
                        count5++;
                    }
                    if (currentString.equals(wordSetting.get(5))) {
                        count6++;
                    }
                }
                alert.setMessage(c.getString(0) + "\n" + wordSetting.get(0) + ": " + count1 + "번\n" + wordSetting.get(1) + ": " + count2 + "번\n" + wordSetting.get(2) + ": " + count3 + "번\n" +
                        wordSetting.get(3) + ": " + count4 + "번\n" + wordSetting.get(4) + ": " + count5 + "번\n" + wordSetting.get(5) + ": " + count6 + "번");
                c.close();
                if (stringDB.isOpen())
                    stringDB.close();
                alert.show();
            }
            else {
                alert.setMessage(c.getString(0));
                c.close();
                if (stringDB.isOpen())
                    stringDB.close();
                alert.show();
            }
        }
    }


    public void restoreDB(View v){  // DB 초기화 | Initialize DB
        this.deleteDatabase(DATABASE_NAME);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        alert.setMessage("데이터베이스가 초기화 되었습니다. \nDatabase is initialized.");
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_favorite:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
