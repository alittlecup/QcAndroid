package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.staff.model.body.BatchPayResponse;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentSignInTimePayOnceBinding;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimePayOnceEvent;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.MoneyValueFilter;
import com.bigkoo.pickerview.lib.DensityUtil;

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
          } else if (Float.valueOf(content) == 0) {
            ToastUtils.show("单次支付价格应大于0元");
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

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    updatePayMethod(data);
  }

  public void setOncePay(boolean isOpen, float price) {
    datas.setValue(new Pair<>(isOpen, price));
  }

  private BatchPayResponse data = new BatchPayResponse();

  public void setPayMethos(BatchPayResponse response) {
    this.data = response;
  }

  private void updatePayMethod(BatchPayResponse payResponse) {
    if (payResponse == null) {
      payResponse = new BatchPayResponse();
    }
    int size = DensityUtil.dip2px(getContext(), 16);
    TextView aliSPPayText = new TextView(getContext());
    aliSPPayText.setText("全民健身卡");
    Drawable alispDrawable = getResources().getDrawable(
        payResponse.alisp ? cn.qingchengfit.saasbase.R.drawable.icon_alisp_circle_enable
            : cn.qingchengfit.saasbase.R.drawable.icon_alisp_circle_disable);
    alispDrawable.setBounds(0, 0, size, size);
    aliSPPayText.setCompoundDrawables(alispDrawable, null, null, null);
    aliSPPayText.setCompoundDrawablePadding(DensityUtil.dip2px(getContext(),5));

    TextView corpPayText = new TextView(getContext());
    corpPayText.setText("企业健身卡");
    Drawable corpDrawable = getResources().getDrawable(
        payResponse.corpCard ? cn.qingchengfit.saasbase.R.drawable.icon_corp_circle
            : cn.qingchengfit.saasbase.R.drawable.icon_crop_circle_disable);
    corpDrawable.setBounds(0, 0, size, size);
    corpPayText.setCompoundDrawables(corpDrawable, null, null, null);
    corpPayText.setCompoundDrawablePadding(DensityUtil.dip2px(getContext(),5));

    ViewGroup.LayoutParams layoutParams = mBinding.tvWxPay.getLayoutParams();
    ViewParent parent = mBinding.tvWxPay.getParent();
    if (parent instanceof LinearLayout) {
      ((LinearLayout) parent).addView(aliSPPayText, 2, layoutParams);
      ((LinearLayout) parent).addView(corpPayText, 3, layoutParams);
    }
  }
}
