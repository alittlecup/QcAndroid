package cn.qingchengfit.items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.recruit.model.ChatGym;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChatGymItem extends AbstractFlexibleItem<ChatGymItem.ChatGymVH> {

    ChatGym chatGym;
    boolean canChoose = false;

    public ChatGymItem(ChatGym chatGym) {
        this.chatGym = chatGym;
        this.canChoose = false;
    }
    public ChatGymItem(ChatGym chatGym, boolean canChoose) {
        this.chatGym = chatGym;
        this.canChoose = canChoose;
    }

    public ChatGym getChatGym() {
        return chatGym;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_chat_gym;
    }

    @Override public ChatGymVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChatGymVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChatGymVH holder, int position, List payloads) {
        holder.checkbox.setVisibility(canChoose ? View.VISIBLE : View.GONE);
        holder.checkbox.setChecked(adapter.isSelected(position));
        holder.tvGymName.setText(chatGym.name);
        int cont = (chatGym.coaches == null ? 0 : chatGym.coaches.size()) + (chatGym.staffs == null ? 0 : chatGym.staffs.size());
        holder.tvStaffCount.setText(cont + "人");
        Glide.with(holder.itemView.getContext())
            .load(chatGym.photo)
            .asBitmap()
            .into(new CircleImgWrapper(holder.imgAvatar, holder.imgAvatar.getContext()));
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChatGymVH extends FlexibleViewHolder {

	CheckBox checkbox;
	ImageView imgAvatar;
	TextView tvGymName;
	TextView tvStaffCount;

        public ChatGymVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          checkbox = (CheckBox) view.findViewById(R.id.checkbox);
          imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
          tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
          tvStaffCount = (TextView) view.findViewById(R.id.tv_staff_count);
        }
    }
}