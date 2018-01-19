package cn.qingchengfit.views.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
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
 * Created by Paper on 2017/2/7.
 */

public class TrialProDialogFragment extends BaseDialogFragment {

    @BindView(R2.id.tv_title) TextView tvTitle;
    @BindView(R2.id.tv_content_1) TextView tvContent1;
    @BindView(R2.id.tv_content_2) TextView tvContent2;
    @BindView(R2.id.tv_content_3) TextView tvContent3;

    @Inject GymWrapper gymWrapper;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trial_pro, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText(R.string.title_welcome_trial);
        tvContent1.setText(gymWrapper.brand_name() + gymWrapper.name() + "成功升级到高级版");
        tvContent2.setText("有效期至：" + gymWrapper.system_end());
        final SpannableString ss = new SpannableString("快去探索高级版d的强大功能吧!");
        //得到drawable对象，即所要插入的图片
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.ic_pro_lineout);
        d.setBounds(0, 0, MeasureUtils.dpToPx(24f, getResources()), MeasureUtils.dpToPx(12f, getResources()));
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 7, 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvContent3.setText(ss);
        return view;
    }

    @OnClick(R2.id.btn_start) public void onClick() {
        RxBus.getBus().post(new EventFreshCoachService());
        dismiss();
    }
}
