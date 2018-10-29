package cn.qingchengfit.saascommon.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.adapter.AdvertiseAdapter;
import cn.qingchengfit.saascommon.model.AdvertiseInfo;

public class AdvertiseDialog extends Dialog {
    private static final String TAG = "AdvertiseDialog";
    private Context context;
    private AdvertiseAdapter adapter;
    //    private List<View> pages = new ArrayList<>();
    private ImageView dot;
    private ImageView[] dots;

    private DialogClickListener listener;
    private SubscriberOnNextListener getAdvertiseOnNext;

    private List<AdvertiseInfo> mAdvertiseInfo;

    private ImageView closeBtn;
    private ViewPager viewPager;
    private ViewGroup group;


    public AdvertiseDialog(Context context) {
        super(context);
        this.context = context;
        getAdvertiseOnNext = new SubscriberOnNextListener<List<AdvertiseInfo>>() {
            @Override
            public void onNext(List<AdvertiseInfo> advertiseInfos) {

            }
        };
        setContentView(R.layout.dialog_advertise);
        closeBtn = findViewById(R.id.btn_close);
        viewPager = findViewById(R.id.vp_advertise);
        group = findViewById(R.id.dot_parent);
        setCanceledOnTouchOutside(true);
    }

    public void setAdData(@NonNull List<AdvertiseInfo> advertiseInfos) {
        if (!advertiseInfos.isEmpty()) {
            mAdvertiseInfo = advertiseInfos;
            initView();
            initAdapter();
            if(mAdvertiseInfo.size()!=1){
                initPointer();
            }
        }

    }

    private void initView() {
        closeBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCloseClickListener(this);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.length; i++) {
                    dots[position].setBackgroundResource(R.drawable.ic_dot);
                    if (i != position)
                        dots[i].setBackgroundResource(R.drawable.ic_dot_o);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initAdapter() {
        adapter = new AdvertiseAdapter(context, listener, mAdvertiseInfo);
        viewPager.setAdapter(adapter);
    }

    private void initPointer() {
        dots = new ImageView[mAdvertiseInfo.size()];
        for (int i = 0; i < dots.length; i++) {
            dot = new ImageView(context);
            dot.setLayoutParams(new ViewGroup.LayoutParams(25, 25));
            dot.setPadding(20, 0, 20, 0);
            dots[i] = dot;
            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.ic_dot);
            } else {
                dots[i].setBackgroundResource(R.drawable.ic_dot_o);
            }
            group.addView(dots[i]);
        }
    }

    public void showDialog() {
        Window window = getWindow();
        window.setWindowAnimations(R.style.QcStyleDialog);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
        show();
    }


    public void setClickListener(DialogClickListener listener) {
        this.listener = listener;
    }

    public interface DialogClickListener {
        void onCloseClickListener(Dialog dialog);

        void onItemClickListener(AdvertiseInfo item);
    }

    public interface SubscriberOnNextListener<T> {
        void onNext(T t);
    }

}
