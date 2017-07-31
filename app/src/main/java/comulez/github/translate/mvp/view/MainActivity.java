package comulez.github.translate.mvp.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comulez.github.translate.R;
import comulez.github.translate.beans.YouDaoBean;
import comulez.github.translate.mvp.ListenClipboardService;
import comulez.github.translate.utils.Constant;
import comulez.github.translate.utils.Utils;

import static comulez.github.translate.utils.Constant.showPop;

public class MainActivity extends AppCompatActivity implements ITranslateView {

    private static String TAG = "MainActivity";
    @Bind(R.id.et_word)
    EditText etWord;
    @Bind(R.id.iv_trans)
    ImageView ivTrans;
    @Bind(R.id.tv_pronounce)
    TextView tvPronounce;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_Explains)
    TextView tvExplains;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.pop_view_content_view)
    RelativeLayout popViewContentView;
    @Bind(R.id.button_per)
    Button buttonPer;
    @Bind(R.id.button_clean)
    Button buttonClean;
    private Intent intent;
    private int OVERLAY_PERMISSION_REQ_CODE = 45;
    private String[] PERMISSIONS = {
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private ListenClipboardService clipboardService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        intent = new Intent(this, ListenClipboardService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        askForPermission();
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i("lcy", "onServiceConnected");
            ListenClipboardService.LocalBinder binder = (ListenClipboardService.LocalBinder) service;
            clipboardService = binder.getServiceInstance();
            clipboardService.attachAct(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("lcy", "onServiceDisconnected");
        }
    };

    @Override
    protected void onResume() {
        Utils.putT(showPop, false);
        super.onResume();
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    private void askForPermission() {
        if (Utils.isM() && !Utils.getBoolean(Constant.hasPermission, false)) {
            new AlertDialog.Builder(this).setMessage(getString(R.string.tip1))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermission();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        } else {
            startService(intent);
        }
    }

    private void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
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
        Utils.putT(Constant.showPop, true);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy -- unbindService");
        unbindService(mConnection);
    }

    @Override
    public void showLoading() {
        Utils.hideSoftKeyboard(this);
        tvResult.setText(getString(R.string.loading));
        tvExplains.setText("");
        tvPronounce.setText("");
    }

    @Override
    public void onError(String msg) {
        tvResult.setText(msg);
    }

    @Override
    public void showResult(YouDaoBean youDaoBean) {
        try {
            if (youDaoBean != null) {
                resetText();
                etWord.setText(youDaoBean.getQuery());
                Utils.setEditTextSelectionToEnd(etWord);
                tvResult.setText(youDaoBean.getTranslation().get(0));
                String phonetic = youDaoBean.getBasic().getPhonetic();
                if (!TextUtils.isEmpty(phonetic))
                    tvPronounce.setText("[" + phonetic + "]");
                tvExplains.setText(Utils.getALl(youDaoBean.getBasic().getExplains(), youDaoBean.getWeb()));
            } else {
                tvResult.setText(getString(R.string.noresult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetText() {
        tvExplains.setText("");
        tvResult.setText("");
        tvPronounce.setText("");
    }


    @OnClick({R.id.et_word, R.id.iv_trans, R.id.button, R.id.button_per, R.id.button_clean})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_word:
                break;
            case R.id.iv_trans:
                String q = etWord.getText().toString();
                clipboardService.translate(q, "en", "zh_CHS", Constant.appkey, 2, Utils.md5(Constant.appkey + q + 2 + Constant.miyao));
                break;
            case R.id.button:
                stopService(intent);
                finish();
                break;
            case R.id.button_per:
                requestPermission();
                break;
            case R.id.button_clean:
                etWord.setText("");
                resetText();
                break;
        }
    }
}
