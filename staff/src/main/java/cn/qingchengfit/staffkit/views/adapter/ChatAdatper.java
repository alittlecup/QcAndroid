package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.FollowRecord;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/4/29 2016.
 */
public class ChatAdatper extends RecyclerView.Adapter<ChatAdatper.ChatViewHolder> implements View.OnClickListener, View.OnTouchListener {
    List<FollowRecord> datas;
    private OnRecycleItemClickListener listener;
    private OnRecycleItemClickListener onTouchListener;

    public ChatAdatper(List<FollowRecord> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public OnRecycleItemClickListener getOnTouchListener() {
        return onTouchListener;
    }

    public void setOnTouchListener(OnRecycleItemClickListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatViewHolder holder = new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnTouchListener(this);
        return holder;
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.itemView.setTag(position);
        FollowRecord followRecord = datas.get(position);
        holder.time.setText(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(followRecord.created_at)));
        holder.name.setText(followRecord.created_by.getUsername());
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(followRecord.created_by.getAvatar()))
            .asBitmap()
            .placeholder(R.drawable.ic_default_head_nogender)
            .into(new CircleImgWrapper(holder.avatar, holder.itemView.getContext()));
        if (followRecord.type.equalsIgnoreCase("Record")) {
            holder.chatImg.setVisibility(View.GONE);
            holder.chatText.setVisibility(View.VISIBLE);
            holder.chatText.setText(followRecord.content);
        } else if (followRecord.type.equalsIgnoreCase("attachment")) {
            holder.chatImg.setVisibility(View.VISIBLE);
            holder.chatText.setVisibility(View.GONE);
            Glide.with(holder.itemView.getContext()).load(followRecord.thumbnail).into(holder.chatImg);
        }
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    @Override public boolean onTouch(View v, MotionEvent event) {
        if (onTouchListener != null) {
            onTouchListener.onItemClick(v, 0);
        }
        return false;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.chat_text) TextView chatText;
        @BindView(R.id.chat_img) ImageView chatImg;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
