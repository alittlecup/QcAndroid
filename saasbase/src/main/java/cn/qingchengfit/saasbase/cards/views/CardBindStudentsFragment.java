package cn.qingchengfit.saasbase.cards.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.saasbase.student.items.StudentNoStatusItem;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.events.EventSelectedStudent;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentParams;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/9/29.
 */
@Leaf(module = "card", path = "/bind/students/")
public class CardBindStudentsFragment extends BaseListFragment implements
        SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.OnItemClickListener {

    Toolbar toolbar;
    TextView toolbarTitle;

    @Need
    public Card card;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaasbaseParamsInjector.inject(this);
        RxBus.getBus().register(EventSelectedStudent.class)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BusSubscribe<EventSelectedStudent>() {
                    @Override
                    public void onNext(EventSelectedStudent eventSelectedStudent) {
                        card.setUsers(eventSelectedStudent.getStudents());
                        initData();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout ll =
                (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, container, false);
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ll.addView(v, 1);
        toolbar = ll.findViewById(R.id.toolbar);
        toolbarTitle = ll.findViewById(R.id.toolbar_title);
        initToolbar(toolbar);
        initData();
        initListener(this);
        return ll;
    }

    private void initData() {
        List<StudentItem> studentItems = new ArrayList<>();
        for (QcStudentBean qcStudentBean : card.getUsers()) {
            studentItems.add(new StudentNoStatusItem(qcStudentBean));
        }
        setDatas(studentItems, 1);
    }


    @Override
    public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitle.setText(R.string.t_card_bind_student);
        toolbar.getMenu().clear();
        toolbar.getMenu().add("修改").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.setOnMenuItemClickListener(item -> {
            routeTo("student", "/choose/student/", new ChooseAndSearchStudentParams()
                    .studentIdList((ArrayList<String>) card.getUserIds())
                    .build());
            return true;
        });
    }

    @Override
    public int getNoDataIconRes() {
        return R.drawable.vd_img_empty_universe;
    }

    @Override
    public String getNoDataStr() {
        return "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        stopRefresh();
    }

    @Override
    public boolean onItemClick(int position) {
        if (position < commonFlexAdapter.getItemCount() && position >= 0) {
            IFlexible item = commonFlexAdapter.getItem(position);
            if (item == null) return true;
            if (item instanceof StudentItem) {
                try {
                    // TODO: 2018/4/16 这里需要会员模块解耦
                    Intent it = new Intent("android.intent.action.VIEW", Uri.parse(AppUtils.getCurAppSchema(getContext())+"://studentdetail"));
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra("qcstudent", ((StudentItem) item).getQcStudentBean());
                    startActivity(it);
                }catch (Exception e){

                }

            }
        }
        return false;
    }
}
