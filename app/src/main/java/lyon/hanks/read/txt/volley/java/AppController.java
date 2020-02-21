package lyon.hanks.read.txt.volley.java;

import android.app.Application;

import lyon.hanks.read.txt.volley.java.VolleyTool.VolleyTool;

public class AppController extends Application {
    String TAG = AppController.class.getSimpleName();
    private static AppController appController;
    VolleyTool volleyTool;

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
        volleyTool = new VolleyTool(this);
    }

    public static synchronized AppController getInstance() {
        return appController;
    }

    public VolleyTool getVolleyTool(){
        return volleyTool;
    }


}
