package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/7 2015.
 */
public class WorkExepSettingFragment extends BaseSettingFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.record_comfirm_no_img)
    ImageView recordComfirmNoImg;
    @BindView(R.id.record_comfirm_no_txt)
    TextView recordComfirmNoTxt;
    @BindView(R.id.record_confirm_none)
    RelativeLayout recordConfirmNone;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private WorkExepAdapter adapter;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workexp_list, container, false);
        unbinder=ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.add, 0, getActivity().getString(R.string.workexper_title));
        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                fragmentCallBack.onFragmentChange(WorkExpeEditFragment.newInstance("添加工作经历", null));
                Intent toSearch = new Intent(getActivity(), SearchActivity.class);
                toSearch.putExtra("type", SearchFragment.TYPE_GYM);
                startActivityForResult(toSearch, 10010);
                return false;
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerview.setItemAnimator(new DefaultItemAnimator());


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
                refresh.setRefreshing(true);
                freshData();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("onActivityReslut:" + requestCode + "  " + requestCode);
        if (requestCode == 10010 && resultCode > 0) {
            QcExperienceResponse.DataEntity.ExperiencesEntity entity = new QcExperienceResponse.DataEntity.ExperiencesEntity();
            QcExperienceResponse.DataEntity.ExperiencesEntity.GymEntity gymEntity = new QcExperienceResponse.DataEntity.ExperiencesEntity.GymEntity();
            gymEntity.setId(data.getLongExtra("id", 0));
            gymEntity.setName(data.getStringExtra("username"));
            gymEntity.setAddress(data.getStringExtra("address"));
            gymEntity.setPhoto(data.getStringExtra("pic"));
            gymEntity.setIs_authenticated(data.getBooleanExtra("isauth", false));
            entity.setGym(gymEntity);

            fragmentCallBack.onFragmentChange(WorkExpeEditFragment.newInstance("添加工作经历", entity,true));

        }
    }

    public void freshData() {

        QcCloudClient.getApi().getApi.qcGetExperiences(App.coachid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcExperienceResponse -> {
                            if (recyclerview != null) {
                                if (qcExperienceResponse.getData().getExperiences() != null && qcExperienceResponse.getData().getExperiences().size() > 0) {
                                    recyclerview.setVisibility(View.VISIBLE);
                                    recordConfirmNone.setVisibility(View.GONE);
                                    adapter = new WorkExepAdapter(qcExperienceResponse.getData().getExperiences());
                                    adapter.setListener(new OnRecycleItemClickListener() {
                                        @Override
                                        public void onItemClick(View v, int pos) {
                                            fragmentCallBack.onFragmentChange(WorkExpDetailFragment.newInstance(qcExperienceResponse.getData().getExperiences().get(pos).getId()));
                                        }
                                    });
                                    recyclerview.setAdapter(adapter);
                                } else {
                                    recyclerview.setVisibility(View.GONE);
                                    recordComfirmNoImg.setImageResource(R.drawable.img_no_experience);
                                    recordComfirmNoTxt.setText("您还没有添加任何工作经历请点击添加按钮");
                                    recordConfirmNone.setVisibility(View.VISIBLE);
                                }
                                refresh.setRefreshing(false);
                            }
                        }, throwable -> {
                        }, () -> {
                        }
                );
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class WorkExepVH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_workexpe_name)
        TextView itemWorkexpeName;
        @BindView(R.id.item_workexpe_time)
        TextView itemWorkexpeTime;
        @BindView(R.id.item_workexpe)
        LinearLayout itemWorkexpe;
        @BindView(R.id.item_workexpe_address)
        TextView itemAddress;
        @BindView(R.id.qc_identify)
        ImageView qcIdentify;
        @BindView(R.id.item_workexpe_hidden)
        View isHidden;
        @BindView(R.id.item_workexpe_img)
        ImageView img;

        public WorkExepVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class WorkExepAdapter extends RecyclerView.Adapter<WorkExepVH> implements View.OnClickListener {


        private List<QcExperienceResponse.DataEntity.ExperiencesEntity> datas;
        private OnRecycleItemClickListener listener;

        public WorkExepAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public WorkExepVH onCreateViewHolder(ViewGroup parent, int viewType) {
            WorkExepVH holder = new WorkExepVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workexpe, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(WorkExepVH holder, int position) {
            holder.itemView.setTag(position);
            QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity = datas.get(position);

            holder.itemWorkexpeName.setText(experiencesEntity.getGym().getName());
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            stringBuffer.append(getContext().getString(R.string.comom_time_to));
            Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000)
                stringBuffer.append(getContext().getString(R.string.common_today));
            else
                stringBuffer.append(DateUtils.Date2YYYYMMDD(d));
            holder.itemWorkexpeTime.setText(stringBuffer.toString());
            if (experiencesEntity.is_authenticated())
                holder.qcIdentify.setVisibility(View.VISIBLE);
            else holder.qcIdentify.setVisibility(View.GONE);
            if (experiencesEntity.getGym().getDistrict() != null && experiencesEntity.getGym().getDistrict().city != null &&
                    !TextUtils.isEmpty(experiencesEntity.getGym().getDistrict().city.name))
                holder.itemAddress.setText("|" + experiencesEntity.getGym().getDistrict().city.name);

            if (experiencesEntity.is_hidden()) {
                holder.isHidden.setVisibility(View.VISIBLE);
                holder.itemWorkexpeTime.setText("已隐藏");
            } else {
                holder.isHidden.setVisibility(View.GONE);
            }
            Glide.with(App.AppContex).load(experiencesEntity.getGym().getPhoto()).asBitmap().into(new CircleImgWrapper(holder.img, App.AppContex));
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
