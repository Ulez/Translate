package comulez.github.translate.rx;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import comulez.github.translate.exception.ApiExceptionFactory;
import comulez.github.translate.utils.Utils;
import rx.Subscriber;

/**
 * Created by Ulez on 2017/7/26.
 * Email：lcy1532110757@gmail.com
 */


public abstract class PbSubscriber<T> extends Subscriber<T> {

    private ProgressBar progressBar;

    protected PbSubscriber() {
        this(null);
    }

    protected PbSubscriber(View progressBar) {
        super(null, false);
        if (progressBar instanceof ProgressBar)
            this.progressBar = (ProgressBar) progressBar;
    }

    @Override
    public void onCompleted() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    private String TAG = "PbSubscriber";

    @Override
    public void onError(Throwable e) {
        Log.i(TAG, e.toString());
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        Utils.t(ApiExceptionFactory.getApiException(e).getDisplayMessage());
    }

    @Override
    public void onNext(T t) {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }
}
