package com.qingchengfit.fitcoach.fragment.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.qingchengfit.staffkit.inject.commpont.GymComponent;
import cn.qingchengfit.staffkit.model.bean.Course;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.response.QcResponseChooseCourses;
import cn.qingchengfit.staffkit.usecase.response.QcResponseCourse;
import cn.qingchengfit.staffkit.usecase.response.ResponseConstant;
import cn.qingchengfit.staffkit.utils.PhotoUtils;
import cn.qingchengfit.staffkit.utils.ToastUtils;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.gym.addcourse.AddGuideCourseFragment;
import rx.Subscription;
import rx.functions.Action1;

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
 * Created by Paper on 16/5/4 2016.
 */
public class GymCourseListFragment extends BaseFragment {

    @Bind(R.id.recyclerview)
    RecycleViewWithNoImg recycleview;
    @Bind(R.id.add)
    Button add;

    /**
     * 课程类型
     */
    private int mType;


    @Inject
    GymUseCase usecase;
    @Inject
    CoachService coachService;
    private Subscription sp;
    private List<Course> datas = new ArrayList<>();

    /**
     * @param type 团课/私教种类
     */
    public static GymCourseListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        GymCourseListFragment fragment = new GymCourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gymcourslist, container, false);
        ButterKnife.bind(this, view);
        if (mCallbackActivity.getComponent() instanceof GymComponent) {
            ((GymComponent) mCallbackActivity.getComponent()).inject(this);
        }
        if (mType == Configs.TYPE_GROUP) {
            mCallbackActivity.setToolbar("选择团课种类", false, null, 0, null);
            add.setText("+添加团课种类");
        } else {
            mCallbackActivity.setToolbar("选择私教教练", false, null, 0, null);
            add.setText("+添加私教种类");
        }

        final CourseAdatper adatper = new CourseAdatper(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(adatper);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = usecase.getCourses(coachService.getId(), coachService.getModel(), mType == Configs.TYPE_PRIVATE, new Action1<QcResponseChooseCourses>() {
                    @Override
                    public void call(QcResponseChooseCourses qcResponseChooseCourses) {
                        if (qcResponseChooseCourses.getStatus() == ResponseConstant.SUCCESS) {
                            datas.clear();
                            datas.addAll(qcResponseChooseCourses.data.courses);
                            adatper.notifyDataSetChanged();
                            recycleview.setNoData(datas.size() == 0);
                        } else ToastUtils.logHttp(qcResponseChooseCourses);
                    }
                });
            }
        });
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                QcResponseCourse.Course c = new QcResponseCourse.Course();
                Course o = datas.get(pos);
                c.id = Integer.parseInt(o.getId());
                c.is_private = o.is_private();
                c.length = Integer.parseInt(o.getLength());
                c.name = o.getName();
                c.photo = o.getPhoto();
                if (getActivity() instanceof ChooseActivity) {
                    Intent it = new Intent();
                    it.putExtra("course", datas.get(pos));
                    getActivity().setResult(Activity.RESULT_OK, it);
                    getActivity().finish();
                } else {
                    getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(), AddGuideCourseFragment.newInstance(AddGuideCourseFragment.COURSE_EDIT, 0, c))
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        sp = usecase.getCourses(coachService.getId(), coachService.getModel(), mType == Configs.TYPE_PRIVATE, new Action1<QcResponseChooseCourses>() {
            @Override
            public void call(QcResponseChooseCourses qcResponseChooseCourses) {
                if (qcResponseChooseCourses.getStatus() == ResponseConstant.SUCCESS) {
                    datas.clear();
                    datas.addAll(qcResponseChooseCourses.data.courses);
                    adatper.notifyDataSetChanged();
                    recycleview.setNoData(datas.size() == 0);
                } else ToastUtils.logHttp(qcResponseChooseCourses);
            }
        });

        return view;
    }

    @OnClick(R.id.add)
    public void onAdd() {
        /*
        getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), AddCourseFragment.newInstance(AddCourseFragment.COURSE_ADD, mType, null))
                .addToBackStack(null)
                .commit();
    */
    }

    @Override
    public void onDestroyView() {
        if (sp != null)
            sp.unsubscribe();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    public class CourseAdatper extends RecyclerView.Adapter<CourseVh> implements View.OnClickListener {
        public CourseAdatper(List<Course> datas) {
            this.datas = datas;
        }

        List<Course> datas;

        OnRecycleItemClickListener listener;


        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public CourseVh onCreateViewHolder(ViewGroup parent, int viewType) {
            CourseVh vh = new CourseVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
            vh.itemView.setOnClickListener(this);
            return vh;
        }

        @Override
        public void onBindViewHolder(CourseVh holder, int position) {
            holder.itemView.setTag(position);
            Course c = datas.get(position);
            Glide.with(getContext()).load(PhotoUtils.getSmall(c.getPhoto())).into(holder.img);
            holder.text1.setText(c.getName());
            holder.text3.setText(String.format(Locale.CHINA, "时长%d分钟", (int) (Float.parseFloat(c.getLength()) / 60)));
            holder.righticon.setVisibility(View.VISIBLE);
            holder.imgFoot.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.ic_arrow_right).into(holder.righticon);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class CourseVh extends RecyclerView.ViewHolder {
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

        public CourseVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
