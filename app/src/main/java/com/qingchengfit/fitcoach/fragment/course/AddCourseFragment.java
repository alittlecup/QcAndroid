package com.qingchengfit.fitcoach.fragment.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.bean.CourseDetail;
import cn.qingchengfit.staffkit.usecase.bean.Brand;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.bean.CoursePlan;
import cn.qingchengfit.staffkit.usecase.body.CourseBody;
import cn.qingchengfit.staffkit.usecase.response.QcResponseBrandShops;
import cn.qingchengfit.staffkit.utils.GymUtils;
import cn.qingchengfit.staffkit.utils.IntentUtils;
import cn.qingchengfit.staffkit.utils.ToastUtils;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
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
 * Created by Paper on 16/8/2.
 */
public class AddCourseFragment extends BaseFragment implements AddCoursePresenter.AddCourseView {

    public static final int RESULT_GYMS = 0;


    @Bind(R.id.btn_suit_gyms)
    CommonInputView btnSuitGyms;
    @Bind(R.id.layout_suit_gym)
    LinearLayout layoutSuitGym;
    private CourseBaseInfoEditFragment mEditBaseInfo;

    @Inject
    AddCoursePresenter mPresenter;
    @Inject
    Brand brand;
    @Inject
    CoachService coachService;



    public static AddCourseFragment newInstance(boolean isPrivate) {

        Bundle args = new Bundle();
        args.putBoolean("p",isPrivate);
        AddCourseFragment fragment = new AddCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() instanceof CourseActivity){
            ((CourseActivity) getActivity()).getComponent().inject(this);
            delegatePresenter(mPresenter,this);
        }
        RxBusAdd(CoursePlan.class).subscribe(new Action1<CoursePlan>() {
            @Override
            public void call(CoursePlan coursePlan) {
                mCallbackActivity.setToolbar(getArguments().getBoolean("p") ? "新增私教种类" : "新增团课种类", false, null, R.menu.menu_compelete, listener);
            }
        });
        mCallbackActivity.setToolbar(getArguments().getBoolean("p") ? "新增私教种类" : "新增团课种类", false, null, R.menu.menu_compelete, listener);
        CourseDetail courseDetail = new CourseDetail();
        courseDetail.setIs_private(getArguments().getBoolean("p"));
        if (mEditBaseInfo == null) {
            mEditBaseInfo = CourseBaseInfoEditFragment.newInstance(courseDetail);
        }
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_baseinfo, mEditBaseInfo)
                    .commit();

        if (GymUtils.isInBrand(coachService)){
            layoutSuitGym.setVisibility(View.VISIBLE);
        }else {
            layoutSuitGym.setVisibility(View.GONE);
        }

        return view;
    }

    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mEditBaseInfo !=null ){
                CourseDetail courseDetail = mEditBaseInfo.getCourse();
                if (courseDetail != null){
                    String support_gym = "";
                    if (GymUtils.isInBrand(coachService)){
                        if (layoutSuitGym.getVisibility() == View.VISIBLE){
                            support_gym = (String)btnSuitGyms.getTag();
                        }
                    }else {
                        support_gym = null;
                    }
                    if (GymUtils.isInBrand(coachService) && support_gym == null){
                        ToastUtils.show("请至少选择一个家场馆");
                        return true;
                    }


                    String Planid = null;
                    if (courseDetail.getPlan() != null){
                        Planid = Long.toString(courseDetail.getPlan().getId());
                    }
                    CourseBody body = new CourseBody.Builder()
                            .name(courseDetail.getName())
                            .capacity(courseDetail.getCapacity())
                            .is_private(getArguments().getBoolean("p")?1:0)
                            .length(Integer.parseInt(courseDetail.getLength()))
                            .min_users(getArguments().getBoolean("p")?null:courseDetail.getMin_users())
                            .photo(courseDetail.getPhoto())
                            .plan_id(Planid)
                            .shop_ids(support_gym)
                            .build();
                    showLoading();
                    mPresenter.addCourse(App.staffId,body);
                }
            }

            return true;
        }
    };

    @Override
    public String getFragmentName() {
        return AddCourseFragment.class.getName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_suit_gyms)
    public void onClick() {
        MutiChooseGymFragment.start(this,false,null,getArguments().getBoolean("p") ?PermissionServerUtils.PRISETTING_CAN_WRITE: PermissionServerUtils.TEAMSETTING_CAN_WRITE,RESULT_GYMS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == RESULT_GYMS){
                String ids = "";
                ArrayList<QcResponseBrandShops.BrandShop> shops = data.getParcelableArrayListExtra(IntentUtils.RESULT);
                if (shops != null) {
                    for (int i = 0; i < shops.size(); i++) {
                        if (i < shops.size() - 1)
                            ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
                        else
                            ids = TextUtils.concat(ids, shops.get(i).id).toString();
                    }
                    btnSuitGyms.setTag(ids);
                    btnSuitGyms.setContent(shops.size() + "家");
                }
            }
        }
    }

    /**
     * 可以使用viewStub 优化 todo
     */
    @Override
    public void showSuitGym() {
        layoutSuitGym.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override
    public void onFailed(String s) {
        hideLoading();
        ToastUtils.show(s);
    }
}
