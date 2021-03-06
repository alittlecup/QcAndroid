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
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.VpFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.UpYunUtils;
import com.qingchengfit.fitcoach.component.GalleryPhotoViewDialog;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.TrainerRepository;
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

	RecyclerView recyclerview;
	RelativeLayout recordConfirmNone;
	ImageView recordComfirmNoImg;
	TextView recordComfirmNoTxt;
    private RecordComfirmAdapter adapter;
    private List<QcCertificatesReponse.DataEntity.CertificatesEntity> datas;


    public RecordComfirmFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
      recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
      recordConfirmNone = (RelativeLayout) view.findViewById(R.id.record_confirm_none);
      recordComfirmNoImg = (ImageView) view.findViewById(R.id.record_comfirm_no_img);
      recordComfirmNoTxt = (TextView) view.findViewById(R.id.record_comfirm_no_txt);


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

        TrainerRepository.getStaticTrainerAllApi().qcGetCertificates(App.coachid)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(qcCertificatesReponse -> {

            if (recyclerview != null) {
                if (qcCertificatesReponse.getData().getCertificates() != null
                    && qcCertificatesReponse.getData().getCertificates().size() > 0) {
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
        }, new HttpThrowable(), () -> {
        });
    }

    @Override public void onResume() {
        super.onResume();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

    @Override public String getTitle() {
        return "资质认证";
    }

    public static class RecordComfirmVH extends RecyclerView.ViewHolder {
	TextView recordcomfirmTitle;
	TextView recordcomfirmSubtitle;
	TextView recordcomfirmTime;
	TextView recordcomfirmDate;
	ImageView recordImg;
	ImageView img;

        public RecordComfirmVH(View view) {
            super(view);
            recordcomfirmTitle = (TextView) view.findViewById(R.id.recordcomfirm_title);
            recordcomfirmSubtitle = (TextView) view.findViewById(R.id.recordcomfirm_subtitle);
            recordcomfirmTime = (TextView) view.findViewById(R.id.recordcomfirm_time);
            recordcomfirmDate = (TextView) view.findViewById(R.id.recordcomfirm_date);
            recordImg = (ImageView) view.findViewById(R.id.img);
            img = (ImageView) view.findViewById(R.id.recordcomfirm_comfirm);
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
                new RecordComfirmVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recordcomfirm, parent, false));

            return holder;
        }

        @Override public void onBindViewHolder(RecordComfirmVH holder, int position) {
            holder.itemView.setTag(position);
            holder.recordImg.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (listener != null) listener.onItemClick(v, position);
                }
            });
            QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity = datas.get(position);
            holder.recordcomfirmTitle.setText(certificatesEntity.getName());
            holder.recordcomfirmSubtitle.setText("发证单位:" + certificatesEntity.getOrganization().getName());
            if (certificatesEntity.getIs_authenticated()) {
                holder.img.setVisibility(View.VISIBLE);
            } else {
                holder.img.setVisibility(View.INVISIBLE);
            }

            holder.recordcomfirmDate.setText(
                "发证日期:" + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));

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

                    if (c.get(Calendar.YEAR) == 3000) {
                        holder.recordcomfirmTime.setText("长期有效");
                    } else {
                        sb.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getEnd())));
                        holder.recordcomfirmTime.setText(sb.toString());
                    }
                }
            } else {
                holder.recordcomfirmTime.setText("长期有效");
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
