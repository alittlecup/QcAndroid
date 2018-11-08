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
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.DialogSheet;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.ImageIconBean;
import com.qingchengfit.fitcoach.adapter.ImageThreeTextBean;
import com.qingchengfit.fitcoach.adapter.SimpleTextIconAdapter;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.GetBatchesResponse;
import com.qingchengfit.fitcoach.http.bean.QcOneCourseResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDetailFragment extends Fragment {

  Toolbar toolbar;
  ImageView img;
  TextView text1;
  ImageView texticon;
  TextView text2;
  TextView text3;
  ImageView righticon;
  TextView courseCount;
  TextView preview;
  RecyclerView recyclerview;
  LinearLayout noData;
  private ImageThreeTextBean mBean;
  private SimpleTextIconAdapter simpleTextIconAdapter;
  private List<ImageIconBean> datas = new ArrayList<>();
  private DialogSheet delCourseDialog;
  private DialogSheet delBatchDialog;
  private Observable<String> mObservableRefresh;
  private MaterialDialog delDialog;
  private MaterialDialog delBatchComfirmDialog;

  public CourseDetailFragment() {
  }

  public static CourseDetailFragment newInstance(ImageThreeTextBean bean) {

    Bundle args = new Bundle();
    args.putParcelable("bean", bean);
    CourseDetailFragment fragment = new CourseDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mBean = getArguments().getParcelable("bean");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    img = (ImageView) view.findViewById(R.id.img);
    text1 = (TextView) view.findViewById(R.id.text1);
    texticon = (ImageView) view.findViewById(R.id.texticon);
    text2 = (TextView) view.findViewById(R.id.text2);
    text3 = (TextView) view.findViewById(R.id.text3);
    righticon = (ImageView) view.findViewById(R.id.righticon);
    courseCount = (TextView) view.findViewById(R.id.course_count);
    preview = (TextView) view.findViewById(R.id.preview);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    noData = (LinearLayout) view.findViewById(R.id.no_data);
    view.findViewById(R.id.preview).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onAddCouseManage();
      }
    });

    toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });
    toolbar.setTitle("课程详情");
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        showDelCourse();
        return true;
      }
    });
    Glide.with(App.AppContex).load(PhotoUtils.getSmall(mBean.imgUrl)).into(img);
    text1.setText(mBean.text1);
    text2.setText(mBean.text2);
    text3.setText(mBean.text3);

    courseCount.setText("课程安排");
    preview.setText("添加排期");
    simpleTextIconAdapter = new SimpleTextIconAdapter(datas);
    recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    recyclerview.setAdapter(simpleTextIconAdapter);
    simpleTextIconAdapter.setListener(new OnRecycleItemClickListener() {
      @Override public void onItemClick(View v, int pos) {
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
    mObservableRefresh.onBackpressureBuffer()
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(String s) {
            loadGroupData();
            HashMap<String, String> params = new HashMap<>();
            params.put("model", mBean.tags.get(ImageThreeTextBean.TAG_MODEL));
            params.put("id", mBean.tags.get(ImageThreeTextBean.TAG_ID));
            TrainerRepository.getStaticTrainerAllApi().qcGetOneCourse(App.coachid,
                mBean.tags.get(ImageThreeTextBean.TAG_COURSE), params)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcOneCourseResponse>() {
                  @Override public void onCompleted() {

                  }

                  @Override public void onError(Throwable e) {

                  }

                  @Override public void onNext(QcOneCourseResponse qcOneCourseResponse) {
                    if (qcOneCourseResponse.status == ResponseResult.SUCCESS) {
                      Glide.with(App.AppContex)
                          .load(PhotoUtils.getSmall(qcOneCourseResponse.data.course.photo))
                          .into(img);
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
        @Override public void onClick(View v) {
          //编辑课程
          delCourseDialog.dismiss();
          getFragmentManager().beginTransaction()
              .add(R.id.web_frag_layout,
                  AddCourseFrament.newInstance(2, mBean.tags.get(ImageThreeTextBean.TAG_MODEL),
                      Integer.parseInt(mBean.tags.get(ImageThreeTextBean.TAG_ID)),
                      mBean.tags.get(ImageThreeTextBean.TAG_COURSE)))
              .addToBackStack(null)
              .commit();
        }
      });
      delCourseDialog.addButton("删除", new View.OnClickListener() {
        @Override public void onClick(View v) {
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
    DialogUtils.showConfirm(getActivity(), "", "是否删除课程?", (dialog, action) -> {
      dialog.dismiss();
      if (action == DialogAction.POSITIVE) {
        deleCurses();
      }
    });
  }

  private void deleCurses() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("course_id", mBean.tags.get(ImageThreeTextBean.TAG_COURSE));
    params.put("model", mBean.tags.get(ImageThreeTextBean.TAG_MODEL));
    params.put("id", mBean.tags.get(ImageThreeTextBean.TAG_ID));


  }

  private void showDelBatch(int pos) {

    delBatchDialog = new DialogSheet(getContext());
    delBatchDialog.addButton("编辑", new View.OnClickListener() {
      @Override public void onClick(View v) {
        delBatchDialog.dismiss();
        goBatch(pos);
      }
    });
    delBatchDialog.addButton("删除", new View.OnClickListener() {
      @Override public void onClick(View v) {
        //删除排期
        DialogUtils.showConfirm(getActivity(), "", "是否删除排期?", (dialog, action) -> {
          dialog.dismiss();
          if (action == DialogAction.POSITIVE) {
            deleteBatch(pos);
          }
        });
      }
    });

    delBatchDialog.show();
  }

  private void deleteBatch(int pos) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("model", mBean.tags.get(ImageThreeTextBean.TAG_MODEL));
    params.put("id", mBean.tags.get(ImageThreeTextBean.TAG_ID));
    TrainerRepository.getStaticTrainerAllApi().qcDelBatch(App.coachid + "", datas.get(pos).id, params)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Subscriber<QcResponse>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(QcResponse qcResponse) {
            if (qcResponse.status == ResponseResult.SUCCESS) {
              ToastUtils.showDefaultStyle("删除成功");
              datas.remove(pos);
              if (datas.size() == 0) {
                noData.setVisibility(View.VISIBLE);
              }
              simpleTextIconAdapter.notifyDataSetChanged();
            }
          }
        });
  }

  public void goBatch(int pos) {
    //getFragmentManager().beginTransaction()
    //    .add(R.id.web_frag_layout,
    //        CourseManageFragment.newInstance(mBean.tags.get(ImageThreeTextBean.TAG_MODEL), mBean.tags.get(ImageThreeTextBean.TAG_ID),
    //            datas.get(pos).id, Integer.parseInt(mBean.tags.get(ImageThreeTextBean.TAG_COURSETYPE))))
    //    .addToBackStack(null)
    //    .commit();
  }

  public void loadGroupData() {
    //团课
    HashMap<String, String> params = new HashMap<>();
    params.put("model", mBean.tags.get("model"));
    params.put("id", mBean.tags.get("gymid"));
    TrainerRepository.getStaticTrainerAllApi().qcGetGroupManage(App.coachid, mBean.tags.get("courseid"), params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<GetBatchesResponse>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(GetBatchesResponse qcBatchResponse) {
            if (qcBatchResponse.status != ResponseResult.SUCCESS) {
              ToastUtils.showDefaultStyle("服务器错误");
              return;
            }
            datas.clear();
            for (GetBatchesResponse.Batch schedule : qcBatchResponse.data.batches) {
              ImageIconBean bean = new ImageIconBean(schedule.from_date + "至" + schedule.to_date,
                  R.drawable.ic_options);
              bean.id = schedule.id;
              datas.add(bean);
            }
            if (datas.size() > 0) {
              simpleTextIconAdapter.notifyDataSetChanged();
              noData.setVisibility(View.GONE);
            } else {
              noData.setVisibility(View.VISIBLE);
            }
          }
        });
  }

  /**
   * 添加课程排期
   */
  public void onAddCouseManage() {
    getFragmentManager().beginTransaction()
        .add(R.id.web_frag_layout,
            AddCourseManageFragment.newInstance(mBean.tags.get("model"), mBean.tags.get("gymid"),
                mBean.tags.get(ImageThreeTextBean.TAG_COURSE),
                Integer.parseInt(mBean.tags.get(ImageThreeTextBean.TAG_COURSETYPE)),
                mBean.tags.get("length")))
        .addToBackStack(null)
        .commit();
  }

  @Override public void onDestroyView() {
    RxBus.getBus().unregister(RxBus.BUS_REFRESH, mObservableRefresh);
    super.onDestroyView();
  }
}
