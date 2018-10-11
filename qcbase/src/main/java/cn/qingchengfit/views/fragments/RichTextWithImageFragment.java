package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.R;

/**
 * Created by huangbaole on 2018/4/17.
 */

public class RichTextWithImageFragment extends BaseFragment {
  RichTxtFragment richTxtFragment;
  ChoosePictureFragmentNewDialog choosePictureFragmentDialog;

  Toolbar toolbar;
  TextView title;
  String hint = "";
  String content = "";
  TextView btnConfirm;

  public static RichTextWithImageFragment newInstance(String title, String hint, String content) {
    RichTextWithImageFragment fragment = new RichTextWithImageFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", title);
    bundle.putString("hint", hint);
    bundle.putString("content", content);
    fragment.setArguments(bundle);
    return fragment;
  }


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    richTxtFragment = new RichTxtFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View root = inflater.inflate(R.layout.fragment_rich_text_image, container, false);
    toolbar = root.findViewById(R.id.toolbar);
    title = root.findViewById(R.id.toolbar_title);
    initToolbar(toolbar);
    if (getArguments() != null) {
      String title = getArguments().getString("title");
      hint = getArguments().getString("hint");
      this.title.setText(title);
      content = getArguments().getString("content");
    }
    root.findViewById(R.id.btn_insert_img).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (choosePictureFragmentDialog == null) {
          choosePictureFragmentDialog = ChoosePictureFragmentNewDialog.newInstance();
        }
        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
          @Override public void onChoosefile(String filePath) {

          }

          @Override public void onUploadComplete(String filePaht, String url) {
            if (richTxtFragment != null) {
              richTxtFragment.insertImg(url);
            }
          }
        });
        choosePictureFragmentDialog.show(getChildFragmentManager(), "");
      }
    });
    btnConfirm = root.findViewById(R.id.btn_confirm);
    btnConfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (richTxtFragment != null) {
          String content = richTxtFragment.getContent();
          setBackPressNull();
          getActivity().getSupportFragmentManager().popBackStackImmediate();
          RxBus.getBus().post(RichTextWithImageFragment.class, content);
        }
      }
    });
    btnConfirm.setEnabled(false);
    setBackPress();
    return root;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(richTxtFragment);
  }

  @Override public int getLayoutRes() {
    return R.id.frag_desc;
  }

  @Override public String getFragmentName() {
    return RichTextWithImageFragment.class.getName();
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RichTxtFragment) {
      if (TextUtils.isEmpty(hint)) {
        hint = "请填写描述内容";
      }
      richTxtFragment.initContent(content, hint);
      richTxtFragment.setChangeListenter(content -> {
        btnConfirm.setEnabled(!TextUtils.equals(this.content, content));
      });
    }
  }

  @Override public boolean onFragmentBackPress() {
    if (!TextUtils.isEmpty(richTxtFragment.getContent())) {
      DialogUtils.showConfirm(getContext(), "", "确定放弃所做修改？", (dialog, which) -> {
        dialog.dismiss();
        setBackPressNull();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
      });
      return true;
    } else {
      return false;
    }
  }
}
