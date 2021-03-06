package com.qingchengfit.fitcoach.items;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.Space;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SpaceItem extends AbstractFlexibleItem<SpaceItem.SpaceVH> {
    public Context mContext;
    public Space mSpace;
    public int courseType;

    public SpaceItem(Context context, Space space, int type) {
        mContext = context;
        mSpace = space;
        this.courseType = type;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_space;
    }

    @Override public SpaceVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new SpaceVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SpaceVH holder, int position, List payloads) {
        holder.text1.setText(mSpace.getName());
        String content = holder.itemView.getContext().getResources().getString(R.string.space_content, mSpace.getCapacity());
        if (mSpace.is_support_team()) content = content.concat(mContext.getString(R.string.course_group));
        if (mSpace.is_support_private()) content = content.concat(mContext.getString(R.string.space_support_private));
        holder.text2.setText(content);
        if (courseType == Configs.TYPE_GROUP) {
            holder.righticon.setImageResource(R.drawable.ic_arrow_right);
        } else {
            holder.righticon.setImageResource(adapter.isSelected(position) ? R.drawable.ic_radio_checked : R.drawable.ic_radio_unchecked);
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class SpaceVH extends FlexibleViewHolder {
	TextView text1;
	TextView text2;
	ImageView righticon;

        public SpaceVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          text1 = (TextView) view.findViewById(R.id.text1);
          text2 = (TextView) view.findViewById(R.id.text2);
          righticon = (ImageView) view.findViewById(R.id.righticon);
        }
    }
}