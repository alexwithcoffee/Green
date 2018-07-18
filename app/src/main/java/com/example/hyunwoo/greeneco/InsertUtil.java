package com.example.hyunwoo.greeneco;

import android.annotation.SuppressLint;
import android.os.StrictMode;

/**
 * Created by hyunwoo on 2017-12-21.
 */

public class InsertUtil {
    @SuppressLint("NewApi")
    static public void setInsertUtil() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
