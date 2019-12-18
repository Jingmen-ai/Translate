package com.youdao.sdk.ydtranslatedemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.youdao.sdk.ydtranslatedemo.R;

public class Start extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button BT1=(Button)findViewById(R.id.button);
        Button BT2=(Button)findViewById(R.id.button2);
        BT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Start.this,TranslateActivity.class);
                startActivity(intent1);
            }
        });
        BT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(Start.this,Book.class);
                startActivity(intent2);
            }
        });
    }
}
