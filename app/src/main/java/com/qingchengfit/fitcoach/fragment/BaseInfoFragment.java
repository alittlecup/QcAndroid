package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.BaseInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.baseinfo_recyclerview)
    RecyclerView baseinfoRecyclerview;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public BaseInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaseInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaseInfoFragment newInstance(String param1, String param2) {
        BaseInfoFragment fragment = new BaseInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_info, container, false);
        ButterKnife.bind(this, view);
        List<BaseInfoBean> datas = new ArrayList<>();
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_phone,"电话","15123358198"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_city,"城市","北京"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce,"介绍","没什么好介绍的"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "介绍", "没什么好介绍的"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "介绍", "没什么好介绍的"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "介绍", "没什么好介绍的"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "介绍", "没什么好介绍的"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "介绍", "没什么好介绍的"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "介绍", "没什么好介绍的"));
        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "介绍", "没什么好介绍的"));
        BaseInfoAdapter adapter = new BaseInfoAdapter(datas);
        baseinfoRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        baseinfoRecyclerview.setAdapter(adapter);

        return view;
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

        public BaseInfoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BaseInfoAdapter extends RecyclerView.Adapter<BaseInfoVH>{

        private List<BaseInfoBean> datas;

        public BaseInfoAdapter(List<BaseInfoBean> datas) {
            this.datas = datas;
        }

        @Override
        public BaseInfoVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BaseInfoVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baseinfo,null));
        }

        @Override
        public void onBindViewHolder(BaseInfoVH holder, int position) {
            holder.itemImg.setImageResource(datas.get(position).icon);
            holder.itemLabel.setText(datas.get(position).label);
            holder.itemContent.setText(datas.get(position).content);
        }



        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

}
