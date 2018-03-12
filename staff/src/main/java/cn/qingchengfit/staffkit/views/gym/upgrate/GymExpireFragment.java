package cn.qingchengfit.staffkit.views.gym.upgrate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.PopFromBottomActivity;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.activity.BaseActivity;
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
 * Created by Paper on 2017/2/6.
 */
public class GymExpireFragment extends BaseDialogFragment {

    @BindView(R.id.tag_pro) TextView tagPro;
    @BindView(R.id.content) TextView content;
    @Inject GymWrapper gymWrapper;
    long startTime = 0L;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_expire, container, false);
        unbinder = ButterKnife.bind(this, view);
        String Strcontent = "高级版d将于" + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(gymWrapper.system_end())) + "到期\n请及时续费以免影响使用";
        final SpannableString ss = new SpannableString(Strcontent);
        //得到drawable对象，即所要插入的图片
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.ic_pro_lineout);
        d.setBounds(0, 0, MeasureUtils.dpToPx(30f, getResources()), MeasureUtils.dpToPx(15f, getResources()));
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 3, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        content.setText(ss);

        SensorsUtils.track("QcSaasAlmostExpireWindowOpen").commit(getContext());
        startTime = System.currentTimeMillis()/1000;
        return view;
    }



    @OnClick({ R.id.cancel, R.id.comfirm }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                SensorsUtils.track("QcSaasAlmostExpireWindowCancelBtnClick")
                  .addProperty("qc_stay_time",System.currentTimeMillis()/1000-startTime)
                  .commit(getContext());
                break;
            case R.id.comfirm:

                Intent toRenewal = new Intent(getActivity(), PopFromBottomActivity.class);
                toRenewal.putExtra(BaseActivity.ROUTER, Router.BOTTOM_RENEWAL);
                startActivity(toRenewal);
                SensorsUtils.track("QcSaasAlmostExpireWindowConfirmBtnClick")
                  .addProperty("qc_stay_time",System.currentTimeMillis()/1000-startTime)
                  .commit(getContext());
                break;
        }
        dismiss();
    }

    @Override public void onDestroyView() {
        SensorsUtils.track("QcSaasAlmostExpireWindowClose")
          .addProperty("qc_stay_time",System.currentTimeMillis()/1000-startTime)
          .commit(getContext());
        super.onDestroyView();
    }
}
