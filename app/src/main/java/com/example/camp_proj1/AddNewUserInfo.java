package com.example.camp_proj1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.camp_proj1.Fragment1.resultlist;



public class AddNewUserInfo extends AppCompatActivity {
    public static String dorefresh;
    public static String resultchanged;

    public String name;
    public String number;
    public String email;
    public static UserInfo added_data;

    EditText nameText;
    EditText numberText;
    EditText emailText;

    String id="abcdef";

    //
    //
    //
    // public String UserID;
    int[] images = {R.drawable.basic,R.drawable.basic2,R.drawable.basic3};
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.addnewuserinfo);
        Button savebutton = (Button) findViewById(R.id.save);
        Button cancelbutton = (Button) findViewById(R.id.cancel);
        nameText = findViewById(R.id.nameText);
        numberText =  findViewById(R.id.numberText);
        emailText =  findViewById(R.id.emailText);
        ImageView imageView = (ImageView) findViewById(R.id.sampleimage);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);


        //int rand = new Random().nextInt(images.length);



        savebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = nameText.getText().toString();
                    number = numberText.getText().toString();
                    email = emailText.getText().toString();
                    new AddContactTask().execute("http://192.249.18.251:8080/addContact?id=");//AsyncTask 시작시킴
                    added_data = new UserInfo(name, number, email,R.drawable.basic2);
                }
            });

        cancelbutton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // Intent intent2 = getIntent();
        // UserID = intent.getExtras().getString("UserID");

    }


    //public String UserID = Profile.getCurrentProfile().getId();


    public class AddContactTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //넘겨줄 정보
                jsonObject.accumulate("id", LoginActivity.UserID);
                jsonObject.accumulate("name", name);
                jsonObject.accumulate("phone", number);
                jsonObject.accumulate("email", email);

                HttpURLConnection con = null;
                BufferedReader reader = null;
                try{
                    //연결할 URL
                    URL url = new URL(urls[0] + id);
                    //URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();
                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌
                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultchanged = result;
            if (Fragment1.resultlist != result){
                dorefresh = "1";
            }
            else{dorefresh="0";}
            //tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }

    }
}
