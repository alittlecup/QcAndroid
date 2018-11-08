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
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGymsFragment extends BaseFragment {
    public static final String TAG = MyGymsFragment.class.getName();
	Toolbar toolbar;
	RecyclerView recyclerview;
	SwipeRefreshLayout refresh;
	SwipeRefreshLayout refreshNodata;
    private GymsAdapter mGymAdapter;
    private List<CoachService> adapterData = new ArrayList<>();
    private boolean mHasPrivate = false;


    public MyGymsFragment() {
    }

    @Override public String getFragmentName() {
        return MyGymsFragment.class.getName();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_gyms_true, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
      refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
      refreshNodata = (SwipeRefreshLayout) view.findViewById(R.id.refresh_nodata);

      view.findViewById(R.id.course_add_private).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickprivate();
        }
      });
      view.findViewById(R.id.course_add_belong).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickBelong();
        }
      });

      toolbar.setTitle("我的健身房");
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        //        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        //        toolbar.inflateMenu(R.menu.add);
        toolbar.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), FragActivity.class);
            intent.putExtra("type", 3);
            startActivityForResult(intent, 11);
            return true;
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mGymAdapter = new GymsAdapter(adapterData);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mGymAdapter);
        mGymAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), FragActivity.class);
                intent.putExtra("id", adapterData.get(pos).id);
                //                intent.putExtra("isPrivate", adapterData.get(pos).);
                intent.putExtra("model", adapterData.get(pos).model);
                intent.putExtra("type", 6);
                startActivityForResult(intent, 11);
            }
        });

        freshData();
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                freshData();
            }
        });
        refreshNodata.setColorSchemeResources(R.color.primary);
        refreshNodata.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                freshData();
            }
        });
        return view;
    }

 public void onClickprivate() {
        Intent intent = new Intent(getActivity(), FragActivity.class);
        if (mHasPrivate) {
            intent.putExtra("type", 2);
        } else {
            intent.putExtra("type", 3);
        }
        startActivityForResult(intent, 11);
    }

 public void onClickBelong() {
        Intent intent = new Intent(getActivity(), FragActivity.class);
        intent.putExtra("type", 4);
        startActivityForResult(intent, 11);
    }

    @Override public void onResume() {
        super.onResume();
        freshData();
    }

    public void freshData() {

        TrainerRepository.getStaticTrainerAllApi().qcGetCoachService(App.coachid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .map(qcCoachSystemDetailResponse -> {
                adapterData.clear();
                adapterData.addAll(qcCoachSystemDetailResponse.data.services);
                if (adapterData.size() > 0) {

                    refresh.setVisibility(View.VISIBLE);
                    refreshNodata.setVisibility(View.GONE);
                } else {
                    mHasPrivate = false;
                    refresh.setVisibility(View.GONE);
                    refreshNodata.setVisibility(View.VISIBLE);
                }
                for (CoachService service : qcCoachSystemDetailResponse.data.services) {
                    if (service.model.equals("service") && service.type == 1) {
                        mHasPrivate = true;
                        break;
                    } else {
                        mHasPrivate = false;
                    }
                }
                if (mHasPrivate) {
                    toolbar.getMenu().clear();
                } else {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.add);
                }
                return true;
            })
            .subscribe(aBoolean -> {
                mGymAdapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
                refreshNodata.setRefreshing(false);
            },new HttpThrowable(), () -> {
            });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) {

        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

    public static class GymsVH extends RecyclerView.ViewHolder {
	ImageView itemGymHeader;
	TextView itemGymName;
	TextView itemGymPhonenum;
	ImageView itemIsPersonal;
	TextView brand;

        public GymsVH(View view) {
            super(view);
          itemGymHeader = (ImageView) view.findViewById(R.id.item_gym_header);
          itemGymName = (TextView) view.findViewById(R.id.item_gym_name);
          itemGymPhonenum = (TextView) view.findViewById(R.id.item_gym_phonenum);
          itemIsPersonal = (ImageView) view.findViewById(R.id.qc_identify);
          brand = (TextView) view.findViewById(R.id.item_gym_brand);

        }
    }

    class GymsAdapter extends RecyclerView.Adapter<GymsVH> implements View.OnClickListener {

        private List<CoachService> datas;
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
            CoachService detail = datas.get(position);
            holder.itemGymName.setText(detail.name);
            holder.itemGymPhonenum.setText(detail.courses_count + "门课程, " + detail.users_count + "名学员");
            holder.brand.setText(detail.brand_name);
            if (detail.model.equals("service") && detail.type == 1) {
                holder.itemIsPersonal.setVisibility(View.GONE);
            } else {
                holder.itemIsPersonal.setVisibility(View.VISIBLE);
            }
            Glide.with(App.AppContex)
                .load(PhotoUtils.getSmall(detail.photo))
                .asBitmap()
                .placeholder(R.drawable.ic_default_header)
                .error(R.drawable.ic_default_header)
                .into(new CircleImgWrapper(holder.itemGymHeader, App.AppContex));
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }
}
