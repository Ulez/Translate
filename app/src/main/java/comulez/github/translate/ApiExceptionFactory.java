package comulez.github.translate;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Ulez on 2017/7/26.
 * Email：lcy1532110757@gmail.com
 */


public class ApiExceptionFactory {
    public static ApiException getApiException(Throwable e) {
        ApiException apiException = new ApiException(e);
        String msg;
        int code;
        if (e instanceof ConnectException) {
            msg = MyApplication.getContext().getString(R.string.server_connect_error);
            code = Constant.NETWORD_ERROR;
        } else if (e instanceof UnknownHostException) {
            msg = MyApplication.getContext().getString(R.string.unknown_host_error);
            code = Constant.Unknown_Host_ERROR;
        } else if (e instanceof JsonParseException) {
            msg = MyApplication.getContext().getString(R.string.json_parse_error);
            code = Constant.Json_Parse_ERROR;
        } else if (e instanceof SocketTimeoutException) {
            msg = MyApplication.getContext().getString(R.string.timeout_error);
            code = Constant.CONNECT_ERROR;
        } else if (e instanceof MalformedJsonException) {
            msg = MyApplication.getContext().getString(R.string.json_error2);
            code = Constant.JSON_ERROR2;
        } else if (e instanceof HttpException) {
            msg = MyApplication.getContext().getString(R.string.error404);
            code = Constant.ERROR404;
        } else {
            msg = MyApplication.getContext().getString(R.string.unknow_error);
            code = Constant.UNKNOWN_ERROR;
        }
        apiException.setCode(code);
        apiException.setDisplayMessage(msg);
        return apiException;
    }
}
