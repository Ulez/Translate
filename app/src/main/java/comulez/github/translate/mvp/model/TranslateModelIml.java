package comulez.github.translate.mvp.model;

import android.util.Log;

import java.io.UnsupportedEncodingException;

import comulez.github.translate.exception.ApiExceptionFactory;
import comulez.github.translate.mvp.DataListener;
import comulez.github.translate.rx.PbSubscriber;
import comulez.github.translate.net.TRRetrofit;
import comulez.github.translate.beans.YouDaoBean;
import comulez.github.translate.utils.Constant;
import comulez.github.translate.utils.Utils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static comulez.github.translate.utils.Constant.showPop;
import static comulez.github.translate.utils.Constant.youdao;

/**
 * Created by Ulez on 2017/7/28.
 * Email：lcy1532110757@gmail.com
 */


public class TranslateModelIml implements ITranslateModel {
    @Override
    public void translate(String q, String from, String to, String appKey, int salt, String sign, final DataListener listener) {
        if (Utils.getBoolean(Constant.youdao, true)) {
            TRRetrofit.getInstance().getmPRService().getYoudaoTras(q, from, to, appKey, salt, sign)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new PbSubscriber<YouDaoBean>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            listener.onError(ApiExceptionFactory.getApiException(e).getDisplayMessage());
                        }

                        @Override
                        public void onNext(YouDaoBean youDaoBean) {
                            listener.onResult(youDaoBean);
                        }
                    });
        } else {
            salt = 1435660288;
            String appid = "20180503000153022";
            String miyao = "rYiyA7Sj8Ey95cRr6g6P";
            String s1 = appid + q + salt + miyao;
            sign = Utils.md5(s1);
            to = "zh";
            TRRetrofit.getInstance().getmPRService().getBaiduTras(q, from, to, appid, salt, sign)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new PbSubscriber<YouDaoBean>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            listener.onError(ApiExceptionFactory.getApiException(e).getDisplayMessage());
                        }

                        @Override
                        public void onNext(YouDaoBean youDaoBean) {
                            listener.onResult(youDaoBean);
                        }
                    });
        }
    }

}
