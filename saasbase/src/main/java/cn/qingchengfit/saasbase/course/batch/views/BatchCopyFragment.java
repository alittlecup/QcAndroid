package cn.qingchengfit.saasbase.course.batch.views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCopyCoach;
import cn.qingchengfit.saasbase.course.batch.event.BatchCoachEvent;
import cn.qingchengfit.saasbase.course.batch.event.CourseTypeEvent;
import cn.qingchengfit.saasbase.course.batch.viewmodel.BatchCopyViewModel;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.databinding.FragmentCopyBatchBinding;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2018/4/4.
 */

@Leaf(module = "course", path = "/batch/copy/")
public class BatchCopyFragment extends SaasBaseFragment{

  @Inject ViewModelProvider.Factory viewModelFactory;
  private BatchCopyViewModel viewModel;
  protected FragmentCopyBatchBinding binding;
  @Need public Boolean isPrivate;
  @Need public CourseType course;
  @Need public BatchCopyCoach coach;


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_copy_batch, container, false);
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(BatchCopyViewModel.class);
    viewModel.setPrivate(isPrivate);
    viewModel.isCopySuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        if (viewModel.isCopySuccess.get()){
          ToastUtils.show(R.drawable.vd_hook_white, "复制成功");
          popBack();
        }
      }
    });
    //binding.inputCopyEnd.setOnEditNoClick(false);
    if (course != null) {
      viewModel.setCourseCategory(course);
      viewModel.setFirstCourseHave(true);
      binding.inputCourse.setOnEditNoClick(false);
      //binding.inputCourse.setEnable(false);
    }
    if (coach != null) {
      viewModel.setCoachValue(coach);
      viewModel.setFirstCoachHave(true);
      binding.inputCoach.setOnEditNoClick(false);
      //binding.inputCoach.setEnable(false);
    }
    binding.setCopyViewModel(viewModel);
    initToolbar(binding.includeLayoutToolbar.toolbar);
    initView();
    initBus();
    return binding.getRoot();
  }

  private void initView(){
    binding.getCopyViewModel().coach.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        if (binding.getCopyViewModel().coach.get() == null){
          binding.inputCoach.setContent(getResources().getString(R.string.text_coach_copy_batch_error));
          //binding.inputCoach.setOnEditNoClick(false);
          //binding.inputCoach.setEnable(false);
        }
      }
    });
    binding.getCopyViewModel().courseValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        if (binding.getCopyViewModel().courseValue.get() == null){
          binding.inputCourse.setContent(getResources().getString(R.string.text_course_copy_batch_error));
          //binding.inputCourse.setOnEditNoClick(false);
          //binding.inputCourse.setEnable(false);
        }
      }
    });
    binding.getCopyViewModel().isLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        if (binding.getCopyViewModel().isLoading.get()){
          if (getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).showLoading();
          }
        }else{
          if (getActivity() instanceof BaseActivity){
            ((BaseActivity)getActivity()).hideLoading();
          }
        }
      }
    });
  }

  private void initBus(){
    RxRegiste(RxBus.getBus().register(BatchCoachEvent.class).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe(batchCoachEvent -> {
          if (batchCoachEvent != null) {
            viewModel.setCoachValue(batchCoachEvent.getCoach());
            viewModel.setIs_coach_all(batchCoachEvent.getCoach().getId().equals("0"));
          }
        }));

    RxRegiste(RxBus.getBus().register(CourseTypeEvent.class).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe(courseEvent -> {
      if (courseEvent != null) {
        viewModel.setCourseCategory(courseEvent.getCourseType());
        viewModel.setIs_course_all(courseEvent.getCourseType().getId().equals("0"));
      }
    }));
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    binding.setToolbarModel(new ToolbarModel.Builder().title("复制排期").build());
  }
}
