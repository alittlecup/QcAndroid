package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.ImageIconBean;
import com.qingchengfit.fitcoach.adapter.ImageThreeTextBean;
import com.qingchengfit.fitcoach.adapter.SimpleTextIconAdapter;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.GetBatchesResponse;
import com.qingchengfit.fitcoach.http.bean.QcOneCourseResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDetailFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.img)
    ImageView img;
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
    @Bind(R.id.course_count)
    TextView courseCount;
    @Bind(R.id.preview)
    TextView preview;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.no_data)
    LinearLayout noData;
    private ImageThreeTextBean mBean;
    private SimpleTextIconAdapter simpleTextIconAdapter;
    private List<ImageIconBean> datas = new ArrayList<>();
    private DialogSheet delCourseDialog;
    private DialogSheet delBatchDialog;
    private Observable<String> mObservableRefresh;
    private MaterialDialog delDialog;
    private MaterialDialog delBatchComfirmDialog;

    public static CourseDetailFragment newInstance(ImageThreeTextBean bean) {

        Bundle args = new Bundle();
        args.putParcelable("bean", bean);
        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CourseDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBean = getArguments().getParcelable("bean");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle("课程详情");
        toolbar.inflateMenu(R.menu.menu_flow);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showDelCourse();
                return true;
            }
        });
        Glide.with(App.AppContex).load(mBean.imgUrl).into(img);
        text1.setText(mBean.text1);
        text2.setText(mBean.text2);
        text3.setText(mBean.text3);

        courseCount.setText("课程安排");
        preview.setText("添加排期");
        simpleTextIconAdapter = new SimpleTextIconAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(simpleTextIconAdapter);
        simpleTextIconAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (v.getId() == R.id.icon) {
                    //点击option
                    showDelBatch(pos);
                } else {
                    //点击整行
//                    getFragmentManager().beginTransaction()
//                            .add(R.id.web_frag_layout, CourseManageFragment.newInstance(mBean.tags.get(ImageThreeTextBean.TAG_MODEL)
//                                    , mBean.tags.get(ImageThreeTextBean.TAG_ID)
//                                    , datas.get(pos).id
//                                    , Integer.parseInt(mBean.tags.get(ImageThreeTextBean.TAG_COURSETYPE))))
//                            .addToBackStack(null)
//                            .commit();
                    goBatch(pos);
                }
            }
        });
        loadGroupData();
        mObservableRefresh = RxBus.getBus().register(RxBus.BUS_REFRESH);
        mObservableRefresh.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        loadGroupData();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("model", mBean.tags.get(ImageThreeTextBean.TAG_MODEL));
                        params.put("id", mBean.tags.get(ImageThreeTextBean.TAG_ID));
                        QcCloudClient.getApi().getApi.qcGetOneCourse(App.coachid, mBean.tags.get(ImageThreeTextBean.TAG_COURSE), params)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<QcOneCourseResponse>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(QcOneCourseResponse qcOneCourseResponse) {
                                        if (qcOneCourseResponse.status == ResponseResult.SUCCESS) {
                                            Glide.with(App.AppContex).load(qcOneCourseResponse.data.course.photo).into(img);
                                            text1.setText(qcOneCourseResponse.data.course.name);
                                            text2.setText("时长: " + qcOneCourseResponse.data.course.length / 60 + "min");
                                        }
                                    }
                                });
                    }
                });
        return view;
    }

    private void showDelCourse() {
        if (delCourseDialog == null) {
            delCourseDialog = new DialogSheet(getContext());
            delCourseDialog.addButton("编辑", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //编辑课程
                    delCourseDialog.dismiss();
                    getFragmentManager().beginTransaction()
                            .add(R.id.web_frag_layout, AddCourseFrament.newInstance(2, mBean.tags.get(ImageThreeTextBean.TAG_MODEL), Integer.parseInt(mBean.tags.get(ImageThreeTextBean.TAG_ID)), mBean.tags.get(ImageThreeTextBean.TAG_COURSE)))
                            .addToBackStack(null)
                            .commit();
                }
            });
            delCourseDialog.addButton("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除课程
                    delCourseDialog.dismiss();
                    delCourse();
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put("course_id", mBean.tags.get(ImageThreeTextBean.TAG_COURSE));
//                    params.put("model", mBean.tags.get(ImageThreeTextBean.TAG_MODEL));
//                    params.put("id", mBean.tags.get(ImageThreeTextBean.TAG_ID));
//                    QcCloudClient.getApi().postApi.qcDelCourse(App.coachid, params)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeOn(Schedulers.io())
//                            .subscribe(new Subscriber<QcResponse>() {
//                                @Override
//                                public void onCompleted() {
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//
//                                }
//
//                                @Override
//                                public void onNext(QcResponse qcResponse) {
//                                    if (qcResponse.status == ResponseResult.SUCCESS) {
//                                        ToastUtils.showDefaultStyle("删除成功");
//                                        getActivity().onBackPressed();
//                                    }
//                                }
//                            });
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
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.dismiss();
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("course_id", mBean.tags.get(ImageThreeTextBean.TAG_COURSE));
                            params.put("model", mBean.tags.get(ImageThreeTextBean.TAG_MODEL));
                            params.put("id", mBean.tags.get(ImageThreeTextBean.TAG_ID));

                            QcCloudClient.getApi().postApi.qcDelCourse(App.coachid, params)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<QcResponse>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(QcResponse qcResponse) {
                                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                                ToastUtils.show("删除成功");
                                                getActivity().onBackPressed();
                                                RxBus.getBus().post(RxBus.BUS_REFRESH);
                                            } else {

                                            }
                                        }
                                    });


                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .build();
        }
        delDialog.show();
    }


    private void showDelBatch(int pos) {

        delBatchDialog = new DialogSheet(getContext());
        delBatchDialog.addButton("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delBatchDialog.dismiss();
                goBatch(pos);
            }
        });
        delBatchDialog.addButton("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除排期
                delBatchDialog.dismiss();
                delBatchComfirmDialog = new MaterialDialog.Builder(getActivity())
                        .autoDismiss(true)
                        .content("是否删除排期?")
                        .positiveText("确定")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("model", mBean.tags.get(ImageThreeTextBean.TAG_MODEL));
                                params.put("id", mBean.tags.get(ImageThreeTextBean.TAG_ID));
                                QcCloudClient.getApi().postApi.qcDelBatch(App.coachid, datas.get(pos).id, params)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Subscriber<QcResponse>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(QcResponse qcResponse) {
                                                if (qcResponse.status == ResponseResult.SUCCESS) {
                                                    ToastUtils.showDefaultStyle("删除成功");
                                                    datas.remove(pos);
                                                    if (datas.size() == 0){
                                                        noData.setVisibility(View.VISIBLE);
                                                    }
                                                    simpleTextIconAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        })
                        .build();
                    delBatchComfirmDialog.show();



            }
        });

        delBatchDialog.show();
    }

    public void goBatch(int pos) {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, CourseManageFragment.newInstance(mBean.tags.get(ImageThreeTextBean.TAG_MODEL)
                        , mBean.tags.get(ImageThreeTextBean.TAG_ID)
                        , datas.get(pos).id
                        , Integer.parseInt(mBean.tags.get(ImageThreeTextBean.TAG_COURSETYPE))))
                .addToBackStack(null)
                .commit();
    }

    public void loadGroupData() {
        //团课
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mBean.tags.get("model"));
        params.put("id", mBean.tags.get("gymid"));
        QcCloudClient.getApi().getApi.qcGetGroupManage(App.coachid, mBean.tags.get("courseid"), params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetBatchesResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GetBatchesResponse qcBatchResponse) {
                        if (qcBatchResponse.status != ResponseResult.SUCCESS) {
                            ToastUtils.showDefaultStyle("服务器错误");
                            return;
                        }
                        datas.clear();
                        for (GetBatchesResponse.Batch schedule : qcBatchResponse.data.batches) {
                            ImageIconBean bean = new ImageIconBean(schedule.from_date + "至" + schedule.to_date, R.drawable.ic_options);
                            bean.id = schedule.id;
                            datas.add(bean);
                        }
                        if (datas.size() > 0) {
                            simpleTextIconAdapter.notifyDataSetChanged();
                            noData.setVisibility(View.GONE);
                        } else noData.setVisibility(View.VISIBLE);
                    }
                });
    }


    /**
     * 添加课程排期
     */
    @OnClick(R.id.preview)
    public void onAddCouseManage() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, AddCourseManageFragment.newInstance(mBean.tags.get("model"), mBean.tags.get("gymid"), mBean.tags.get(ImageThreeTextBean.TAG_COURSE), Integer.parseInt(mBean.tags.get(ImageThreeTextBean.TAG_COURSETYPE)), mBean.tags.get("length")))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        RxBus.getBus().unregister(RxBus.BUS_REFRESH, mObservableRefresh);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
