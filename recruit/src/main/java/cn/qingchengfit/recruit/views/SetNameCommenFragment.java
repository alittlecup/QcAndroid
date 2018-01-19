package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.databinding.FragmentSetNameCommonBinding;
import cn.qingchengfit.recruit.event.EventSetName;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by fb on 2017/7/3.
 */

public class SetNameCommenFragment extends BaseFragment {

  //@BindView(R2.id.toolbar) Toolbar toolbar;
  //@BindView(R2.id.toolbar_title) TextView toolbarTitle;
  //@BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  //@BindView(R2.id.edit_group_name) EditText editGroupName;
  //@BindView(R2.id.image_clear_name) ImageView imageClearName;
  FragmentSetNameCommonBinding db;
  public static SetNameCommenFragment newInstance(String title, String txt, String hint) {
    Bundle args = new Bundle();
    args.putString("toolbarTitle", title);
    args.putString("hint", hint);
    args.putString("txt", txt);
    SetNameCommenFragment fragment = new SetNameCommenFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    //View view = inflater.inflate(R.layout.fragment_set_name_common, container, false);
    db = FragmentSetNameCommonBinding.inflate(inflater);
    setBackPress();
    setToolbar();
    initEdit();
    db.imageClearName.setOnClickListener(view -> onClearContent());
    return db.getRoot();
  }

  private void setToolbar() {
    initToolbar(db.layoutToolbar.toolbar);
    ToolbarModel tbm = new ToolbarModel(getArguments() != null ? getArguments().getString("toolbarTitle") : "");
    tbm.setMenu(R.menu.menu_save);
    tbm.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (db.editGroupName.getText().toString().equals("")) {
          DialogUtils.showAlert(getContext(), "请填写职位名称");
          return false;
        } else {
          RxBus.getBus().post(new EventSetName(db.editGroupName.getText().toString()));
          getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
        return false;
      }
    });
    db.setToolbarModel(tbm);
  }

  @Override public boolean onFragmentBackPress() {
    if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("txt"))) {
      DialogUtils.instanceDelDialog(getContext(), "确定放弃所做修改？",
          new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              dialog.dismiss();
              getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
          }).show();
      return true;
    } else {
      return false;
    }
  }

  public void onClearContent() {
    db.editGroupName.setText("");
    initEdit();
  }

  @Override protected void onFinishAnimation() {
    db.editGroupName.requestFocus();
    AppUtils.showKeyboard(getContext(),db.editGroupName);
  }

  private void initEdit() {
    db.editGroupName.setHint(getArguments() != null ? getArguments().getString("hint") : "");
    db.editGroupName.setText(getArguments() != null ? getArguments().getString("txt") : "");
    db.editGroupName.setHintTextColor(ContextCompat.getColor(getContext(), R.color.qc_text_grey));
    db.imageClearName.setVisibility(View.GONE);
    db.editGroupName.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void afterTextChanged(Editable editable) {
        if (!TextUtils.isEmpty(editable.toString().trim())) {
          db.imageClearName.setVisibility(View.VISIBLE);
        } else {
          db.imageClearName.setVisibility(View.GONE);
        }
      }
    });
  }
}
