package cn.qingchengfit.staffkit.views.course;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.responese.QcResponseSchedulePhotos;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.CourseImageManageEvent;
import cn.qingchengfit.staffkit.usecase.bean.SchedulePhoto;
import cn.qingchengfit.staffkit.usecase.bean.SchedulePhotos;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.AllCourseImageHeaderItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.AllCourseImageItem;
import cn.qingchengfit.staffkit.views.adapter.AllCourseImagesAdapter;
import cn.qingchengfit.views.activity.WebActivity;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/7/28.
 */
public class CourseImagesFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    AllCourseImagesAdapter mAdatper;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private int page = 1;
    private int totalPage = 1;

    public static CourseImagesFragment newInstance(String courseid) {

        Bundle args = new Bundle();
        args.putString("courseid", courseid);
        CourseImagesFragment fragment = new CourseImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_course_images, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        //init recycle
        mAdatper = new AllCourseImagesAdapter(mDatas, this);
        mAdatper.setMode(SelectableAdapter.MODE_SINGLE);
        mAdatper.setDisplayHeadersAtStartUp(true);

        SmoothScrollGridLayoutManager layoutManager = new SmoothScrollGridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                switch (mAdatper.getItemViewType(position)) {

                    case R.layout.item_all_course_image_view:
                        return 1;
                    default:
                        return 3;
                }
            }
        });
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setAdapter(mAdatper);
        recyclerview.setHasFixedSize(true);

        RxRegiste(restRepository.getGet_api()
            .qcGetSchedulePhotos(App.staffId, getArguments().getString("courseid"), page, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseSchedulePhotos>() {
                @Override public void call(QcResponseSchedulePhotos qcResponseSchedulePhotos) {
                    if (ResponseConstant.checkSuccess(qcResponseSchedulePhotos)) {
                        totalPage = qcResponseSchedulePhotos.data.pages;
                        if (qcResponseSchedulePhotos.data.schedules != null) {

                            if (qcResponseSchedulePhotos.data.schedules.size() == 0) {
                                mAdatper.addItem(0, new CommonNoDataItem(R.drawable.no_images_schedules, "暂无上课照片"));
                            } else {
                                mAdatper.setEndlessScrollListener(CourseImagesFragment.this, new ProgressItem(getContext()));
                                mAdatper.setEndlessScrollThreshold(1);//Default=1
                                for (int i = 0; i < qcResponseSchedulePhotos.data.schedules.size(); i++) {
                                    SchedulePhotos schedules = qcResponseSchedulePhotos.data.schedules.get(i);
                                    AllCourseImageHeaderItem head = new AllCourseImageHeaderItem(schedules);
                                    if (schedules.getPhotos() == null || schedules.getPhotos().size() == 0) {
                                        mDatas.add(head);
                                    } else {
                                        head.photoSize = schedules.getPhotos().size();
                                        mDatas.add(head);
                                        for (int j = 0; j < schedules.getPhotos().size(); j++) {
                                            SchedulePhoto photo = schedules.getPhotos().get(j);
                                            mDatas.add(new AllCourseImageItem(photo, j == 0 ? head : null));
                                        }
                                    }
                                }
                                mAdatper.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));

        RxBusAdd(CourseImageManageEvent.class).subscribe(new Action1<CourseImageManageEvent>() {
            @Override public void call(CourseImageManageEvent courseImageManageEvent) {
                if (mAdatper.getItem(courseImageManageEvent.pos) instanceof AllCourseImageHeaderItem) {

                    String url = ((AllCourseImageHeaderItem) mAdatper.getItem(courseImageManageEvent.pos)).schedulePhotos.getUrl();
                    WebActivity.startWeb(url, getContext());
                }
            }
        });
        RxBusAdd(AllCourseImageItem.class).subscribe(new Action1<AllCourseImageItem>() {
            @Override public void call(AllCourseImageItem allCourseImageItem) {
                getFragmentManager().beginTransaction()
                    .add(mCallbackActivity.getFragId(), CourseImageViewFragment.newInstance(allCourseImageItem.schedulePhoto))
                    .addToBackStack(getFragmentName())
                    .commit();
            }
        });

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("全部课程照片");
    }

    @Override public String getFragmentName() {
        return CourseImagesFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        //        if (mAdatper.getItem(position) instanceof AllCourseImageItem){
        //            getFragmentManager().beginTransaction()
        //                    .replace(mCallbackActivity.getFragId(),CourseImageViewFragment.newInstance(((AllCourseImageItem) mAdatper.getItem(position)).schedulePhoto))
        //                    .addToBackStack(getFragmentName())
        //                    .commit();
        //        }
        return true;
    }

    public void onLoadMore() {
        page++;

        if (page > totalPage) {
            mAdatper.removeItem(mAdatper.getItemCount() - 1);
            mAdatper.onLoadMoreComplete(null);
            return;
        }
        RxRegiste(restRepository.getGet_api()
            .qcGetSchedulePhotos(App.staffId, getArguments().getString("courseid"), page, gymWrapper.getParams())
            .delay(1, TimeUnit.SECONDS).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(new Action1<QcResponseSchedulePhotos>() {
                @Override public void call(final QcResponseSchedulePhotos qcResponseSchedulePhotos) {
                    if (ResponseConstant.checkSuccess(qcResponseSchedulePhotos)) {
                        totalPage = qcResponseSchedulePhotos.data.pages;
                        if (qcResponseSchedulePhotos.data.schedules != null) {
                            if (qcResponseSchedulePhotos.data.schedules.size() == 0) {
                                mAdatper.onLoadMoreComplete(null);
                            } else {

                                List<AbstractFlexibleItem> addItems = new ArrayList<AbstractFlexibleItem>();
                                for (int i = 0; i < qcResponseSchedulePhotos.data.schedules.size(); i++) {
                                    SchedulePhotos schedules = qcResponseSchedulePhotos.data.schedules.get(i);
                                    AllCourseImageHeaderItem head = new AllCourseImageHeaderItem(schedules);
                                    if (schedules.getPhotos() == null || schedules.getPhotos().size() == 0) {
                                        addItems.add(head);
                                        //                                            addItems.add(new AllCourseEmptyItem(head));
                                    } else {
                                        head.photoSize = schedules.getPhotos().size();
                                        addItems.add(head);
                                        for (int j = 0; j < schedules.getPhotos().size(); j++) {
                                            SchedulePhoto photo = schedules.getPhotos().get(j);
                                            addItems.add(new AllCourseImageItem(photo, j == 0 ? head : null));
                                        }
                                    }
                                }
                                mAdatper.removeItem(mAdatper.getItemCount() - 1);
                                mAdatper.onLoadMoreComplete(addItems);
                            }
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    @Override public void noMoreLoad(int i) {

    }

    @Override public void onLoadMore(int i, int i1) {
        onLoadMore();
    }
}