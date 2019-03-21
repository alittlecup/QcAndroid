package cn.qingchengfit.gym.gymconfig.views;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import com.anbillon.flabellum.annotations.Leaf;
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
 * Created by Paper on 2018/3/7.
 */
@Leaf(module = "gym",path = "/upgrade/done/")
public class UpgradeDoneFragment extends SaasCommonFragment {

  @Inject GymWrapper gymWrapper;

  TextView tv ;
  TextView title ;
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_gym_pay_success, container, false);
    tv = view.findViewById(R.id.tv_upgrade_done);
    title = view.findViewById(R.id.toolbar_title);
    initToolbar(view.findViewById(R.id.toolbar));
    title.setText("升级成功");
    view.findViewById(R.id.btn_start).setOnClickListener(view1 -> {
      if (getActivity() != null)
        getActivity().finish();
    });
    Drawable d = getResources().getDrawable(R.drawable.ic_pro_lineout);
    Paint paint = new Paint();
    paint.setTextSize(tv.getTextSize());
    int h = paint.getFontMetricsInt().bottom-paint.getFontMetricsInt().top;
    d.setBounds(0, 0, d.getIntrinsicWidth()*h/d.getIntrinsicHeight(), h);
    ImageSpan span1 = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
    ImageSpan span2 = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
    SpannableString spanString = new SpannableString(getString(R.string.str_upgrade_done,gymWrapper.system_end()));
    int c1= spanString.toString().indexOf("[pro1]");
    int c2= spanString.toString().indexOf("[pro2]");
    spanString.setSpan(span1,c1,c1+6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    spanString.setSpan(span2,c2,c2+6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    tv.setText(spanString);
    return view;
  }

  @Override public String getFragmentName() {
    return UpgradeDoneFragment.class.getName();
  }
}
