package com.qingchengfit.fitcoach.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;




import cn.qingchengfit.bean.CoursePlan;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCoursePlanFragment extends BaseFragment {
    public static final String TAG = MyCoursePlanFragment.class.getName();
	Toolbar toolbar;
	RecyclerView recyclerview;
	Button courseAdd;
	LinearLayout noData;
	SwipeRefreshLayout refresh;
	TextView toolbarTitle;
	RelativeLayout layoutToolbar;
    @Inject QcRestRepository restRepository;
    private GymsAdapter mGymAdapter;
    private List<CoursePlan> adapterData = new ArrayList<>();

    private CoachService mCoachService;

    public MyCoursePlanFragment() {

    }

    @Override public String getFragmentName() {
        return MyCoursePlanFragment.class.getName();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_gyms, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
      courseAdd = (Button) view.findViewById(R.id.course_add);
      noData = (LinearLayout) view.findViewById(R.id.no_data);
      refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
      toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
      layoutToolbar = (RelativeLayout) view.findViewById(R.id.layout_toolbar);

      view.findViewById(R.id.course_add).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAddCourse();
        }
      });

      toolbarTitle.setText(getString(R.string.my_course_template));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        if (getActivity() instanceof FragActivity && ((FragActivity) getActivity()).getCoachService() != null) {
            mCoachService = ((FragActivity) getActivity()).getCoachService();
            if (mCoachService.getModel() == null || TextUtils.isEmpty(mCoachService.getId())) {
                ToastUtils.show("无场馆信息");
                getActivity().onBackPressed();
                return view;
            }
        } else {
            ToastUtils.show("无场馆信息");
            getActivity().onBackPressed();
            return view;
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mGymAdapter = new GymsAdapter(adapterData);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mGymAdapter.setListener((v, pos) -> {
            Intent toWeb = new Intent(getContext(), WebActivity.class);
            //toWeb.putExtra("url",Configs.ServerIp + "/fitness/redirect/plantpl/detail/?model="+mCoachService.getModel() +"&id="+mCoachService.getId()+"&plan_id="+ adapterData.get(pos).getId());
            toWeb.putExtra("url", adapterData.get(pos).getUrl());
            startActivityForResult(toWeb, pos);
        });
        recyclerview.setAdapter(mGymAdapter);
      //        QcCloudClient.getApi().getApi.qcGetAllPlans(App.coachid).onBackpressureBuffer().subscribeOn(Schedulers.io())
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
        refresh.setOnRefreshListener(() -> freshData());
        return view;
    }

    @Override public void onResume() {
        super.onResume();
        freshData();
    }

    public void freshData() {
        if (getActivity() instanceof FragActivity) {
            CoachService coachService = ((FragActivity) getActivity()).getCoachService();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", coachService.getId());
            params.put("model", coachService.getModel());

            RxRegiste(restRepository.createGetApi(QcCloudClient.GetApi.class)
                .qcGetGymAllPlans(App.coachid, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcAllCoursePlanResponse -> {
                    if (ResponseConstant.checkSuccess(qcAllCoursePlanResponse)) {
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
                }, new NetWorkThrowable()));
        }
    }

 public void onAddCourse() {
        Intent toWeb = new Intent(getContext(), WebActivity.class);
        toWeb.putExtra("url", Configs.Server + "mobile/coaches/add/plans/");
        startActivityForResult(toWeb, 10001);
    }

    //@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
    //    //        if (resultCode == Activity.RESULT_OK){
    //    if (getActivity() instanceof FragActivity) {
    //        CoachService coachService = ((FragActivity) getActivity()).getCoachService();
    //        HashMap<String, Object> params = new HashMap<>();
    //        params.put("id", coachService.getId());
    //        params.put("model", coachService.getModel());
    //        if (resultCode > 1000) {
    //            QcCloudClient.getApi().getApi.qcGetAllPlans(App.coachid,params)
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribe(qcAllCoursePlanResponse -> {
    //                    adapterData.clear();
    //                    adapterData.addAll(qcAllCoursePlanResponse.data.plans);
    //                    mGymAdapter.notifyDataSetChanged();
    //                    recyclerview.scrollToPosition(adapterData.size() - 1);
    //                }, throwable -> {
    //                }, () -> {
    //                });
    //        } else if (requestCode >= 0 && requestCode < 10000) {
    //            QcCloudClient.getApi().getApi.qcGetAllPlans(App.coachid,params)
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribe(qcAllCoursePlanResponse -> {
    //                    adapterData.clear();
    //                    adapterData.addAll(qcAllCoursePlanResponse.data.plans);
    //                    mGymAdapter.notifyItemChanged(requestCode);
    //                }, throwable -> {
    //                }, () -> {
    //                });
    //        }
    //    }
    //}

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

    public static class GymsVH extends RecyclerView.ViewHolder {
	ImageView itemGymHeader;
	TextView itemGymName;
	TextView itemGymPhonenum;
	ImageView itemIsPersonal;
	TextView itemItemBrand;
	ImageView itemRight;

        public GymsVH(View view) {
            super(view);
          itemGymHeader = (ImageView) view.findViewById(R.id.item_gym_header);
          itemGymName = (TextView) view.findViewById(R.id.item_gym_name);
          itemGymPhonenum = (TextView) view.findViewById(R.id.item_gym_phonenum);
          itemIsPersonal = (ImageView) view.findViewById(R.id.qc_identify);
          itemItemBrand = (TextView) view.findViewById(R.id.item_gym_brand);
          itemRight = (ImageView) view.findViewById(R.id.item_right);
        }
    }

    class GymsAdapter extends RecyclerView.Adapter<GymsVH> implements View.OnClickListener {
        private List<CoursePlan> datas;
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

        @Override public GymsVH onCreateViewHolder(ViewGroup parent, int viewType) {
            GymsVH holder = new GymsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(GymsVH holder, int position) {
            holder.itemView.setTag(position);
            CoursePlan detail = datas.get(position);
            holder.itemItemBrand.setVisibility(View.GONE);
            holder.itemGymName.setText(detail.getName());
            StringBuffer sb = new StringBuffer();
            for (String s : detail.getTags()) {
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
            holder.itemRight.setVisibility(View.GONE);

        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null && (int) v.getTag() < datas.size()) listener.onItemClick(v, (int) v.getTag());
        }
    }
}
