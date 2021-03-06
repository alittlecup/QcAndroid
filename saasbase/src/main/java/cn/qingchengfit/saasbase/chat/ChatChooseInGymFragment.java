package cn.qingchengfit.saasbase.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.chat.events.EventChoosePerson;
import cn.qingchengfit.saasbase.chat.events.EventFresh;
import cn.qingchengfit.saasbase.chat.model.ChatGym;
import cn.qingchengfit.saasbase.items.ChooseStaffItem;
import cn.qingchengfit.saasbase.items.PositionHeaderItem;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

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
 * Created by Paper on 2017/3/31.
 */
public class ChatChooseInGymFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener {

	protected CheckBox checkbox;
  protected CommonFlexAdapter adapter;
	ImageView imgAvatar;
	TextView tvGymName;
	TextView tvStaffCount;
	ImageView icEntryTriangel;
	RecyclerView recyclerview;
    ChatGym chatGym;
    private List<AbstractFlexibleItem> datas = new ArrayList<>();

  public static ChatChooseInGymFragment newInstance(ChatGym chatGym) {
    Bundle args = new Bundle();
    args.putParcelable("chatgym", chatGym);
    ChatChooseInGymFragment fragment = new ChatChooseInGymFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    if (getArguments() != null) chatGym = getArguments().getParcelable("chatgym");
    }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_choose_in_gym, container, false);
    checkbox = (CheckBox) view.findViewById(R.id.checkbox);
    imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
    tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
    tvStaffCount = (TextView) view.findViewById(R.id.tv_staff_count);
    icEntryTriangel = (ImageView) view.findViewById(R.id.ic_entry_triangel);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    view.findViewById(R.id.layout_gym).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickLayoutGym();
      }
    });

    icEntryTriangel.setVisibility(View.GONE);
        Glide.with(getContext()).load(chatGym.photo).asBitmap().into(new CircleImgWrapper(imgAvatar, getContext()));
        tvGymName.setText(chatGym.name + " | " + chatGym.brand_name);
        checkbox.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        adapter = new CommonFlexAdapter(datas, this);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new QcLeftRightDivider(getContext(), 0, R.layout.item_choose_staff, 166, 0));
        recyclerview.addItemDecoration(new QcLeftRightDivider(getContext(), 0, R.layout.item_position_header, 0, 0));
        recyclerview.setAdapter(adapter);
        recyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(recyclerview.getViewTreeObserver(), this);
                initRecycleData();
            }
        });
        //清空数据
        RxBusAdd(EventFresh.class).subscribe(new Action1<EventFresh>() {
            @Override public void call(EventFresh eventFresh) {
                checkbox.setChecked(false);
            }
        });
        if (getParentFragment() instanceof ConversationFriendsFragment) {
            ((ConversationFriendsFragment) getParentFragment()).setLeft(R.string.button_back);
        }
        return view;
    }

    @Override public void onDestroyView() {
        if (getParentFragment() instanceof ConversationFriendsFragment) {
            ((ConversationFriendsFragment) getParentFragment()).setLeft(R.string.cancel);
        }
        super.onDestroyView();
    }

    public void initRecycleData() {
        datas.clear();

        if (chatGym.coaches != null && chatGym.coaches.size() > 0) {
          PositionHeaderItem item = newPositionHeader("教练");
            for (int i = 0; i < chatGym.coaches.size(); i++) {
                item.addChild(new ChooseStaffItem(chatGym.coaches.get(i), ChooseStaffItem.UNDER_GYM));
            }
            datas.add(item);
        }

        if (chatGym.staffs != null && chatGym.staffs.size() > 0) {
            String postionStr = chatGym.staffs.get(0).position_str;
          PositionHeaderItem item = newPositionHeader(postionStr);
            for (int i = 0; i < chatGym.staffs.size(); i++) {
                if (!chatGym.staffs.get(i).getPosition_str().equalsIgnoreCase(postionStr)) {
                    datas.add(item);
                    postionStr = chatGym.staffs.get(i).position_str;
                  item = newPositionHeader(postionStr);
                }
                item.addChild(new ChooseStaffItem(chatGym.staffs.get(i), ChooseStaffItem.UNDER_GYM));
            }
            datas.add(item);
        }
        adapter.updateDataSet(datas);
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i) instanceof ChooseStaffItem) {
                ChooseStaffItem item = (ChooseStaffItem) adapter.getItem(i);
                if (DirtySender.studentList.contains(item.getStaff())) {
                    adapter.addSelection(i);
                }
            }
        }
        adapter.updateDataSet(datas);
    }

    @Override public String getFragmentName() {
        return ChatChooseInGymFragment.class.getName();
    }

    @Override public boolean onItemClick(int i) {
        if (adapter.getItem(i) instanceof PositionHeaderItem) {
            adapter.notifyItemChanged(i);
            return false;
        } else if (adapter.getItem(i) instanceof ChooseStaffItem) {
            Personage p = ((ChooseStaffItem) adapter.getItem(i)).getStaff();
            if (DirtySender.studentList.contains(p)) {
                DirtySender.studentList.remove(p);
            } else {
                QcStudentBean studentBean = new QcStudentBean(p);
                DirtySender.studentList.add(studentBean);
            }
            adapter.notifyItemChanged(i);
            adapter.notifyItemChanged(adapter.getGlobalPositionOf(adapter.getExpandableOf(adapter.getItem(i))));

            RxBus.getBus().post(new EventChoosePerson());
            return true;
        }
        return false;
    }

  protected PositionHeaderItem newPositionHeader(String s) {
    return new PositionHeaderItem(s);
    }

 public void onClickLayoutGym() {
        checkbox.toggle();
        if (checkbox.isChecked()) {
            DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), chatGym.coaches));
            DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), chatGym.staffs));

            adapter.notifyDataSetChanged();
        } else {
            DirtySender.studentList.removeAll(chatGym.coaches);
            DirtySender.studentList.removeAll(chatGym.staffs);

            adapter.notifyDataSetChanged();
        }
        RxBus.getBus().post(new EventChoosePerson());
    }
}
