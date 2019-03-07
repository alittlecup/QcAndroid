package cn.qingchengfit.staffkit.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staff.routers.StaffParamsInjector;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.databinding.FragmentGymServiceSettingBinding;
import cn.qingchengfit.staffkit.views.gym.items.GymServiceSettingItem;
import cn.qingchengfit.staffkit.views.signin.SignInActivity;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "staff", path = "/gym/setting") public class GymServiceSettingFragment
    extends SaasCommonFragment implements FlexibleAdapter.OnItemClickListener {
  FragmentGymServiceSettingBinding mBinding;
  CommonFlexAdapter adapter;
  @Need ArrayList<Integer> types;
  @Inject StaffRespository restRepository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentGymServiceSettingBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    if (types != null && !types.isEmpty()) {
      List<GymServiceSettingItem> items = new ArrayList<>();
      for (int type : types) {
        items.add(new GymServiceSettingItem(type));
      }
      adapter.updateDataSet(items);
    }
    return mBinding.getRoot();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    SaasbaseParamsInjector.inject(this);
    StaffParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("服务设置");
    toolbarModel.setMenu(cn.qingchengfit.staffkit.R.menu.menu_skip);
    toolbarModel.setListener(item -> {
      skipSetting();
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  private void skipSetting() {
    showLoading();
    RxRegiste(restRepository.getStaffAllApi()
        .qcPutGymSetting(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .doOnTerminate(this::hideLoading)
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            getActivity().onBackPressed();
          } else {
            ToastUtils.show(response.getMsg());
          }
        }));
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof GymServiceSettingItem) {
      if (!adapter.isSelected(position)) {
        int type = ((GymServiceSettingItem) item).getType();
        switch (type) {
          case 1:
            routeTo("course", "/batch/add/",
                new BundleBuilder().withBoolean("isGroup", true).build());
            break;
          case 2:
            routeTo("course", "/batch/add/", null);
            break;
          case 3:
            Intent toSignInConfigs = new Intent(getContext(), SignInActivity.class);
            toSignInConfigs.setAction(getContext().getString(R.string.qc_action));
            toSignInConfigs.setData(Uri.parse("checkin/config"));
            toSignInConfigs.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toSignInConfigs);
            break;
          case 4:
            routeTo("shop", "/product/add", null);
            break;
        }
      }
    }
    return false;
  }
}
