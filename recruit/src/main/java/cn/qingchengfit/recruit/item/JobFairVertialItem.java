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
  public int status = -1;

  public JobFairVertialItem(JobFair jobFair) {
    super(jobFair);
  }

  public JobFairVertialItem(JobFair jobFair, int status) {
    super(jobFair);
    this.status = status;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, JobFairVH holder, int position,
      List payloads) {
    holder.tvTitle.setText(jobFair.name);
    holder.tvContent.setText(DateUtils.getDuringFromServer(jobFair.start, jobFair.end));
    PhotoUtils.conner4dp(holder.imgBg, jobFair.banner);
    if (status > 0) {
      holder.tvStatus.setVisibility(View.VISIBLE);
      switch (status) {
        case 1:
          break;
        case 2:
          holder.tvStatus.setText("审核中");
          holder.tvStatus.setBackgroundResource(R.color.warning_orange);
          break;
        default:
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
