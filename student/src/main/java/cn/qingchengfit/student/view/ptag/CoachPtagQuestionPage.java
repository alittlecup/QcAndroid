package cn.qingchengfit.student.view.ptag;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.PtagQuestion;
import cn.qingchengfit.student.databinding.StPageCoachPtagQuestionBinding;
import cn.qingchengfit.student.event.DynamicPtagItemEvent;
import cn.qingchengfit.student.item.CoachPtagItem;
import cn.qingchengfit.student.routers.StudentParamsInjector;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 问卷内容页面
 */
@Leaf(module = "student", path = "/coach/ptag/question") public class CoachPtagQuestionPage
    extends SaasBindingFragment<StPageCoachPtagQuestionBinding, CoachPtagQuestionVM>
    implements Toolbar.OnMenuItemClickListener {

  @Inject GymWrapper gymWrapper;
  @Need String type;
  @Need String userId;
  @Need String naireId;

  @Override protected void subscribeUI() {
    StudentParamsInjector.inject(this);

    mViewModel.getErrorMsg().observe(this, str -> ToastUtils.show(str));

    mViewModel.getLiveItems().observe(this, coachPtagItems -> {
      if (coachPtagItems == null || coachPtagItems.size() == 0) {
        List<AbstractFlexibleItem> items = new ArrayList<>();
        //items.add(new CommonNoDataItem())
        return;
      }


      Iterator<CoachPtagItem> itemIterator = coachPtagItems.iterator();
      while (itemIterator.hasNext()) {
        PtagQuestion question = itemIterator.next().getData();
        if (!question.isShow()) {
          itemIterator.remove();
        }
      }
      mViewModel.items.set(new ArrayList<>(coachPtagItems));
    });

    mViewModel.isLoading.observe(this, aBoolean -> {
      if (aBoolean != null && !aBoolean) {
        hideLoading();
      }
    });
    mViewModel.data.observe(this, data -> {
      if (mBinding != null) {
        mBinding.setToolbarModel(new ToolbarModel.Builder().title(data.getTitle())
            .menu(R.menu.st_menu_pdf)
            .listener(this)
            .build());
        initToolbar(mBinding.includeToolbar.toolbar);
      }
    });
    mViewModel.defaultResult.observe(this, this::dealResource);
    mViewModel.isWarnning.observe(this, aBoolean -> {
      if (aBoolean != null && aBoolean) {
        DialogUtils.showAlert(getContext(), "请将问卷填写完整");
      }
    });
  }

  //根据选项更改题目
  private void initRxbus() {
    RxRegiste(RxBus.getBus()
        .register(DynamicPtagItemEvent.class)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(dynamicPtagItemEvent -> {
          mViewModel.dynamicNotifyItem(dynamicPtagItemEvent);
        }));
  }

  public <T> void dealResource(Resource<T> resource) {
    switch (resource.status) {
      case SUCCESS:
        if (resource.data instanceof String && !TextUtils.isEmpty((String) resource.data)) {
          ToastUtils.show("保存成功");
          getActivity().onBackPressed();
        }
        break;
      case ERROR:
        ToastUtils.show(resource.message);
        break;
      case LOADING:
        break;
    }
  }

  private void initRecyclerView() {
    FlexibleAdapter adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerCoachPtagQuestion.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerCoachPtagQuestion.setAdapter(adapter);
  }

  @Override public StPageCoachPtagQuestionBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = StPageCoachPtagQuestionBinding.inflate(inflater, container, false);
    mBinding.setModel(mViewModel);
    initRecyclerView();
    initRxbus();
    showLoading();
    return mBinding;
  }

  @Override public void onResume() {
    super.onResume();
    HashMap<String, Object> params = new HashMap<>();
    if (userId != null && !userId.isEmpty()) {
      params.put("question_naire_type", type);
      params.put("user_id", userId);
    } else if (naireId != null && !naireId.isEmpty()){
      params.put("naire_id", naireId);
    }else{
      params.put("type", type);
    }
    mViewModel.loadSource(params);
  }

  @Override public boolean onMenuItemClick(MenuItem item) {
    WebActivity.startWeb(Constants.PTAG_DOWNLOAD_PDF, getContext());
    return false;
  }
}
