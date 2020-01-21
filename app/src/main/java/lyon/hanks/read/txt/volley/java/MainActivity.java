package lyon.hanks.read.txt.volley.java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
    };

    public static final int REQUEST_CODE = 0; // ???

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
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

    int times = 0;
    String path[] ;
    private void init(){
        new FileRead(this) {
            @Override
            public void theFileContent(String content) {
                if(textView!=null)
                    textView.setText(content);
                int pathLen=0;
                times = 0;
                path=content.split("\n");
                pathLen = path.length;
                Log.d(TAG,"path size:"+pathLen);

                if(pathLen>0) {
                    handler.postDelayed(runnable, 500);
                }
            }
        };
    }

    Handler handler = new Handler();

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
