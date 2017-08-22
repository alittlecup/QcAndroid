package cn.qingchengfit.staffkit.views.batch.looplist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CourseManageBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/1/6 2016.
 */
public class CourseManagerAdapter extends RecyclerView.Adapter<CourseManagerAdapter.CourseManagerVH> implements View.OnClickListener {

    private List<CourseManageBean> datas;
    private OnRecycleItemClickListener clickTimeListener;
    private boolean isEditing = false;

    public CourseManagerAdapter(List<CourseManageBean> datas) {
        this.datas = datas;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public void setClickTimeListener(OnRecycleItemClickListener clickTimeListener) {
        this.clickTimeListener = clickTimeListener;
    }

    @Override public CourseManagerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        CourseManagerVH vh =
            new CourseManagerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_manage, parent, false));
        vh.itemView.setOnClickListener(this);
        //        vh.time.setOnClickListener(this);
        vh.view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return vh;
    }

    @Override public void onBindViewHolder(CourseManagerVH holder, int position) {
        holder.itemView.setTag(position);
        holder.time.setTag(position);
        CourseManageBean bean = datas.get(position);

        holder.date.setText(bean.day);
        holder.weekday.setText(holder.itemView.getContext().getResources().getStringArray(R.array.weeks)[bean.WeekDay]);
        holder.time.setText(bean.time);
        if (bean.outdue) {
            holder.outofdate.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.itemCheckbox.setVisibility(View.INVISIBLE);
            holder.rightIcon.setVisibility(View.GONE);
        } else {
            holder.outofdate.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
            holder.itemCheckbox.setVisibility(View.VISIBLE);
            holder.itemCheckbox.setChecked(bean.checked);
            holder.rightIcon.setVisibility(View.VISIBLE);
        }
        //是否显示titile
        if (position > 0) {
            if (!bean.month.equalsIgnoreCase(datas.get(position - 1).month)) {
                holder.month.setVisibility(View.VISIBLE);
                holder.month.setText(bean.month + "排期");
            } else {
                holder.month.setVisibility(View.GONE);
            }
        } else {
            holder.month.setVisibility(View.VISIBLE);
            holder.month.setText(bean.month + "排期");
        }

        holder.itemCheckbox.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }

    public String getChooseIds() {
        if (datas != null && datas.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (CourseManageBean b : datas) {
                if (b.checked) ids.add(b.id);
            }
            return StringUtils.List2Str(ids);
        } else {
            return "";
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (clickTimeListener != null) clickTimeListener.onItemClick(v, (int) v.getTag());
    }

    public static class CourseManagerVH extends RecyclerView.ViewHolder {
        @BindView(R.id.month) TextView month;
        @BindView(R.id.item_checkbox) CheckBox itemCheckbox;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.weekday) TextView weekday;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.outofdate) TextView outofdate;
        @BindView(R.id.outofdatelayout) View view;
        @BindView(R.id.right_icon) ImageView rightIcon;

        public CourseManagerVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
