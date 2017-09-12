package cn.qingchengfit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventShareFun;
import cn.qingchengfit.items.ShareDialogExtendsItem;
import cn.qingchengfit.model.common.ShareBean;
import cn.qingchengfit.model.common.ShareExtends;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/9/11.
 */

public class ShareDialogWithExtendFragment extends ShareDialogFragment
    implements ShareDialogExtendLayout.OnClickExtendListener {

  private ShareBean shareBean;
  private CommonFlexAdapter adapter;
  private List<ShareDialogExtendsItem> itemList = new ArrayList<>();

  public static ShareDialogWithExtendFragment newInstance(ShareBean shareBean) {
    Bundle args = new Bundle();
    args.putParcelable("share", shareBean);
    ShareDialogWithExtendFragment fragment = new ShareDialogWithExtendFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      shareBean = getArguments().getParcelable("share");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    layoutExtends.setVisibility(View.VISIBLE);
    initView();
    return view;
  }

  private void initView() {
    if (itemList.size() > 0) {
      itemList.clear();
    }
    if (shareBean.extra == null) {
      return;
    }
    for (int i = 0; i < shareBean.extra.size(); i++) {
      ShareDialogExtendLayout extendLayout = new ShareDialogExtendLayout(getContext());
      extendLayout.setExtend(getContext(), shareBean.extra.get(i));
      extendLayout.setListener(this);
      layoutExtends.setDividerDrawable(
          getResources().getDrawable(R.drawable.bg_rect_square_divider_grey_1dp));
      layoutExtends.addView(extendLayout, i);
    }
    layoutExtends.postInvalidate();
    //for (ShareExtends shareExtends : shareBean.extra){
    //  //litemList.add(new ShareDialogExtendsItem(shareExtends, this));
    //}
    //adapter = new CommonFlexAdapter(itemList, this);
    //recyclerShareExtends.setLayoutManager(new LinearLayoutManager(getContext()));
    //recyclerShareExtends.addItemDecoration(new FlexibleItemDecoration(getContext()).withDivider(
    //    R.drawable.divider_grey_left_margin).withBottomEdge(true));
    //recyclerShareExtends.setAdapter(adapter);
  }

  @Override public void onSelectExtend(ShareExtends key) {
    RxBus.getBus().post(new EventShareFun(key));
    dismiss();
  }
}
