package comulez.github.translate;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public class OkhttpUtils {
    public OkHttpClient mOkHttpClient;
    private static OkhttpUtils mInstance;
    private String TAG = "TAG-OkhttpUtils";

    private OkhttpUtils() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(9, TimeUnit.SECONDS)
                .writeTimeout(9, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
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
