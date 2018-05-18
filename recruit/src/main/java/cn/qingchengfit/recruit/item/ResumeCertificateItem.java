package cn.qingchengfit.recruit.item;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeCertificateItem extends AbstractFlexibleItem<ResumeCertificateItem.ResumeCertificateVH> {

  Certificate certificate;

  public ResumeCertificateItem(Certificate certificate) {
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

    //是否同步
    holder.imgQcComfirm.setVisibility(certificate.is_authenticated ? View.VISIBLE : View.GONE);
    holder.layoutSyncFromQc.setVisibility(certificate.is_authenticated ? View.VISIBLE : View.GONE);
    if (TextUtils.isEmpty(certificate.photo)) {
      holder.layoutCertificatePhoto.setVisibility(View.GONE);
    } else {
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
	ImageView imgGym;
	TextView tvCertificateName;
	TextView tvMeetingStart;
	TextView tvCertificateOrganization;
	TextView tvCertificateOrganizationAddress;
	ImageView imgQcComfirm;
	TextView tvCertificateCardName;
	TextView tvCertificatePeriod;
	ImageView imgCertificate;
	LinearLayout layoutCertificatePhoto;
	LinearLayout layoutSyncFromQc;

    public ResumeCertificateVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgGym = (ImageView) view.findViewById(R.id.img_gym);
      tvCertificateName = (TextView) view.findViewById(R.id.tv_certificate_name);
      tvMeetingStart = (TextView) view.findViewById(R.id.tv_meeting_start);
      tvCertificateOrganization = (TextView) view.findViewById(R.id.tv_certificate_organization);
      tvCertificateOrganizationAddress =
          (TextView) view.findViewById(R.id.tv_certificate_organization_address);
      imgQcComfirm = (ImageView) view.findViewById(R.id.img_qc_comfirm);
      tvCertificateCardName = (TextView) view.findViewById(R.id.tv_certificate_card_name);
      tvCertificatePeriod = (TextView) view.findViewById(R.id.tv_certificate_period);
      imgCertificate = (ImageView) view.findViewById(R.id.img_certificate);
      layoutCertificatePhoto = (LinearLayout) view.findViewById(R.id.layout_certificate_photo);
      layoutSyncFromQc = (LinearLayout) view.findViewById(R.id.layout_sync_from_qc);
    }
  }
}