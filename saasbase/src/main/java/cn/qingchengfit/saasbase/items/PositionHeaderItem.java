package cn.qingchengfit.saasbase.items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.chat.events.EventChoosePerson;
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

    public String getPostionStr() {
        return postionStr;
    }

    public void setPostionStr(String postionStr) {
        this.postionStr = postionStr;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_position_header;
    }

    @Override public PositionHeaderVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new PositionHeaderVH(view, adapter);
    }

    protected boolean canRemoveSu(Personage personage) {
        return true;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, PositionHeaderVH holder, int position, List payloads) {
        holder.tvPositon.setText(this.postionStr + "(" + childrens.size() + "人)");
        holder.icRight.setImageResource(adapter.isExpanded(position) ? R.drawable.ic_common_arrow_up : R.drawable.ic_common_arrow_down);
        allChecked = true;
        boolean hasChoose = false;
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
	CheckBox checkbox;
	TextView tvPositon;
	ImageView icRight;

        public PositionHeaderVH(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
          checkbox = (CheckBox) view.findViewById(R.id.checkbox);
          tvPositon = (TextView) view.findViewById(R.id.tv_positon);
          icRight = (ImageView) view.findViewById(R.id.ic_right);

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
                                if (canRemoveSu(childrens.get(i).getStaff())) {
                                    DirtySender.studentList.remove(childrens.get(i).getStaff());
                                }
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