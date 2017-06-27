package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.views.fragments.BottomListFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.event.EventChooseCourse;
import com.qingchengfit.fitcoach.fragment.course.SimpleTextItemItem;
import com.qingchengfit.fitcoach.fragment.statement.item.ChooseCourseItem;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseList;
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

@FragmentWithArgs public class GymChooseCourseFragment extends BottomListFragment {
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
        QcCloudClient.getApi().getApi.qcGetCoursesAll(App.coachid + "", prams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseCourseList>() {
                @Override public void call(QcResponseCourseList qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data != null && qcResponse.data.courses != null) {
                            mDatas.clear();
                            mDatas.add(new SimpleTextItemItem("全部课程"));
                            for (int i = 0; i < qcResponse.data.courses.size(); i++) {
                                mDatas.add(new ChooseCourseItem(qcResponse.data.courses.get(i)));
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
        return "请选择课程";
    }

    @Override public boolean onItemClick(int position) {

        if (mDatas.get(position) instanceof ChooseCourseItem) {
            RxBus.getBus()
                .post(new EventChooseCourse.Builder().courseId(((ChooseCourseItem) mDatas.get(position)).mCourse.getId())
                    .courseName(((ChooseCourseItem) mDatas.get(position)).mCourse.getName())
                    .build());
        } else if (mDatas.get(position) instanceof SimpleTextItemItem) {
            RxBus.getBus().post(new EventChooseCourse.Builder().courseId("").courseName("全部课程").build());
        }
        dismiss();
        return true;
    }
}
