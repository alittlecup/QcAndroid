package cn.qingchengfit.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.QcStudentBean;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.event.EventChoosePerson;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.viewholders.ExpandableViewHolder;
import java.util.ArrayList;
import java.util.List;

public class PositionHeaderItem extends AbstractHeaderItem<PositionHeaderItem.PositionHeaderVH>
    implements IExpandable<PositionHeaderItem.PositionHeaderVH, ChooseStaffItem> {

    public boolean allChecked;
    String postionStr;
    private boolean expanded;
    private List<ChooseStaffItem> childrens = new ArrayList<>();

    public PositionHeaderItem(String postionStr) {
        this.postionStr = postionStr;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getPostionStr() {
        return postionStr;
    }

    public void setPostionStr(String postionStr) {
        this.postionStr = postionStr;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_position_header;
    }

    @Override public PositionHeaderVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new PositionHeaderVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, PositionHeaderVH holder, int position, List payloads) {
        holder.tvPositon.setText(this.postionStr + "(" + childrens.size() + "人)");
        holder.icRight.setImageResource(adapter.isExpanded(position) ? R.drawable.ic_common_arrow_up : R.drawable.ic_common_arrow_down);
        allChecked = true;
        boolean hasChoose = false;
        //PositionHeaderItem item = (PositionHeaderItem) adapter.getItem(position);

        for (int i = 0; i < childrens.size(); i++) {
            if (!DirtySender.studentList.contains(childrens.get(i).getStaff())) {
                allChecked = false;
            } else {
                hasChoose = true;
            }
        }
        if (hasChoose) {
            //判断是否全选
            holder.checkbox.setActivated(true);
        } else {
            holder.checkbox.setActivated(false);
        }
        holder.checkbox.setChecked(allChecked);
    }

    @Override public boolean equals(Object o) {
        if (o instanceof PositionHeaderItem) {
            return ((PositionHeaderItem) o).getPostionStr().equals(postionStr);
        } else {
            return false;
        }
    }

    @Override public boolean isExpanded() {
        return expanded;
    }

    @Override public void setExpanded(boolean b) {
        this.expanded = b;
    }

    @Override public int getExpansionLevel() {
        return 0;
    }

    public void addChild(ChooseStaffItem item) {
        childrens.add(item);
    }

    public void clearChildrens() {
        childrens.clear();
    }

    @Override public List<ChooseStaffItem> getSubItems() {
        return childrens;
    }

    public class PositionHeaderVH extends ExpandableViewHolder {
        @BindView(R.id.checkbox) CheckBox checkbox;
        @BindView(R.id.tv_positon) TextView tvPositon;
        @BindView(R.id.ic_right) ImageView icRight;

        public PositionHeaderVH(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (adapter.getItem(getAdapterPosition()) instanceof PositionHeaderItem) {
                        PositionHeaderItem item = (PositionHeaderItem) adapter.getItem(getAdapterPosition());
                        item.allChecked = !item.allChecked;
                        List<ChooseStaffItem> childrens = item.getSubItems();
                        for (int i = 0; i < childrens.size(); i++) {
                            if (item.allChecked) {
                                QcStudentBean qs = new QcStudentBean(childrens.get(i).getStaff());
                                if (!DirtySender.studentList.contains(qs)) DirtySender.studentList.add(qs);
                            } else {
                                DirtySender.studentList.remove(childrens.get(i).getStaff());
                            }
                            try {
                                adapter.notifyDataSetChanged();
                                RxBus.getBus().post(new EventChoosePerson());
                            } catch (Exception e) {
                                //由于设置check造成的 重新变化？
                            }
                        }
                    }
                }
            });
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                }
            });
            //checkbox.setOnClickListener(new View.OnClickListener() {
            //    @Override public void onClick(View view) {
            //        boolean b = checkbox.isChecked();
            //
            //    }
            //});
            tvPositon.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    toggleExpansion();
                    adapter.notifyItemChanged(getAdapterPosition());
                }
            });
            icRight.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    toggleExpansion();
                    adapter.notifyItemChanged(getAdapterPosition());
                }
            });
        }

        @Override protected boolean isViewExpandableOnClick() {
            return false;
        }
    }
}