# HanksReadTxtVolley

在app build.gradle 裡面加入

    implementation 'com.mcxiaoke.volley:library:1.0.+'
來使用Volley 這個套件

這裡使用了檔案讀取以及寫入 ，以及因為需要使用Volley上網 
所以需要在AndroidManifast.xml 加入permission 
    
    <!--storage read & write-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <!--Volley-->
    <uses-permission android:name="android.permission.INTERNET" />
 
 在ManiActivity.java這隻檔案中，我們先判斷permission
 
        PermissionsChecker mPermissionsChecker = new PermissionsChecker(this);
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                // Permission is not granted
                PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
            }else{
                init();
            }
取得Permissions權限後，在進入init()，並且進入檔案讀取 FileRead.java

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
    
在檔案讀取 FileRead.java 中，我們判斷了是否有掛載記憶卡

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
        
以及是否有找到資料夾 packageName ，如果沒有找到就建置一個，然後去讀取檔案內容 readFileContent()
，並在檔案內容讀到後使用abstract theFileContent(String content) 回傳內容給MainActivity.java 

    private void readFileContent(){
        File dirFile = new File(path);
        String fileContent = FileUtil.getFileContent(dirFile);
        theFileContent(fileContent);
    }
 ----------------------------------------------------------------------
    /**
     * 讀取日誌檔案
     *
     * @param file 本地txt或log檔案
     * @return 返回讀取到的檔案內容
     */
    public static String getFileContent(File file) {
        String content = "";
        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content = content + line + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.toString();
        }
        return content;
    }
    
 回到MainActivity.java 拿到檔案資料後，我們已經獲取volley所需要的url了
 這裡我們先將內容顯示在textView上面，然後藉由斷行[\n]來切割字串
 
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
                    handler.postDelayed(runnable, delayTime);
                }
            }
            
然後在runnable 去執行無限迴圈Volley讀取Api

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
    
最後不要忘了，因為是無窮迴圈，所以要在關閉app後斷開runnable

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
        
        
        
        
        
        
        
        
        
        
        
        
