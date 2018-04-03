package cn.qingchengfit.shop.ui.product.productdetail;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.PageShopProductDetailBinding;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.views.fragments.RichTxtFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

/**
 * Created by huangbaole on 2018/3/15.
 */
@Leaf(module = "shop", path = "/product/detail") public class ShopProductDetailPage
    extends SaasBaseFragment {
  RichTxtFragment richTxtFragment;
  PageShopProductDetailBinding mBinding;
  public ChoosePictureFragmentNewDialog choosePictureFragmentDialog;
  @Need String content;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding =
        DataBindingUtil.inflate(inflater, R.layout.page_shop_product_detail, container, false);
    richTxtFragment = new RichTxtFragment();
    getChildFragmentManager().beginTransaction().replace(R.id.frag_desc, richTxtFragment).commit();
    initToolbar();

    initOnClick();
    return mBinding.getRoot();
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RichTxtFragment) {
      richTxtFragment.initContent(content, getString(R.string.input_product_desc));
    }
  }

  private void initOnClick() {
    mBinding.btnInsertImg.setOnClickListener(v -> {
      if (choosePictureFragmentDialog == null) {
        choosePictureFragmentDialog = ChoosePictureFragmentNewDialog.newInstance();
      }
      choosePictureFragmentDialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
        @Override public void onChoosefile(String filePath) {

        }

        @Override public void onUploadComplete(String sp, String url) {
          richTxtFragment.insertImg(url);
        }
      });
      choosePictureFragmentDialog.show(getChildFragmentManager(), "");
    });
    mBinding.btnComfirm.setOnClickListener(v -> {
      if (richTxtFragment != null) {
        sendBack();
      }
    });
  }

  private void sendBack() {
    String content = richTxtFragment.getContent();
    Intent intent = new Intent();
    intent.putExtra("detail", content);
    getActivity().setResult(Activity.RESULT_OK, intent);
    getActivity().onBackPressed();
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.product_desc_detail)));
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    initToolbar(toolbar);
    toolbar.setNavigationOnClickListener(v -> {
      if (!TextUtils.isEmpty(content) && TextUtils.isEmpty(richTxtFragment.getContent())) {
        ViewUtil.instanceDelDialog(getContext(), getString(R.string.sure_clear_cur_content), (dialog, which) ->sendBack(),(dialog,which)->getActivity().onBackPressed()).show();
      } else {
        ViewUtil.instanceDelDialog(getContext(), getString(R.string.sure_give_up_modify),
            (dialog, which) -> getActivity().onBackPressed()).show();
      }
    });
  }
}
