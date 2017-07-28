package comulez.github.translate.mvp.model;

import comulez.github.translate.mvp.DataListener;
import comulez.github.translate.rx.PbSubscriber;
import comulez.github.translate.net.TRRetrofit;
import comulez.github.translate.beans.YouDaoBean;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ulez on 2017/7/28.
 * Email：lcy1532110757@gmail.com
 */


public class TranslateModelIml implements ITranslateModel {
    @Override
    public void translate(String q, String from, String to, String appKey, int salt, String sign, final DataListener listener) {
        TRRetrofit.getInstance().getmPRService().getYoudaoTras(q, from, to, appKey, salt, sign)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PbSubscriber<YouDaoBean>() {
                    @Override
                    public void onNext(YouDaoBean youDaoBean) {
                        listener.onResult(youDaoBean);
                    }
                });
    }

}
