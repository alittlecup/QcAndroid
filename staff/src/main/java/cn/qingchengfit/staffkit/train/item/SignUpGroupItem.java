package cn.qingchengfit.staffkit.train.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.train.model.GroupListBean;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/3/22.
 */

//报名表分组的item
public class SignUpGroupItem extends AbstractFlexibleItem<SignUpGroupItem.SignUpGroupVH> {

    private GroupListBean groupListBean;

    public SignUpGroupItem(GroupListBean groupListBean) {
        this.groupListBean = groupListBean;
    }

    public GroupListBean getData() {
        return groupListBean;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SignUpGroupVH holder, int position, List payloads) {
        holder.tvGroupName.setText(groupListBean.name);

        holder.tvGroupMember.setText(groupListBean.username + groupListBean.count + "人");
    }

    @Override public int getLayoutRes() {
        return R.layout.item_sign_up_group;
    }

    @Override public SignUpGroupVH createViewHolder(View view, FlexibleAdapter adapter) {
        SignUpGroupVH vh = new SignUpGroupVH(view, adapter);
        return vh;
    }

    class SignUpGroupVH extends FlexibleViewHolder {

        @BindView(R.id.tv_sign_group_name) TextView tvGroupName;
        @BindView(R.id.tv_sign_group_member) TextView tvGroupMember;

        public SignUpGroupVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
