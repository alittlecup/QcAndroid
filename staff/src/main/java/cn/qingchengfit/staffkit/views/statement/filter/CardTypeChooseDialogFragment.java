package cn.qingchengfit.staffkit.views.statement.filter;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.CardTypeEvent;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.utils.MeasureUtils;
import com.bigkoo.pickerview.lib.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.OnWheelChangedListener;
import com.bigkoo.pickerview.lib.WheelView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Created by Paper on 16/7/1 2016.
 */
public class CardTypeChooseDialogFragment extends BaseDialogFragment {

    @BindView(R.id.comfirm) TextView comfirm;
    @BindView(R.id.course_type) WheelView courseType;
    @BindView(R.id.course_list) WheelView courseList;
    @BindView(R.id.wheellayout) LinearLayout wheellayout;

    List<CardTpl> mCard_tpls = new ArrayList<>();
    List<CardTpl> mOrigin = new ArrayList<>();

    public static CardTypeChooseDialogFragment newInstance(List<CardTpl> card_tpls) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("datas", (ArrayList<CardTpl>) card_tpls);
        CardTypeChooseDialogFragment fragment = new CardTypeChooseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);
        if (getArguments() != null) mOrigin = getArguments().getParcelableArrayList("datas");
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cardtype, container, false);
        unbinder = ButterKnife.bind(this, view);
        ArrayWheelAdapter<String> courseTypeAdatper =
            new ArrayWheelAdapter<String>(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.cardtype_filter))), 8);
        courseType.setAdapter(courseTypeAdatper);
        courseType.TEXT_SIZE = MeasureUtils.sp2px(getContext(), 16f);
        courseType.addChangingListener(new OnWheelChangedListener() {
            @Override public void onChanged(WheelView wheel, int oldValue, int newValue) {
                setSecond(newValue);
            }
        });
        setSecond(0);
        return view;
    }

    public void setSecond(int type) {
        wheellayout.removeViewAt(1);
        mCard_tpls.clear();
        String all = "";
        for (int i = 0; i < mOrigin.size(); i++) {
            CardTpl card_tpl = mOrigin.get(i);
            switch (type) {
                case 0:
                    mCard_tpls.add(card_tpl);
                    all = getString(R.string.cardtype_all_all);
                    break;
                case 1:
                    all = getString(R.string.all_cardtype_value);
                    if (card_tpl.getType() == Configs.CATEGORY_VALUE) {
                        mCard_tpls.add(card_tpl);
                    }
                    break;
                case 2:
                    all = getString(R.string.all_cardtype_times);
                    if (card_tpl.getType() == Configs.CATEGORY_TIMES) {
                        mCard_tpls.add(card_tpl);
                    }
                    break;
                case 3:
                    all = getString(R.string.all_cardtype_date);
                    if (card_tpl.getType() == Configs.CATEGORY_DATE) {
                        mCard_tpls.add(card_tpl);
                    }
                    break;
                default:
                    all = getString(R.string.cardtype_all_all);
                    break;
            }
        }

        WheelView wheelView = new WheelView(getContext());

        ArrayList<String> d = new ArrayList<String>();
        d.add(all);
        for (int i = 0; i < mCard_tpls.size(); i++) {
            CardTpl card_tpl = mCard_tpls.get(i);
            d.add(card_tpl.getName());
        }
        ArrayWheelAdapter<String> courseTypeAdatper = new ArrayWheelAdapter<String>(d, 16);
        wheelView.TEXT_SIZE = MeasureUtils.sp2px(getContext(), 15f);
        wheelView.setAdapter(courseTypeAdatper);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        wheellayout.addView(wheelView, params);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = MeasureUtils.dpToPx(245f, getResources());
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        return dialog;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.comfirm) public void onClick() {
        int pos1 = courseType.getCurrentItem();
        int pos2 = ((WheelView) wheellayout.getChildAt(1)).getCurrentItem();
        if (pos2 == 0) {
            RxBus.getBus().post(new CardTypeEvent(pos1));
        } else {
            RxBus.getBus().post(mCard_tpls.get(pos2 - 1));
        }
        dismiss();
    }
}
