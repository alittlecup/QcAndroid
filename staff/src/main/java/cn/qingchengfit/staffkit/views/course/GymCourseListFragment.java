package cn.qingchengfit.staffkit.views.course;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.common.Course;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.gym.addcourse.AddGuideCourseFragment;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
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

    @BindView(R.id.recyclerview) RecycleViewWithNoImg recycleview;
    @BindView(R.id.add) Button add;
    @Inject GymUseCase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    /**
     * 课程类型
     */
    private int mType;
    private Subscription sp;
    private List<CourseTypeSample> datas = new ArrayList<>();
    private CourseAdatper adatper;

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

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gymcourslist, container, false);
        unbinder = ButterKnife.bind(this, view);
        //
        if (mType == Configs.TYPE_GROUP) {
            mCallbackActivity.setToolbar("选择团课种类", false, null, 0, null);
            add.setText("+添加团课种类");
        } else {
            mCallbackActivity.setToolbar("选择私教教练", false, null, 0, null);
            add.setText("+添加私教种类");
        }

        adatper = new CourseAdatper(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(adatper);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                sp = usecase.getCourses(gymWrapper.id(), gymWrapper.model(), mType == Configs.TYPE_PRIVATE,
                    new Action1<QcResponseData<CourseTypeSamples>>() {
                        @Override public void call(QcResponseData<CourseTypeSamples> qcResponseChooseCourses) {
                            if (qcResponseChooseCourses.getStatus() == ResponseConstant.SUCCESS) {
                                datas.clear();
                                datas.addAll(qcResponseChooseCourses.data.courses);
                                adatper.notifyDataSetChanged();
                                recycleview.setNoData(datas.size() == 0);
                            } else {
                                // ToastUtils.logHttp(qcResponseChooseCourses);
                            }
                        }
                    });
            }
        });
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Course c = new Course();
                CourseTypeSample o = datas.get(pos);
                c.id = o.getId();
                c.is_private = o.is_private();
                c.length = o.getLength();
                c.name = o.getName();
                c.photo = o.getPhoto();
                if (getActivity() instanceof ChooseActivity) {
                    Intent it = new Intent();
                    it.putExtra("course", datas.get(pos));
                    getActivity().setResult(Activity.RESULT_OK, it);
                    getActivity().finish();
                } else {
                    getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(),
                            AddGuideCourseFragment.newInstance(AddGuideCourseFragment.COURSE_EDIT, 0, c))
                        .addToBackStack(null)
                        .commit();
                }
            }
        });
        sp = usecase.getCourses(gymWrapper.id(), gymWrapper.model(), mType == Configs.TYPE_PRIVATE,
            new Action1<QcResponseData<CourseTypeSamples>>() {
                @Override public void call(QcResponseData<CourseTypeSamples> qcResponseChooseCourses) {
                    if (qcResponseChooseCourses.getStatus() == ResponseConstant.SUCCESS) {
                        datas.clear();
                        datas.addAll(qcResponseChooseCourses.data.courses);
                        adatper.notifyDataSetChanged();
                        recycleview.setNoData(datas.size() == 0);
                    } else {
                        // ToastUtils.logHttp(qcResponseChooseCourses);
                    }
                }
            });

        return view;
    }

    @OnClick(R.id.add) public void onAdd() {
        Intent it = new Intent(getActivity(), CourseActivity.class);
        it.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus(false));
        it.putExtra(Configs.EXTRA_COURSE_TYPE,
            mType == Configs.TYPE_PRIVATE ? CourseActivity.ADD_PRIVATE_COURSE : CourseActivity.ADD_GROUP_COURSE);
        startActivityForResult(it, 1);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (usecase != null) {
            sp = usecase.getCourses(gymWrapper.id(), gymWrapper.model(), mType == Configs.TYPE_PRIVATE,
                new Action1<QcResponseData<CourseTypeSamples>>() {
                    @Override public void call(QcResponseData<CourseTypeSamples> qcResponseChooseCourses) {
                        if (qcResponseChooseCourses.getStatus() == ResponseConstant.SUCCESS) {
                            datas.clear();
                            datas.addAll(qcResponseChooseCourses.data.courses);
                            adatper.notifyDataSetChanged();
                            recycleview.setNoData(datas.size() == 0);
                        } else {
                            // ToastUtils.logHttp(qcResponseChooseCourses);
                        }
                    }
                });
        }
    }

    @Override public void onDestroyView() {
        if (sp != null) sp.unsubscribe();
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return null;
    }

    public class CourseAdatper extends RecyclerView.Adapter<CourseVh> implements View.OnClickListener {
        List<CourseTypeSample> datas;
        OnRecycleItemClickListener listener;

        public CourseAdatper(List<CourseTypeSample> datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public CourseVh onCreateViewHolder(ViewGroup parent, int viewType) {
            CourseVh vh = new CourseVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
            vh.itemView.setOnClickListener(this);
            return vh;
        }

        @Override public void onBindViewHolder(CourseVh holder, int position) {
            holder.itemView.setTag(position);
            CourseTypeSample c = datas.get(position);
            Glide.with(getContext()).load(PhotoUtils.getSmall(c.getPhoto())).into(holder.img);
            holder.text1.setText(c.getName());
            holder.text3.setText(String.format(Locale.CHINA, "时长%d分钟", c.getLength() / 60));
            holder.righticon.setVisibility(View.VISIBLE);
            holder.imgFoot.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.ic_arrow_right).into(holder.righticon);
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class CourseVh extends RecyclerView.ViewHolder {
        @BindView(R.id.img) ImageView img;
        @BindView(R.id.img_foot) ImageView imgFoot;
        @BindView(R.id.text1) TextView text1;
        @BindView(R.id.texticon) ImageView texticon;
        @BindView(R.id.text2) TextView text2;
        @BindView(R.id.text3) TextView text3;
        @BindView(R.id.righticon) ImageView righticon;
        @BindView(R.id.course_layout) RelativeLayout courseLayout;

        public CourseVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
