package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.view.View;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;
import java.util.Locale;
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
 * Created by Paper on 2016/12/20.
 */

public class ExpChooseBrandActivity extends ChooseBrandActivity {
    public void queryData() {
        if (sp != null) sp.unsubscribe();
        String curUser = PreferenceUtils.getPrefString(this, "user_info", "");
        if (curUser != null && !curUser.isEmpty()) {
            User u = new Gson().fromJson(curUser, User.class);

            sp = QcCloudClient.getApi().getApi.qcGetBrands(u.id)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseBrands>() {
                    @Override public void call(final QcResponseBrands qcResponseBrands) {
                        if (qcResponseBrands.status == 200) {
                            datas.clear();
                            datas.addAll(qcResponseBrands.data.brands);
                            if (!getIntent().getBooleanExtra("disable", false)) {
                                Brand add = new Brand("-1");
                                datas.add(add);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setListener(new OnRecycleItemClickListener() {
                                @Override public void onItemClick(View v, int pos) {
                                    if (pos < datas.size() - 1) {
                                        if (datas.get(pos).isHas_add_permission()) {
                                            setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                            ExpChooseBrandActivity.this.finish();
                                            //overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
                                        } else {
                                            ToastUtils.show(String.format(Locale.CHINA, getString(R.string.no_permission_brand),
                                                datas.get(pos).getCreated_by() == null
                                                    || datas.get(pos).getCreated_by().getUsername() == null ? ""
                                                    : datas.get(pos).getCreated_by().getUsername()));
                                        }
                                    } else {
                                        if (Long.parseLong(datas.get(pos).getId()) < 0) {
                                            startActivityForResult(new Intent(ExpChooseBrandActivity.this, AddBrandActivity.class), 1);
                                        }
                                    }
                                }
                            });
                        } else {

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

                    }
                });
        }
    }
}
