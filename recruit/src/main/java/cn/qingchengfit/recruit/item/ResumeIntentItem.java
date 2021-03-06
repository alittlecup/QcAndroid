package cn.qingchengfit.recruit.item;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

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
  List<String> cities;
  String salary;
  String[] statusStr;

  public ResumeIntentItem(Context context, List<String> expPos, List<String> cities, String salary, int status) {
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

  @Override public ResumeIntentVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeIntentVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeIntentVH holder, int position, List payloads) {
    holder.tvCurrentStatus.setText(stauts == 0 ? "未填写" : statusStr[stauts % 5 - 1]);
    Drawable d = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.dot_red));
    String color = context.getResources().getStringArray(R.array.resume_status_colors)[Math.abs(stauts) % 5];
    DrawableCompat.setTint(d, Color.parseColor(color));
    holder.tvCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
    holder.tvPosition.setText(CmStringUtils.List2Str(expPos));
    holder.tvCity.setText(CmStringUtils.List2Str(cities));
    holder.tvSalary.setText(salary);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeIntentVH extends FlexibleViewHolder {
	TextView tvPosition;
	TextView tvCity;
	TextView tvSalary;
	LinearLayout layoutJobIntent;
	CompatTextView tvCurrentStatus;

    public ResumeIntentVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvPosition = (TextView) view.findViewById(R.id.tv_position);
      tvCity = (TextView) view.findViewById(R.id.tv_city);
      tvSalary = (TextView) view.findViewById(R.id.tv_salary);
      layoutJobIntent = (LinearLayout) view.findViewById(R.id.layout_job_intent);
      tvCurrentStatus = (CompatTextView) view.findViewById(R.id.tv_current_status);
    }
  }
}