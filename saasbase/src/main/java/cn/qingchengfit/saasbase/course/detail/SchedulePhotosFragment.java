package cn.qingchengfit.saasbase.course.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import cn.qingchengfit.saasbase.databinding.FragmentSchedulePhotosBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.MultiChoosePicFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "course", path = "/schedule/photos") public class SchedulePhotosFragment
    extends SaasBindingFragment<FragmentSchedulePhotosBinding, ScheduleDetailVM>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need SchedulePhotos photos;
  @Need String scheduleID;
  @Need Boolean cancel;

  @Override protected void subscribeUI() {
    mViewModel.detailPhotos.observe(this, this::updatePhotos);
    mViewModel.delPhotoResult.observe(this, aBoolean -> {
      ToastUtils.show(aBoolean ? "删除成功" : "删除失败");
      if (aBoolean) {
        mViewModel.loadCoursePhotos(scheduleID);
      }
    });
  }

  private void updatePhotos(SchedulePhotos photos) {
    if (photos == null || photos.photos == null || photos.photos.isEmpty()) {
      List<CommonNoDataItem> items = new ArrayList<>();
      items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无课程照片"));
      adapter.updateDataSet(items);
      changeSelectMode(false);
    } else {
      adapter.clearSelection();

      List<SquareImageItem> items = new ArrayList<>();
      for (SchedulePhoto photo : photos.photos) {
        items.add(new SquareImageItem(photo));
      }
      adapter.updateDataSet(items);
    }
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override
  public FragmentSchedulePhotosBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = FragmentSchedulePhotosBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    initListener();
    updatePhotos(photos);
    mViewModel.loadCoursePhotos(scheduleID);
    mViewModel.loadShopInfo();
    changeSelectMode(false);
    if(cancel){
      mBinding.flBottom.setVisibility(View.GONE);
      mBinding.setToolbarModel(new ToolbarModel("图片相册"));

    }
    return mBinding;
  }

  private void initListener() {
    mBinding.btnAction.setOnClickListener(v -> {
      int status = adapter.getStatus();
      switch (status) {
        case 0:
          routeAddPhotos();
          break;
        case 1:
          delSelectPos();
          break;
      }
    });
  }

  public void routeAddPhotos() {
    String host = mViewModel.gymWrapper.getCoachService().getHost();
    WebActivity.startWeb(host
        + "/shop/"
        + mViewModel.shopID
        + "/m/upload/photo/?type=coach&schedule_id="
        + scheduleID, getContext());
  }

  private void delSelectPos() {
    List<Integer> selectedPositions = adapter.getSelectedPositions();
    if (selectedPositions.size() > 0) {
      List<String> ids = new ArrayList<>();
      for (Integer index : selectedPositions) {
        SchedulePhoto photo = ((SquareImageItem) adapter.getItem(index)).getPhoto();
        ids.add(String.valueOf(photo.getId()));
      }
      mViewModel.delPhotos(scheduleID, StringUtils.List2Str(ids));
    } else {
      ToastUtils.show("请选择要移除的图片");
    }
  }

  @Override public void onResume() {
    super.onResume();
    mViewModel.loadCoursePhotos(scheduleID);
  }

  private void initRecyclerView() {
    SmoothScrollGridLayoutManager smoothScrollGridLayoutManager =
        new SmoothScrollGridLayoutManager(getContext(), 4);
    smoothScrollGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        IFlexible item = adapter.getItem(position);
        if (item instanceof CommonNoDataItem) {
          return 4;
        } else {
          return 1;
        }
      }
    });
    mBinding.recyclerPhotos.setLayoutManager(smoothScrollGridLayoutManager);
    mBinding.recyclerPhotos.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("图片相册");
    toolbarModel.setMenu(R.menu.menu_choose);
    toolbarModel.setListener(item -> {
      CharSequence title = item.getTitle();
      if (title.equals("选择")) {
        item.setTitle("取消");
        changeSelectMode(true);
      } else {
        item.setTitle("选择");
        changeSelectMode(false);
      }
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void changeSelectMode(boolean openSelected) {
    adapter.clearSelection();
    adapter.setStatus(openSelected ? 1 : 0);
    mBinding.btnAction.setText(openSelected ? "删除" : "立即上传");
    adapter.notifyDataSetChanged();
  }

  /**
   * 通过设置 status 来区分是展示模式还是编辑模式吧
   */
  @Override public boolean onItemClick(int position) {
    if(adapter.getItem(position) instanceof CommonNoDataItem){
      return false;
    }
    if (adapter.getStatus() == 0) {
      showMultiPhotos(position);
      return false;
    } else {
      adapter.toggleSelection(position);
      adapter.notifyItemChanged(position);
    }
    return false;
  }

  private void showMultiPhotos(int position) {
    SchedulePhoto schedulePhoto = mViewModel.detailPhotos.getValue().photos.get(position);
    List<String> url = new ArrayList<>();
    url.add(schedulePhoto.getPhoto());
    MultiChoosePicFragment fragment = MultiChoosePicFragment.newInstance(url);
    fragment.setShowFlag(true);
    fragment.show(getFragmentManager(), "");
  }
}
