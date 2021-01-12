package com.example.camp_proj1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class RecyclerViewClickActivity extends AppCompatActivity {
    public ImageButton back_btn;
    public String deletename;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.recyclerviewclickevent);
        Intent intent = getIntent();

        TextView textView = findViewById(R.id.name);
        TextView textView2 = findViewById(R.id.number);
        TextView textView3 = findViewById(R.id.email);
        ImageView imageView = findViewById(R.id.image);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);


        Button callButton = findViewById(R.id.callbutton);
        callButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phonecall = "tel:0" + intent.getStringExtra("number");
                Context context = v.getContext();
                context.startActivity(new Intent("android.intent.action.CALL", Uri.parse(phonecall)));
            }
        });

        Button deleteButton = findViewById(R.id.deletebutton);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteContact().execute("http://192.249.18.251:8080/deleteContact?id=");//AsyncTask 시작시킴
                finish();
            }
        });


        //imageView.setImageResource(intent.getIntExtra("image",0));
        imageView.setImageResource(R.drawable.basic);
        deletename = intent.getStringExtra("name");
        textView.setText(intent.getStringExtra("name"));
        textView2.setText(intent.getStringExtra("number"));
        textView3.setText(intent.getStringExtra("email"));

        back_btn = findViewById(R.id.goback_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });
    }


    public class DeleteContact extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //넘겨줄 정보: 사용자 아이디와 삭제할 사람 이름
                jsonObject.accumulate("id", LoginActivity.UserID);
                jsonObject.accumulate("deletename", deletename);

                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {
                    //연결할 URL
                    URL url = new URL(urls[0] + "abcdef");
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
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
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
            //tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }

    }


}




