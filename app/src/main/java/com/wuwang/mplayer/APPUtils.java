package com.wuwang.mplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * Created by LinkedME06 on 16/03/2017.
 */

public class APPUtils {
    /**
     * 判断本机是否已经安装了APP
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
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
