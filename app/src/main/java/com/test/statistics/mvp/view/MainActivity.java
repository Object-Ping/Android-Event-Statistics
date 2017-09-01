package com.test.statistics.mvp.view;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.test.statistics.R;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frag);
        Button btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this,"请看log日志",Toast.LENGTH_SHORT).show();
    }
}
