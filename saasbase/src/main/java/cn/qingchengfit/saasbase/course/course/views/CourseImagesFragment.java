package cn.qingchengfit.saasbase.course.course.views;

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
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhotos;
import cn.qingchengfit.saasbase.course.course.items.AllCourseImageHeaderItem;
import cn.qingchengfit.saasbase.course.course.items.AllCourseImageItem;
import cn.qingchengfit.saasbase.events.CourseImageManageEvent;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
@Leaf(module = "course",path = "/image/")
public class CourseImagesFragment extends SaasBaseFragment
  implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {

  @BindView(R2.id.recyclerview) RecyclerView recyclerview;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;

  @Inject ICourseModel courseModel;

  @Need public String course_id;

  CommonFlexAdapter<IFlexible> mAdatper;
  List<AbstractFlexibleItem> mDatas = new ArrayList<>();
  private int page = 1;
  private int totalPage = 1;

  public static CourseImagesFragment newInstance(String courseid) {

    Bundle args = new Bundle();
    args.putString("courseid", courseid);
    CourseImagesFragment fragment = new CourseImagesFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_all_course_images, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    //init recycle
    mAdatper = new CommonFlexAdapter<>(mDatas, this);
    mAdatper.setMode(SelectableAdapter.Mode.SINGLE);
    mAdatper.setDisplayHeadersAtStartUp(true);

    SmoothScrollGridLayoutManager layoutManager =
      new SmoothScrollGridLayoutManager(getContext(), 3);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        int i = mAdatper.getItemViewType(position);
        if (i == R.layout.item_all_course_image_view) {
          return 1;
        } else {
          return 3;
        }
      }
    });
    recyclerview.setLayoutManager(layoutManager);

    recyclerview.setAdapter(mAdatper);
    recyclerview.setHasFixedSize(true);

    RxRegiste(courseModel.qcGetSchedulePhotos(course_id, page)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(qcResponseSchedulePhotos -> {
        if (ResponseConstant.checkSuccess(qcResponseSchedulePhotos)) {
          totalPage = qcResponseSchedulePhotos.data.pages;
          if (qcResponseSchedulePhotos.data.schedules != null) {

            if (qcResponseSchedulePhotos.data.schedules.size() == 0) {
              mAdatper.addItem(0, new CommonNoDataItem(R.drawable.no_images_schedules, "暂无上课照片"));
            } else {
              mAdatper.setEndlessScrollListener(CourseImagesFragment.this,
                new ProgressItem(getContext()));
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
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {

        }
      }));

    RxBusAdd(CourseImageManageEvent.class).subscribe(new Action1<CourseImageManageEvent>() {
      @Override public void call(CourseImageManageEvent courseImageManageEvent) {
        if (mAdatper.getItem(courseImageManageEvent.pos) instanceof AllCourseImageHeaderItem) {
          String url =
            ((AllCourseImageHeaderItem) mAdatper.getItem(courseImageManageEvent.pos)).schedulePhotos
              .getUrl();
          WebActivity.startWeb(url, getContext());
        }
      }
    });
    RxBusAdd(AllCourseImageItem.class).subscribe(new Action1<AllCourseImageItem>() {
      @Override public void call(AllCourseImageItem allCourseImageItem) {
        // TODO: 2017/11/30 图片预览
        //getFragmentManager().beginTransaction()
        //  .add(mCallbackActivity.getFragId(),
        //    CourseImageViewFragment.newInstance(allCourseImageItem.schedulePhoto))
        //  .addToBackStack(getFragmentName())
        //  .commit();
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
    RxRegiste(courseModel
      .qcGetSchedulePhotos( course_id, page)
      .delay(1, TimeUnit.SECONDS)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(qcResponseSchedulePhotos -> {
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
