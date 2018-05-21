package cn.qingchengfit.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ArticleCommentItem extends AbstractFlexibleItem<ArticleCommentItem.ArticleCommentVH> {

    ArticleComment comment;

    public ArticleCommentItem(ArticleComment comment) {
        this.comment = comment;
    }

    public ArticleComment getComment() {
        return comment;
    }

    public void setComment(ArticleComment comment) {
        this.comment = comment;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_comment;
    }

    @Override public ArticleCommentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ArticleCommentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ArticleCommentVH holder, int position, List payloads) {
        try {
            if (comment.user != null) {
                Glide.with(holder.itemView.getContext())
                    .load(PhotoUtils.getSmall(comment.user.getAvatar()))
                    .asBitmap()
                    .placeholder(comment.user.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
                    .into(new CircleImgWrapper(holder.imageCommentHead, holder.itemView.getContext()));
                holder.tvCommentName.setText(comment.user.getUsername());
            }
            holder.tvCommentContent.setText(comment.text);
            holder.tvCommentTime.setText(DateUtils.getNotifacationTimeStr(DateUtils.formatDateFromServer(comment.created_at)));
            if (comment.reply != null && comment.reply.user != null) {
                holder.tvCommentReply.setVisibility(View.VISIBLE);
                holder.tvCommentReply.setText(comment.reply.user.getUsername() + ":" + comment.reply.text);
            } else {
                holder.tvCommentReply.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            CrashUtils.sendCrash(e);
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ArticleCommentVH extends FlexibleViewHolder {
	ImageView imageCommentHead;
	TextView tvCommentName;
	TextView tvCommentContent;
	TextView tvCommentTime;
	TextView tvCommentReply;

        public ArticleCommentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          imageCommentHead = (ImageView) view.findViewById(R.id.image_comment_head);
          tvCommentName = (TextView) view.findViewById(R.id.tv_comment_name);
          tvCommentContent = (TextView) view.findViewById(R.id.tv_comment_content);
          tvCommentTime = (TextView) view.findViewById(R.id.tv_comment_time);
          tvCommentReply = (TextView) view.findViewById(R.id.tv_comment_reply);
        }
    }
}