package com.yyxnb.module_user.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.widget.CompoundButton;

import com.tencent.mmkv.MMKV;
import com.yyxnb.common.utils.ToastUtils;
import com.yyxnb.common.utils.log.LogUtils;
import com.yyxnb.skinloader.SkinManager;
import com.yyxnb.skinloader.util.AssetFileUtils;
import com.yyxnb.utils.permission.PermissionListener;
import com.yyxnb.utils.permission.PermissionUtils;

import java.io.File;

import static com.yyxnb.common_base.config.Constants.SKIN_PATH;

public class UserPresenter {

    public static void jumpWallet() {
//        ((context)BaseFragment).startFragment(new UserWalletFragment());
        ToastUtils.normal("跳");
    }

    public void jumpSetUp() {

    }


    private static void toggleSkin(Context context) {
        // 存储权限
        PermissionUtils.with((Activity) context)
                .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionsCheckListener(new PermissionListener() {
                    @Override
                    public void permissionRequestSuccess() {
                        //将assets目录下的皮肤文件拷贝到data/data/.../cache目录下
                        String saveDir = context.getCacheDir().getAbsolutePath() + "/skins";
                        //将打包生成的apk文件, 重命名为'xxx.skin', 防止apk结尾的文件造成混淆.
                        String savefileName = "/night.skin";
                        String asset_dir = "skins/night.apk";
                        File file = new File(saveDir + File.separator + savefileName);
                        if (!file.exists()) {
                            AssetFileUtils.copyAssetFile(context, asset_dir, saveDir, savefileName);
                        }
                        LogUtils.w(" " + file.getAbsolutePath());
                        MMKV.defaultMMKV().encode(SKIN_PATH, file.getAbsolutePath());
                        SkinManager.get().loadSkin(file.getAbsolutePath());
                    }

                    @Override
                    public void permissionRequestFail(String[] grantedPermissions, String[] deniedPermissions, String[] forceDeniedPermissions) {
                    }
                })
                .createConfig()
                .setForceAllPermissionsGranted(true)
                .buildConfig()
                .startCheckPermission();
    }

    public static boolean isChecked() {
        return !SkinManager.get().isUsingDefaultSkin();
    }

    public static void onCheckedChange(CompoundButton buttonView, boolean isChecked) {

        LogUtils.e("ooo " + isChecked);
        if (!isChecked) {
            SkinManager.get().restoreToDefaultSkin();
            MMKV.defaultMMKV().removeValueForKey(SKIN_PATH);
        } else {
            toggleSkin(buttonView.getContext());
        }

        buttonView.setOnCheckedChangeListener((buttonView1, isChecked1) -> {
            buttonView1.setChecked(isChecked1);
            if (!isChecked1) {
                SkinManager.get().restoreToDefaultSkin();
                MMKV.defaultMMKV().removeValueForKey(SKIN_PATH);
            } else {
                toggleSkin(buttonView.getContext());
            }
        });

    }

}
