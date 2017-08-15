package com.example.timmy.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class registerSuccess extends Activity implements View.OnClickListener {


    private Button rebacktologin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        init();
    }

    private void init() {
        rebacktologin= (Button) findViewById(R.id.rebacktologin);
        rebacktologin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rebacktologin:
                Intent intent=new Intent(this,login.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
