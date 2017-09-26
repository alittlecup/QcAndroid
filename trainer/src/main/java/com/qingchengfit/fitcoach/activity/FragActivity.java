package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.AddBelongGymFragment;
import com.qingchengfit.fitcoach.fragment.AddSelfGymFragment;
import com.qingchengfit.fitcoach.fragment.AddSelfNotiFragment;
import com.qingchengfit.fitcoach.fragment.AddStudentManulkFragment;
import com.qingchengfit.fitcoach.fragment.GymDetailFragment;
import com.qingchengfit.fitcoach.fragment.GymDetailNativeFragment;
import com.qingchengfit.fitcoach.fragment.MyCoursePlanFragment;
import com.qingchengfit.fitcoach.fragment.MyStudentFragment;
import com.qingchengfit.fitcoach.fragment.SaleGlanceFragment;
import com.qingchengfit.fitcoach.fragment.StatementGlanceFragment;
import com.qingchengfit.fitcoach.fragment.SyncGymFragment;
import com.qingchengfit.fitcoach.fragment.batch.SetAccountFragment;
import com.qingchengfit.fitcoach.fragment.batch.space.SpaceListFragment;
import com.qingchengfit.fitcoach.fragment.course.plan.CoursePlanHomeFragment;
import com.qingchengfit.fitcoach.fragment.manage.EditGymFragment;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/10/12 2015.
 */
public class FragActivity extends BaseActivity {

    Fragment fragment;
    CoachService mCoachService;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_base_frag);
        int type = getIntent().getIntExtra("type", 0);
        mCoachService = getIntent().getParcelableExtra("service");

        switch (type) {
            case 0:
                fragment = new StatementGlanceFragment();
                break;
            case 1:
                fragment = new SaleGlanceFragment();
                break;
            case 2:
                fragment = new AddSelfNotiFragment();
                break;
            case 3:
                boolean isnew = getIntent().getBooleanExtra("isNew", false);
                fragment = AddSelfGymFragment.newInstance(isnew);

                break;
            case 4:
                fragment = new AddBelongGymFragment();
                break;
            case 5:
                int id = getIntent().getIntExtra("id", 1);
                String host = getIntent().getStringExtra("host");
                boolean isPrivate = getIntent().getBooleanExtra("isPrivate", false);
                fragment = GymDetailFragment.newInstance(id, host, isPrivate);
                break;
            case 6:
                long gymid = getIntent().getLongExtra("id", 1l);
                String isPrivategym = getIntent().getStringExtra("model");
                fragment = GymDetailNativeFragment.newInstance(gymid, isPrivategym);
                break;
            case 7:
                fragment = AddStudentManulkFragment.newInstance();
                break;
            case 8:
                fragment = new MyCoursePlanFragment();
                break;
            case 9:
                fragment = new MyStudentFragment();
                break;
            case 10://Go sync page
                fragment = new SyncGymFragment();
                break;
            case 11:
                fragment = SpaceListFragment.newInstance(getIntent().getIntExtra("course_type", Configs.TYPE_GROUP));
                break;
            case 12:
                fragment = SetAccountFragment.newInstance(getIntent().getIntExtra("count", 1), getIntent().getBooleanExtra("isfree", true));
                break;
            case 13:
                fragment = EditGymFragment.newInstance(mCoachService.id, mCoachService.model, mCoachService.getBrand_name());
                break;
            case 14:
                fragment = new CoursePlanHomeFragment();
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.web_frag_layout, fragment).commit();
    }

    public CoachService getCoachService() {
        return mCoachService;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override public void onBackPressed() {
        AppUtils.hideKeyboard(this);
        if (fragment instanceof GymDetailFragment && fragment.isVisible()) {
            if (((GymDetailFragment) fragment).canGoBack()) {
                ((GymDetailFragment) fragment).goBack();
            } else {
                this.finish();
            }
        } else if (getSupportFragmentManager().popBackStackImmediate()) {

        } else {

            this.finish();
        }
    }
}
