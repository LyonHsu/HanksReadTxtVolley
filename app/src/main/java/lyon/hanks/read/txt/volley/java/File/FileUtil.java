package lyon.hanks.read.txt.volley.java.File;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    static String TAG = FileUtil.class.getSimpleName();
    /**
     * 遍歷所有檔案
     *
     * @param fileAbsolutePath 傳入的檔案的父目錄
     */
    public static Map<String, String> getFileName(final String fileAbsolutePath) {
        Map<String, String> map = new HashMap<>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        try {
            if (subFile.length > 0) {
                for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                    // 判斷是否為資料夾
                    if (!subFile[iFileLength].isDirectory()) {
                        String filename = subFile[iFileLength].getName();
                        map.put(String.valueOf(iFileLength), filename);
                    }
                }
            }
        } catch (NullPointerException e) {
            e.toString();
        }
        return map;
    }

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

    /**
     * 刪除檔案，可以是檔案或資料夾
     *
     * @param fileName 要刪除的檔名
     * @return 刪除成功返回true，否則返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            Log.e(TAG,"刪除檔案失敗:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 刪除單個檔案
     *
     * @param fileName 要刪除的檔案的檔名
     * @return 單個檔案刪除成功返回true，否則返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName.replaceAll(" ", ""));
        Log.e("TAG", "fileName:" + fileName);
        Log.e("TAG", "file.isFile():" + file.isFile());
        if (file.isFile() || file.exists()) {
            boolean isDel = file.delete();
            return isDel;
        } else {
            Log.e(TAG, "刪除單個檔案失敗：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 刪除目錄及目錄下的檔案
     *
     * @param dir 要刪除的目錄的檔案路徑
     * @return 目錄刪除成功返回true，否則返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以檔案分隔符結尾，自動新增檔案分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("刪除目錄失敗：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("刪除目錄失敗！");
            return false;
        }
        if (dirFile.delete()) {
            System.out.println("刪除目錄" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
}
