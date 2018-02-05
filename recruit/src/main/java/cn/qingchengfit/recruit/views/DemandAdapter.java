package cn.qingchengfit.recruit.views;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.recruit.R;
import java.util.ArrayList;
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
 * Created by Paper on 2017/6/1.
 */

public class DemandAdapter extends RecyclerView.Adapter<DemandAdapter.DemandVH> {

  Context context;
  List<Pair<Integer, String>> datas = new ArrayList<>();

  public DemandAdapter(Context context, List<Pair<Integer, String>> datas) {
    this.context = context;
    this.datas = datas;
  }

  @Override public DemandVH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new DemandVH(LayoutInflater.from(context).inflate(R.layout.item_recruit_demand, parent, false));
  }

  @Override public void onBindViewHolder(DemandVH holder, int position) {
    holder.imgRecruitDemand.setImageResource(datas.get(position).first);
    holder.tvContent.setText(datas.get(position).second);
  }

  @Override public int getItemCount() {
    return datas.size();
  }

  public class DemandVH extends RecyclerView.ViewHolder {
    ImageView imgRecruitDemand;
    TextView tvContent;

    public DemandVH(View itemView) {
      super(itemView);
      imgRecruitDemand = itemView.findViewById(R.id.img_recruit_demand);
      tvContent = itemView.findViewById(R.id.tv_content);
    }
  }
}
