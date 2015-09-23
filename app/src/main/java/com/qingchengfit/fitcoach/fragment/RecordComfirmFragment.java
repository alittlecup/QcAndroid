package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordComfirmFragment extends BaseFragment {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.record_confirm_none)
    RelativeLayout recordConfirmNone;
    @Bind(R.id.record_comfirm_no_img)
    ImageView recordComfirmNoImg;
    @Bind(R.id.record_comfirm_no_txt)
    TextView recordComfirmNoTxt;
    private RecordComfirmAdapter adapter;
    private List<QcCertificatesReponse.DataEntity.CertificatesEntity> datas;

    public RecordComfirmFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || isVisible)
            return;
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        QcCloudClient.getApi().getApi.qcGetCertificates(App.coachid).subscribe(qcCertificatesReponse -> {
            getActivity().runOnUiThread(() -> {
                if (qcCertificatesReponse.getData().getCertificates() != null) {
                    recordConfirmNone.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                    adapter = new RecordComfirmAdapter(qcCertificatesReponse.getData().getCertificates());
                    adapter.setListener((v, pos) -> {
                        ComfirmDetailFragment fragment =
                                ComfirmDetailFragment.newInstance(qcCertificatesReponse.getData().getCertificates().get(pos).getId());

                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.myhome_fraglayout, fragment)
                                .show(fragment).addToBackStack("").commit();
                    });
                    recyclerview.setAdapter(adapter);
                } else {
                    recyclerview.setVisibility(View.GONE);
                    recordComfirmNoImg.setImageResource(R.drawable.img_no_experience);
                    recordComfirmNoTxt.setText("您还没有添加任何工作经历请在设置页面中添加");
                    recordConfirmNone.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public static class RecordComfirmVH extends RecyclerView.ViewHolder {
        @Bind(R.id.recordcomfirm_title)
        TextView recordcomfirmTitle;
        @Bind(R.id.recordcomfirm_subtitle)
        TextView recordcomfirmSubtitle;
        @Bind(R.id.recordcomfirm_time)
        TextView recordcomfirmTime;
        @Bind(R.id.recordcomfirm_comfirm)
        ImageView img;

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
            if (certificatesEntity.getIs_authenticated()) {
                Glide.with(App.AppContex).load(R.drawable.img_record_comfirmed).into(holder.img);
            } else
                Glide.with(App.AppContex).load(R.drawable.img_record_uncomfirmed).into(holder.img);
            StringBuffer sb = new StringBuffer();
            sb.append("有效期:  ");
            sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(certificatesEntity.getCreated_at())));
            sb.append("-");
            sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));
            holder.recordcomfirmTime.setText(sb.toString());

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
