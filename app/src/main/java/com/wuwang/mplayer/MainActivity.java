package com.wuwang.mplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 16/7/22.
 */
public class MainActivity extends AppCompatActivity {

    private static final int VIDEO_REQUEST_CODE = 10002;
    private static final int IMG_REQUEST_CODE = 10003;

    MyJCVideoPlayerStandard myJCVideoPlayerStandard;

    private Button pick_video, pick_img, commit, reset;
    private EditText video_url, img_url, ad_show, ad_close, redirect_url;
    private ImageView ad_img;
    private Uri adImgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }
        pick_video = (Button) findViewById(R.id.pick_video);
        pick_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performVideoSearch();
            }
        });
        pick_img = (Button) findViewById(R.id.pick_img);
        pick_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performImgSearch();
            }
        });
        commit = (Button) findViewById(R.id.commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(video_url.getText().toString()) ||
                        TextUtils.isEmpty(img_url.getText().toString()) ||
                        TextUtils.isEmpty(ad_show.getText().toString()) ||
                        TextUtils.isEmpty(redirect_url.getText().toString()) ||
                        TextUtils.isEmpty(ad_close.getText().toString())) {
                    Toast.makeText(MainActivity.this, R.string.canshubuquan, Toast.LENGTH_SHORT).show();
                } else {
                    String videoUrl = video_url.getText().toString();
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoUrl, MediaStore.Images.Thumbnails.MINI_KIND);
                    initAd(videoUrl, bitmap, Integer.valueOf(ad_show.getText().toString()), Integer.valueOf(ad_close.getText().toString()));
                    Glide.with(MainActivity.this).load(adImgUri).into(ad_img);
                }
            }
        });
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoUrl = "https://github.com/ljpww72729/ad-img/blob/master/page/shinto.mp4?raw=true";
                initAd(videoUrl, null, 5, 10);
            }
        });
        video_url = (EditText) findViewById(R.id.video_url);
        img_url = (EditText) findViewById(R.id.img_url);
        ad_img = (ImageView) findViewById(R.id.ad_img);
        ad_img.setImageResource(R.drawable.shinto);
        ad_show = (EditText) findViewById(R.id.ad_show);
        ad_close = (EditText) findViewById(R.id.ad_close);
        redirect_url = (EditText) findViewById(R.id.redirect_url);
        ad_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "http://www.shinto-tsushin.co.jp";
                if (!TextUtils.isEmpty(redirect_url.getText().toString())) {
                    url = redirect_url.getText().toString();
                }
                intent.setData(Uri.parse(url));
                intent.setPackage("com.android.browser");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myJCVideoPlayerStandard = (MyJCVideoPlayerStandard) findViewById(R.id.jc_video);
        String videoUrl = "https://github.com/ljpww72729/ad-img/blob/master/page/shinto.mp4?raw=true";
        initAd(videoUrl, null, 5, 10);
    }

    private void initAd(String videoUrl, Bitmap bitmap, final int ad_show, final int ad_close) {
        myJCVideoPlayerStandard.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频");
        if (bitmap == null) {
            Glide.with(MainActivity.this).load("https://github.com/ljpww72729/ad-img/blob/master/page/shinto.png?raw=true").into(myJCVideoPlayerStandard.thumbImageView);
        } else {
            myJCVideoPlayerStandard.thumbImageView.setImageBitmap(bitmap);
        }
        myJCVideoPlayerStandard.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO: 28/07/2017 不断更新视频播放进度，在适当的时候显示进度条橱窗广告,以秒为单位
                String videoCurrentTimeString = myJCVideoPlayerStandard.currentTimeTextView.getText().toString();
                String[] secondArray = videoCurrentTimeString.split(":");
                long second = 0;
                for (int i = secondArray.length - 1; i > -1; i--) {
                    second += Integer.valueOf(secondArray[i]) * Math.pow(60, (secondArray.length - 1 - i));
                }
                if (second >= ad_show && second <= ad_close) {
                    ad_img.setVisibility(View.VISIBLE);
                } else {
                    ad_img.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myJCVideoPlayerStandard.onStartTrackingTouch(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myJCVideoPlayerStandard.onStopTrackingTouch(seekBar);
            }
        });
    }


    public void performVideoSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/mp4");
        startActivityForResult(intent, VIDEO_REQUEST_CODE);
    }

    public void performImgSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, IMG_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == VIDEO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                String uriString = DocumentUtils.getPath(this, uri);
                video_url.setText(uriString);
            }
        } else if (requestCode == IMG_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                adImgUri = uri;
                String uriString = DocumentUtils.getPath(this, uri);
                img_url.setText(uriString);
                Log.i("LinkedME", "onActivityResult: " + uriString);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }


}
