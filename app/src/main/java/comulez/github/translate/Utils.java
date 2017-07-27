package comulez.github.translate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ulez on 2017/7/24.
 * Email：lcy1532110757@gmail.com
 */

public class Utils {
    private static long lastTime = 0;
    private static SharedPreferences sp = MyApplication.getContext().getSharedPreferences("permissions", Context.MODE_PRIVATE);

    /**
     * 生成32位MD5摘要
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = string.getBytes("utf-8");
            /** 获得MD5摘要算法的 MessageDigest 对象 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /** 使用指定的字节更新摘要 */
            mdInst.update(btInput);
            /** 获得密文 */
            byte[] md = mdInst.digest();
            /** 把密文转换成十六进制的字符串形式 */
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 根据api地址和参数生成请求URL
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }
            if (i != 0) {
                builder.append('&');
            }
            builder.append(key);
            builder.append('=');
            builder.append(encode(value));
            i++;
        }
        return builder.toString();
    }

    /**
     * 进行URL编码
     *
     * @param input
     * @return
     */
    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

    public static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean compareTime(long currentTime) {
        long deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        return deltaTime >= 200;
    }

    public static void t(String m) {
        Toast.makeText(MyApplication.getContext(), m, Toast.LENGTH_SHORT).show();
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void tL(String m) {
        Toast.makeText(MyApplication.getContext(), m, Toast.LENGTH_LONG).show();
    }

    public static void t(int sId) {
        Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(sId), Toast.LENGTH_LONG).show();
    }

    public static boolean hasOverlayPermission() {
        return getBoolean(Constant.hasPermission, false);
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static void putT(String key, Object value) {
        if (value instanceof String) {
            sp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).commit();
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public static void remove(String key) {
        sp.edit().remove(key).commit();
    }

    public static void removeAll() {
        sp.edit().clear().commit();
    }

    public static String getALl(List<String> explains, List<YouDaoBean.WebBean> webBeanList) {
        if (explains == null || explains.size() <= 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : explains) {
            stringBuilder.append(s + "\n");
        }
        if (webBeanList == null || webBeanList.size() <= 0)
            return stringBuilder.toString();
        stringBuilder.append("\n");
        for (YouDaoBean.WebBean webBean : webBeanList) {
            stringBuilder.append(webBean.getKey() + ":" + webBean.getValue() + "\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 设置输入框的光标到末尾
     */
    public static final void setEditTextSelectionToEnd(EditText editText) {
        Editable editable = editText.getEditableText();
        Selection.setSelection((Spannable) editable, editable.toString().length());
    }
}
