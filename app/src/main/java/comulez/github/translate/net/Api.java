package comulez.github.translate.net;

import comulez.github.translate.beans.YouDaoBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public interface Api {
    //http://openapi.youdao.com/api?q=good&from=en&to=zh_CHS&appKey=7d3db18774c877c1&salt=2&sign=b80cf62c51edfdbf94a875552e6da409
    @GET("/api")
    Observable<YouDaoBean> getYoudaoTras(@Query("q") String q, @Query("from") String from, @Query("to") String to, @Query("appKey") String appKey, @Query("salt") int salt, @Query("sign") String sign);
}
