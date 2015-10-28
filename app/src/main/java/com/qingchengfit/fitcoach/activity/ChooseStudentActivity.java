package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.StudentBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseStudentActivity extends BaseAcitivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.choosestudent_rv)
    RecyclerView choosestudentRv;
    List<StudentBean> studentBeans = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private StudentAdapter studentAdapter;
    private int chosenCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_choose_student);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        choosestudentRv.setLayoutManager(mLinearLayoutManager);
        studentAdapter = new StudentAdapter(studentBeans);
        choosestudentRv.setAdapter(studentAdapter);
        studentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                StudentBean s = studentBeans.get(pos);
                if (s.isChosen) {
                    s.isChosen = false;
                    chosenCount--;
                } else {
                    s.isChosen = true;
                    chosenCount++;
                }
                studentAdapter.notifyItemChanged(pos);
            }
        });
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    return PhoneFuncUtils.initContactList(this);
                })
                .map(contacts -> {
                    studentBeans.clear();
                    for (Contact contact : contacts) {
                        StudentBean studentBean = new StudentBean();
                        studentBean.phoneStr = contact.getPhone();
                        studentBean.name = contact.getPhone();
                        studentBean.head = contact.getSortKey();
                        studentBeans.add(studentBean);
                    }
                    return "";
                })
                .subscribe(s1 -> {
                    studentAdapter.notifyDataSetChanged();
                });


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
        @Bind(R.id.item_checkbox)
        CheckBox itemCheckbox;
        @Bind(R.id.item_student_gender)
        ImageView itemStudentGender;
        @Bind(R.id.item_student_alpha)
        TextView itemStudentAlpha;

        public StudentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class StudentAdapter extends RecyclerView.Adapter<StudentsHolder> implements View.OnClickListener {
        List<StudentBean> datas;
        private OnRecycleItemClickListener listener;

        public StudentAdapter(List<StudentBean> d) {
            this.datas = d;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public StudentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            StudentsHolder holder = new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_choose, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(StudentsHolder holder, int position) {
            holder.itemView.setTag(position);
            StudentBean studentBean = datas.get(position);


            holder.itemStudentName.setText(studentBean.name);
            holder.itemStudentPhonenum.setText(studentBean.phoneStr);
            if (studentBean.gender) {//男
                holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_male);
            } else {
                holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_female);
            }
            if (studentBean.isChosen) {
                holder.itemCheckbox.setChecked(true);
            } else holder.itemCheckbox.setChecked(false);

            if (studentBean.isTag) {
                holder.itemStudentAlpha.setText(studentBean.head);
                holder.itemStudentAlpha.setVisibility(View.VISIBLE);
            } else holder.itemStudentAlpha.setVisibility(View.GONE);
            Glide.with(App.AppContex).load(studentBean.headerPic).asBitmap().into(new CircleImgWrapper(holder.itemStudentHeader, App.AppContex));

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
