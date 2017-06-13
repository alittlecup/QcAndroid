package cn.qingchengfit.staffkit.views.wardrobe.hire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.body.HireWardrobeBody;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventLongHire;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.staffkit.views.student.MutiChooseStudentActivity;
import cn.qingchengfit.staffkit.views.wardrobe.edit.WardrobeEditFragment;
import cn.qingchengfit.utils.ToastUtils;
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
 * Created by Paper on 16/8/31.
 */
public class WardrobeShortHireFragment extends BaseFragment implements WardrobeShortHirePresenter.MVPView {

    @BindView(R.id.choose_student) CommonInputView chooseStudent;
    @BindView(R.id.comfirm) Button comfirm;

    @Inject WardrobeShortHirePresenter mPersenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private Locker mLocker;
    private StudentBean mChoseStu;
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_edit) {
                getParentFragment().getFragmentManager()
                    .beginTransaction()
                    .replace(mCallbackActivity.getFragId(), WardrobeEditFragment.newInstance(mLocker))
                    .addToBackStack("")
                    .commit();
            }
            return true;
        }
    };

    public static WardrobeShortHireFragment newInstance(Locker locker) {

        Bundle args = new Bundle();
        args.putParcelable("l", locker);
        WardrobeShortHireFragment fragment = new WardrobeShortHireFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocker = getArguments().getParcelable("l");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_short_hire, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCallbackActivity.setToolbar("租用更衣柜", false, null, R.menu.menu_edit, listener);
        //
        delegatePresenter(mPersenter, this);

        return view;
    }

    @Override public String getFragmentName() {
        return WardrobeShortHireFragment.class.getName();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                StudentBean studentBean = data.getParcelableExtra(MutiChooseStudentActivity.EXTRA_STUDENTS);
                if (studentBean != null) {
                    chooseStudent.setContent(studentBean.getUsername());
                    mChoseStu = studentBean;
                }
            }
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.comfirm, R.id.btn_long_hire }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comfirm:
                showLoading();
                mPersenter.hireWardrobe(App.staffId, new HireWardrobeBody.Builder().is_long_term_borrow(false)
                    .locker_id(mLocker.id)
                    .user_id(mChoseStu == null ? null : mChoseStu.getId())
                    .build());
                break;
            case R.id.btn_long_hire://跳去长期租用
                RxBus.getBus().post(new EventLongHire());
                //                getParentFragment().getChildFragmentManager().beginTransaction()
                //                        .replace(R.id.frag,WardrobeLongHireFragment.newInstance(mLocker))
                //                        .addToBackStack(getFragmentName())
                //                        .commit();
                break;
        }
    }

    @Override public void onHireOK() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @OnClick(R.id.choose_student) public void onClick() {
        // TODO: 2017/3/2
        Intent toChooseStudent = new Intent(getContext(), MutiChooseStudentActivity.class);
        toChooseStudent.putExtra(Configs.EXTRA_PERMISSION_KEY, PermissionServerUtils.LOCKER_SETTING);
        toChooseStudent.putExtra(Configs.EXTRA_PERMISSION_METHOD, "post");
        startActivityForResult(toChooseStudent, 1);
    }
}
