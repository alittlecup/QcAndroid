package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import cn.qingchengfit.utils.DateUtils;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends BaseSettingFragment {
    public static final String TAG = RecordFragment.class.getName();
    //@BindView(R.id.recyclerview)
    //RecyclerView recyclerview;
    Gson gson = new Gson();
    //@BindView(R.id.record_comfirm_no_img)
    //ImageView recordComfirmNoImg;
    //@BindView(R.id.record_comfirm_no_txt)
    //TextView recordComfirmNoTxt;
    //@BindView(record_confirm_none)
    //RelativeLayout recordConfirmNone;
    //@BindView(R.id.refresh)
    //SwipeRefreshLayout refresh;
    private RecordComfirmAdapter adapter;


    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

      //unbinder=ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.add_certification, 0, getActivity().getString(R.string.record_title));
        fragmentCallBack.onToolbarClickListener(item -> {
            int requestCode = 10011;
            switch (item.getItemId()) {
                case R.id.action_add_meeting:
                    requestCode = 10011;
                    break;
                case R.id.action_add_comfirm:
                    requestCode = 10012;
                    break;
                case R.id.action_add_competition:
                    requestCode = 10013;
                    break;
                default:
                    return false;
            }
            Intent toSearch = new Intent(getActivity(), SearchActivity.class);
            toSearch.putExtra("type", SearchFragment.TYPE_ORGANASITON);
            startActivityForResult(toSearch, requestCode);
            return false;
        });
        WebFragmentNoToolbar web = WebFragmentNoToolbar.newInstance(Configs.Server + Configs.HOST_EDUCATION);
        web.setTouchBig(true);
        getChildFragmentManager().beginTransaction().replace(R.id.frag_record, web).commit();
        return view;
    }

    public void freshData() {

    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10011 && resultCode > 0) {
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity =
                new QcCertificatesReponse.DataEntity.CertificatesEntity();
            QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity entity =
                new QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity();
            entity.setId((int) data.getLongExtra("id", 0));
            entity.setName(data.getStringExtra("username"));
            entity.setPhoto(data.getStringExtra("pic"));
            entity.setContact(data.getStringExtra("address"));
            certificatesEntity.setIs_authenticated(data.getBooleanExtra("isauth", false));
            certificatesEntity.setOrganization(entity);
            RecordEditFragment fragment = RecordEditFragment.newInstance(false, gson.toJson(certificatesEntity), 1);
            fragmentCallBack.onFragmentChange(fragment);
        } else if (requestCode == 10012 && resultCode > 0) {
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity =
                new QcCertificatesReponse.DataEntity.CertificatesEntity();
            QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity entity =
                new QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity();
            entity.setId((int) data.getLongExtra("id", 0));
            entity.setName(data.getStringExtra("username"));
            entity.setPhoto(data.getStringExtra("pic"));
            entity.setContact(data.getStringExtra("address"));
            certificatesEntity.setOrganization(entity);
            certificatesEntity.setIs_authenticated(data.getBooleanExtra("isauth", false));
            RecordEditFragment fragment = RecordEditFragment.newInstance(false, gson.toJson(certificatesEntity), 2);
            fragmentCallBack.onFragmentChange(fragment);
        } else if (requestCode == 10013 && resultCode > 0) {
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity =
                new QcCertificatesReponse.DataEntity.CertificatesEntity();
            QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity entity =
                new QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity();
            entity.setId((int) data.getLongExtra("id", 0));
            entity.setName(data.getStringExtra("username"));
            entity.setPhoto(data.getStringExtra("pic"));
            entity.setContact(data.getStringExtra("address"));
            certificatesEntity.setIs_authenticated(data.getBooleanExtra("isauth", false));
            certificatesEntity.setOrganization(entity);
            RecordEditFragment fragment = RecordEditFragment.newInstance(false, gson.toJson(certificatesEntity), 3);
            fragmentCallBack.onFragmentChange(fragment);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class RecordComfirmVH extends RecyclerView.ViewHolder {
	TextView recordcomfirmTitle;
	TextView recordcomfirmSubtitle;
	TextView recordcomfirmTime;
	ImageView recordcomfirmImg;
	View hideView;

        public RecordComfirmVH(View view) {
            super(view);
            recordcomfirmTitle = (TextView) view.findViewById(R.id.recordcomfirm_title);
            recordcomfirmSubtitle = (TextView) view.findViewById(R.id.recordcomfirm_subtitle);
            recordcomfirmTime = (TextView) view.findViewById(R.id.recordcomfirm_time);
            recordcomfirmImg = (ImageView) view.findViewById(R.id.recordcomfirm_comfirm);
            hideView = (View) view.findViewById(R.id.item__hidden);

        }
    }

    class RecordComfirmAdapter extends RecyclerView.Adapter<RecordComfirmVH> implements View.OnClickListener {

        private List<QcCertificatesReponse.DataEntity.CertificatesEntity> datas;
        private OnRecycleItemClickListener listener;

        public RecordComfirmAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public RecordComfirmVH onCreateViewHolder(ViewGroup parent, int viewType) {
            RecordComfirmVH holder =
                new RecordComfirmVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_list, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(RecordComfirmVH holder, int position) {
            holder.itemView.setTag(position);
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity = datas.get(position);
            holder.recordcomfirmTitle.setText(certificatesEntity.getName());
            holder.recordcomfirmSubtitle.setText(certificatesEntity.getOrganization().getName());
            if (certificatesEntity.getIs_authenticated()) {
                holder.recordcomfirmImg.setVisibility(View.VISIBLE);
                if (!certificatesEntity.is_hidden()) {
                    holder.hideView.setVisibility(View.GONE);
                    if (certificatesEntity.isWill_expired()) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("有效期:");
                        sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
                        sb.append("至");
                        Date d = DateUtils.formatDateFromServer(certificatesEntity.getEnd());
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        c.setTime(d);
                        if (c.get(Calendar.YEAR) == 3000) {
                            holder.recordcomfirmTime.setText("长期有效");
                        } else {
                            sb.append(DateUtils.Date2YYYYMMDD(d));
                            holder.recordcomfirmTime.setText(sb.toString());
                        }
                    } else {
                        holder.recordcomfirmTime.setText("长期有效");
                    }
                } else {
                    holder.hideView.setVisibility(View.VISIBLE);
                    holder.recordcomfirmTime.setText("已隐藏");
                }
            } else {
                holder.recordcomfirmImg.setVisibility(View.GONE);
                holder.hideView.setVisibility(View.GONE);
                if (TextUtils.isEmpty(certificatesEntity.getStart()) || TextUtils.isEmpty(certificatesEntity.getEnd())) {
                    holder.recordcomfirmTime.setText("");
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("有效期:");
                    sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
                    sb.append("至");
                    Date d = DateUtils.formatDateFromServer(certificatesEntity.getEnd());
                    Calendar c = Calendar.getInstance(Locale.getDefault());
                    c.setTime(d);
                    if (c.get(Calendar.YEAR) == 3000) {
                        holder.recordcomfirmTime.setText("长期有效");
                    } else {
                        sb.append(DateUtils.Date2YYYYMMDD(d));
                        holder.recordcomfirmTime.setText(sb.toString());
                    }
                }
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }
}
