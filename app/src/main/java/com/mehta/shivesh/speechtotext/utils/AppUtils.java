package com.mehta.shivesh.speechtotext.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.mehta.shivesh.speechtotext.application.SpeechToTextApp;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Shivesh
 */
public class AppUtils {

    public static boolean isNetworkAvailable() {
        ConnectivityManager cn = (ConnectivityManager) SpeechToTextApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = Objects.requireNonNull(cn).getActiveNetworkInfo();
        return nf != null && nf.isConnected();
    }


    public static int computeDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();

        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);

                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }


    public static String computeInPercentage(String valueA, String valueB) {
        if (valueA == null) return "-";
        if (valueB == null) return "-";

        double len = Math.max(valueA.length(), valueB.length());
        double distance = len - computeDistance(valueA, valueB);
        if (distance == 0) return "0%";

        BigDecimal percent = new BigDecimal(distance / len * 100d).setScale(0, RoundingMode.HALF_EVEN);
        return percent.toString() + "%";

    }


    public static File getStorageDir(Context ctx) {
        String filesDirPath = Environment.getExternalStorageDirectory().toString() + "/";

        File ret = new File(filesDirPath);
        if (!ret.exists()) {
            ret.mkdirs();
        }
        return ret;
    }

    public static ArrayList<File> listFiles(Context ctx) {
        ArrayList<File> ret = new ArrayList<File>();
        File filesDir = getStorageDir(ctx);
        File[] list = filesDir.listFiles();
        for (File f : list) {
            if (f.getAbsolutePath().contains("MULTIBHASHI_AUDIO_")) {
                ret.add(f);
            }
        }
        return ret;
    }
}
