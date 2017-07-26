package comulez.github.translate;

/**
 * Created by Ulez on 2017/7/26.
 * Email：lcy1532110757@gmail.com
 */


public interface ITranslate {
    void translate(String q, String from, String to, String appKey, int salt, String sign);

    void showResult(YouDaoBean youDaoBean);
}
