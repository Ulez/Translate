package comulez.github.translate;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public class TRRetrofit {
    private static TRRetrofit mInstance;
    private Api mPRService;
    private Retrofit retrofit;

    private TRRetrofit() {
        retrofit = new Retrofit.Builder()
                .client(OkhttpUtils.getInstance().mOkHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://openapi.youdao.com")
                .build();
        mPRService = retrofit.create(Api.class);
    }

    public static TRRetrofit getInstance() {
        if (mInstance == null) {
            synchronized (TRRetrofit.class) {
                if (mInstance == null)
                    mInstance = new TRRetrofit();
            }
        }
        return mInstance;
    }

    public static void reset() {
        mInstance = null;
    }

    public Api getmPRService() {
        return mPRService;
    }
}
