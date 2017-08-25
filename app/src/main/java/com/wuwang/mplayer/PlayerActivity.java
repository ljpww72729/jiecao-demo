/*
 *
 * PlayerActivity.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.mplayer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Description:
 */
public class PlayerActivity extends AppCompatActivity {

    private EditText mEditAddress;
    private SurfaceView mPlayerView;
    private SurfaceView mPlayerView_n;
    private MPlayer player;
    private MPlayer playerN;
    private SeekBar seekBar;
    private SeekBar seekBar_n;
    private ImageView buy;
    private ImageView buy_n;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private CloseView close_buy;
    private RelativeLayout rl_buy;
    private ImageView ic_play;
    private ImageView ic_play_n;
    private ImageView pause_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        initViewN();
        initPlayer();
        initPlayerN();
    }

    private void initView() {
        mEditAddress = (EditText) findViewById(R.id.mEditAddress);
        mPlayerView = (SurfaceView) findViewById(R.id.mPlayerView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        buy = (ImageView) findViewById(R.id.buy);
        rl_buy = (RelativeLayout) findViewById(R.id.rl_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rl_buy.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "http://www.shinto-tsushin.co.jp";
                intent.setData(Uri.parse(url));
//                intent.setPackage("com.android.browser");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(PlayerActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        close_buy = (CloseView) findViewById(R.id.close_buy);
        close_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_buy.setVisibility(View.GONE);
            }
        });
        ic_play = (ImageView) findViewById(R.id.play);
        ic_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                ic_play.setVisibility(View.GONE);
            }
        });
    }

    private void initViewN() {
        mPlayerView_n = (SurfaceView) findViewById(R.id.mPlayerView_n);
        seekBar_n = (SeekBar) findViewById(R.id.seekBar_n);
        buy_n = (ImageView) findViewById(R.id.buy_n);
        buy_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接打开
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "http://i.waimai.meituan.com/external/poi/473903379564459?utm_source=5801&amp;wmi_from=cpoiinfo&amp;user_id=32570540&utm_term=AiphoneBwaimaiC5.8.1DqqEwm-restautantG6EF62C04BFF101D35C16CA2E611BAFB096EDFCE9AA7E094DCC0B1739666E2E6D20170714161827911&utm_source=appshare&utm_medium=iOSweb";
                intent.setData(Uri.parse(url));
//                intent.setPackage("com.android.browser");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(PlayerActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ic_play_n = (ImageView) findViewById(R.id.play_n);
        ic_play_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playN();
                ic_play_n.setVisibility(View.GONE);
            }
        });
        pause_n = (ImageView) findViewById(R.id.pause_n);
        pause_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAndPauseN();
            }
        });
    }

    private void initPlayer() {
        player = new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
    }

    private void initPlayerN() {
        playerN = new MPlayer();
        playerN.setDisplay(new MinimalDisplay(mPlayerView_n));
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.onResume();
        playerN.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onPause();
        playerN.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestroy();
        playerN.onDestroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mPlay:
                play();
                break;
            case R.id.mPlay_n:
                playN();
                break;
            case R.id.mPlayerView:
                playAndPause();
                break;
            case R.id.mPlayerView_n:
                playAndPauseN();
                break;
            case R.id.mType:
                player.setCrop(!player.isCrop());
                break;
        }
    }

    private void playAndPause(){
        if (player.isPlaying()) {
            player.pause();
        } else {
            try {
                player.play();
            } catch (MPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    private void playAndPauseN(){
        if (playerN.isPlaying()) {
            playerN.pause();
        } else {
            try {
                playerN.play();
            } catch (MPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    private void play() {
        //                String mUrl=mEditAddress.getText().toString();
        Uri mUri = Uri.parse("android.resource://"
                + getPackageName() + "/"
                + R.raw.shinto);
//                if(mUrl.length()>0){
//                    Log.e("wuwang","播放->"+mUrl);
        try {
            player.setSourceUri(PlayerActivity.this, mUri);
            player.play();
            seekBar.setProgress(0);
            player.setPlayListener(new IMPlayListener() {
                @Override
                public void onStart(IMPlayer implayer) {
                    Log.i("media", "onStart: " + implayer.getDuration());
                    seekBar.setMax(implayer.getDuration());
                }

                @Override
                public void onPause(IMPlayer implayer) {
                    Toast.makeText(PlayerActivity.this, "pause", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResume(IMPlayer implayer) {

                }

                @Override
                public void onComplete(IMPlayer implayer) {

                }

                @Override
                public void onProcess(IMPlayer player, int currentPosition) {
                    Log.i("media", "onProcess: " + currentPosition);
                    seekBar.setProgress(currentPosition);
                }


            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        player.seekTo(progress);
                    } else {
                        if (progress > 3000 && progress < 15000) {
                            buy.setVisibility(View.VISIBLE);
                        } else {
                            buy.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (MPlayerException e) {
            e.printStackTrace();
        }
//                }
    }

    private void playN() {
        //                String mUrl=mEditAddress.getText().toString();
        Uri mUri_n = Uri.parse("android.resource://"
                + getPackageName() + "/"
                + R.raw.niurou);
//                if(mUrl.length()>0){
//                    Log.e("wuwang","播放->"+mUrl);
        try {
            playerN.setSourceUri(PlayerActivity.this, mUri_n);
            playerN.play();
            seekBar_n.setProgress(0);
            playerN.setPlayListener(new IMPlayListener() {
                @Override
                public void onStart(IMPlayer implayer) {
                    Log.i("media", "onStart: " + implayer.getDuration());
                    seekBar_n.setMax(implayer.getDuration());
                    pause_n.setImageResource(R.drawable.ic_play_arrow);
                }

                @Override
                public void onPause(IMPlayer implayer) {
                    pause_n.setImageResource(R.drawable.ic_play_arrow);
                }

                @Override
                public void onResume(IMPlayer implayer) {
                    Toast.makeText(PlayerActivity.this, "resume", Toast.LENGTH_SHORT).show();
                    pause_n.setImageResource(R.drawable.ic_pause);
                }

                @Override
                public void onComplete(IMPlayer implayer) {
                    pause_n.setImageResource(R.drawable.ic_play_arrow);
                }

                @Override
                public void onProcess(IMPlayer player, int currentPosition) {
                    Log.i("media", "onProcess: " + currentPosition);
                    seekBar_n.setProgress(currentPosition);
                }


            });
            seekBar_n.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        playerN.seekTo(progress);
                    } else {
                        if (progress > 1000 && progress < 11000) {
                            buy_n.setVisibility(View.VISIBLE);
                        } else {
                            buy_n.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (MPlayerException e) {
            e.printStackTrace();
        }
//                }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BuyFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }


    }

}
