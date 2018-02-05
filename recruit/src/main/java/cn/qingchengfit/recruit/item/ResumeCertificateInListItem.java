package cn.qingchengfit.recruit.item;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventDownlowdCertification;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeCertificateInListItem extends AbstractFlexibleItem<ResumeCertificateInListItem.ResumeCertificateVH> {

  Certificate certificate;

  public ResumeCertificateInListItem(Certificate certificate) {
    this.certificate = certificate;
  }

  public Certificate getCertificate() {
    return certificate;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_certificate;
  }

  @Override public ResumeCertificateVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeCertificateVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeCertificateVH holder, int position, List payloads) {
    if (certificate.organization != null) {
      PhotoUtils.small(holder.imgGym, certificate.organization.photo);
      holder.tvCertificateOrganization.setText(certificate.organization.name);
      holder.tvCertificateOrganizationAddress.setText(certificate.project_name);
      holder.tvCertificateName.setText(certificate.name);
      holder.tvMeetingStart.setText(DateUtils.getYYMMfromServer(certificate.date_of_issue));
    }
    holder.tvCertificateName.setText(certificate.name);

    //是否同步
    holder.layoutSyncFromQc.setVisibility(certificate.is_authenticated ? View.VISIBLE : View.GONE);
    holder.imgQcComfirm.setVisibility(certificate.is_authenticated ? View.VISIBLE : View.GONE);
    if (TextUtils.isEmpty(certificate.photo)) {
      holder.btnDownload.setVisibility(View.GONE);
      holder.layoutCertificatePhoto.setVisibility(View.GONE);
    } else {
      holder.btnDownload.setVisibility(View.VISIBLE);
      holder.layoutCertificatePhoto.setVisibility(View.VISIBLE);
      holder.tvCertificateCardName.setText("证书名称：" + certificate.certificate_name);
      holder.tvCertificatePeriod.setText(
          "有效期  ：" + DateUtils.getYYYYMMDDfromServer(certificate.start) + "至" + DateUtils.getYYYYMMDDfromServer(certificate.end));
      PhotoUtils.origin(holder.imgCertificate, certificate.photo);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeCertificateVH extends FlexibleViewHolder {
    @BindView(R2.id.img_gym) ImageView imgGym;
    @BindView(R2.id.tv_certificate_name) TextView tvCertificateName;
    @BindView(R2.id.tv_meeting_start) TextView tvMeetingStart;
    @BindView(R2.id.tv_certificate_organization) TextView tvCertificateOrganization;
    @BindView(R2.id.tv_certificate_organization_address) TextView tvCertificateOrganizationAddress;
    @BindView(R2.id.img_qc_comfirm) ImageView imgQcComfirm;
    @BindView(R2.id.tv_certificate_card_name) TextView tvCertificateCardName;
    @BindView(R2.id.tv_certificate_period) TextView tvCertificatePeriod;
    @BindView(R2.id.img_certificate) ImageView imgCertificate;
    @BindView(R2.id.layout_certificate_photo) LinearLayout layoutCertificatePhoto;
    @BindView(R2.id.layout_sync_from_qc) LinearLayout layoutSyncFromQc;
    @BindView(R2.id.btn_down) FrameLayout btnDownload;

    public ResumeCertificateVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      btnDownload.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          IFlexible item = adapter.getItem(getAdapterPosition());
          if (item instanceof ResumeCertificateInListItem) {
            RxBus.getBus().post(new EventDownlowdCertification(((ResumeCertificateInListItem) item).getCertificate().getPhoto()));
          }
        }
      });
    }
  }
}