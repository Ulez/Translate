package comulez.github.translate.mvp.presenter;

import comulez.github.translate.mvp.DataListener;
import comulez.github.translate.beans.YouDaoBean;
import comulez.github.translate.mvp.model.TranslateModelIml;
import comulez.github.translate.mvp.view.ITranslateView;


/**
 * Created by Ulez on 2017/7/28.
 * Email：lcy1532110757@gmail.com
 */


public class TranslatePresenter {
    private ITranslateView translateView;
    private final TranslateModelIml modelIml;

    public TranslatePresenter(ITranslateView translateView) {
        modelIml = new TranslateModelIml();
        this.translateView = translateView;
    }

    public void translate(String q, String from, String to, String appKey, int salt, String sign) {
        translateView.showLoading();
        modelIml.translate(q, from, to, appKey, salt, sign, new DataListener() {
            @Override
            public void onResult(YouDaoBean youDaoBean) {
                translateView.showResult(youDaoBean);
            }
        });
    }
}
