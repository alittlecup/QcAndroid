package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;

import java.util.List;

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
    private RecordComfirmAdapter adapter;

    public RecordFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.add, 0, getActivity().getString(R.string.record_title));
        fragmentCallBack.onToolbarClickListener(item -> {
            fragmentCallBack.onFragmentChange(RecordEditFragment.newInstance(false, null));
            return true;

        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        QcCloudClient.getApi().getApi.qcGetCertificates(App.coachid).subscribe(qcCertificatesReponse -> {
            getActivity().runOnUiThread(() -> {
                if (qcCertificatesReponse.getData().getCertificates() != null) {
                    adapter = new RecordComfirmAdapter(qcCertificatesReponse.getData().getCertificates());
                    adapter.setListener((v, pos) -> {
//                        ComfirmDetailFragment fragment =
//                                ComfirmDetailFragment.newInstance(qcCertificatesReponse.getData().getCertificates().get(pos).getId());
                        RecordEditFragment fragment = RecordEditFragment.newInstance(true, gson.toJson(qcCertificatesReponse.getData().getCertificates().get(pos)));
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.settting_fraglayout, fragment)
                                .show(fragment).addToBackStack("").commit();
                    });
                    recyclerview.setAdapter(adapter);
                }
            });
        });
        return view;
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
            sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(certificatesEntity.getCreated_at())));
            sb.append("至");
            sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));

            holder.recordcomfirmTime.setText(sb.toString());
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
