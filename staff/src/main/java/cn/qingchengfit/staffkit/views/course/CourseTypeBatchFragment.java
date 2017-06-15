package cn.qingchengfit.staffkit.views.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.course.limit.OrderLimitFragment;
import cn.qingchengfit.staffkit.views.course.limit.OrderLimitFragmentBuilder;
import cn.qingchengfit.staffkit.views.course.msg.MsgNotiFragment;
import cn.qingchengfit.staffkit.views.course.msg.MsgNotiFragmentBuilder;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.IntentUtils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
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
 * Created by Paper on 2017/1/9.
 */
@FragmentWithArgs public class CourseTypeBatchFragment extends BaseFragment {

    public static final int RESULT_MENU = 1;

    /**
     * 是否为私教
     */
    @Inject GymWrapper gymWrapper;
    @Arg boolean mIsPrivate;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_type_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        getChildFragmentManager().beginTransaction()
            .replace(R.id.course_batch_frag,
                cn.qingchengfit.staffkit.views.batch.CourseListFragment.newInstance(mIsPrivate ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP))
            .commit();

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(getString(mIsPrivate ? R.string.course_type_private : R.string.course_type_group));
        toolbar.inflateMenu(R.menu.menu_flow);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                BottomSheetListDialogFragment.start(CourseTypeBatchFragment.this, RESULT_MENU,
                    getResources().getStringArray(mIsPrivate ? R.array.course_private_menu : R.array.course_group_menu));
                return true;
            }
        });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_MENU) {
                int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                if (pos == 0) {//跳至课程列表
                    if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(),
                        mIsPrivate ? PermissionServerUtils.PRISETTING : PermissionServerUtils.TEAMSETTING)) {
                        showAlert(R.string.sorry_for_no_permission);
                        return;
                    }
                    getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                        .replace(R.id.frag, CourseListFragment.newInstance(mIsPrivate))
                        .addToBackStack(null)
                        .commit();
                } else if (pos == 1) {
                    /**
                     * 预约限制 {@link OrderLimitFragment}
                     */
                    if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(),
                        mIsPrivate ? PermissionServerUtils.PRIVATE_COURSE_LIMIT : PermissionServerUtils.TEAM_COURSE_LIMIT)) {
                        showAlert(R.string.sorry_for_no_permission);
                        return;
                    }
                    getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                        .replace(R.id.frag, new OrderLimitFragmentBuilder(mIsPrivate).build())
                        .addToBackStack(null)
                        .commit();
                } else if (pos == 2) {
                    /**
                     * 短信通知{@link MsgNotiFragment}
                     */
                    if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(),
                        mIsPrivate ? PermissionServerUtils.PRIVATE_COURSE_MSG_SETTING : PermissionServerUtils.TEAM_COURSE_MSG_SETTING)) {
                        showAlert(R.string.sorry_for_no_permission);
                        return;
                    }
                    getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                        .replace(R.id.frag, new MsgNotiFragmentBuilder(mIsPrivate).build())
                        .addToBackStack(null)
                        .commit();
                } else {
                    /**
                     * 课件
                     */
                    if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.PLANSSETTING)) {
                        showAlert(R.string.sorry_for_no_permission);
                        return;
                    }

                    GymFunctionFactory.goQrScan(this,
                        mIsPrivate ? GymFunctionFactory.PLANS_SETTING_PRIVATE : GymFunctionFactory.PLANS_SETTING_GROUP, null,
                        gymWrapper.getCoachService());
                }
            }
        }
    }

    @Override public String getFragmentName() {
        return CourseTypeBatchFragment.class.getName();
    }
}
