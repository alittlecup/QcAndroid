package com.qingchengfit.fitcoach.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.StringUtils;
import co.hkm.soltag.TagContainerLayout;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.bean.QcResponseShopComment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
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
 * Created by Paper on 16/8/1.
 */
public class ShopCommentItem extends AbstractFlexibleItem<ShopCommentItem.ShopCommentVH> {

    public QcResponseShopComment.CommentShop commentShop;

    public ShopCommentItem(QcResponseShopComment.CommentShop commentShop) {
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
            holder.comments.setTags(BusinessUtils.impress2Str(commentShop.impressions));
        }
        //adapter.animateView(holder.itemView,position,false);
    }

    public static class ShopCommentVH extends FlexibleViewHolder {

	ImageView shopImg;
	TextView coachScore;
	TextView courseScore;
	TextView serverScore;
	TagContainerLayout comments;
	TextView checkDetail;
	TextView shopName;
	TextView noImpression;

        public ShopCommentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          shopImg = (ImageView) view.findViewById(R.id.shop_img);
          coachScore = (TextView) view.findViewById(R.id.coach_score);
          courseScore = (TextView) view.findViewById(R.id.course_score);
          serverScore = (TextView) view.findViewById(R.id.server_score);
          comments = (TagContainerLayout) view.findViewById(R.id.comments);
          checkDetail = (TextView) view.findViewById(R.id.check_detail);
          shopName = (TextView) view.findViewById(R.id.shop_name);
          noImpression = (TextView) view.findViewById(R.id.no_impression);
        }
    }
}
