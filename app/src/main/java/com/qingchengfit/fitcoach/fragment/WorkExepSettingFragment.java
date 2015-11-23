package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
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

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.record_comfirm_no_img)
    ImageView recordComfirmNoImg;
    @Bind(R.id.record_comfirm_no_txt)
    TextView recordComfirmNoTxt;
    @Bind(R.id.record_confirm_none)
    RelativeLayout recordConfirmNone;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    private WorkExepAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.add, 0, getActivity().getString(R.string.workexper_title));
        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragmentCallBack.onFragmentChange(WorkExpeEditFragment.newInstance("添加工作经历", null));
                return false;
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
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
                refresh.setRefreshing(true);
            }
        });

        return view;
    }

    public void freshData() {

        QcCloudClient.getApi().getApi.qcGetExperiences(App.coachid)
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
                                            fragmentCallBack.onFragmentChange(WorkExpeEditFragment.newInstance("编辑工作经历", qcExperienceResponse.getData().getExperiences().get(pos)));
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
        ButterKnife.unbind(this);
    }


    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class WorkExepVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_workexpe_name)
        TextView itemWorkexpeName;
        @Bind(R.id.item_workexpe_time)
        TextView itemWorkexpeTime;
        @Bind(R.id.item_workexpe)
        LinearLayout itemWorkexpe;

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
            stringBuffer.append(DateUtils.getServerDateDay(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            stringBuffer.append(getContext().getString(R.string.comom_time_to));
            Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000)
                stringBuffer.append(getContext().getString(R.string.common_today));
            else
                stringBuffer.append(DateUtils.getServerDateDay(d));
            holder.itemWorkexpeTime.setText(stringBuffer.toString());
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
