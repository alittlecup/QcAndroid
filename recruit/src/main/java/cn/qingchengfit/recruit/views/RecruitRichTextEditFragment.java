package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventRichTextBack;
import cn.qingchengfit.recruit.event.EventSetName;
import cn.qingchengfit.recruit.views.resume.ResumeEditDescFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.views.fragments.RichTxtFragment;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

/**
 * Created by fb on 2017/7/3.
 */

@FragmentWithArgs public class RecruitRichTextEditFragment extends BaseFragment {

  RichTxtFragment richTxtFragment;
  ChoosePictureFragmentNewDialog choosePictureFragmentDialog;

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;

  @Arg String content;
  @Arg(required = false) int type;            //type用来区分多个富文本编辑器返回结果(发布职位)
  @Arg String toolbarTitle;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitRichTextEditFragmentBuilder.injectArguments(this);
    richTxtFragment = new RichTxtFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_edit_self_desc, container, false);
    super.onCreateView(inflater, container, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(toolbarTitle);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(richTxtFragment);
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RichTxtFragment) {
      richTxtFragment.initContent(content);
    }
  }

  @Override public int getLayoutRes() {
    return R.id.frag_desc;
  }

  @Override public String getFragmentName() {
    return ResumeEditDescFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.btn_insert_img) public void onBtnInsertImgClicked() {
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
  }

  @OnClick(R2.id.btn_comfirm) public void onBtnComfirmClicked() {
    if (richTxtFragment != null) {
      String x = richTxtFragment.getContent();
      RxBus.getBus().post(new EventRichTextBack(x, type));
      getActivity().onBackPressed();
    }
  }
}
