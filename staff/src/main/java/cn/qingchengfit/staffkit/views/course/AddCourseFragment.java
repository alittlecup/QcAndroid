package cn.qingchengfit.staffkit.views.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.CourseBody;
import cn.qingchengfit.model.responese.CourseType;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import java.util.ArrayList;
import javax.inject.Inject;

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

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject AddCoursePresenter mPresenter;

    @BindView(R.id.btn_suit_gyms) CommonInputView btnSuitGyms;
    @BindView(R.id.layout_suit_gym) LinearLayout layoutSuitGym;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private CourseBaseInfoEditFragment mEditBaseInfo;
    private ArrayList<String> selectedIds = new ArrayList<>();
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (mEditBaseInfo != null) {
                CourseType courseDetail = mEditBaseInfo.getCourse();
                if (courseDetail != null) {
                    String support_gym = "";
                    if (gymWrapper.inBrand()) {
                        if (layoutSuitGym.getVisibility() == View.VISIBLE) {
                            support_gym = (String) btnSuitGyms.getTag();
                        }
                    } else {
                        support_gym = null;
                    }
                    if (gymWrapper.inBrand() && support_gym == null) {
                        ToastUtils.show("请至少选择一个家场馆");
                        return true;
                    }

                    String Planid = null;
                    if (courseDetail.getPlan() != null) {
                        Planid = Long.toString(courseDetail.getPlan().getId());
                    }
                    CourseBody body = new CourseBody.Builder().name(courseDetail.getName())
                        .capacity(courseDetail.getCapacity())
                        .is_private(getArguments().getBoolean("p") ? 1 : 0)
                        .length(courseDetail.getLength())
                        .min_users(getArguments().getBoolean("p") ? null : courseDetail.getMin_users())
                        .photo(courseDetail.getPhoto())
                        .plan_id(Planid)
                        .shop_ids(support_gym)
                        .build();
                    showLoading();
                    mPresenter.addCourse(App.staffId, body);
                }
            }

            return true;
        }
    };

    public static AddCourseFragment newInstance(boolean isPrivate) {

        Bundle args = new Bundle();
        args.putBoolean("p", isPrivate);
        AddCourseFragment fragment = new AddCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(mPresenter, this);
        initToolbar(toolbar);
        CourseType courseDetail = new CourseType();
        courseDetail.setIs_private(getArguments().getBoolean("p"));
        if (mEditBaseInfo == null) {
            mEditBaseInfo = CourseBaseInfoEditFragment.newInstance(courseDetail);
        }
        getFragmentManager().beginTransaction().replace(R.id.frag_baseinfo, mEditBaseInfo).commit();

        if (gymWrapper.inBrand()) {
            layoutSuitGym.setVisibility(View.VISIBLE);
        } else {
            layoutSuitGym.setVisibility(View.GONE);
        }

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        //mCallbackActivity.setToolbar(getArguments().getBoolean("p") ? "新增私教种类" : "新增团课种类", false, null, R.menu.menu_compelete, listener);
        toolbarTitile.setText(getArguments().getBoolean("p") ? "新增私教种类" : "新增团课种类");
        toolbar.inflateMenu(R.menu.menu_compelete);
        toolbar.setOnMenuItemClickListener(listener);
    }

    @Override public String getFragmentName() {
        return AddCourseFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.btn_suit_gyms) public void onClick() {
        MutiChooseGymFragment.start(this, false, selectedIds,
            getArguments().getBoolean("p") ? PermissionServerUtils.PRISETTING_CAN_WRITE : PermissionServerUtils.TEAMSETTING_CAN_WRITE,
            RESULT_GYMS);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_GYMS) {
                String ids = "";
                ArrayList<Shop> shops = data.getParcelableArrayListExtra(IntentUtils.RESULT);
                if (selectedIds.size() > 0){
                    selectedIds.clear();
                }
                if (shops != null) {
                    for (int i = 0; i < shops.size(); i++) {
                        selectedIds.add(shops.get(i).id);
                        if (i < shops.size() - 1) {
                            ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
                        } else {
                            ids = TextUtils.concat(ids, shops.get(i).id).toString();
                        }
                    }
                    btnSuitGyms.setTag(ids);
                    btnSuitGyms.setContent(shops.size() + "家");
                }
            }
        }
    }

    @Override public void showSuitGym() {
        layoutSuitGym.setVisibility(View.VISIBLE);
    }

    @Override public void onSuccess() {
        RxBus.getBus().post(new RefreshCourseEvent());
        hideLoading();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override public void onFailed(String s) {
        hideLoading();
        ToastUtils.show(s);
    }
}
