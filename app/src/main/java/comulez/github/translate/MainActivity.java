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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static comulez.github.translate.Constant.showPop;

public class MainActivity extends AppCompatActivity implements ITranslate {

    private static String TAG = "MainActivity";
    @Bind(R.id.tv_word)
    EditText tvWord;
    @Bind(R.id.iv_trans)
    ImageView ivTrans;
    @Bind(R.id.tv_pronounce)
    TextView tvPronounce;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_Explains)
    TextView tvExplains;
    @Bind(R.id.tv_synonyms)
    TextView tvSynonyms;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.pop_view_content_view)
    RelativeLayout popViewContentView;
    private Intent intent;
    private int OVERLAY_PERMISSION_REQ_CODE = 45;
    private String[] PERMISSIONS = {
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private YouDaoBean youDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        intent = new Intent(this, ListenClipboardService.class);
        askForPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.putT(showPop, false);
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission() {
        if (Utils.isM() && !Utils.getBoolean(Constant.hasPermission, false)) {
            new AlertDialog.Builder(this).setMessage("使用翻译的划词翻译功能需要你授予浮窗权限。")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
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
        Utils.putT(Constant.showPop, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void translate(String q, String from, String to, String appKey, int salt, String sign) {
        Utils.hideSoftKeyboard(this);
        tvResult.setText(getString(R.string.loading));
        TRRetrofit.getInstance().getmPRService().getYoudaoTras(q, from, to, appKey, salt, sign)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PbSubscriber<YouDaoBean>() {
                    @Override
                    public void onNext(YouDaoBean youDaoBean) {
                        if (youDaoBean != null) {
                            try {
                                showResult(youDaoBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            tvResult.setText(getString(R.string.noresult));
                        }
                    }
                });
    }

    @Override
    public void showResult(YouDaoBean youDaoBean) {
        youDao = youDaoBean;
        resetText();
        tvWord.setText(youDaoBean.getQuery());
        tvResult.setText(youDaoBean.getTranslation().get(0));
        String phonetic = youDaoBean.getBasic().getPhonetic();
        if (!TextUtils.isEmpty(phonetic))
            tvPronounce.setText("[" + phonetic + "]");
        tvExplains.setText(getALl(youDao.getBasic().getExplains()));
        tvSynonyms.setText(getALl2(youDao.getWeb()).replace("\\n", "\n"));
    }

    private void resetText() {
        tvExplains.setText("");
        tvSynonyms.setText("");
        tvWord.setText("");
        tvResult.setText("");
        tvPronounce.setText("");
    }

    private String getALl2(List<YouDaoBean.WebBean> webBeanList) {
        if (webBeanList == null || webBeanList.size() <= 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (YouDaoBean.WebBean webBean : webBeanList) {
            stringBuilder.append(webBean.getKey() + ":" + webBean.getValue() + "\n");
        }
        return stringBuilder.toString();
    }

    private String getALl(List<String> explains) {
        if (explains == null || explains.size() <= 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : explains) {
            stringBuilder.append(s + "\n");
        }
        return stringBuilder.toString();
    }

    @OnClick({R.id.tv_word, R.id.iv_trans, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_word:
                break;
            case R.id.iv_trans:
                String q = tvWord.getText().toString();
                translate(q, "en", "zh_CHS", Constant.appkey, 2, Utils.md5(Constant.appkey + q + 2 + Constant.miyao));
                break;
            case R.id.button:
                stopService(intent);
                finish();
                break;
        }
    }
}
