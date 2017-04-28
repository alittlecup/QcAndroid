package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.UpYunUtils;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.GalleryPhotoViewDialog;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordComfirmFragment extends VpFragment {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.record_confirm_none)
    RelativeLayout recordConfirmNone;
    @BindView(R.id.record_comfirm_no_img)
    ImageView recordComfirmNoImg;
    @BindView(R.id.record_comfirm_no_txt)
    TextView recordComfirmNoTxt;
    private RecordComfirmAdapter adapter;
    private List<QcCertificatesReponse.DataEntity.CertificatesEntity> datas;
    private Unbinder unbinder;

    public RecordComfirmFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
        unbinder=ButterKnife.bind(this, view);
        lazyLoad();
        return view;
    }

    protected void lazyLoad() {
//        if (!isPrepared || isVisible)
//            return;
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        QcCloudClient.getApi().getApi.qcGetCertificates(App.coachid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcCertificatesReponse -> {

                    if (recyclerview != null) {
                        if (qcCertificatesReponse.getData().getCertificates() != null && qcCertificatesReponse.getData().getCertificates().size() > 0) {
                            recordConfirmNone.setVisibility(View.GONE);
                            recyclerview.setVisibility(View.VISIBLE);
                            adapter = new RecordComfirmAdapter(qcCertificatesReponse.getData().getCertificates());
                            adapter.setListener((v, pos) -> {
                                String photo = adapter.datas.get(pos).getPhoto();
                                if (!TextUtils.isEmpty(photo)) {
                                    GalleryPhotoViewDialog dialog = new GalleryPhotoViewDialog(getContext());
                                    dialog.setImage(photo);
                                    dialog.show();
                                }
                                //显示图片
//                                ComfirmDetailFragment fragment =
//                                        ComfirmDetailFragment.newInstance(qcCertificatesReponse.getData().getCertificates().get(pos).getId());
//
//                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.myhome_fraglayout, fragment)
//                                        .show(fragment).addToBackStack("").commit();
                            });
                            recyclerview.setAdapter(adapter);
                        } else {
                            recyclerview.setVisibility(View.GONE);
                            recordComfirmNoImg.setImageResource(R.drawable.img_no_certificate);
                            recordComfirmNoTxt.setText("您还没有添加任何资质认证请在设置页面中添加");
                            recordConfirmNone.setVisibility(View.VISIBLE);
                        }
                    }
                }, throwable -> {
                }, () -> {
                });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getTitle() {
        return "资质认证";
    }


    public static class RecordComfirmVH extends RecyclerView.ViewHolder {
        @BindView(R.id.recordcomfirm_title)
        TextView recordcomfirmTitle;
        @BindView(R.id.recordcomfirm_subtitle)
        TextView recordcomfirmSubtitle;
        @BindView(R.id.recordcomfirm_time)
        TextView recordcomfirmTime;
        @BindView(R.id.recordcomfirm_date)
        TextView recordcomfirmDate;
        @BindView(R.id.img)
        ImageView recordImg;
        @BindView(R.id.recordcomfirm_comfirm)
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


            return holder;
        }

        @Override
        public void onBindViewHolder(RecordComfirmVH holder, int position) {
            holder.itemView.setTag(position);
            holder.recordImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(v,position);
                }
            });
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity = datas.get(position);
            holder.recordcomfirmTitle.setText(certificatesEntity.getName());
            holder.recordcomfirmSubtitle.setText("发证单位:" + certificatesEntity.getOrganization().getName());
            if (certificatesEntity.getIs_authenticated()) {
                holder.img.setVisibility(View.VISIBLE);
            } else
                holder.img.setVisibility(View.INVISIBLE);

            holder.recordcomfirmDate.setText("发证日期:" + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));

            if (TextUtils.isEmpty(certificatesEntity.getPhoto())) {
                holder.recordImg.setVisibility(View.GONE);
            } else {
                holder.recordImg.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(UpYunUtils.getMiniPhoto(certificatesEntity.getPhoto())).into(holder.recordImg);
            }

            if (certificatesEntity.isWill_expired()) {
                if (TextUtils.isEmpty(certificatesEntity.getStart()) && TextUtils.isEmpty(certificatesEntity.getEnd())) {
                    holder.recordcomfirmTime.setText("有效期:未填写");
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("有效期:  ");
                    sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
                    sb.append("至");
                    Date d = DateUtils.formatDateFromServer(certificatesEntity.getEnd());
                    Calendar c = Calendar.getInstance(Locale.getDefault());
                    c.setTime(d);


                    if (c.get(Calendar.YEAR) == 3000)
                        holder.recordcomfirmTime.setText("长期有效");
                    else {
                        sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getEnd())));
                        holder.recordcomfirmTime.setText(sb.toString());
                    }
                }
            } else {
                holder.recordcomfirmTime.setText("长期有效");
            }


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
