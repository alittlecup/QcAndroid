package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.views.fragments.BottomListFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.event.EventChooseStudent;
import com.qingchengfit.fitcoach.fragment.course.SimpleTextItemItem;
import com.qingchengfit.fitcoach.fragment.statement.item.ChooseStudentItem;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcAllStudentResponse;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.HashMap;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2016/12/9.
 */
@FragmentWithArgs public class GymStudentChooseFragment extends BottomListFragment {
    @Arg String id;
    @Arg String model;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public void loadData(List<AbstractFlexibleItem> ds) {
        HashMap<String, Object> prams = new HashMap<>();
        prams.put("id", id);
        prams.put("model", model);
        QcCloudClient.getApi().getApi.qcGetAllStudent(App.coachid, prams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcAllStudentResponse>() {
                @Override public void call(QcAllStudentResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data != null && qcResponse.data.users != null) {
                            mDatas.clear();
                            mDatas.add(new SimpleTextItemItem("全部学员"));
                            for (int i = 0; i < qcResponse.data.users.size(); i++) {
                                mDatas.add(new ChooseStudentItem(qcResponse.data.users.get(i)));
                            }
                            mFlexibleAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            });
    }

    @Override public String getTitle() {
        return "请选择学员";
    }

    @Override public boolean onItemClick(int position) {

        if (mDatas.get(position) instanceof ChooseStudentItem) {
            RxBus.getBus()
                .post(new EventChooseStudent.Builder().chooseStudentid(((ChooseStudentItem) mDatas.get(position)).mQcStudentBean.user.id)
                    .StudentName(((ChooseStudentItem) mDatas.get(position)).mQcStudentBean.username)
                    .build());
        } else if (mDatas.get(position) instanceof SimpleTextItemItem) {
            RxBus.getBus().post(new EventChooseStudent.Builder().chooseStudentid("0").StudentName("全部学员").build());
        }
        dismiss();
        return true;
    }
}
