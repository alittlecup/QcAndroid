package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends BaseSettingFragment {
    public static final String TAG = RecordFragment.class.getName();
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    Gson gson = new Gson();
    @Bind(R.id.record_comfirm_no_img)
    ImageView recordComfirmNoImg;
    @Bind(R.id.record_comfirm_no_txt)
    TextView recordComfirmNoTxt;
    @Bind(R.id.record_confirm_none)
    RelativeLayout recordConfirmNone;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    private RecordComfirmAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.add_certification, 0, getActivity().getString(R.string.record_title));
        fragmentCallBack.onToolbarClickListener(item -> {
//            fragmentCallBack.onFragmentChange(RecordEditFragment.newInstance(false, null));
            int requestCode = 10011;
            switch (item.getItemId()){
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
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        freshData();
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshData();
            }
        });
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                refresh.setRefreshing(false);
            }
        });
        return view;
    }

    public void freshData() {

        QcCloudClient.getApi().getApi.qcGetCertificates(App.coachid).subscribe(qcCertificatesReponse -> {
            getActivity().runOnUiThread(() -> {
                if (qcCertificatesReponse.getData().getCertificates() != null && qcCertificatesReponse.getData().getCertificates().size() > 0) {
                    recyclerview.setVisibility(View.VISIBLE);
                    recordConfirmNone.setVisibility(View.GONE);
                    adapter = new RecordComfirmAdapter(qcCertificatesReponse.getData().getCertificates());
                    adapter.setListener((v, pos) -> {
                        int type = qcCertificatesReponse.getData().getCertificates().get(pos).getType();
                        RecordEditFragment fragment = RecordEditFragment.newInstance(true, gson.toJson(qcCertificatesReponse.getData().getCertificates().get(pos)), type);
                        fragmentCallBack.onFragmentChange(fragment);
                    });
                    recyclerview.setAdapter(adapter);
                } else {
                    recyclerview.setVisibility(View.GONE);
                    recordComfirmNoImg.setImageResource(R.drawable.img_no_certificate);
                    recordComfirmNoTxt.setText("您还没有添加任何认证信息请点击添加按钮");
                    recordConfirmNone.setVisibility(View.VISIBLE);
                }
                refresh.setRefreshing(false);
            });
        }, throwable -> {
        }, () -> {
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("onActivityReslut:" + requestCode + "  " + requestCode);
        if (requestCode == 10011 && resultCode > 0) {
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity = new QcCertificatesReponse.DataEntity.CertificatesEntity();
            QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity entity = new QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity();
            entity.setId(data.getIntExtra("id", 0));
            entity.setName(data.getStringExtra("username"));
            entity.setPhoto(data.getStringExtra("pic"));
            certificatesEntity.setIs_authenticated(data.getBooleanExtra("isauth",false));
            certificatesEntity.setOrganization(entity);
            RecordEditFragment fragment = RecordEditFragment.newInstance(false, gson.toJson(certificatesEntity),1);
            fragmentCallBack.onFragmentChange(fragment);
        }else if (requestCode == 10012 && resultCode > 0){
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity = new QcCertificatesReponse.DataEntity.CertificatesEntity();
            QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity entity = new QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity();
            entity.setId(data.getIntExtra("id", 0));
            entity.setName(data.getStringExtra("username"));
            entity.setPhoto(data.getStringExtra("pic"));
            certificatesEntity.setOrganization(entity);
            certificatesEntity.setIs_authenticated(data.getBooleanExtra("isauth", false));
            RecordEditFragment fragment = RecordEditFragment.newInstance(false, gson.toJson(certificatesEntity),2);
            fragmentCallBack.onFragmentChange(fragment);
        }else if (requestCode == 10013 && resultCode > 0){
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity = new QcCertificatesReponse.DataEntity.CertificatesEntity();
            QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity entity = new QcCertificatesReponse.DataEntity.CertificatesEntity.OrganizationEntity();
            entity.setId(data.getIntExtra("id", 0));
            entity.setName(data.getStringExtra("username"));
            entity.setPhoto(data.getStringExtra("pic"));
            certificatesEntity.setIs_authenticated(data.getBooleanExtra("isauth",false));
            certificatesEntity.setOrganization(entity);
            RecordEditFragment fragment = RecordEditFragment.newInstance(false, gson.toJson(certificatesEntity),3);
            fragmentCallBack.onFragmentChange(fragment);
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class RecordComfirmVH extends RecyclerView.ViewHolder {
        @Bind(R.id.recordcomfirm_title)
        TextView recordcomfirmTitle;
        @Bind(R.id.recordcomfirm_subtitle)
        TextView recordcomfirmSubtitle;
        @Bind(R.id.recordcomfirm_time)
        TextView recordcomfirmTime;
        @Bind(R.id.recordcomfirm_comfirm)
        ImageView recordcomfirmImg;

        public RecordComfirmVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

        @Override
        public RecordComfirmVH onCreateViewHolder(ViewGroup parent, int viewType) {
            RecordComfirmVH holder = new RecordComfirmVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recordcomfirm, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecordComfirmVH holder, int position) {
            holder.itemView.setTag(position);
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity = datas.get(position);
            holder.recordcomfirmTitle.setText(certificatesEntity.getName());
            holder.recordcomfirmSubtitle.setText(certificatesEntity.getOrganization().getName());
            StringBuffer sb = new StringBuffer();
            sb.append("有效期:");
            sb.append(DateUtils.getServerDateDay(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
            sb.append("至");
            Date d = DateUtils.formatDateFromServer(certificatesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000)
                holder.recordcomfirmTime.setText("有效期:长期有效");
            else {
                sb.append(DateUtils.getServerDateDay(d));
                holder.recordcomfirmTime.setText(sb.toString());
            }
            if (certificatesEntity.getIs_authenticated())
                Glide.with(App.AppContex).load(R.drawable.img_record_comfirmed).into(holder.recordcomfirmImg);
            else
                Glide.with(App.AppContex).load(R.drawable.img_record_uncomfirmed).into(holder.recordcomfirmImg);


        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

}
