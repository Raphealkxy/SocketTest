package com.example.timmy.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

import static android.content.ContentValues.TAG;


public class register extends Activity implements View.OnClickListener {

    private TextView id;
    private TextView textView;
    private TextView username;
    private TextView password;
    private TextView telephone;
    private TextView email;
    private TextView sex;
    private Button submit;
    private Button reset;
    private String mBaseUrl = "http://192.168.1.109:8080/AMS/getdata";
    private String origin=register.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();

    }

    private void initUI() {
        id= (TextView) findViewById(R.id.registerId);
        username= (TextView) findViewById(R.id.registerUsername);
        password= (TextView) findViewById(R.id.registerPassword);
        sex= (TextView) findViewById(R.id.registerSex);
        email= (TextView) findViewById(R.id.registerEmail);
        telephone= (TextView) findViewById(R.id.registerTelephone);
        textView= (TextView) findViewById(R.id.showmesg);
        submit= (Button) findViewById(R.id.register_submit);
        reset= (Button) findViewById(R.id.registerReset);
        submit.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register_submit:
                submit();
               break;
            case R.id.registerReset:
                reset();
                break;
            default:
                break;
        }
    }

    private void submit() {
        postString(id.getText()+"",username.getText()+"",password.getText()+"",sex.getText()+"",telephone.getText()+"",email.getText()+"");

    }

    public void postString(String id,String username,String password,String sex,String telphone,String email)
    {
        String url = mBaseUrl;
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(new Gson().toJson(new User(id,username, password,sex,telphone,email)))
                .build()
                .execute(new register.MyStringCallback());

    }
    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id)
        {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            textView.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete");
           //15416119 textView.setText("onResponse:" + response);

//            switch (id)
//            {
//                case 100:
//                    Toast.makeText(register.this, "http", Toast.LENGTH_SHORT).show();
//                    break;
//                case 101:
//                    Toast.makeText(register.this, "https", Toast.LENGTH_SHORT).show();
//                    break;
//            }

            direct();

        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            //  mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void direct() {
        Intent intent=new Intent(this,registerFace.class);
        intent.putExtra("id",id.getText()+"");
        intent.putExtra("username",username.getText()+"");
        startActivity(intent);

        finish();
    }

    private void reset() {
        username.setText("");
        password.setText("");
        sex.setText("");
        email.setText("");
        telephone.setText("");
    }
}
