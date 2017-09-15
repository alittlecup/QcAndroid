package com.tencent.qcloud.timchat.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMMessage;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.R2;
import com.tencent.qcloud.timchat.chatmodel.ImageMessage;
import com.tencent.qcloud.timchat.chatmodel.Message;
import com.tencent.qcloud.timchat.common.AppData;
import com.tencent.qcloud.timchat.widget.ChatImageView;
import com.tencent.qcloud.timchat.widget.CustomShapeTransformation;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * Created by fb on 2017/5/22.
 */

public class ChatImageItem extends ChatItem<ChatImageItem.ChatImageVH>
    implements View.OnClickListener {

  private ImageMessage imageMessage;
  private Context context;
  private TIMImageElem e;

  /**
   * Constructor
   *
   * @param context The current context.
   */
  public ChatImageItem(Context context, Message message, String avatar,
      OnDeleteMessageItem onDeleteMessageItem) {
    super(context, message, avatar, onDeleteMessageItem);
    this.imageMessage = (ImageMessage) message;
    this.context = context;
  }

  @Override public ChatImageVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    ChatImageVH holder = new ChatImageVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    return holder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ChatImageVH holder, int position,
      List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
    TIMMessage message = imageMessage.getMessage();
    e = (TIMImageElem) message.getElement(0);
    if (message.isSelf()) {
      Glide.with(context)
          .load(e.getImageList().size() > 0 ? e.getImageList().get(0).getUrl() : e.getPath())
          .asBitmap()
          .override(368, 368)
          .fitCenter()
          .transform(new CustomShapeTransformation(context, R.drawable.chat_bubble_green))
          .into(holder.rightImageMessage);
    } else {
      Glide.with(context)
          .load(e.getImageList().get(0).getUrl())
          .asBitmap()
          .override(400, 400)
          .centerCrop()
          .transform(new CustomShapeTransformation(context, R.drawable.chat_bubble_grey))
          .into(holder.leftImageMessage);
    }
    imageMessage.showStatus(holder);
  }

  public TIMImageElem getElem() {
    return e;
  }

  public ImageMessage getImageMessage() {
    return imageMessage;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_image_message;
  }

  @Override public void onClick(View view) {
    TIMImageElem elem = (TIMImageElem) imageMessage.getMessage().getElement(0);
    if (elem.getImageList() != null && elem.getImageList().size() > 0) {
      //for (TIMImage image : elem.getImageList()){
      imageMessage.navToImageview(elem.getImageList().get(0), context);
      //}
    }
  }

  class ChatImageVH extends ChatItem.ViewHolder {

    @BindView(R2.id.left_image_message) ChatImageView leftImageMessage;
    @BindView(R2.id.right_image_message) ChatImageView rightImageMessage;

    public ChatImageVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
