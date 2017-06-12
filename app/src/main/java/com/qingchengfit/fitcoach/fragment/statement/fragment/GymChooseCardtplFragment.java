package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.views.fragments.BottomListFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.event.EventChooseCardtpl;
import com.qingchengfit.fitcoach.fragment.course.SimpleTextItemItem;
import com.qingchengfit.fitcoach.fragment.statement.item.ChooseCardtplItem;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.QcResponseCardTpls;
import com.qingchengfit.fitcoach.http.ResponseConstant;
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

@FragmentWithArgs public class GymChooseCardtplFragment extends BottomListFragment {
    @Arg String id;
    @Arg String model;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public void loadData(List<AbstractFlexibleItem> ds) {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("id", id);
        prams.put("model", model);
        QcCloudClient.getApi().getApi.qcGetCardTpls(App.coachid + "", id, model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseCardTpls>() {
                @Override public void call(QcResponseCardTpls qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data != null && qcResponse.data.card_tpls != null) {
                            mDatas.clear();
                            mDatas.add(new SimpleTextItemItem("全部会员卡种类"));
                            for (int i = 0; i < qcResponse.data.card_tpls.size(); i++) {
                                mDatas.add(new ChooseCardtplItem(qcResponse.data.card_tpls.get(i)));
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
        return "请选择会员卡种类";
    }

    @Override public boolean onItemClick(int position) {

        if (mDatas.get(position) instanceof ChooseCardtplItem) {
            RxBus.getBus()
                .post(new EventChooseCardtpl.Builder().cardtplId(((ChooseCardtplItem) mDatas.get(position)).mCard_tpl.getId())
                    .cardtplName(((ChooseCardtplItem) mDatas.get(position)).mCard_tpl.getName())
                    .build());
        } else if (mDatas.get(position) instanceof SimpleTextItemItem) {
            RxBus.getBus().post(new EventChooseCardtpl.Builder().cardtplId("").cardtplName("全部会员卡种类").build());
        }
        dismiss();
        return true;
    }
}
