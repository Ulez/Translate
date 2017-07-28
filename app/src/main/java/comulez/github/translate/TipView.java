package comulez.github.translate;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Ulez on 2017/7/25.
 * Email：lcy1532110757@gmail.com
 */


public class TipView extends LinearLayout {

    private WeakReference<ListenClipboardService> serviceWeakReference;
    private static final String TAG = "TipView";
    private TextView tvExplains;
    private TextView tvWord;
    private TextView tvResult;
    private TextView tvPronounce;
    private RelativeLayout mLlSrc;
    private View mContentView;
    private YouDaoBean youDao;
    private int DURATION_TIME = 400;
    private boolean expand = false;

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
        tvExplains = (TextView) mContainer.findViewById(R.id.tv_Explains);
        mContainer.findViewById(R.id.pop_view_content_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand) {
                    expand = true;
                    addMore();
                } else {
                    if (serviceWeakReference != null && serviceWeakReference.get() != null) {
                        expand = false;
                        serviceWeakReference.get().hide();
                    }
                }
            }
        });
        mLlSrc = (RelativeLayout) mContainer.findViewById(R.id.ll_pop_src);
        mContentView = mContainer.findViewById(R.id.pop_view_content_view);
    }

    public void update(YouDaoBean youDaoBean) {
        try {
            youDao = youDaoBean;
            resetText();
            tvWord.setText(youDaoBean.getQuery());
            tvResult.setText(youDaoBean.getTranslation().get(0));
            tvPronounce.setText("[" + youDaoBean.getBasic().getPhonetic() + "]");
            startWithAnim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startWithAnim() {
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(mContentView, "translationY", -700, 0);
        translationAnim.setDuration(DURATION_TIME);
        translationAnim.start();
    }

    public void hideWithAnim(final WindowManager mWindowManager, final TipView tipView) {
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(mContentView, "translationY", 0, -700);
        translationAnim.setDuration(DURATION_TIME);
        translationAnim.start();
        translationAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mWindowManager.removeView(tipView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mWindowManager.removeView(tipView);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void resetText() {
        tvExplains.setText("");
        tvWord.setText("");
        tvResult.setText("");
        tvPronounce.setText("");

        tvExplains.setVisibility(GONE);
    }

    public void addMore() {
        try {
            if (youDao != null) {
                if (serviceWeakReference != null && serviceWeakReference.get() != null)
                    serviceWeakReference.get().cancelHide();
                tvExplains.setVisibility(VISIBLE);
                tvExplains.setText(Utils.getALl(youDao.getBasic().getExplains(), youDao.getWeb()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnMoreClickListener(ListenClipboardService service) {
        serviceWeakReference = new WeakReference<>(service);
    }
}
