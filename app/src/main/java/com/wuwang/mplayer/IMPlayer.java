/*
 *
 * IMPlayer.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.mplayer;

import android.content.Context;
import android.net.Uri;

/**
 * Description:
 */
public interface IMPlayer {

    /**
     * 设置资源
     *
     * @param url 资源路径
     */
    void setSource(String url) throws MPlayerException;

    void setSourceUri(Context context, Uri uri) throws MPlayerException;

    /**
     * 设置显示视频的载体
     *
     * @param display 视频播放的载体及相关界面
     */
    void setDisplay(IMDisplay display);

    /**
     * 播放视频
     */
    void play() throws MPlayerException;

    /**
     * 暂停视频
     */
    void pause();


    void onPause();

    void onResume();

    void onDestroy();

    //获取总时长
    int getDuration();

}
