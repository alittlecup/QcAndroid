package cn.qingchengfit.recruit.views.resume;

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



import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.presenter.ResumePostPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.views.fragments.RichTxtFragment;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/6/14.
 */

@FragmentWithArgs public class ResumeEditDescFragment extends BaseFragment implements ResumePostPresenter.MVPView {
  RichTxtFragment richTxtFragment;
  ChoosePictureFragmentNewDialog choosePictureFragmentDialog;

	Toolbar toolbar;
	TextView toolbarTitile;

  @Arg String content;

  @Inject ResumePostPresenter postPresenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeEditDescFragmentBuilder.injectArguments(this);
    richTxtFragment = new RichTxtFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_edit_self_desc, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.btn_insert_img).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnInsertImgClicked();
      }
    });
    view.findViewById(R.id.btn_comfirm).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnComfirmClicked();
      }
    });
    super.onCreateView(inflater, container, savedInstanceState);

    delegatePresenter(postPresenter, this);
    initToolbar(toolbar);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("编辑自我描述");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(richTxtFragment);
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RichTxtFragment) {
      richTxtFragment.initContent(content, "");
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

 public void onBtnInsertImgClicked() {
    if (choosePictureFragmentDialog == null) choosePictureFragmentDialog = ChoosePictureFragmentNewDialog.newInstance();
    choosePictureFragmentDialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
      @Override public void onChoosefile(String filePath) {

      }

      @Override public void onUploadComplete(String sp, String url) {
        richTxtFragment.insertImg(url);
      }
    });
    choosePictureFragmentDialog.show(getChildFragmentManager(), "");
  }

 public void onBtnComfirmClicked() {
    if (richTxtFragment != null) {
      String x = richTxtFragment.getContent();
      showLoading();
      postPresenter.editResume(new ResumeBody.Builder().self_description(x).build());
    }
  }

  @Override public void onPostOk() {
    hideLoading();
    RxBus.getBus().post(new EventResumeFresh());
    getActivity().onBackPressed();
  }
}
