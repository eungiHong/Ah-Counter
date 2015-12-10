package ekhong.ahcounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button button1 = (Button) findViewById(R.id.button1); // 한국어 모드 | Korean Mode
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), KoreanRecognition.class);
                startActivity(i);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2); // 영어 모드 | English Mode
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EnglishRecognition.class);
                startActivity(i);
            }
        });

        Button button3 = (Button) findViewById(R.id.button3); // 코딩카페 홈페이지 이동 | Go to Coding Cafe web page
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/groups/codin9cafe"));
                startActivity(i);
            }
        });

    }

    public void ondialogbtnClicked(View v) {   // 버전정보 | Version info
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("INFO");
        alert.setMessage("Ah Counter ver 1.1");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
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
            case R.id.action_favorite:
                return true;
            case R.id.korean:
                Intent k = new Intent(getApplicationContext(), KoreanRecognition.class);
                startActivity(k);
                return true;
            case R.id.english:
                Intent e = new Intent(getApplicationContext(), EnglishRecognition.class);
                startActivity(e);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}