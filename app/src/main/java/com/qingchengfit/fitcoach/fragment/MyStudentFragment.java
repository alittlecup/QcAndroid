package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStudentFragment extends MainBaseFragment {
    public static final String TAG = MyStudentFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.searchview_et)
    EditText searchviewEt;
    @Bind(R.id.searchview_clear)
    ImageView searchviewClear;
    @Bind(R.id.searchview_cancle)
    Button searchviewCancle;
    @Bind(R.id.searchview)
    LinearLayout searchview;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;


    public MyStudentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_student, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.inflateMenu(R.menu.menu_students);
        toolbar.setOverflowIcon(getContext().getResources().getDrawable(R.drawable.ic_action_add));
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                searchview.setVisibility(View.VISIBLE);
            } else if (item.getItemId() == R.id.action_add_mannul) {

            } else if (item.getItemId() == R.id.action_add_phone) {

            }
            return true;
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @OnClick(R.id.searchview_cancle)
    public void onClickCancel() {
        searchviewEt.setText("");
        searchview.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * recycle adapter
     */
    public static class StudentsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_student_header)
        ImageView itemStudentHeader;
        @Bind(R.id.item_student_name)
        TextView itemStudentName;
        @Bind(R.id.item_student_phonenum)
        TextView itemStudentPhonenum;
        @Bind(R.id.item_student_gymname)
        TextView itemStudentGymname;

        public StudentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }

    public class StudentAdapter extends RecyclerView.Adapter<StudentsHolder> {

        public StudentAdapter() {
        }

        @Override
        public StudentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
        }

        @Override
        public void onBindViewHolder(StudentsHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}
