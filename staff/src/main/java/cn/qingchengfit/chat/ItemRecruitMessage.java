package cn.qingchengfit.chat;

import android.content.Context;
import com.tencent.qcloud.timchat.chatmodel.Conversation;
import com.tencent.qcloud.timchat.ui.qcchat.ConversationFlexItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * Created by fb on 2017/7/11.
 */

public class ItemRecruitMessage extends ConversationFlexItem {

  private String gymName;

  public ItemRecruitMessage(Context context, Conversation conversation, String gymName) {
    super(context, conversation);
    this.gymName = gymName;
  }

  public ItemRecruitMessage(Context context, Conversation conversation) {
    super(context, conversation);
  }


  @Override public void onChangeName(ConversationViewHolder holder) {
    super.onChangeName(holder);
    holder.tvName.setText(holder.tvName.getText().toString() + " | " + gymName);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, ConversationViewHolder holder, int position,
      List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
  }

}
