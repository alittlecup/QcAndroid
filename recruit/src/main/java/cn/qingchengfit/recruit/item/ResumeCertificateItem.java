package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.model.Certificate;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeCertificateItem extends AbstractFlexibleItem<ResumeCertificateItem.ResumeCertificateVH> {

    Certificate certificate;

    @Override public int getLayoutRes() {
        return R.layout.item_resume_certificate;
    }

    @Override public ResumeCertificateVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeCertificateVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeCertificateVH holder, int position, List payloads) {
        //PhotoUtils.small(holder.imgGym,certificate.organization); todo 机构图片
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeCertificateVH extends FlexibleViewHolder {
        //@BindView(R2.id.img_gym) ImageView imgGym;
        //@BindView(R2.id.tv_certificate_name) TextView tvCertificateName;
        //@BindView(R2.id.tv_certificate_organization) TextView tvCertificateOrganization;
        //@BindView(R2.id.tv_certificate_organization_address) TextView tvCertificateOrganizationAddress;
        //@BindView(R2.id.img_qc_comfirm) ImageView imgQcComfirm;
        //@BindView(R2.id.tv_certificate_name) TextView tvCertificateName;
        //@BindView(R2.id.tv_certificate_period) TextView tvCertificatePeriod;
        //@BindView(R2.id.img_certificate) ImageView imgCertificate;
        //@BindView(R2.id.layout_certificate) LinearLayout layoutCertificate;
        //@BindView(R2.id.layout_sync_from_qc) LinearLayout layoutSyncFromQc;
        public ResumeCertificateVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}