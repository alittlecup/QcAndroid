package cn.qingchengfit.chat;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.chat.model.ChatGym;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.DirtySender;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventChoosePerson;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.AlphabetHeaderItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ChatGymItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ChooseStaffItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ChooseStudentItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.CommonNoDataItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.QcPersonalComparator;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.widgets.AlphabetLessView;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 2017/3/30.
 */
public class ChatFriendAllChooseFragment extends BaseFragment implements ChatFriendPresenter.MVPView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.search_clear) ImageView searchClear;
    @BindView(R.id.layout_alphabet) AlphabetLessView layoutAlphabet;

    @Inject ChatFriendPresenter presenter;

    private CommonFlexAdapter adapter;
    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private List<Staff> originData = new ArrayList<>();
    private List<ChatGym> originGyms = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatfriend_all_choose, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);

        etSearch.setHint(R.string.search_hint);
        etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_24dp_grey), null,
            null, null);
        searchClear.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                etSearch.setText("");
            }
        });
        adapter = new CommonFlexAdapter(items, this);
        recyclerview.setNestedScrollingEnabled(false);
        linearLayoutManager = new SmoothScrollLinearLayoutManager(getContext());
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.addItemDecoration(new QcLeftRightDivider(getContext(), 0, R.layout.item_choose_staff, 116, 0));
        recyclerview.addItemDecoration(new QcLeftRightDivider(getContext(), 0, R.layout.item_chat_gym, 80, 0));

        recyclerview.setAdapter(adapter);
        layoutAlphabet.setOnAlphabetChange(new AlphabetLessView.OnAlphabetChange() {
            @Override public void onChange(int i, String s, int i1) {
                try {
                    linearLayoutManager.scrollToPosition(items.indexOf(adapter.getHeaderItems().get(i)));
                } catch (Exception e) {

                }
            }
        });
        presenter.queryChatFriend();
        showLoadingTrans();
        RxTextView.textChangeEvents(etSearch).subscribe(new Action1<TextViewTextChangeEvent>() {
            @Override public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                if (textViewTextChangeEvent.text().length() > 0) {
                    searchClear.setVisibility(View.VISIBLE);
                    localFilter(textViewTextChangeEvent.text().toString());
                } else {
                    searchClear.setVisibility(View.GONE);
                    presenter.queryChatFriend();
                }
            }
        });
        RxBusAdd(EventFresh.class).subscribe(new Action1<EventFresh>() {
            @Override public void call(EventFresh eventFresh) {
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return ChatFriendAllChooseFragment.class.getName();
    }

    @Override public void onShowError(String e) {
        super.onShowError(e);
    }

    @Override public void onShowError(@StringRes int e) {
        super.onShowError(e);
    }

    @Override public void onGymList(List<ChatGym> gyms) {
        if (gyms != null) {
            originGyms.clear();
            originGyms.addAll(gyms);
        }
    }

    @Override public void onUserList(List<Staff> staffs) {
        hideLoadingTrans();
        if (staffs != null) {
            originData.clear();
            originData.addAll(staffs);
        }
        initData();
    }

    @Override public boolean onItemClick(int i) {
        if (adapter.getItem(i) instanceof ChatGymItem) {
            getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_fade_out, R.anim.slide_fade_in, R.anim.slide_right_out)
                .replace(R.id.chat_friend_frag, new ChatChooseInGymFragmentBuilder(((ChatGymItem) adapter.getItem(i)).getChatGym()).build())
                .addToBackStack(null)
                .commit();
        } else if (adapter.getItem(i) instanceof ChooseStaffItem) {
            adapter.toggleSelection(i);
            adapter.notifyItemChanged(i);
            Personage p = ((ChooseStaffItem) adapter.getItem(i)).getStaff();
            if (DirtySender.studentList.contains(p)) {
                DirtySender.studentList.remove(p);
            } else {
                QcStudentBean studentBean = new QcStudentBean(p);
                DirtySender.studentList.add(studentBean);
            }
            RxBus.getBus().post(new EventChoosePerson());
        }
        return false;
    }

    public void localFilter(String s) {
        if (TextUtils.isEmpty(s)) {
            adapter.setSearchText(null);
            initData();
        } else {
            adapter.setSearchText(s);
            adapter.filterItems(items, 100);
            adapter.hideAllHeaders();
        }
    }

    public void initData() {
        items.clear();
        layoutAlphabet.clearElement();
        for (int i = 0; i < originGyms.size(); i++) {
            items.add(new ChatGymItem(originGyms.get(i)));
        }
        layoutAlphabet.setVisibility(View.VISIBLE);
        for (int i = 0; i < originData.size(); i++) {
            if (StringUtils.isEmpty(originData.get(i).getHead()) || !"abcdefghijklmnopqrstuvwxyz".contains(originData.get(i).getHead())) {
                originData.get(i).setHead("~");
            }
        }
        Collections.sort(originData, new QcPersonalComparator());
        String header = originData.get(0).getHead();
        AlphabetHeaderItem itemA = new AlphabetHeaderItem(TextUtils.equals(header, "~") ? "#" : header.toUpperCase());
        layoutAlphabet.addElement(header, 0);
        for (int i = 0; i < originData.size(); i++) {
            Staff a = originData.get(i);
            if (!TextUtils.equals(a.getHead(), header)) {
                header = a.getHead();
                itemA = new AlphabetHeaderItem(TextUtils.equals(header, "~") ? "#" : header.toUpperCase());
                layoutAlphabet.addElement(header, i);
            }
            items.add(new ChooseStaffItem(a, itemA));
        }
        layoutAlphabet.init();

        adapter.updateDataSet(items);

        adapter.notifyDataSetChanged();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i) instanceof ChooseStudentItem) {
                if (DirtySender.studentList.contains(((ChooseStudentItem) adapter.getItem(i)).getUser())) adapter.addSelection(i);
            }
        }
        adapter.showAllHeaders();
        if (adapter.getItemCount() == 0) {
            adapter.addItem(new CommonNoDataItem(0, "请在您的健身房里添加工作人员", "暂无联系人"));
        }
        adapter.notifyDataSetChanged();
    }
}
