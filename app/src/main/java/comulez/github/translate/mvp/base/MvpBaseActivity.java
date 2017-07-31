package comulez.github.translate.mvp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ulez on 2017/7/31.
 * Email：lcy1532110757@gmail.com
 */


public abstract class MvpBaseActivity<V, P extends BasePresenter<V>> extends AppCompatActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter.isViewAttached())
            mPresenter.detachView();
    }

    protected abstract P createPresenter();
}
