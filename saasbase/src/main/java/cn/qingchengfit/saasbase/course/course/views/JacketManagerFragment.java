package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.adapter.JacketManageAdapter;
import cn.qingchengfit.saasbase.course.course.items.JackManageItem;
import cn.qingchengfit.saasbase.course.course.items.JackTitleItem;
import cn.qingchengfit.saasbase.course.course.items.JacketAddItem;
import cn.qingchengfit.saasbase.course.course.presenters.JacketManagerPresenter;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

@Leaf(module = "course", path = "/jacket/manager/") public class JacketManagerFragment
  extends SaasBaseFragment
  implements FlexibleAdapter.OnItemClickListener, JacketManagerPresenter.JacketManagerView {

  @Inject JacketManagerPresenter mPresenter;
  @BindView(R2.id.recycler_view) RecyclerView mRecyclerView;
  @BindView(R2.id.custom_switch) ExpandedLayout customSwitch;
  @BindView(R2.id.costum_jacket_hint) TextView costumJacketHint;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  private ArrayList<AbstractFlexibleItem> mDatas = new ArrayList<>();
  private JacketManageAdapter mAdapter;
  @Need public String courseid;
  @Need public ArrayList<String> stringList = new ArrayList<>();
  @Need public Boolean randomOpen ;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_jacket_manager, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(mPresenter, this);
    initToolbar(toolbar);
    mAdapter = new JacketManageAdapter(mDatas, this);
    mAdapter.addItem(0, new JackTitleItem());
    mAdapter.addItem(1, new JacketAddItem());

    SmoothScrollLinearLayoutManager manager = new SmoothScrollLinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.setHandleDragEnabled(true);
    customSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          costumJacketHint.setVisibility(View.GONE);
          mRecyclerView.setVisibility(View.VISIBLE);
        } else {
          costumJacketHint.setVisibility(View.VISIBLE);
          mRecyclerView.setVisibility(View.GONE);
        }
      }
    });
    onSwitch(randomOpen);
    mPresenter.queryJacket(courseid);

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("编辑封面照片");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        mPresenter.completeJacket(courseid, item2Str(), customSwitch.isExpanded());
        return true;
      }
    });
  }

  /**
   * 添加照片
   */
  public void onAddImg() {
    if (mAdapter.getItemCount() >= 7) {
      ToastUtils.show("最多可添加五张封面");
      return;
    }
    ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
    dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
      @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
        if (isSuccess) {
          showLoading();
          RxRegiste(UpYunClient.rxUpLoad("/jacket", filePath).subscribe(new Action1<String>() {
            @Override public void call(String s) {
              mAdapter.addItem(mAdapter.getItemCount() - 1, new JackManageItem(s));
              hideLoading();
            }
          }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
              hideLoading();
            }
          }));
        } else {

        }
      }
    });
    dialog.show(getFragmentManager(), "");
  }

  @Override public String getFragmentName() {
    return JacketManagerFragment.class.getName();
  }

  @Override public void onPause() {
    super.onPause();
  }

  private List<JackManageItem> str2Item(@NonNull List<String> strings) {
    List<JackManageItem> items = new ArrayList<>();
    for (int i = 0; i < strings.size(); i++) {
      items.add(new JackManageItem(strings.get(i)));
    }
    return items;
  }

  private List<String> item2Str() {
    List<String> ret = new ArrayList<>();
    for (int i = 0; i < mAdapter.getItemCount(); i++) {
      if (mAdapter.getItem(i) instanceof JackManageItem) {
        ret.add(((JackManageItem) mAdapter.getItem(i)).getImg());
      }
    }
    return ret;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    if (mAdapter.getItem(position) instanceof JacketAddItem) {
      onAddImg();
    }
    return false;
  }

  @Override public void setJacket(List<String> jackets) {
    for (int i = 0; i < jackets.size(); i++) {
      mAdapter.addItem(mAdapter.getItemCount() - 1, new JackManageItem(jackets.get(i)));
    }
  }

  @Override public void onSuccess() {
    getActivity().onBackPressed();
  }

  @Override public void onFaied(String s) {
    ToastUtils.show(s);
  }

  @Override public void onSwitch(boolean s) {
    customSwitch.setExpanded(!s);
  }

  @Override public void onShowError(String e) {
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }
}
