package cn.qingchengfit.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.event.EventRecycleClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ArticleReplyItem extends AbstractFlexibleItem<ArticleReplyItem.ArticleReplyVH> {

    ArticleComment articleComment;

    public ArticleReplyItem(ArticleComment articleComment) {
        this.articleComment = articleComment;
    }

    public ArticleComment getArticleComment() {
        return articleComment;
    }

    public void setArticleComment(ArticleComment articleComment) {
        this.articleComment = articleComment;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_article_reply;
    }

    @Override public ArticleReplyVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ArticleReplyVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ArticleReplyVH holder, int position, List payloads) {
        if (articleComment.user != null) {
            holder.tvByName.setText(articleComment.user.getUsername());
            Glide.with(holder.itemView.getContext())
                .load(articleComment.user.getAvatar())
                .asBitmap()
                .placeholder(articleComment.user.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
                .into(new CircleImgWrapper(holder.imgAvatar, holder.itemView.getContext()));
        }
        if (articleComment.reply != null && articleComment.reply.user != null) {
            holder.tvAtName.setText(articleComment.reply.user.getUsername());
        }
        if (articleComment.news != null) {
            holder.layoutArticle.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(articleComment.news.thumbnail).into(holder.imgArticle);
            holder.tvArticleTitle.setText(articleComment.news.title);
            holder.tvArticleContent.setText(articleComment.news.sub_title);
        } else {
            holder.layoutArticle.setVisibility(View.GONE);
        }
        holder.tvContent.setText(articleComment.text);
        holder.tvTime.setText(DateUtils.getNotifacationTimeStr(DateUtils.formatDateFromServer(articleComment.created_at)));
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ArticleReplyVH extends FlexibleViewHolder {
        @BindView(R.id.img_avatar) ImageView imgAvatar;
        @BindView(R.id.tv_by_name) TextView tvByName;
        @BindView(R.id.tv_at_name) TextView tvAtName;
        @BindView(R.id.tv_time) TextView tvTime;
        @BindView(R.id.tv_content) TextView tvContent;
        @BindView(R.id.img_article) ImageView imgArticle;
        @BindView(R.id.tv_article_title) TextView tvArticleTitle;
        @BindView(R.id.tv_article_content) TextView tvArticleContent;
        @BindView(R.id.layout_article) RelativeLayout layoutArticle;

        public ArticleReplyVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        @OnClick({ R.id.btn_reply, R.id.layout_article }) public void onReply(View view) {
            RxBus.getBus().post(new EventRecycleClick(getAdapterPosition(), view.getId()));
        }
    }
}