package cn.qingchengfit.chat;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.chat.model.ChatGym;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.items.ChooseStaffItem;
import cn.qingchengfit.items.PositionHeaderItem;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.event.EventChoosePerson;
import com.qingchengfit.fitcoach.event.EventFresh;
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
@FragmentWithArgs public class ChatChooseInGymFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.checkbox) CheckBox checkbox;
    @BindView(R.id.img_avatar) ImageView imgAvatar;
    @BindView(R.id.tv_gym_name) TextView tvGymName;
    @BindView(R.id.tv_staff_count) TextView tvStaffCount;
    @BindView(R.id.ic_entry_triangel) ImageView icEntryTriangel;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;

    @Arg ChatGym chatGym;

    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter adapter;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_choose_in_gym, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        recyclerview.addItemDecoration(new DividerItemDecorationChatChooseInGym(getContext(), R.drawable.divier_linear_vectail));
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
                adapter.notifyDataSetChanged();
                checkbox.setChecked(false);
            }
        });
        return view;
    }

    public void initRecycleData() {
        datas.clear();

        if (chatGym.coaches != null && chatGym.coaches.size() > 0) {
            PositionHeaderItem item = new PositionHeaderItem("教练");
            for (int i = 0; i < chatGym.coaches.size(); i++) {
                item.addChild(new ChooseStaffItem(chatGym.coaches.get(i), ChooseStaffItem.UNDER_GYM));
            }
            datas.add(item);
        }

        if (chatGym.staffs != null && chatGym.staffs.size() > 0) {
            String postionStr = chatGym.staffs.get(0).position_str;
            PositionHeaderItem item = new PositionHeaderItem(postionStr);
            for (int i = 0; i < chatGym.staffs.size(); i++) {
                if (!chatGym.staffs.get(i).getPosition_str().equalsIgnoreCase(postionStr)) {
                    datas.add(item);
                    postionStr = chatGym.staffs.get(i).position_str;
                    item = new PositionHeaderItem(postionStr);
                }
                item.addChild(new ChooseStaffItem(chatGym.staffs.get(i), ChooseStaffItem.UNDER_GYM));
            }
            datas.add(item);
        }
        adapter.notifyDataSetChanged();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i) instanceof ChooseStaffItem) {
                ChooseStaffItem item = (ChooseStaffItem) adapter.getItem(i);
                if (DirtySender.studentList.contains(item.getStaff())) {
                    adapter.addSelection(i);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override public String getFragmentName() {
        return ChatChooseInGymFragment.class.getName();
    }

    @Override public boolean onItemClick(int i) {
        if (adapter.getItem(i) instanceof PositionHeaderItem) {
            //PositionHeaderItem positionHeaderItem = (PositionHeaderItem)adapter.getItem(i);
            //if (positionHeaderItem.isExpanded()){
            //    adapter.collapse(i);
            //}else adapter.expand(i);
            adapter.notifyItemChanged(i);
            return false;
        } else if (adapter.getItem(i) instanceof ChooseStaffItem) {
            Personage p = ((ChooseStaffItem) adapter.getItem(i)).getStaff();
            if (DirtySender.studentList.contains(p)) {
                DirtySender.studentList.remove(p);
            } else {
                QcStudentBean studentBean = new QcStudentBean(p);
                if (!DirtySender.studentList.contains(studentBean)) DirtySender.studentList.add(studentBean);
            }
            adapter.notifyItemChanged(i);
            adapter.notifyItemChanged(adapter.getGlobalPositionOf(adapter.getExpandableOf(adapter.getItem(i))));

            RxBus.getBus().post(new EventChoosePerson());
            return true;
        }
        return false;
    }

    @OnClick(R.id.layout_gym) public void onClickLayoutGym() {
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
