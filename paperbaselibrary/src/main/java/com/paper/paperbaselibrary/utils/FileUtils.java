package com.paper.paperbaselibrary.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/7/31 2015.
 */
public class FileUtils {
    public static void saveCache(String key,String value){
        String path = Environment.getDataDirectory().getPath()+"/key";
        File f1 = new File(path);

//        if (f1.exists()) {
            try {
                if (!f1.exists())
                    f1.createNewFile();
                FileOutputStream is = new FileOutputStream(f1);

                is.write(value.getBytes());
                is.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
//    }
    }

    public static String readCache(String key){
        String path = Environment.getDataDirectory().getPath()+"/key";
        File f1 = new File(path);
        String ret = null;
        if (f1.exists()) {
            try {
            FileInputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                ret = bf.readLine();

                bf.close();
                input.close();
                is.close();


            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return ret;
    }
}
