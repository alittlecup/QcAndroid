package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentSignInTimePayOnceBinding;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimePayOnceEvent;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.MoneyValueFilter;

/**
 * @author huangbaole
 */
public class SignInTimePayOnceFragment extends SaasCommonFragment {
  FragmentSignInTimePayOnceBinding mBinding;
  MutableLiveData<Pair<Boolean, Float>> datas = new MutableLiveData<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentSignInTimePayOnceBinding.inflate(inflater, container, false);
    ToolbarModel toolbarModel = new ToolbarModel("入场时间");
    toolbarModel.setMenu(R.menu.menu_save);
    toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem menuItem) {
        String content = mBinding.civPayCount.getContent();
        boolean expanded = mBinding.swOpen.isExpanded();
        if (expanded) {
          if (TextUtils.isEmpty(content)) {
            ToastUtils.show("请输入单次支付价格");
            return false;
          } else {
            RxBus.getBus().post(new SignInTimePayOnceEvent(true, Float.valueOf(content)));
          }
        } else {
          RxBus.getBus().post(new SignInTimePayOnceEvent(false));
        }
        getActivity().onBackPressed();

        return false;
      }
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.swOpen.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mBinding.civPayCount.setEnable(true);
        mBinding.civPayCount.setCanClick(false);
        mBinding.civPayCount.setEditable(true);
      } else {
        mBinding.civPayCount.setContent("");
        mBinding.civPayCount.setEditable(false);
        mBinding.civPayCount.setCanClick(true);
        mBinding.civPayCount.setEnable(false);
      }
    });
    datas.observe(this, data -> {
      mBinding.swOpen.setExpanded(data.first);
      if (data.first) {
        mBinding.civPayCount.setContent(String.valueOf(data.second));
      }
    });
    mBinding.civPayCount.getEditText().setFilters(new InputFilter[] { new MoneyValueFilter() });
    return mBinding.getRoot();
  }

  public void setOncePay(boolean isOpen, float price) {
    datas.setValue(new Pair<>(isOpen, price));
  }
}
