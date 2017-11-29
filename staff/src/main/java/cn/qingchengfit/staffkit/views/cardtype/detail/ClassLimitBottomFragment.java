package cn.qingchengfit.staffkit.views.cardtype.detail;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.bigkoo.pickerview.lib.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.NumericWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/16 2016.
 */
public class ClassLimitBottomFragment extends BaseDialogFragment {

    @BindView(R.id.freqent) WheelView freqent;
    @BindView(R.id.times) WheelView times;
    private ArrayWheelAdapter<String> frequentAdatper;

    public static void start(Fragment fragment, int requestCode) {
        ClassLimitBottomFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ClassLimitBottomFragment newInstance() {

        Bundle args = new Bundle();

        ClassLimitBottomFragment fragment = new ClassLimitBottomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_limit, container, false);
        unbinder = ButterKnife.bind(this, view);
        times.TEXT_SIZE = MeasureUtils.sp2px(getContext(), 15f);
        times.setAdapter(new NumericWheelAdapter(0, 100, 1));
        frequentAdatper =
            new ArrayWheelAdapter<String>(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.class_frequent))), 20);
        freqent.TEXT_SIZE = MeasureUtils.sp2px(getContext(), 15f);
        freqent.setAdapter(frequentAdatper);
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

    @OnClick(R.id.comfirm) public void onClick() {
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
            IntentUtils.instanceStringsIntent(freqent.getCurrentItem() + "", times.getCurrentItem() + ""));
        ClassLimitBottomFragment.this.dismiss();
    }

    @Override public void show(FragmentManager manager, String tag) {

        super.show(manager, tag);
    }
}
