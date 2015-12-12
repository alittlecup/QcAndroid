package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcAllCoursePlanResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCoursePlanFragment extends MainBaseFragment {
    public static final String TAG = MyCoursePlanFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.course_add)
    Button courseAdd;
    @Bind(R.id.no_data)
    LinearLayout noData;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    private GymsAdapter mGymAdapter;
    private List<QcAllCoursePlanResponse.Plan> adapterData = new ArrayList<>();

    public MyCoursePlanFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_gyms, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(getString(R.string.my_course_template));
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.inflateMenu(R.menu.add);
        toolbar.setOnMenuItemClickListener(item -> {
            Intent toWeb = new Intent(getContext(), WebActivity.class);
            toWeb.putExtra("url", Configs.Server + "mobile/coaches/add/plans/");
            startActivityForResult(toWeb, 10001);
            return true;
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mGymAdapter = new GymsAdapter(adapterData);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mGymAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent toWeb = new Intent(getContext(), WebActivity.class);
                toWeb.putExtra("url", adapterData.get(pos).url);
                startActivityForResult(toWeb, pos);
            }
        });
        recyclerview.setAdapter(mGymAdapter);
//        QcCloudClient.getApi().getApi.qcGetAllPlans(App.coachid).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(qcAllCoursePlanResponse -> {
//                    adapterData.clear();
//                    adapterData.addAll(qcAllCoursePlanResponse.data.plans);
//                    if (adapterData.size() == 0) {
//                        noData.setVisibility(View.VISIBLE);
//                    } else {
//                        noData.setVisibility(View.GONE);
//                        mGymAdapter.notifyDataSetChanged();
//                    }
//
//
//                });
        freshData();
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshData();
            }
        });
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            freshData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        freshData();
    }

    public void freshData() {
        QcCloudClient.getApi().getApi.qcGetAllPlans(App.coachid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcAllCoursePlanResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcAllCoursePlanResponse qcAllCoursePlanResponse) {
                        adapterData.clear();
                        adapterData.addAll(qcAllCoursePlanResponse.data.plans);
                        if (adapterData.size() == 0) {
                            refresh.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        } else {
                            noData.setVisibility(View.GONE);
                            refresh.setVisibility(View.VISIBLE);
                            mGymAdapter.notifyDataSetChanged();
                        }
                        refresh.setRefreshing(false);
                    }
                });
    }


    @OnClick(R.id.course_add)
    public void onAddCourse() {
        Intent toWeb = new Intent(getContext(), WebActivity.class);
        toWeb.putExtra("url", Configs.Server + "mobile/coaches/add/plans/");
        startActivityForResult(toWeb, 10001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK){

        if (resultCode > 1000) {
            QcCloudClient.getApi().getApi.qcGetAllPlans(App.coachid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(qcAllCoursePlanResponse -> {
                        adapterData.clear();
                        adapterData.addAll(qcAllCoursePlanResponse.data.plans);
                        mGymAdapter.notifyDataSetChanged();
                        recyclerview.scrollToPosition(adapterData.size() - 1);
                    }, throwable -> {
                    }, () -> {
                    });
        } else if (requestCode >= 0 && requestCode < 10000) {
            QcCloudClient.getApi().getApi.qcGetAllPlans(App.coachid).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(qcAllCoursePlanResponse -> {
                        adapterData.clear();
                        adapterData.addAll(qcAllCoursePlanResponse.data.plans);
                        mGymAdapter.notifyItemChanged(requestCode);
                    }, throwable -> {
                    }, () -> {
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static class GymsVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_gym_header)
        ImageView itemGymHeader;
        @Bind(R.id.item_gym_name)
        TextView itemGymName;
        @Bind(R.id.item_gym_phonenum)
        TextView itemGymPhonenum;
        @Bind(R.id.qc_identify)
        ImageView itemIsPersonal;

        public GymsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GymsAdapter extends RecyclerView.Adapter<GymsVH> implements View.OnClickListener {
        private List<QcAllCoursePlanResponse.Plan> datas;
        private OnRecycleItemClickListener listener;


        public GymsAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public GymsVH onCreateViewHolder(ViewGroup parent, int viewType) {
            GymsVH holder = new GymsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(GymsVH holder, int position) {
            holder.itemView.setTag(position);
            QcAllCoursePlanResponse.Plan detail = datas.get(position);

            holder.itemGymName.setText(detail.name);
            StringBuffer sb = new StringBuffer();
            for (String s : detail.tags) {
                sb.append(s);
                sb.append(",");
            }
            if (sb.length() > 2) {
                holder.itemGymPhonenum.setText(sb.substring(0, sb.length() - 1));
            }
            if (detail.type == 1) {//个人
                holder.itemIsPersonal.setVisibility(View.GONE);
            } else { //2  所属  3 会议
                holder.itemIsPersonal.setVisibility(View.VISIBLE);
            }
            holder.itemGymHeader.setVisibility(View.GONE);
        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null && (int) v.getTag() < datas.size())
                listener.onItemClick(v, (int) v.getTag());
        }
    }
}
