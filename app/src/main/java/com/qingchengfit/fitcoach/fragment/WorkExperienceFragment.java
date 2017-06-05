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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.VpFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkExperienceFragment extends VpFragment {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.record_comfirm_no_img) ImageView recordComfirmNoImg;
    @BindView(R.id.record_comfirm_no_txt) TextView recordComfirmNoTxt;
    @BindView(R.id.record_confirm_none) RelativeLayout recordConfirmNone;
    private QcExperienceResponse qcExperienceResponse;
    private WorkExperiencAdapter adapter;
    private Unbinder unbinder;

    public WorkExperienceFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
        unbinder = ButterKnife.bind(this, view);
        //        isPrepared = true;
        lazyLoad();
        return view;
    }

    //    @Override
    protected void lazyLoad() {
        //        if (!isPrepared || isVisible) {
        //            return;
        //        }
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(true);
        QcCloudClient.getApi().getApi.qcGetExperiences(App.coachid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(qcExperienceResponse -> {
                //                                getActivity().runOnUiThread(() -> {
                if (recyclerview != null) {
                    if (qcExperienceResponse.getData().getExperiences() != null && qcExperienceResponse.getData().getExperiences().size() > 0) {
                        recyclerview.setVisibility(View.VISIBLE);
                        recordConfirmNone.setVisibility(View.GONE);
                        int i = 0;
                        List<QcExperienceResponse.DataEntity.ExperiencesEntity> datas =
                            new ArrayList<QcExperienceResponse.DataEntity.ExperiencesEntity>();
                        for (QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity : qcExperienceResponse.getData()
                            .getExperiences()) {
                            if (!experiencesEntity.is_hidden()) datas.add(experiencesEntity);
                            }
                        adapter = new WorkExperiencAdapter(datas);
                        recyclerview.setAdapter(adapter);
                    } else {
                        recyclerview.setVisibility(View.GONE);
                        recordComfirmNoImg.setImageResource(R.drawable.img_no_experience);
                        recordComfirmNoTxt.setText("您还没有添加任何工作经历请在设置页面中添加");
                        recordConfirmNone.setVisibility(View.VISIBLE);
                        }
                }
            }, throwable -> {
            }, () -> {
            }

        );
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public String getTitle() {
        return "工作经历";
    }

    public static class WorkExperienceVH extends RecyclerView.ViewHolder {
        @BindView(R.id.gym_img) ImageView gymImg;
        @BindView(R.id.gym_identify) ImageView gymIdentify;
        @BindView(R.id.gym_name) TextView gymName;
        @BindView(R.id.gym_address) TextView gymAddress;
        @BindView(R.id.gym_time) TextView gymTime;
        @BindView(R.id.workexp_detail_position) TextView workexpDetailPosition;
        @BindView(R.id.workexp_detail_desc) TextView workexpDetailDesc;
        @BindView(R.id.workexp_detail_group_count) TextView workexpDetailGroupCount;
        @BindView(R.id.workexp_detail_group_server) TextView workexpDetailGroupServer;
        @BindView(R.id.workexp_detail_group_layout) LinearLayout workexpDetailGroupLayout;
        @BindView(R.id.workexp_detail_private_count) TextView workexpDetailPrivateCount;
        @BindView(R.id.workexp_detail_private_server) TextView workexpDetailPrivateServer;
        @BindView(R.id.workexp_detail_private_layout) LinearLayout workexpDetailPrivateLayout;
        @BindView(R.id.workexp_detail_sale) TextView workexpDetailSale;
        @BindView(R.id.workexp_detail_sale_layout) LinearLayout workexpDetailSaleLayout;
        //        @BindView(R.id.item_studio_expaned)
        //        ToggleButton itemStudioExpaned;

        public WorkExperienceVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //            itemStudioExpaned.setOnCheckedChangeListener((compoundButton, b) -> {
            //                if (b) {
            //                    itemStudioClasses.setVisibility(View.VISIBLE);
            //                    itemTagGroup.setVisibility(View.VISIBLE);
            //                } else {
            //                    itemTagGroup.setVisibility(View.GONE);
            //                    itemStudioClasses.setVisibility(View.GONE);
            //                }
            //            });

        }
    }

    class WorkExperiencAdapter extends RecyclerView.Adapter<WorkExperienceVH> {

        private List<QcExperienceResponse.DataEntity.ExperiencesEntity> datas;

        public WorkExperiencAdapter(List datas) {
            this.datas = datas;
        }

        @Override public WorkExperienceVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WorkExperienceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workexperience, parent, false));
        }

        @Override public void onBindViewHolder(WorkExperienceVH holder, int position) {
            if (position >= datas.size()) return;
            QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity = datas.get(position);
            if (experiencesEntity.is_hidden()) {
                holder.itemView.setVisibility(View.GONE);
                return;
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }

            QcExperienceResponse.DataEntity.ExperiencesEntity.GymEntity gym = experiencesEntity.getGym();
            holder.gymName.setText(gym.getName());
            if (gym.getDistrict() != null && gym.getDistrict().city != null && !TextUtils.isEmpty(gym.getDistrict().city.name)) {
                holder.gymAddress.setText("|" + gym.getDistrict().city.name);
            }
            //设置工作时间
            String start = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(experiencesEntity.getStart()));
            Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000) {
                start = start + "至今";
            } else {
                start = start + "至" + DateUtils.Date2YYYYMMDD(d);
            }
            holder.gymTime.setText(start);
            if (experiencesEntity.is_authenticated()) {
                holder.gymIdentify.setVisibility(View.VISIBLE);
            } else {
                holder.gymIdentify.setVisibility(View.GONE);
            }
            Glide.with(App.AppContex).load(gym.getPhoto()).asBitmap().into(new CircleImgWrapper(holder.gymImg, App.AppContex));
            holder.workexpDetailPosition.setText(experiencesEntity.getPosition());
            holder.workexpDetailDesc.setText(experiencesEntity.getDescription());
            if (experiencesEntity.getGroup_course() != 0) {
                holder.workexpDetailGroupLayout.setVisibility(View.VISIBLE);
                holder.workexpDetailGroupCount.setText(experiencesEntity.getGroup_course() + "");
                holder.workexpDetailGroupServer.setText(experiencesEntity.getGroup_user() + "");
            } else {
                holder.workexpDetailGroupLayout.setVisibility(View.GONE);
            }
            if (experiencesEntity.getPrivate_course() != 0) {
                holder.workexpDetailPrivateLayout.setVisibility(View.VISIBLE);
                holder.workexpDetailPrivateCount.setText(experiencesEntity.getPrivate_course() + "");
                holder.workexpDetailPrivateServer.setText(experiencesEntity.getPrivate_user() + "");
            } else {
                holder.workexpDetailPrivateLayout.setVisibility(View.GONE);
            }
            if (Float.parseFloat(experiencesEntity.getSale()) != 0) {
                holder.workexpDetailSaleLayout.setVisibility(View.VISIBLE);
                holder.workexpDetailSale.setText(experiencesEntity.getSale() + "");
            } else {
                holder.workexpDetailSaleLayout.setVisibility(View.GONE);
            }

            //            holder.itemStudioName.setText(experiencesEntity.getGym().getName());
            //            SpannableString ssPosition = new SpannableString("职位: " + experiencesEntity.getPosition());
            //            holder.itemStudioPos.setText(ssPosition);
            //            ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.primary));
            //            drawable.setAlpha(20);
            //            holder.itemTime.setBackgroundDrawable(drawable);
            //
            //            holder.itemStudioComplish.setVisibility(View.VISIBLE);
            //            StringBuffer sb = new StringBuffer();
            //            sb.append("业绩: ");
            //            if (experiencesEntity.getGroup_course() == 0 &&
            //                    experiencesEntity.getPrivate_course() == 0 &&
            //                    experiencesEntity.getSale() == 0 &&
            //                    experiencesEntity.getGroup_user() == 0 &&
            //                    experiencesEntity.getPrivate_user() == 0
            //                    ) {
            //                sb.append("无");
            //                holder.itemStudioComplish.setText(sb.toString());
            //            } else {
            //                if (experiencesEntity.getGroup_course() != 0 || experiencesEntity.getGroup_user() != 0) {
            //                    sb.append("团课");
            //                    sb.append(experiencesEntity.getGroup_course());
            //                    sb.append("节,服务");
            //                    sb.append(experiencesEntity.getGroup_user());
            //                    sb.append("人次;");
            //
            //                }
            //                if (experiencesEntity.getPrivate_course() != 0 || experiencesEntity.getPrivate_user() != 0) {
            //                    sb.append("私教课");
            //                    sb.append(experiencesEntity.getPrivate_course());
            //                    sb.append("节,服务");
            //                    sb.append(experiencesEntity.getPrivate_user());
            //                    sb.append("人次;");
            //                }
            //                if (experiencesEntity.getSale() != 0) {
            //                    sb.append("销售额达");
            //                    sb.append(experiencesEntity.getSale());
            //                    sb.append("元;");
            //                }
            //                holder.itemStudioComplish.setText(sb.toString());
            //
            //            }
            //
            //
            //
            //            SpannableString sDes = new SpannableString("描述: " + experiencesEntity.getDescription());
            //            holder.itemStudioDes.setText(sDes);
            //
            //            StringBuffer ss = new StringBuffer();
            //            ss.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            //            ss.append(getContext().getString(R.string.comom_time_to));
            //            Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
            //            Calendar c = Calendar.getInstance(Locale.getDefault());
            //            c.setTime(d);
            //            if (c.get(Calendar.YEAR) == 3000)
            //                ss.append(getContext().getString(R.string.common_today));
            //            else
            //                ss.append(DateUtils.Date2YYYYMMDD(d));
            //            holder.itemTime.setText(ss.toString());
            //            if (experiencesEntity.getIs_authenticated()) {
            //                holder.itemStudioComfirm.setText("已确认");
            //                holder.itemStudioComfirm.setBackgroundResource(R.drawable.bg_tag_green);
            //            } else {
            //                holder.itemStudioComfirm.setText("待确认");
            //                holder.itemStudioComfirm.setBackgroundResource(R.drawable.bg_tag_red);
            //            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }
    }

    class ClassBean {
        String name;
        String classNum;
        String studentNum;

        public ClassBean(String name, String classNum, String studentNum) {
            this.name = name;
            this.classNum = classNum;
            this.studentNum = studentNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassNum() {
            return classNum;
        }

        public void setClassNum(String classNum) {
            this.classNum = classNum;
        }

        public String getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(String studentNum) {
            this.studentNum = studentNum;
        }
    }

    //    class GridInScrollAdapter extends BaseAdapter {
    //        List data;
    //
    //        public GridInScrollAdapter(List data) {
    //            this.data = data;
    //        }
    //
    //        @Override
    //        public int getCount() {
    //            return data.size();
    //        }
    //
    //        @Override
    //        public Object getItem(int i) {
    //            return data.get(i);
    //        }
    //
    //        @Override
    //        public long getItemId(int i) {
    //            return i;
    //        }
    //
    //        @Override
    //        public View getView(int i, View view, ViewGroup viewGroup) {
    //            ViewHolder holder = null;
    //            if (view == null) {
    //                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_classes, null);
    //                holder = new ViewHolder(view);
    //                view.setTag(holder);
    //            } else {
    //                holder = (ViewHolder) view.getTag();
    //            }
    //
    //            return view;
    //        }
    //
    //
    //    }
}
