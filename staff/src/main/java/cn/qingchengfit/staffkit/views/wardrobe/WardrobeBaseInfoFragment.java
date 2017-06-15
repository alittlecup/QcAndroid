package cn.qingchengfit.staffkit.views.wardrobe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;

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
 * Created by Paper on 16/9/6.
 */
public class WardrobeBaseInfoFragment extends BaseFragment {

    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.tv_time_limit) TextView tvTimeLimit;
    @BindView(R.id.tv_student) TextView tvStudent;
    @BindView(R.id.tv_remind_day) TextView tvRemindDay;

    private Locker mLocker;

    public static WardrobeBaseInfoFragment newInstance(Locker l) {

        Bundle args = new Bundle();
        args.putParcelable("l", l);
        WardrobeBaseInfoFragment fragment = new WardrobeBaseInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocker = getArguments().getParcelable("l");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_base, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvStatus.setText(getString(R.string.long_term_hire));
        tvStudent.setText(getString(R.string.user_phone, mLocker.user.getUsername(), mLocker.user.getPhone()));
        tvTimeLimit.setText(
            getString(R.string.start_to_end, DateUtils.getYYYYMMDDfromServer(mLocker.start), DateUtils.getYYYYMMDDfromServer(mLocker.end)));
        SpannableString s =
            new SpannableString(getString(R.string.some_day, DateUtils.dayNumFromToday(DateUtils.formatDateFromServer(mLocker.end))));
        ForegroundColorSpan span = new ForegroundColorSpan(CompatUtils.getColor(getContext(), R.color.text_black));
        s.setSpan(span, s.length() - 1, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRemindDay.setText(s);

        return view;
    }

    @Override public String getFragmentName() {
        return WardrobeBaseInfoFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
