package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static  final String TITLE="isomatedString";
    private static  final String SUBTITLE= "String_length";

    private static  final String SHARED_PREFS_NAME = "my_pref";
    private static  final String SHARED_PREFS_KEY = "my_key";

    private List<Map<String,String>>values = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);

        init();

        final BaseAdapter listContentAdapter = createAdapter(values);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(listContentAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                values.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });

final SwipeRefreshLayout refreshLayout = findViewById(R.id.swipe);
refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        values.clear();
        init();
        listContentAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }
});

}



    private BaseAdapter createAdapter(List<Map<String,String>> values){
        String[] from = {TITLE, SUBTITLE};
        int[] to = {R.id.tv_title, R.id.tv_subtitle};
        return new SimpleAdapter(this,values, R.layout.list_item, from, to);
    }


    private void init(){
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS_NAME,MODE_PRIVATE);
        String savedStr = preferences.getString(SHARED_PREFS_KEY,"");
        if(savedStr.isEmpty()){
            String resourseStr = getString(R.string.large_text);
            values.addAll(prepareContent(resourseStr));
            preferences.edit()
                    .putString(SHARED_PREFS_KEY,resourseStr)
                    .apply();
        } else  {
            values.addAll(prepareContent(savedStr));
        }


    }


    private  List<Map<String,String>> prepareContent(String value){

        String[] strings = value.split("\n\n");
        List<Map<String,String>> list=new ArrayList<>();
        for (String str: strings){
            Map<String,String> map = new HashMap<>();
            map.put(TITLE,str.length()+"");
            map.put(SUBTITLE,str);
            list.add(map);

        }
        return  list;

    }
}