package comulez.github.translate.mvp.base;

import android.app.Service;
import android.util.Log;

/**
 * Created by Ulez on 2017/7/31.
 * Email：lcy1532110757@gmail.com
 */


public abstract class MvpBaseService<V, P extends BasePresenter<V>> extends Service {
    private static String TAG = "MvpBaseService";
    protected P mPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter.isViewAttached())
            mPresenter.detachView();
    }

    protected abstract P createPresenter();
}
