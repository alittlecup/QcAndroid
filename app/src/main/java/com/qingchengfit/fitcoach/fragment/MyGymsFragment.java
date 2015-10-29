package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemDetailResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGymsFragment extends MainBaseFragment {
    public static final String TAG = MyGymsFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private GymsAdapter mGymAdapter;
    private List<QcCoachSystemDetailResponse.CoachSystemDetail> adapterData = new ArrayList<>();
    private boolean mHasPrivate = false;

    public MyGymsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_gyms, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("我的健身房");
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.inflateMenu(R.menu.add_gym);
        toolbar.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), FragActivity.class);
            if (item.getItemId() == R.id.action_add_self) {
                if (mHasPrivate) {
                    intent.putExtra("type", 2);
                } else {
                    intent.putExtra("type", 3);
                }
                startActivityForResult(intent, 11);
            } else if (item.getItemId() == R.id.action_add_public) {
                intent.putExtra("type", 4);
                startActivityForResult(intent, 11);
            }

            return true;
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mGymAdapter = new GymsAdapter(adapterData);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mGymAdapter);
        mGymAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent toWeb = new Intent(getActivity(), FragActivity.class);
                toWeb.putExtra("host", adapterData.get(pos).url);
                toWeb.putExtra("id", adapterData.get(pos).id);
                toWeb.putExtra("isPrivate", adapterData.get(pos).is_personal_system);
                toWeb.putExtra("type", 5);
                startActivityForResult(toWeb, 404);
//                Intent toWeb = new Intent(getActivity() , WebActivity.class);
//                toWeb.putExtra("url",adapterData.get(pos).url+"/mobile/coach/shop/welcome/");
//                startActivity(toWeb);
            }
        });
        QcCloudClient.getApi().getApi.qcGetCoachSystemDetail(App.coachid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(qcCoachSystemDetailResponse -> {
                    adapterData.clear();
                    adapterData.addAll(qcCoachSystemDetailResponse.date.systems);
                    for (QcCoachSystemDetailResponse.CoachSystemDetail systemDetail : qcCoachSystemDetailResponse.date.systems) {
                        if (systemDetail.is_personal_system) {
                            mHasPrivate = true;
                            break;
                        } else mHasPrivate = false;

                    }
                    return true;
                })
                .subscribe(aBoolean -> mGymAdapter.notifyDataSetChanged());
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) {

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
        @Bind(R.id.item_is_personal)
        TextView itemIsPersonal;

        public GymsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GymsAdapter extends RecyclerView.Adapter<GymsVH> implements View.OnClickListener {


        private List<QcCoachSystemDetailResponse.CoachSystemDetail> datas;
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
            QcCoachSystemDetailResponse.CoachSystemDetail detail = datas.get(position);
            holder.itemGymName.setText(detail.name);
            holder.itemGymPhonenum.setText(detail.courses_count + "门课程," + detail.users_count + "名学员");
            if (detail.is_personal_system) {
                holder.itemIsPersonal.setBackgroundResource(R.drawable.bg_tag_red);
                holder.itemIsPersonal.setText("个人");
            } else {
                holder.itemIsPersonal.setBackgroundResource(R.drawable.bg_tag_green);
                holder.itemIsPersonal.setText("所属");
            }
            Glide.with(App.AppContex).load(detail.photo).asBitmap().into(new CircleImgWrapper(holder.itemGymHeader, App.AppContex));
        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }
}
