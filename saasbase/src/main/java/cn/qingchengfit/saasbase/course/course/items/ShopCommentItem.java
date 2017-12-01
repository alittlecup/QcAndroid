package cn.qingchengfit.saasbase.course.course.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.course.bean.CommentShop;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.ListUtils;
import co.hkm.soltag.TagContainerLayout;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ShopCommentItem extends AbstractFlexibleItem<ShopCommentItem.ShopCommentVH> {

    public CommentShop commentShop;

    public ShopCommentItem(CommentShop commentShop) {
        this.commentShop = commentShop;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_shop_comment;
    }

    @Override public ShopCommentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ShopCommentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ShopCommentVH holder, int position, List payloads) {
        holder.serverScore.setText(StringUtils.getFloatDot1(commentShop.service_score));
        holder.courseScore.setText(StringUtils.getFloatDot1(commentShop.course_score));
        holder.coachScore.setText(StringUtils.getFloatDot1(commentShop.teacher_score));
        Glide.with(adapter.getRecyclerView().getContext())
            .load(commentShop.logo)
            .asBitmap()
            .placeholder(R.drawable.ic_default_header)
            .into(new CircleImgWrapper(holder.shopImg, adapter.getRecyclerView().getContext()));
        holder.shopName.setText(commentShop.name);
        if (commentShop.impressions == null || commentShop.impressions.size() == 0) {
            holder.noImpression.setVisibility(View.VISIBLE);
        } else {
            holder.noImpression.setVisibility(View.GONE);
            holder.comments.setTags(ListUtils.ListObj2Str(commentShop.impressions));
        }
    }

    public static class ShopCommentVH extends FlexibleViewHolder {

        @BindView(R2.id.shop_img) ImageView shopImg;
        @BindView(R2.id.coach_score) TextView coachScore;
        @BindView(R2.id.course_score) TextView courseScore;
        @BindView(R2.id.server_score) TextView serverScore;
        @BindView(R2.id.comments) TagContainerLayout comments;
        @BindView(R2.id.check_detail) TextView checkDetail;
        @BindView(R2.id.shop_name) TextView shopName;
        @BindView(R2.id.no_impression) TextView noImpression;

        public ShopCommentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
