package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.RectAddItem;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.item.Image120DelItem;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.presenter.ResumePostPresenter;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.SpacesItemDecoration;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
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
@FragmentWithArgs public class ResumeShowImgsFragment extends BaseFragment
    implements ResumePostPresenter.MVPView, FlexibleAdapter.OnItemClickListener {
  @Arg ArrayList<String> phots;

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.rv) RecyclerView rv;
  @Inject ResumePostPresenter presenter;
  private CommonFlexAdapter commonFlexAdapter;
  private ChoosePictureFragmentNewDialog choosePictureFragmentNewDialog;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeShowImgsFragmentBuilder.injectArguments(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_show_imgs, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    rv.setLayoutManager(new SmoothScrollGridLayoutManager(getContext(), 3));
    rv.addItemDecoration(new SpacesItemDecoration(MeasureUtils.dpToPx(6f, getResources())));
    rv.setAdapter(commonFlexAdapter);
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    commonFlexAdapter.clear();
    if (commonFlexAdapter != null && rv != null) {
      for (int i = 0; i < phots.size(); i++) {
        commonFlexAdapter.addItem(new Image120DelItem(phots.get(i)));
      }
      commonFlexAdapter.addItem(new RectAddItem());
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("编辑照片");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        showLoading();
        presenter.editResume(new ResumeBody.Builder().photos(getPhotos()).build());
        return false;
      }
    });
  }

  public List<String> getPhotos() {
    List<String> ret = new ArrayList<>();
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible flexible = commonFlexAdapter.getItem(i);
      if (flexible instanceof Image120DelItem) {
        ret.add(((Image120DelItem) flexible).getUrl());
      }
    }
    return ret;
  }

  @Override public String getFragmentName() {
    return ResumeShowImgsFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof Image120DelItem) {

    } else if (commonFlexAdapter.getItem(i) instanceof RectAddItem) {
      if (choosePictureFragmentNewDialog == null) choosePictureFragmentNewDialog = ChoosePictureFragmentNewDialog.newInstance();
      choosePictureFragmentNewDialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
        @Override public void onChoosefile(String filePath) {
        }

        @Override public void onUploadComplete(String filePaht, String url) {
          commonFlexAdapter.addItem(commonFlexAdapter.getItemCount() - 1, new Image120DelItem(url));
        }
      });
      choosePictureFragmentNewDialog.show(getChildFragmentManager(), "");
    }
    return true;
  }

  @Override public void onPostOk() {
    hideLoading();
    RxBus.getBus().post(new EventResumeFresh());
    getActivity().onBackPressed();
  }
}
