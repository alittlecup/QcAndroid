package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.VpFragment;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.adapter.ImageThreeTextAdapter;
import com.qingchengfit.fitcoach.adapter.ImageThreeTextBean;
import com.qingchengfit.fitcoach.bean.RxAddCourse;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends VpFragment {

    @BindView(R.id.course_count) TextView courseCount;
    @BindView(R.id.preview) TextView preview;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.no_data) LinearLayout noData;

    private ImageThreeTextAdapter mImageTwoTextAdapter;
    private List<ImageThreeTextBean> datas = new ArrayList<>();
    private int mCourseType = 1;//当前页的类型
    private int mGymType = 1;//个人健身房 0是同步健身房
    private int course_count;
    private String toUrl;
    private Unbinder unbinder;

    public CourseListFragment() {

    }

    /**
     * @param GymType 0是同步健身房 1是个人
     * @param CourseType 1是私教 2是团课
     */
    public static CourseListFragment newInstance(int GymType, int CourseType, ArrayList<ImageThreeTextBean> d, String url) {

        Bundle args = new Bundle();
        args.putInt("gymtype", GymType);
        args.putInt("coursetype", CourseType);
        args.putString("url", url);
        args.putParcelableArrayList("data", d);
        CourseListFragment fragment = new CourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseType = getArguments().getInt("coursetype");
            mGymType = getArguments().getInt("gymtype");
            datas = getArguments().getParcelableArrayList("data");
            toUrl = getArguments().getString("url");
            course_count = datas.size();
            if (mGymType == 1) {
                if (mCourseType == 1) {
                    ImageThreeTextBean bean = new ImageThreeTextBean("", "+ 添加私教种类", "", "");
                    bean.type = 1;
                    datas.add(bean);
                } else {
                    ImageThreeTextBean bean = new ImageThreeTextBean("", "+ 添加团课种类", "", "");
                    bean.type = 1;
                    datas.add(bean);
                }
            }
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (mCourseType == Configs.TYPE_PRIVATE) {
            courseCount.setText(course_count + "节私教课");
            preview.setText("会员私教页预览");
        } else {
            courseCount.setText(course_count + "节团课");
            preview.setText("会员团课页预览");
        }
        if (datas.size() == 0) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
        mImageTwoTextAdapter = new ImageThreeTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mImageTwoTextAdapter);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent toWeb = new Intent(getContext(), WebActivity.class);
                toWeb.putExtra("url", toUrl);
                startActivity(toWeb);
            }
        });
        mImageTwoTextAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                ImageThreeTextBean b = datas.get(pos);
                if (b.type == 1) {
                    //添加课程
                    if (mCourseType == 1) {
                        RxBus.getBus().post(new RxAddCourse(Configs.TYPE_PRIVATE));
                    } else {
                        RxBus.getBus().post(new RxAddCourse(Configs.TYPE_GROUP));
                    }
                } else {
                    //课程详情
                    if (mGymType != 0) RxBus.getBus().post(b);
                }
            }
        });

        return view;
    }

    /**
     * 预约课程 跳转到web页面
     */
    //    @OnClick(R.id.preview)
    //    public void onPreview() {
    //
    //    }
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public String getTitle() {
        if (isAdded()) {
            if (mCourseType == 1) {
                return getString(R.string.course_private) + "排期";
            } else if (mCourseType == 2) {
                return getString(R.string.course_group) + "排期";
            } else {
                return getString(R.string.course_group) + "排期";
            }
        } else {
            return "";
        }
    }
}
