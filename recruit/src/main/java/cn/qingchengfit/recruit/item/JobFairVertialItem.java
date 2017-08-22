package cn.qingchengfit.recruit.item;

import android.view.View;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
 * Created by Paper on 2017/7/5.
 */

public class JobFairVertialItem extends JobFairHorizonItem {


  public JobFairVertialItem(JobFair jobFair) {
    super(jobFair);
  }



  @Override public void bindViewHolder(FlexibleAdapter adapter, JobFairVH holder, int position,
      List payloads) {
    holder.tvTitle.setText(jobFair.name);
    holder.tvContent.setText(DateUtils.getDuringFromServer(jobFair.start, jobFair.end));
    PhotoUtils.origin(holder.imgBg, jobFair.banner);
    if (jobFair.status >= 0) {
      switch (jobFair.status) {
        case 1:
          holder.tvStatus.setVisibility(View.INVISIBLE);
          break;
        case 2:
          holder.tvStatus.setVisibility(View.VISIBLE);
          holder.tvStatus.setText("审核中");
          holder.tvStatus.setBackgroundResource(R.color.warning_orange);
          break;
        default:
          holder.tvStatus.setVisibility(View.VISIBLE);
          holder.tvStatus.setText("审核不通过");
          holder.tvStatus.setBackgroundResource(R.color.recurit_red);
          break;
      }
    } else {
      holder.tvStatus.setVisibility(View.GONE);
    }
  }

  @Override public int getLayoutRes() {
    return R.layout.item_vertical_jobfairs;
  }
}
