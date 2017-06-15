package cn.qingchengfit.recruit.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.CmStringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeIntentItem extends AbstractFlexibleItem<ResumeIntentItem.ResumeIntentVH> {

    Context context;

    int stauts;
    List<String> expPos;
    List<CityBean> cities;
    String salary;
    String[] statusStr;

    public ResumeIntentItem(Context context, List<String> expPos, List<CityBean> cities, String salary, int status) {
        this.context = context;
        this.expPos = expPos;
        this.cities = cities;
        this.salary = salary;
        this.stauts = status;
        statusStr = context.getResources().getStringArray(R.array.resume_self_status);
    }

    @Override public int getLayoutRes() {
        return R.layout.item_resume_work_intent;
    }

    @Override public ResumeIntentVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeIntentVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeIntentVH holder, int position, List payloads) {
        String statu = Math.abs(stauts) < 5 ? statusStr[Math.abs(stauts)] : "";
        holder.tvCurrentStatus.setText(statu);
        holder.tvCity.setText(CmStringUtils.List2Str(expPos));
        // TODO: 2017/6/13 城市
        holder.tvSalary.setText(salary);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeIntentVH extends FlexibleViewHolder {
        @BindView(R2.id.tv_position) TextView tvPosition;
        @BindView(R2.id.tv_city) TextView tvCity;
        @BindView(R2.id.tv_salary) TextView tvSalary;
        @BindView(R2.id.layout_job_intent) LinearLayout layoutJobIntent;
        @BindView(R2.id.tv_current_status) CompatTextView tvCurrentStatus;
        public ResumeIntentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}