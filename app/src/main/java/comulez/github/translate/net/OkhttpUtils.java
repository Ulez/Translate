package comulez.github.translate.net;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comulez.github.translate.utils.Utils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public class OkhttpUtils {
    public OkHttpClient mOkHttpClient;
    private static OkhttpUtils mInstance;
    private String TAG = "TAG-OkhttpUtils";

    public static final String BASE_URL_YOUDAO = "http://openapi.youdao.com";
    public static final String BASE_URL_BAIDU = "http://api.fanyi.baidu.com";

    private OkhttpUtils() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(9, TimeUnit.SECONDS)
                .writeTimeout(9, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                //添加应用拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //获取request
                        Request request = chain.request();
                        //获取request的创建者builder
                        Request.Builder builder = request.newBuilder();
                        //从request中获取headers，通过给定的键url_name
                        List<String> headerValues = request.headers("url_name");
                        if (headerValues != null && headerValues.size() > 0) {
                            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                            builder.removeHeader("url_name");
                            //匹配获得新的BaseUrl
                            String headerValue = headerValues.get(0);
                            HttpUrl newBaseUrl = null;
                            //从request中获取原有的HttpUrl实例oldHttpUrl
                            HttpUrl oldHttpUrl = request.url();
                            if ("youdao".equals(headerValue)) {
                                newBaseUrl = HttpUrl.parse(BASE_URL_YOUDAO);
                            } else if ("baidu".equals(headerValue)) {
                                newBaseUrl = HttpUrl.parse(BASE_URL_BAIDU);
                            } else {
                                newBaseUrl = oldHttpUrl;
                            }
                            //重建新的HttpUrl，修改需要修改的url部分
                            HttpUrl newFullUrl = oldHttpUrl
                                    .newBuilder()
                                    .scheme(newBaseUrl.scheme())
                                    .host(newBaseUrl.host())
                                    .port(newBaseUrl.port())
                                    .build();
                            //重建这个request，通过builder.url(newFullUrl).build()；
                            //然后返回一个response至此结束修改
//                            Log.e("lcy", "newFullUrl=" + newFullUrl);
                            return chain.proceed(builder.url(newFullUrl).build());
                        } else {
                            return chain.proceed(request);
                        }
                    }
                })
                .build();
    }

    public static OkhttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkhttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkhttpUtils();
                }
            }
        }
        return mInstance;
    }
}
