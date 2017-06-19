package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TwoScrollPicker;
import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Inject;

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
 * Created by Paper on 2017/6/13.
 */
public class ResumeIntentsFragment extends BaseFragment {
    public static final int MIN_SALARY = 1;
    public static final int MAX_SALARY = 99;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R2.id.civ_intent_status) CommonInputView civIntentStatus;
    @BindView(R2.id.civ_intent_postion) CommonInputView civIntentPostion;
    @BindView(R2.id.civ_intent_city) CommonInputView civIntentCity;
    @BindView(R2.id.civ_intent_salay) CommonInputView civIntentSalay;
    Unbinder unbinder;
    @Inject RecruitRouter router;
    private SimpleScrollPicker simpleScrollPicker;
    private TwoScrollPicker twoScrollPicker;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleScrollPicker = new SimpleScrollPicker(getContext());
        twoScrollPicker = new TwoScrollPicker(getContext());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume_intents, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public String getFragmentName() {
        return ResumeIntentsFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 当前状态
     */
    @OnClick(R2.id.civ_intent_status) public void onCivIntentStatusClicked() {
        final ArrayList<String> d = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.resume_self_status)));
        simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
            @Override public void onSelectItem(int pos) {
                civIntentStatus.setContent(d.get(pos));
            }
        });
        simpleScrollPicker.show(d, 0);
    }

    /**
     * 期望职位
     */
    @OnClick(R2.id.civ_intent_postion) public void onCivIntentPostionClicked() {
        router.toIntentPosition();
    }

    /**
     * 期望城市
     */
    @OnClick(R2.id.civ_intent_city) public void onCivIntentCityClicked() {
        router.toIntentCities();
    }

    /**
     * 期望薪水
     */
    @OnClick(R2.id.civ_intent_salay) public void onCivIntentSalayClicked() {
        twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
            @Override public void onSelectItem(int left, int right) {
                civIntentSalay.setContent((left - MIN_SALARY) + "K-" + (right - MIN_SALARY) + "K");
            }
        });
        twoScrollPicker.show(MIN_SALARY, MAX_SALARY, MIN_SALARY, MAX_SALARY, 1, 0, 0);
    }
}
