package comulez.github.translate.mvp;

import comulez.github.translate.beans.YouDaoBean;

/**
 * Created by Ulez on 2017/7/28.
 * Email：lcy1532110757@gmail.com
 */


public interface DataListener {
    void onResult(YouDaoBean youDaoBean);

    void onError(String displayMessage);
}
