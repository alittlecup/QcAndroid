package cn.qingchengfit.recruit.views.resume;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.event.EventDownlowdCertification;
import cn.qingchengfit.recruit.item.ResumeCertificateInListItem;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Organization;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.CertificateListWrap;
import cn.qingchengfit.recruit.views.organization.SearchActivity;
import cn.qingchengfit.recruit.views.organization.SearchFragment;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.UUID;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class ResumeCertificateListFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.rv) RecyclerView rv;

  CommonFlexAdapter commonFlexAdapter;

  @Inject QcRestRepository restRepository;
  @Inject RecruitRouter router;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_cm_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 1));
    rv.setAdapter(commonFlexAdapter);
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryCertifications()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CertificateListWrap>>() {
          @Override public void call(QcDataResponse<CertificateListWrap> certificateListWrapQcDataResponse) {
            if (certificateListWrapQcDataResponse.status == 200) {
              commonFlexAdapter.clear();
              for (Certificate c : certificateListWrapQcDataResponse.data.certificates) {
                commonFlexAdapter.addItem(new ResumeCertificateInListItem(c));
              }
            } else {
              onShowError(certificateListWrapQcDataResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {

          }
        }));
    RxBusAdd(EventDownlowdCertification.class).subscribe(new Action1<EventDownlowdCertification>() {
      @Override public void call(EventDownlowdCertification eventDownlowdCertification) {
        downloadFile(eventDownlowdCertification.url, "image/*");
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("编辑学习培训");
    toolbar.inflateMenu(R.menu.menu_add);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        int requestCode = 10012;
        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
        toSearch.putExtra("type", SearchFragment.TYPE_ORGANASITON);
        startActivityForResult(toSearch, requestCode);
        return true;
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode > 0 && requestCode == 10012) {
      Organization gym = new Organization();
      gym.setId(data.getLongExtra("id", 0) + "");
      gym.setName(data.getStringExtra("username"));
      gym.setPhoto(data.getStringExtra("pic"));
      gym.setContact(data.getStringExtra("address"));
      router.addCertification(2, gym);
      return;
    }
  }

  @Override public String getFragmentName() {
    return ResumeCertificateListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof ResumeCertificateInListItem) {
      Certificate c = ((ResumeCertificateInListItem) commonFlexAdapter.getItem(i)).getCertificate();
      router.editCertification(c.type, c.organization, c);
    }
    return false;
  }

  public void downloadFile(String url, String mime) {
    try {
      DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
      DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
      request.setMimeType(mime);
      request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
          "证书：" + UUID.randomUUID() + "." + MimeTypeMap.getFileExtensionFromUrl(url));
      request.allowScanningByMediaScanner();
      downloadManager.enqueue(request);
      ToastUtils.show("证书已下载");
    } catch (Exception e) {
      ToastUtils.show("证书下载失败");
    }
  }
}
