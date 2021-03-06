package cn.qingchengfit.saasbase.cards.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.event.EventLimitBuyCount;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import com.bigkoo.pickerview.lib.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import java.util.ArrayList;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/10/18.
 */

public class BottomBuyLimitFragment extends BaseDialogFragment {

    private static final int mMaxLimitCount = 10;
	WheelView mLimitCountWhellview;
    private ArrayList<String> mLimitCountlist;

    public static BottomBuyLimitFragment newInstance(int count) {

        Bundle args = new Bundle();
        args.putInt("i", count);
        BottomBuyLimitFragment fragment = new BottomBuyLimitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_limit_buy, container, false);
      mLimitCountWhellview = (WheelView) view.findViewById(R.id.limit_count_whellview);
      view.findViewById(R.id.comfirm_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomBuyLimitFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.limit_count_whellview).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomBuyLimitFragment.this.onClick(v);
        }
      });

      mLimitCountlist = new ArrayList<>();
        mLimitCountlist.add(getString(R.string.buy_card_no_limit));
        for (int i = 0; i < mMaxLimitCount; i++) {
            mLimitCountlist.add(getString(R.string.card_count_unit, i + 1));
        }
        ArrayWheelAdapter<String> mAdatper = new ArrayWheelAdapter<>(mLimitCountlist, 8);
        mLimitCountWhellview.setAdapter(mAdatper);
        mLimitCountWhellview.TEXT_SIZE = MeasureUtils.sp2px(getContext(), 15f);
        mLimitCountWhellview.setCurrentItem(getArguments().getInt("i", 0));
        return view;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        //        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

 public void onClick(View view) {
        if (view.getId() == R.id.comfirm_btn) {
            RxBus.getBus()
                .post(new EventLimitBuyCount.Builder().buy_count(
                    mLimitCountWhellview.getCurrentItem())
                    .text(mLimitCountlist.get(mLimitCountWhellview.getCurrentItem()))
                    .build());
            dismiss();
        }
    }
}
