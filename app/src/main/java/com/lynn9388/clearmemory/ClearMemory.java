package com.lynn9388.clearmemory;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class ClearMemory extends IntentService {
    public static final String ACTION_CLEAR = "com.lynn9388.clearmemory.action.clear";

    public static final String KING_ROOT_PACKAGE = "com.kingroot";
    public static final String SCREEN_RECORD = "ScreenRecord.mp4";
    public static final String PCAP = "Tcpdump.pcap";
    public static final String AUDIO_RECORD = "zqt";
    public static final String VIDEO_URL = "urls";

    public ClearMemory() {
        super("ClearMemory");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CLEAR.equals(action)) {
                handleActionClear();
            }
        }
    }

    private void handleActionClear() {
        clearMemoryCard();
        clearRAM();
    }

    private void clearMemoryCard() {
        try {
            wipeDirectory(Environment.getExternalStorageDirectory().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //.d("lynn", "Clear memory card finished.");
    }

    private void wipeDirectory(String path) {
        File directoryFile = new File(path);
        File[] files = directoryFile.listFiles();
        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                if (!filename.equals(SCREEN_RECORD)
                        && !filename.equals(PCAP)
                        && !filename.equals(AUDIO_RECORD)
                        && !filename.equals(VIDEO_URL)) {
                    if (file.isDirectory()) {
                        wipeDirectory(file.toString());
                        //Log.d("lynn", file.toString());
                        file.delete();
                    } else {
                        //Log.d("lynn", file.toString());
                        file.delete();
                    }
                }
            }
        }
    }

    private void clearRAM() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();

        if (infos != null) {
            for (ActivityManager.RunningAppProcessInfo info : infos) {
                //Log.d("lynn", info.processName);
                if (info.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
                        && !info.processName.contains(KING_ROOT_PACKAGE)) {
                    String[] pkgList = info.pkgList;
                    for (String pkg : pkgList) {
                        try {
                            //Log.d("lynn", pkg);
                            manager.killBackgroundProcesses(pkg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //Log.d("lynn", "Clear memory card finished.");
    }
}
