package cn.qingchengfit.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.R;

/**
 * Created by huangbaole on 2018/4/4.
 */

public class CommonInputLimitFragment extends BaseFragment {

  private Toolbar toolbar;
  private EditText contentEdit;

  public static CommonInputLimitFragment newInstance(@NonNull String title, String hint,
      String content, Integer limit) {
    CommonInputLimitFragment fragment = new CommonInputLimitFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", title);
    bundle.putString("hint", hint);
    bundle.putString("content", content);
    if (limit != null) {
      bundle.putInt("limit", limit);
    }
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View root = inflater.inflate(R.layout.common_input_limit_fragment, container, false);
    TextView title = root.findViewById(R.id.toolbar_title);
    ImageView imgClear = root.findViewById(R.id.img_clear);
    contentEdit = root.findViewById(R.id.content_edit);
    TextView txtLimit = root.findViewById(R.id.txt_limit);

    toolbar = root.findViewById(R.id.toolbar);

    if (getArguments() != null) {
      String titleStr = getArguments().getString("title", "");
      String hint = getArguments().getString("hint", "");
      String content = getArguments().getString("content", "");
      int limit = getArguments().getInt("limit", Integer.MAX_VALUE);

      title.setText(titleStr);

      contentEdit.setHint(hint);
      if (!TextUtils.isEmpty(content)) {
        contentEdit.setText(content);
        imgClear.setVisibility(View.VISIBLE);
      }
      if (limit != Integer.MAX_VALUE && limit != 0) {
        txtLimit.setVisibility(View.VISIBLE);
        txtLimit.setText("0/" + limit);
      } else {
        txtLimit.setVisibility(View.GONE);
      }
      contentEdit.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
          String trim = s.toString().trim();
          if (!TextUtils.isEmpty(trim)) {
            if (trim.length() > limit) {
              contentEdit.setText(trim.substring(0, limit));
              contentEdit.setSelection(contentEdit.getText().length());
            } else {
              txtLimit.setText(trim.length() + "/" + limit);
            }
            imgClear.setVisibility(View.VISIBLE);
            setToolbarEnable(true);
          } else {
            txtLimit.setText("0/" + limit);
            imgClear.setVisibility(View.INVISIBLE);
            setToolbarEnable(false);
          }
        }
      });
    }
    imgClear.setOnClickListener(v -> contentEdit.setText(""));

    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });

    toolbar.inflateMenu(R.menu.menu_compelete);

    setToolbarEnable(false);

    toolbar.setOnMenuItemClickListener(item -> {
      RxBus.getBus()
          .post(new EventTxT.Builder().txt(contentEdit.getText().toString().trim()).build());
      setBackPressNull();
      getActivity().onBackPressed();
      return false;
    });
    initToolbar(toolbar);
    setBackPress();
    contentEdit.post(new Runnable() {
      @Override public void run() {
        contentEdit.requestFocus();
        AppUtils.showKeyboard(getContext(), contentEdit);
      }
    });
    return root;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public boolean onFragmentBackPress() {
    DialogUtils.showConfirm(getContext(), "", "确定放弃修改吗？", (materialDialog, dialogAction) -> {
      switch (dialogAction) {
        case POSITIVE:
          setBackPressNull();
          getActivity().onBackPressed();
          break;
      }
      materialDialog.dismiss();
    });
    return ((BaseActivity) getActivity()).getBackPress() != null;
  }

  private void setToolbarEnable(boolean enable) {

    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("完成");
    spannableStringBuilder.setSpan(new ForegroundColorSpan(
            enable ? getResources().getColor(R.color.colorPrimary) : Color.argb(123, 0, 206, 216)), 0,
        2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    toolbar.getMenu().getItem(0).setTitle(spannableStringBuilder);
    toolbar.getMenu().getItem(0).setEnabled(enable);
  }
}

