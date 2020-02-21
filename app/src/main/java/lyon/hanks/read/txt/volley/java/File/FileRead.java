package lyon.hanks.read.txt.volley.java.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import lyon.hanks.read.txt.volley.java.MainActivity;

import static lyon.hanks.read.txt.volley.java.MainActivity.fileNameSave;

/**
 * https://blog.csdn.net/nugongahou110/article/details/48154859
 */
public abstract class FileRead {
    String TAG = FileRead.class.getSimpleName();
    String path = Environment.getExternalStorageDirectory().getPath() + MainActivity.packageName;
    public FileRead(Context context){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "path=" + path);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            readFileContent();
        }else{
            Toast.makeText(context,"Can't get SD path",Toast.LENGTH_SHORT).show();
        }
    }

    private void readFileContent(){
        File dirFile = new File(path+"/"+fileNameSave);
        Log.d(TAG,"readFileContent :"+dirFile.getPath());
        String fileContent = FileUtil.getFileContent(dirFile);
        theFileContent(fileContent);
    }

    public abstract void theFileContent(String content);

}
