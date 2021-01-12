package com.example.camp_proj1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Fragment3 extends Fragment {
    public String id = "abcdef";
    public String UserID;
    RecyclerView recyclerView;
    public GetMoneyTask gct;
    public String UserPhone = Fragment1.UserPhone;
    public static ArrayList<MoneyInfo> offData = new ArrayList<>();
    public static ArrayList<MoneyInfo> onData = new ArrayList<>();
    public RecyclerViewAdapter3 onAdapter;
    public RecyclerViewAdapter3 offAdapter;
    public ToggleButton toggle;

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetMoneyTask gct = new GetMoneyTask();
        gct.execute("http://192.249.18.251:8080/getMoney?id=");
    }

    public static String resultlist;

    public class GetMoneyTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //Log.d("User id",UserID);
                //넘겨줄 정보
                jsonObject.accumulate("id", LoginActivity.UserID);
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {//연결할 URL 주소
                    URL url = new URL(urls[0] + id);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();
                    //서버로 보내기위해서 스트림 만듦
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
            super.onPostExecute(result);    //서버로 부터 받은 값을 출력해주는 부분
            //Todo: 내용 완전 수정해줘야됨 -> money list 받아오는 부분
            //data parsing 하기
            //parts_id에 내 전화번호 있는지 확인 -> 있으면 그 object array에 넣어서 display
            JSONArray jsonArray;
            String writer;
            String writer_id;
            String parts_tmp;
            String parts[];
            String parts_id_tmp;
            String parts_id[];
            String date;
            String money;
            String account;
            try {
                jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject order = jsonArray.getJSONObject(i);
                    writer = order.getString("writer");
                    writer_id = order.getString("writer_id");
                    parts_id_tmp = order.getString("parts_id");
                    Log.i("Parts_ID: ", parts_id_tmp);

                    parts_id = parts_id_tmp.substring(3, parts_id_tmp.length() - 3).split(", ");
                    for(int j = 0; j < parts_id.length; j++){
                        if(parts_id[j].equals(UserPhone) || writer_id.equals(UserPhone)){ //정보를 가져와야 하는 경우: (내가 참가자인 경우 || 내가 호스트인 경우)
                            parts_tmp = order.getString("participants");
                            parts = parts_tmp.substring(3, parts_tmp.length() - 3).split(", ");
                            date = order.getString("date");
                            money = order.getString("money");
                            account = order.getString("account");
                            Log.i("MyList: ", money);
                            //money_tv.setText(writer);//서버로 부터 받은 값을 출력해주는 부분
                            if(writer_id.equals(UserPhone)){ //내가 호스트인 경우
                                offData.add(new MoneyInfo(writer, UserPhone, parts, parts_id, date, money, account));
                            }
                            else{   //내가 참여자인 경우
                                onData.add(new MoneyInfo(writer, UserPhone, parts, parts_id, date, money, account));
                            }
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            refreshFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_3, container, false);
        Context context = view.getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView3);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 0));
        recyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);

        toggle = (ToggleButton) view.findViewById(R.id.host_toggle);

        FloatingActionButton fab2 = view.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddMoneyInfo.class);
                context.startActivity(intent);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        offAdapter = new RecyclerViewAdapter3(context, offData);
        recyclerView.setAdapter(offAdapter);

 //toggle
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if(toggleButton.isChecked()){   //toggle ON
                    onAdapter = new RecyclerViewAdapter3(context, onData);
                    recyclerView.setAdapter(onAdapter);
                }
                else{   //toggle OFF
                    offAdapter = new RecyclerViewAdapter3(context, offData);
                    recyclerView.setAdapter(offAdapter);
                }
                refreshFragment();
            }
        });


        return view;
    }



    void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}



