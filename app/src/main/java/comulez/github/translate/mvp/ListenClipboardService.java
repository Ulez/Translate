package comulez.github.translate.mvp;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import comulez.github.translate.R;
import comulez.github.translate.beans.YouDaoBean;
import comulez.github.translate.mvp.base.MvpBaseService;
import comulez.github.translate.mvp.presenter.TranslatePresenter;
import comulez.github.translate.mvp.view.ITranslateView;
import comulez.github.translate.utils.Constant;
import comulez.github.translate.utils.Utils;
import comulez.github.translate.widget.TipView;

import static comulez.github.translate.utils.Constant.showPop;

public class ListenClipboardService extends MvpBaseService<ITranslateView, TranslatePresenter> implements View.OnClickListener, ITranslateView {

    private ClipboardManager clipboard;

    private static final String TAG = "ListenClipboardService";
    private MyHandler handler;
    private WindowManager mWindowManager;
    private TipView tipView;
    private ClipboardManager.OnPrimaryClipChangedListener listener;

    @Override
    public void onClick(View v) {

    }

    public void cancelHide() {
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }

    public void hide() {
        Message msg = Message.obtain();
        msg.what = Constant.removePop;
        handler.sendMessage(msg);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.removePop:
                    ListenClipboardService service = (ListenClipboardService) reference.get();
                    if (service != null) {
                        service.onRemove();
                    }
            }
        }
    }

    private void onRemove() {
        if (mWindowManager != null && tipView != null && tipView.getParent() != null) {
            tipView.hideWithAnim(mWindowManager, tipView);
        }
    }

    public ListenClipboardService() {
    }

    @Override
    public void showResult(YouDaoBean youDaoBean) {
        Log.i(TAG, "service showResult");
        if (!Utils.getBoolean(showPop, true))
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Utils.t(youDaoBean.getTranslation().get(0));
                return;
            }
        }
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (tipView == null) {
            tipView = new TipView(this);
            tipView.setOnMoreClickListener(this);
        }
        try {
            tipView.update(youDaoBean);
            mWindowManager.addView(tipView, getPopViewParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tipView.startWithAnim();
        Message msg = Message.obtain();
        msg.what = Constant.removePop;
        handler.sendMessageDelayed(msg, Utils.getInt(Constant.SHOW_DURATION, 3000));
    }

    @Override
    public void resetText() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onError(String msg) {

    }

    private WindowManager.LayoutParams getPopViewParams() {
        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags = 0;
        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        layoutParams.gravity = Gravity.CENTER | Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;
        return layoutParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        handler = new MyHandler(this);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (Utils.compareTime(System.currentTimeMillis())) {
                    Log.i(TAG, "onPrimaryClipChanged");
                    try {
                        String q = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                        if (TextUtils.isEmpty(q)) {
                            Utils.t(R.string.cant);
                            return;
                        }
                        mPresenter.translate(q, "en", "zh_CHS", Constant.appkey, 2, Utils.md5(Constant.appkey + q + 2 + Constant.miyao));
                    } catch (Exception e) {
                        Utils.t(R.string.cant);
                        e.printStackTrace();
                    }
                }
            }
        };
        clipboard.addPrimaryClipChangedListener(listener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (clipboard != null && listener != null)
            clipboard.removePrimaryClipChangedListener(listener);
        Log.i(TAG, "onDestroy ListenClipboardService");
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected TranslatePresenter createPresenter() {
        return new TranslatePresenter();
    }
}
