package com.wuwang.mplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * Created by LinkedME06 on 14/07/2017.
 */

public class BuyFragment extends Fragment {

    private int position;

    public BuyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }

    public static BuyFragment newInstance(int position) {
        BuyFragment fragment = new BuyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_frag, container, false);
        ImageView buy_one = (ImageView) view.findViewById(R.id.buy_one);


        buy_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                String
//                intent.setData(Uri.parse(deeplinks.getText().toString()));
//                intent.setPackage("com.android.browser");
//                PackageManager packageManager = getPackageManager();
//                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                if (info != null) {
//                    //如果有可接受该intent的APP则直接唤起APP
//                    startActivity(intent);
//                } else {
//                    //未安装该浏览器
//                    Toast.makeText(MainActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
//                }
//                String uri_scheme = "taobao://item.taobao.com/item.htm?ut_sk=1.WU8hp72Cu%2BMDADQmUJFgOzTe_21380790_1500020176318.Copy.baobeixiangqingfenxiang&from=tbkfenxiangplus&suid=86B85947-E377-4E97-8C2A-7B7A0650B1D2&tbkShareId=907030413&id=548657153644&systype=m";
//                String uri_scheme = "taobao://item.taobao.com/item.htm?id=548657153644";
                String uri_scheme = "tbopen://m.taobao.com/tbopen/index.html?appkey=23082328&appName=橱窗&packageName=com.wuwang.mplayer&action=ali.open.nav&module=detail&source=bc&TTID=2014_0_23082328@baichuan_ios_2.1&tag=tag1&utdid=WUivi6jFZ6gDAFS0sUEasFy5&params=%7B%0A%20%20%22itemId%22%20%3A%20%22548657153644%22%2C%0A%20%20%22u_channel%22%20%3A%20%221-23082328%22%2C%0A%20%20%22umpChannel%22%20%3A%20%221-23082328%22%2C%0A%20%20%22sdkVersion%22%20%3A%20%222.1%22%2C%0A%20%20%22_viewType%22%20%3A%20%22taobaoH5%22%2C%0A%20%20%22isv_code%22%20%3A%20%22tag1%22%2C%0A%20%20%22time%22%20%3A%20%221498550993445%22%2C%0A%20%20%22sign%22%20%3A%20%229523E08861C999AF071729FE29BD7733%22%2C%0A%20%20%22pid%22%20%3A%20%22mm_97100348_7476080_24834937%22%0A%7D&v=1.1.5&backURL=lmmedia://";
//                String uri_scheme = "tbopen://m.taobao.com/tbopen/index.html?v=2.0.0&umpChannel=1-23091647&packageName=com.liwushuo.gifttalk&source=bc&appkey=23091647&action=ali.open.nav&utdid=WQAc7+KJiAMDAJwcljAC1BU1&module=detail&backURL=alisdk://&ttid=2014_0_23091647%40baichuan_android_3.1.1.11&TTID=2014_0_23091647@baichuan_android_3.1.1.11&appName=礼物说&u_channel=1-23091647&pms={\"umpChannel\":\"1-23091647\",\"itemId\":\"520263347387\",\"pid\":\"mm_56503797_8596089_29498842\",\"time\":\"1500029608481\",\"ttid\":\"2014_0_23091647@baichuan_android_3.1.1.11\",\"u_channel\":\"1-23091647\"}";
                String packageName = "com.taobao.taobao";
                openAppWithUriScheme(getActivity(), uri_scheme, packageName);
            }
        });
        ImageView buy_two = (ImageView) view.findViewById(R.id.buy_two);
        buy_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri_scheme = "taobao://item.taobao.com/item.htm?ut_sk=1.WU8hp72Cu%2BMDADQmUJFgOzTe_21380790_1500020176318.Copy.baobeixiangqingfenxiang&from=tbkfenxiangplus&suid=86B85947-E377-4E97-8C2A-7B7A0650B1D2&tbkShareId=907030413&id=548657153644&systype=m";
                String packageName = "com.taobao.taobao";
                openAppWithUriScheme(getActivity(), uri_scheme, packageName);
            }
        });
        if (position == 0){
            buy_one.setImageResource(R.drawable.three);
            buy_two.setImageResource(R.drawable.two);
        }else {
            buy_one.setImageResource(R.drawable.four);
        }
        return view;
    }

    public static void openAppWithUriScheme(Context context, String uri_scheme, String packageName) {
        try {
            Log.d("linkedme", "openAppWithUriScheme: uri scheme ==== " + uri_scheme);
            if (TextUtils.isEmpty(uri_scheme)) {
                Toast.makeText(context, "Uri Scheme不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (uri_scheme.contains(" ")) {
                Toast.makeText(context, "Uri Scheme中含有空格！", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = Intent.parseUri(uri_scheme, Intent.URI_INTENT_SCHEME);
            if (!TextUtils.isEmpty(packageName)) {
                if (packageName.contains(" ")) {
                    Toast.makeText(context, "packageName中含有空格！", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setPackage(packageName);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo =
                    context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfo != null) {
                Toast.makeText(context, "正在跳转...", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "无可处理该Uri Scheme的APP，无法唤起", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException ignore) {
            Toast.makeText(context, "Uri Scheme解析异常，无法唤起APP", Toast.LENGTH_SHORT).show();
        }
    }

}
