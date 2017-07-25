package comulez.github.translate;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Ulez on 2017/7/25.
 * Email：lcy1532110757@gmail.com
 */


public class TipView extends LinearLayout {

    private ImageView imHide;
    private WeakReference<ListenClipboardService> serviceWeakReference;
    private static final String TAG = "TipView";
    private ImageView imMore;
    private TextView tvExplains;
    private TextView tvSynonyms;
    private TextView tvWord;
    private TextView tvResult;
    private TextView tvPronounce;
    private LinearLayout mLlSrc;
    private View mContentView;
    private YouDaoBean youDao;

    public TipView(Context context) {
        this(context, null);
    }

    public TipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RelativeLayout mContainer = (RelativeLayout) View.inflate(context, R.layout.pop_view, null);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        tvWord = (TextView) mContainer.findViewById(R.id.tv_word);
        tvResult = (TextView) mContainer.findViewById(R.id.tv_result);
        tvPronounce = (TextView) mContainer.findViewById(R.id.tv_pronounce);
        tvSynonyms = (TextView) mContainer.findViewById(R.id.tv_synonyms);
        tvExplains = (TextView) mContainer.findViewById(R.id.tv_Explains);
        imMore = (ImageView) mContainer.findViewById(R.id.im_more);
        imHide = (ImageView) mContainer.findViewById(R.id.im_hide);
        imMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imMore.setVisibility(GONE);
                addMore();
            }
        });
        imHide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceWeakReference != null && serviceWeakReference.get() != null)
                    serviceWeakReference.get().hide();
            }
        });
        mLlSrc = (LinearLayout) mContainer.findViewById(R.id.ll_pop_src);
        mContentView = mContainer.findViewById(R.id.pop_view_content_view);
    }

    public void update(YouDaoBean youDaoBean) {
        youDao = youDaoBean;
        imMore.setVisibility(VISIBLE);
        imHide.setVisibility(GONE);
        tvSynonyms.setVisibility(GONE);
        tvExplains.setVisibility(GONE);
        Log.e(TAG, "update=" + youDaoBean.getQuery() + youDaoBean.getTranslation().get(0));
        tvWord.setText(youDaoBean.getQuery());
        tvPronounce.setText("[" + youDaoBean.getBasic().getPhonetic() + "]");
        tvResult.setText(youDaoBean.getTranslation().get(0));
    }

    public void addMore() {
        if (youDao != null) {
            if (serviceWeakReference != null && serviceWeakReference.get() != null)
                serviceWeakReference.get().cancelHide();
            tvExplains.setVisibility(VISIBLE);
            tvSynonyms.setVisibility(VISIBLE);
            imHide.setVisibility(VISIBLE);
            List<String> explains = youDao.getBasic().getExplains();
            List<YouDaoBean.WebBean> webBeanList = youDao.getWeb();
            String result = "";
            for (String s : explains) {
                result += s + "\n";
            }
            tvExplains.setText(result);
            for (YouDaoBean.WebBean webBean : webBeanList) {
                result += (webBean.getKey() + ":" + webBean.getValue() + "\n");
            }
            tvSynonyms.setText(result);
        }
    }

    public void setOnMoreClickListener(ListenClipboardService service) {
        serviceWeakReference = new WeakReference<>(service);
    }
}
