package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class PlayWAV {

    ContinuousAudioDataStream cads;
    private static PlayWAV P;

    private PlayWAV() {
        try {
            FileInputStream fis = new FileInputStream("sounds/alarm11.wav");// 获得声音文件
            AudioStream as = new AudioStream(fis);
            // AudioPlayer.player.start(as);
            AudioData ad = as.getData();// 转化的wav文件这句会报错
            cads = new ContinuousAudioDataStream(ad);// 循环播放
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static PlayWAV getInstance() {
        if (P == null) {
            synchronized (PlayWAV.class) {
                if (P == null) {
                    P = new PlayWAV();
                }
            }
        }
        return P;
    }

    public void play() {
        if (MyConfigure.isVioceWarn() && !MyConfigure.isRunningVioceWarn()) {
            MyConfigure.setRunningVoiceWarn(true);
            AudioPlayer.player.start(cads);// Play audio.
        }
    }

    public void stop() {
        MyConfigure.setRunningVoiceWarn(false);
        AudioPlayer.player.stop(cads);// Stop audio.
    }

}
