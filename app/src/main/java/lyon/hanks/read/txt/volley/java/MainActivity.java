package lyon.hanks.read.txt.volley.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import lyon.hanks.read.txt.volley.java.File.FileRead;
import lyon.hanks.read.txt.volley.java.Permission.PermissionsActivity;
import lyon.hanks.read.txt.volley.java.Permission.PermissionsChecker;
import lyon.hanks.read.txt.volley.java.VolleyTool.VolleyTool;

public class MainActivity extends AppCompatActivity {

    public static String packageName="/Hanks/";
    public static String fileNameSave = "hanks.txt";
    int delayTime = 500;

    String TAG = MainActivity.class.getSimpleName();
    final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
    };

    public static final int REQUEST_CODE = 0; // ???

    TextView textView ,timesTextView;
    int time =0;
    final int SHOWTIME = 0;
    ListView listview;
    ArrayAdapter adapter;
    int times = 0;
    String path[] ;
    ArrayList<String> path2= new ArrayList<>() ;
    ArrayList<Integer> timeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        timesTextView = (TextView)findViewById(R.id.times);
        adapter = new ArrayAdapter<>(this,R.layout.customizedlistitem,path2);
        listview = (ListView)findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setSelector(R.drawable.btn_light_blue);

        PermissionsChecker mPermissionsChecker = new PermissionsChecker(this);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            // Permission is not granted
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        }else{
            init();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED){
            init();
        }
    }


    private void init(){
        time =0;
        new FileRead(this) {
            @Override
            public void theFileContent(String content) {
                textView.setText("");
                int pathLen=0;
                times = 0;
                path=content.split("\n");
                pathLen = path.length;
                Log.d(TAG,"path size:"+pathLen);
                for(int i=0;i<path.length;i++){
                    timeList.add(0);

                    if(textView!=null)
                        textView.append("["+i+"]:"+path[i]+"\n");
                    path2.add("["+i+"]:"+path[i]);
                }
                adapter.notifyDataSetChanged();
                if(pathLen>0) {
                    handler.postDelayed(runnable, delayTime);
                }
            }
        };
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOWTIME:
                    if(timesTextView!=null)
                        timesTextView.setText(msg.obj.toString());
                    break;
            }

        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int pathLen=path.length;
            if(path==null || pathLen<1) {
                Toast.makeText(MainActivity.this,"No Url",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"path size:"+ pathLen);
                return;
            }
            if(times>=pathLen){
                times= 0;
            }
            time++;
            int t = timeList.get(times);
            t++;
            timeList.add(times,t);
            Message msg = new Message();
            msg.what = SHOWTIME;
            msg.obj = "total:"+time +" ,"+"["+t+"]:"+ path[times];
            handler.sendMessage(msg);

            listview.setSelection(times);
            listview.requestFocusFromTouch();
            listview.requestFocus();




            Log.e(TAG ,"Runnable path:"+path[times]);
            if(path[times]!=null && !path[times].isEmpty()){
                Log.e(TAG ,"Runnable pathLen:"+pathLen);
                new VolleyTool(MainActivity.this, path[times]) {
                    @Override
                    public void Next() {
                        times++;
                        handler.postDelayed(runnable, delayTime);
                    }
                };
            }else{
                times++;
                Log.e(TAG ,"Runnable pathLen:"+pathLen);
                handler.postDelayed(runnable, delayTime);
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
