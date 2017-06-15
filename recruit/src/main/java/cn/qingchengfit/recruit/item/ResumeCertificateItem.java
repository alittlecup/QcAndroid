package cn.qingchengfit.recruit.item;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.model.Certificate;
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

    @Override public int getLayoutRes() {
        return R.layout.item_resume_certificate;
    }

    @Override public ResumeCertificateVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeCertificateVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeCertificateVH holder, int position, List payloads) {
        if (certificate.organization != null) {
            PhotoUtils.small(holder.imgGym, certificate.organization.photo);
            holder.tvCertificateOrganization.setText(certificate.organization.name);
            holder.tvCertificateOrganizationAddress.setText(certificate.organization.contact);
        }
        holder.tvCertificateName.setText(certificate.name);
        //是否同步
        holder.layoutSyncFromQc.setVisibility(certificate.is_authenticated ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(certificate.photo)) {
            holder.layoutCertificatePhoto.setVisibility(View.GONE);
        } else {
            holder.layoutCertificatePhoto.setVisibility(View.VISIBLE);
            holder.tvCertificateCardName.setText("证书名称" + certificate.certificate_name);
            holder.tvCertificatePeriod.setText("有效期" + certificate.start + "至" + certificate.end);
            PhotoUtils.origin(holder.imgCertificate, certificate.photo);
        }


    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeCertificateVH extends FlexibleViewHolder {
        @BindView(R2.id.img_gym) ImageView imgGym;
        @BindView(R2.id.tv_certificate_name) TextView tvCertificateName;
        @BindView(R2.id.tv_certificate_organization) TextView tvCertificateOrganization;
        @BindView(R2.id.tv_certificate_organization_address) TextView tvCertificateOrganizationAddress;
        @BindView(R2.id.img_qc_comfirm) ImageView imgQcComfirm;
        @BindView(R2.id.tv_certificate_card_name) TextView tvCertificateCardName;
        @BindView(R2.id.tv_certificate_period) TextView tvCertificatePeriod;
        @BindView(R2.id.img_certificate) ImageView imgCertificate;
        @BindView(R2.id.layout_certificate_photo) LinearLayout layoutCertificatePhoto;
        @BindView(R2.id.layout_sync_from_qc) LinearLayout layoutSyncFromQc;
        public ResumeCertificateVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}