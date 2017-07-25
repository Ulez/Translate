package comulez.github.translate;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private Intent intent;
    //    private BroadcastReceiver receiver;
    private boolean showPop = false;
    private int OVERLAY_PERMISSION_REQ_CODE = 45;
    private String[] PERMISSIONS = {
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                finish();
            }
        });
        intent = new Intent(this, ListenClipboardService.class);
        askForPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPop = false;
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission() {
        if (Utils.isM() && !Utils.hasOverlayPermission()) {
            new AlertDialog.Builder(this).setMessage("使用翻译的划词翻译功能需要你授予浮窗权限。")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        } else {
            startService(intent);
        }
    }

    /**
     * 用户返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Utils.t("权限授予失败，无法开启悬浮窗");
            } else {
                Utils.t("权限授予成功！");
                Utils.putT(Constant.hasPermission, true);
                startService(intent);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        showPop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
