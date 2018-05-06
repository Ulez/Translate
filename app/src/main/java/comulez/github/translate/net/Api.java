package comulez.github.translate.net;

import comulez.github.translate.beans.YouDaoBean;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public interface Api {
    //http://openapi.youdao.com/api?q=good&from=en&to=zh_CHS&appKey=7d3db18774c877c1&salt=2&sign=b80cf62c51edfdbf94a875552e6da409
    @Headers({"url_name:youdao"})
    @GET("/api")
    Observable<YouDaoBean> getYoudaoTras(@Query("q") String q, @Query("from") String from, @Query("to") String to, @Query("appKey") String appKey, @Query("salt") int salt, @Query("sign") String sign);

    //http://api.fanyi.baidu.com/api/trans/vip/translate?q=apple&from=auto&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4
    @Headers({"url_name:baidu"})
    @GET("/api/trans/vip/translate")
    Observable<YouDaoBean> getBaiduTras(@Query("q") String q, @Query("from") String from, @Query("to") String to, @Query("appid") String appid, @Query("salt") int salt, @Query("sign") String sign);
}
