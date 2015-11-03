package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.HTMLUtils;
import com.qingchengfit.fitcoach.bean.BaseInfoBean;
import com.qingchengfit.fitcoach.bean.BriefInfo;
import com.qingchengfit.fitcoach.component.RecyclerViewInScroll;
import com.qingchengfit.fitcoach.component.ScaleWidthWrapper;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseInfoFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COACH = "coach";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.baseinfo_recyclerview)
    RecyclerViewInScroll baseinfoRecyclerview;

    // TODO: Rename and change types of parameters
    private String mCoachInfo;
    private String mParam2;

    private boolean canScrollup;
    private ArrayList<BaseInfoBean> datas;
    private Gson gson = new Gson();

    public BaseInfoFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static BaseInfoFragment newInstance(String param1, String param2) {
        BaseInfoFragment fragment = new BaseInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COACH, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoachInfo = getArguments().getString(ARG_COACH);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        datas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_info, container, false);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
//        if (!isPrepared || !isVisible) {
//            return;
//        }
        if (TextUtils.isEmpty(mCoachInfo))
            return;
        QcMyhomeResponse.DataEntity.CoachEntity coachEntity = gson.fromJson(mCoachInfo, QcMyhomeResponse.DataEntity.CoachEntity.class);


        datas.clear();
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_phone, "电话", coachEntity.getPhone()));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_city, "城市", coachEntity.getDistrictStr()));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_wechat, "微信", coachEntity.getWeixin()));
        List<BriefInfo> briefInfos;
        try {
            briefInfos = HTMLUtils.fromHTML(coachEntity.getDescription());
        } catch (Exception e) {
            briefInfos = new ArrayList<>();
        }
        for (int i = 0; i < briefInfos.size(); i++) {
            BriefInfo briefInfo = briefInfos.get(i);
            if (TextUtils.isEmpty(briefInfo.getImg())) {
                datas.add(new BaseInfoBean(0, "text", briefInfo.getText()));
            } else datas.add(new BaseInfoBean(0, "img", briefInfo.getImg()));

        }
        BaseInfoAdapter adapter = new BaseInfoAdapter(datas);
        baseinfoRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        baseinfoRecyclerview.setAdapter(adapter);

        baseinfoRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, -1)) {
                        canScrollup = false;
                    } else canScrollup = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    public boolean isCanScrollup() {
        return canScrollup;
    }

    public void setCanScrollup(boolean canScrollup) {
        this.canScrollup = canScrollup;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public static class BaseInfoVH extends RecyclerView.ViewHolder {

        @Bind(R.id.baseinfo_item_icon)
        ImageView itemImg;
        @Bind(R.id.baseinfo_item_label)
        TextView itemLabel;
        @Bind(R.id.baseinfo_item_content)
        TextView itemContent;
        @Bind(R.id.item_divider)
        View itemDivider;

        public BaseInfoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BaseInfoAdapter extends RecyclerView.Adapter<BaseInfoVH> {

        private List<BaseInfoBean> datas;

        public BaseInfoAdapter(List<BaseInfoBean> datas) {
            this.datas = datas;
        }

        @Override
        public BaseInfoVH onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0)
                return new BaseInfoVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baseinfo, parent, false));
            else
                return new BaseInfoVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baseinfo_brife, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseInfoVH holder, int position) {
            if (position < 3) {
                holder.itemImg.setImageResource(datas.get(position).icon);
                holder.itemLabel.setText(datas.get(position).label);
                holder.itemContent.setText(datas.get(position).content);
            } else {
                if (position == 3) {
                    holder.itemDivider.setVisibility(View.VISIBLE);
                } else {
                    holder.itemDivider.setVisibility(View.GONE);
                }
                BaseInfoBean baseInfoBean = datas.get(position);
                if (baseInfoBean.label.equalsIgnoreCase("text")) {
                    holder.itemContent.setVisibility(View.VISIBLE);
                    holder.itemImg.setVisibility(View.GONE);
                    holder.itemLabel.setVisibility(View.GONE);
                    holder.itemContent.setText(baseInfoBean.content);
                } else if (baseInfoBean.label.equalsIgnoreCase("img")) {
                    holder.itemContent.setVisibility(View.GONE);
                    holder.itemImg.setVisibility(View.VISIBLE);
                    holder.itemLabel.setVisibility(View.GONE);
                    Glide.with(App.AppContex).load(baseInfoBean.content).asBitmap().into(new ScaleWidthWrapper(holder.itemImg));
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position < 3)
                return 0;
            else return 1;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

}
