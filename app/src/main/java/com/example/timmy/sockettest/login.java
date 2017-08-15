package com.example.timmy.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class login extends Activity  implements View.OnClickListener{



    private Button resbtn;
    private Button login;
    private TextView username;
    private TextView password;
    private TextView textView;
    private String mBaseUrl = "http://192.168.1.109:8080/AMS/CheckLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        resbtn= (Button) findViewById(R.id.register_user);
        login= (Button) findViewById(R.id.login);
        username= (TextView) findViewById(R.id.username);
        password= (TextView) findViewById(R.id.password);
       resbtn.setOnClickListener(this);
       login.setOnClickListener(this);
        textView= (TextView) findViewById(R.id.login_showmesg);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.register_user:
             direct();
                break;
            case R.id.login:
                submit();
                break;
            default:
                break;






        }
    }

    private void direct() {
        Intent intent=new Intent(this,register.class);
        startActivity(intent);
        finish();
    }

    private void submit() {
        Toast.makeText(login.this, "hahah", Toast.LENGTH_SHORT).show();

        postString(username.getText()+"",password.getText()+"");

    }

    public void postString(String username,String password)
    {
        String url = mBaseUrl;
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(new Gson().toJson(new LoginUser(username, password)))
                .build()
                .execute(new login.MyStringCallback());

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
            textView.setText("onResponse:" + response);

            switch (id)
            {
                case 100:
                    Toast.makeText(login.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(login.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }

            Gson gson=new Gson();
            Code code=gson.fromJson(response,Code.class);
            if(code.getCode()==1)
            {
                Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();

            }else
            {
                Toast.makeText(login.this, "密码或用户名错误", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            //  mProgressBar.setProgress((int) (100 * progress));
        }
    }
}
