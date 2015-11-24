package com.qingchengfit.fitcoach.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.component.GridViewInScroll;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TagGroup;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkExperienceFragment extends BaseFragment {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.record_comfirm_no_img)
    ImageView recordComfirmNoImg;
    @Bind(R.id.record_comfirm_no_txt)
    TextView recordComfirmNoTxt;
    @Bind(R.id.record_confirm_none)
    RelativeLayout recordConfirmNone;
    private QcExperienceResponse qcExperienceResponse;
    private WorkExperiencAdapter adapter;

    public WorkExperienceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || isVisible) {
            return;
        }
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        QcCloudClient.getApi().getApi.qcGetExperiences(App.coachid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcExperienceResponse -> {
//                                getActivity().runOnUiThread(() -> {
                            if (recyclerview != null) {
                                if (qcExperienceResponse.getData().getExperiences() != null && qcExperienceResponse.getData().getExperiences().size() > 0) {
                                    recyclerview.setVisibility(View.VISIBLE);
                                    recordConfirmNone.setVisibility(View.GONE);
                                    adapter = new WorkExperiencAdapter(qcExperienceResponse.getData().getExperiences());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public static class WorkExperienceVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_time)
        TextView itemTime;
        @Bind(R.id.item_studio_name)
        TextView itemStudioName;
        @Bind(R.id.item_studio_comfirm)
        TextView itemStudioComfirm;
        @Bind(R.id.item_studio_pos)
        TextView itemStudioPos;
        @Bind(R.id.item_studio_des)
        TextView itemStudioDes;
        @Bind(R.id.item_studio_complish)
        TextView itemStudioComplish;
        @Bind(R.id.item_studio_classes)
        GridViewInScroll itemStudioClasses;
        @Bind(R.id.item_tag_group)
        TagGroup itemTagGroup;
//        @Bind(R.id.item_studio_expaned)
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

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_work_classes.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.item_workclass_name)
        TextView itemWorkclassName;
        @Bind(R.id.item_workclass_num)
        TextView itemWorkclassNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class WorkExperiencAdapter extends RecyclerView.Adapter<WorkExperienceVH> {


        private List<QcExperienceResponse.DataEntity.ExperiencesEntity> datas;

        public WorkExperiencAdapter(List datas) {
            this.datas = datas;
        }

        @Override
        public WorkExperienceVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WorkExperienceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workexperience, parent, false));
        }

        @Override
        public void onBindViewHolder(WorkExperienceVH holder, int position) {
            if (position >= datas.size())
                return;
            QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity = datas.get(position);
//
            holder.itemStudioName.setText(experiencesEntity.getGym().getName());
            SpannableString ssPosition = new SpannableString("职位: " + experiencesEntity.getPosition());
//            ssPosition.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_grey)), 0, 3 , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.itemStudioPos.setText(ssPosition);
            ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.primary));
            drawable.setAlpha(20);
            holder.itemTime.setBackgroundDrawable(drawable);

            holder.itemStudioComplish.setVisibility(View.VISIBLE);
            StringBuffer sb = new StringBuffer();
            sb.append("业绩: ");
            if (experiencesEntity.getGroup_course() == 0 &&
                    experiencesEntity.getPrivate_course() == 0 &&
                    experiencesEntity.getSale() == 0 &&
                    experiencesEntity.getGroup_user() == 0 &&
                    experiencesEntity.getPrivate_user() == 0
                    ) {
                sb.append("无");
                holder.itemStudioComplish.setText(sb.toString());
            } else {
                if (experiencesEntity.getGroup_course() != 0 || experiencesEntity.getGroup_user() != 0) {
                    sb.append("团课");
                    sb.append(experiencesEntity.getGroup_course());
                    sb.append("节,服务");
                    sb.append(experiencesEntity.getGroup_user());
                    sb.append("人次;");

                }
                if (experiencesEntity.getPrivate_course() != 0 || experiencesEntity.getPrivate_user() != 0) {
                    sb.append("私教课");
                    sb.append(experiencesEntity.getPrivate_course());
                    sb.append("节,服务");
                    sb.append(experiencesEntity.getPrivate_user());
                    sb.append("人次;");
                }
                if (experiencesEntity.getSale() != 0) {
                    sb.append("销售额达");
                    sb.append(experiencesEntity.getSale());
                    sb.append("元.");
                }
                holder.itemStudioComplish.setText(sb.toString());

            }


//            SpannableString sy = new SpannableString(sb.toString());
//            sy.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_grey)), 0, 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//            holder.itemStudioComplish.setText(sy);

            SpannableString sDes = new SpannableString("描述: " + experiencesEntity.getDescription());
//            sDes.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_grey)), 0, 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.itemStudioDes.setText(sDes);

            StringBuffer ss = new StringBuffer();
            ss.append(DateUtils.getServerDateDay(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            ss.append(getContext().getString(R.string.comom_time_to));
            Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000)
                ss.append(getContext().getString(R.string.common_today));
            else
                ss.append(DateUtils.getServerDateDay(d));
            holder.itemTime.setText(ss.toString());
//            holder.itemTagGroup.setTags("非常好", "长得帅", "一般般嘛");
//            List<String> aaaas = new ArrayList<>();
//            aaaas.add("12");
//            aaaas.add("12");
//            aaaas.add("12");
//            aaaas.add("12");
//            GridInScrollAdapter adapter1 = new GridInScrollAdapter(aaaas);
//            holder.itemStudioClasses.setAdapter(adapter1);
            if (experiencesEntity.getIs_authenticated()) {
                holder.itemStudioComfirm.setText("已确认");
                holder.itemStudioComfirm.setBackgroundResource(R.drawable.bg_tag_green);
            } else {
                holder.itemStudioComfirm.setText("待确认");
                holder.itemStudioComfirm.setBackgroundResource(R.drawable.bg_tag_red);
            }
        }

        @Override
        public int getItemCount() {
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

    class GridInScrollAdapter extends BaseAdapter {
        List data;

        public GridInScrollAdapter(List data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_classes, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            return view;
        }


    }
}
