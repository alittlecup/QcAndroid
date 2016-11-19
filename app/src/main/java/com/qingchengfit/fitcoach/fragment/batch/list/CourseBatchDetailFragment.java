package com.qingchengfit.fitcoach.fragment.batch.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.inject.commpont.GymComponent;
import cn.qingchengfit.staffkit.model.bean.Course;
import cn.qingchengfit.staffkit.model.bean.ImageIconBean;
import cn.qingchengfit.staffkit.model.dataaction.SerPermisAction;
import cn.qingchengfit.staffkit.usecase.response.QcResponseGroupCourse;
import cn.qingchengfit.staffkit.usecase.response.QcResponseGroupDetail;
import cn.qingchengfit.staffkit.usecase.response.QcResponsePrivateDetail;
import cn.qingchengfit.staffkit.usecase.response.QcSchedulesResponse;
import cn.qingchengfit.staffkit.utils.PhotoUtils;
import cn.qingchengfit.staffkit.views.adapter.GroupBatchAdapter;
import cn.qingchengfit.staffkit.views.adapter.PrivateBatchAdapter;
import cn.qingchengfit.staffkit.views.batch.GymCoursesFragment;
import cn.qingchengfit.staffkit.views.batch.addbatch.AddBatchFragment;
import cn.qingchengfit.staffkit.views.batch.details.BatchDetailFragment;
import cn.qingchengfit.staffkit.views.custom.DialogSheet;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.ChooseCoachFragment;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/29 2016.
 */
public class CourseBatchDetailFragment extends BaseFragment implements CourseBatchDetailView {
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.img_foot)
    ImageView imgFoot;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.texticon)
    ImageView texticon;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.text3)
    TextView text3;
    @Bind(R.id.righticon)
    ImageView righticon;
    @Bind(R.id.course_layout)
    RelativeLayout courseLayout;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
//    @Bind(R.id.no_data)
//    LinearLayout noData;

    @Inject
    CourseBatchDetailPresenter presenter;
    //    private ImageThreeTextBean mBean;
//    private SimpleTextIconAdapter simpleTextIconAdapter;
    private List<ImageIconBean> datas = new ArrayList<>();
    private DialogSheet delCourseDialog;
    private MaterialDialog delDialog;

    private int mType;
    private String mId;
    private RecyclerView.Adapter adapter;
    private Course mCourese;
    private QcResponsePrivateDetail.PrivateCoach mTeacher;
    private boolean isLoaded = false;

    //    public static CourseBatchDetailFragment newInstance(ImageThreeTextBean bean) {
//        Bundle args = new Bundle();
//        args.putParcelable("bean", bean);
//        CourseBatchDetailFragment fragment = new CourseBatchDetailFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static CourseBatchDetailFragment newInstance(int type, String id) {
        Bundle args = new Bundle();
        CourseBatchDetailFragment fragment = new CourseBatchDetailFragment();
        args.putInt("type", type);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            mId = getArguments().getString("id");
//            mBean = getArguments().getParcelable("bean");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_batch, container, false);
        ButterKnife.bind(this, view);
        ((GymComponent) mCallbackActivity.getComponent()).inject(this);
        presenter.attachView(this);

        mCallbackActivity.setToolbar(mType == Configs.TYPE_PRIVATE ? "私教排期" : "团课排期", false, null, 0, null);
        if (mType == Configs.TYPE_PRIVATE) {
            presenter.queryPrivate(mId);
        } else presenter.queryGroup(mId);
        presenter.queryBatches();

        return view;
    }


    @Override
    public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void showDelCourse() {
        if (delCourseDialog == null) {
            delCourseDialog = new DialogSheet(getContext());
            delCourseDialog.addButton("编辑", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //编辑课程
                    delCourseDialog.dismiss();
                }
            });
            delCourseDialog.addButton("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除课程
                    delCourseDialog.dismiss();
                    delCourse();
                }
            });
        }
        delCourseDialog.show();
    }

    /**
     * 删除课程
     */
    private void delCourse() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .content("是否删除课程?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            presenter.delCourse();
                        }
                    })
                    .cancelable(false)
                    .build();
        }
        delDialog.show();
    }

//    @OnClick(R.id.course_layout)
    public void onChange() {

        ChooseCoachFragment.start(this, 1, "", Configs.INIT_TYPE_CHOOSE);
    }


    /**
     * 添加课程排期
     */
    @OnClick(R.id.add_batch_btn)
    public void onAddCouseManage() {
        if ((mType == Configs.TYPE_GROUP  && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE))
                || (mType == Configs.TYPE_PRIVATE  && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE))) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }


        QcSchedulesResponse.Teacher teacher = null;
        QcResponseGroupCourse.GroupClass groupClass = null;
        if (mTeacher != null) {
            teacher = new QcSchedulesResponse.Teacher();
            teacher.id = mTeacher.id;
            teacher.username = mTeacher.username;
            teacher.avatar = mTeacher.avatar;

        }
        if (mCourese != null) {
            groupClass = new QcResponseGroupCourse.GroupClass();
            groupClass.id = mCourese.getId();
            groupClass.length = mCourese.getLength();
            groupClass.name = mCourese.getName();
            groupClass.photo = mCourese.getPhoto();
        }

        getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), AddBatchFragment.newInstance(teacher, groupClass))
                .addToBackStack(GymCoursesFragment.TAG)
                .commit();

    }


    @Override
    public String getFragmentName() {
        return CourseBatchDetailFragment.class.getName();
    }

    @Override
    public void onGoup(Course course, final List<QcResponseGroupDetail.GroupBatch> batch) {
        mCourese = course;
        Glide.with(getContext()).load(PhotoUtils.getSmall(course.getPhoto())).placeholder(R.drawable.img_default_course).into(img);
        imgFoot.setVisibility(View.GONE);
        text1.setText(course.getName());
        text2.setText("时长" + ((int)(Float.parseFloat(course.getLength()) / 60)) + "分钟");

        adapter = new GroupBatchAdapter(batch);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        if (adapter instanceof GroupBatchAdapter) {


            ((GroupBatchAdapter) adapter).setListener(new OnRecycleItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {

//                    if ((mType == Configs.TYPE_GROUP  && !SerPermisAction.checkAll(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE))
//                            || (mType == Configs.TYPE_PRIVATE  && !SerPermisAction.checkAll(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
//                        showAlert(R.string.alert_permission_forbid);
//                        return;
//                    }

                    getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(), BatchDetailFragment.newInstance(mType, batch.get(pos).id))
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    @Override
    public void onPrivate(QcResponsePrivateDetail.PrivateCoach coach, final List<QcResponsePrivateDetail.PrivateBatch> batch) {
        mTeacher = coach;
        Glide.with(getContext()).load(PhotoUtils.getSmall(coach.avatar)).placeholder(R.drawable.ic_default_header).into(img);
        imgFoot.setVisibility(View.GONE);
        text1.setText(coach.username);
        text2.setText(String.format(Locale.CHINA, "累计%s节,服务%s人次", coach.course_count, coach.users_count));
        adapter = new PrivateBatchAdapter(batch);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        if (adapter instanceof PrivateBatchAdapter) {
            ((PrivateBatchAdapter) adapter).setListener(new OnRecycleItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {


                    getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(), BatchDetailFragment.newInstance(mType, batch.get(pos).id))
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

    }
}
