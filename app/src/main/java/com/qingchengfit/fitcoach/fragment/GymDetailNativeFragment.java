package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImageThreeAdapter;
import com.qingchengfit.fitcoach.bean.ImageThreeBean;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.RecyclerViewInScroll;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymDetailNativeFragment extends Fragment {

    private int mId;
    private boolean mIsPrivate;
    private ImageThreeAdapter mAdapter;
    private List<ImageThreeBean> mDatas = new ArrayList<>();

    public GymDetailNativeFragment() {
    }

    /**
     *
     * @param id 健身房id
     * @param isTag 是否为私人健身房
     * @return
     */
    public static GymDetailNativeFragment newInstance(int id, boolean isTag) {

        Bundle args = new Bundle();
        args.putBoolean("isPrivate", isTag);
        args.putInt("id", id);
        GymDetailNativeFragment fragment = new GymDetailNativeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt("id");
            mIsPrivate = getArguments().getBoolean("isPrivate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_detail_native, container, false);
        TextView nameTextView = (TextView) view.findViewById(R.id.gym_name);
        TextView titleTagTextView = (TextView) view.findViewById(R.id.gym_title_tag);
        LinearLayout addressLinearLayout = (LinearLayout) view.findViewById(R.id.gym_address);
        LinearLayout studentCountLinearLayout = (LinearLayout) view.findViewById(R.id.gym_student_count);
        RelativeLayout courseTotalLayoutRelativeLayout = (RelativeLayout) view.findViewById(R.id.course_total_layout);
        RecyclerViewInScroll courseListRecyclerViewInScroll = (RecyclerViewInScroll) view.findViewById(R.id.course_list);


        mAdapter = new ImageThreeAdapter(mDatas);
        courseListRecyclerViewInScroll.setLayoutManager(new LinearLayoutManager(getContext()));
        courseListRecyclerViewInScroll.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        courseListRecyclerViewInScroll.setAdapter(mAdapter);
        if (mIsPrivate) {
            titleTagTextView.setText("个人");
            titleTagTextView.setBackgroundResource(R.drawable.bg_tag_red);
        } else {
            titleTagTextView.setText("所属");
            titleTagTextView.setBackgroundResource(R.drawable.bg_tag_green);
        }
        return view;
    }

    public void init() {

    }




}
