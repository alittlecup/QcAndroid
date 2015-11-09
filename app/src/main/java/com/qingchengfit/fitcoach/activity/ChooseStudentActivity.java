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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.StudentCompare;
import com.qingchengfit.fitcoach.bean.StudentBean;
import com.qingchengfit.fitcoach.component.AlphabetView;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseStudentActivity extends BaseAcitivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.choosestudent_rv)
    RecyclerView choosestudentRv;

    @Bind(R.id.choosestudent_all)
    CheckBox choosestudentAll;
    @Bind(R.id.choosestudent_choose_num)
    TextView choosestudentChooseNum;
    @Bind(R.id.choosestudent_total_num)
    TextView choosestudentTotalNum;
    @Bind(R.id.choosestudent_comfirm)
    TextView choosestudentComfirm;
    @Bind(R.id.alphabetview)
    AlphabetView alphabetview;
    List<StudentBean> studentBeans = new ArrayList<>(); //通讯录中所有联系人
    private LinearLayoutManager mLinearLayoutManager;
    private StudentAdapter studentAdapter;
    private int chosenCount = 0;
    private HashMap<String, Integer> alphabetSort = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_choose_student);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        toolbar.setTitle("导入通讯录");
        mLinearLayoutManager = new LinearLayoutManager(this);
        choosestudentRv.setLayoutManager(mLinearLayoutManager);
        studentAdapter = new StudentAdapter(studentBeans);
        choosestudentRv.setAdapter(studentAdapter);
        studentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //点击某人,修改选中状态
                StudentBean s = studentBeans.get(pos);
                if (s.isChosen) {
                    s.isChosen = false;
                    chosenCount--;
                } else {
                    s.isChosen = true;
                    chosenCount++;
                }
                if (chosenCount == studentBeans.size())
                    choosestudentAll.setChecked(true);
                else choosestudentAll.setChecked(false);
                studentAdapter.notifyItemChanged(pos);
                choosestudentChooseNum.setText(Integer.toString(chosenCount));
            }
        });
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                List<Contact> contacts = PhoneFuncUtils.initContactList(ChooseStudentActivity.this);
                studentBeans.clear();
                for (Contact contact : contacts) {
                    StudentBean studentBean = new StudentBean();
                    studentBean.phoneStr = contact.getPhone();
                    studentBean.name = contact.getUsername();
                    studentBean.head = contact.getSortKey();
                    studentBeans.add(studentBean);
                }
                Collections.sort(studentBeans, new StudentCompare());
                alphabetSort.clear();
                String tag = "";
                for (int i = 0; i < studentBeans.size(); i++) {
                    StudentBean bean = studentBeans.get(i);
                    if (!bean.head.equalsIgnoreCase(tag)) {
                        bean.isTag = true;
                        tag = bean.head;
                        alphabetSort.put(tag, i);
                    } else bean.isTag = false;
                }

                subscriber.onNext("");
                subscriber.onCompleted();
//                subscriber.onNext(contacts);
            }
        });

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        choosestudentTotalNum.setText("/" + studentBeans.size() + "人");
                        studentAdapter.notifyDataSetChanged();
                    }
                });
        choosestudentAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                choosestudentAll.setChecked(!choosestudentAll.isChecked());
                boolean isChecked = choosestudentAll.isChecked();
                //全选 和 全不选
                for (StudentBean s : studentBeans) {
                    s.isChosen = isChecked;
                }
                if (isChecked) //修改选中人数
                    chosenCount = studentBeans.size();
                else chosenCount = 0;
                choosestudentChooseNum.setText(Integer.toString(chosenCount));
                studentAdapter.notifyDataSetChanged();
            }
        });

        alphabetview.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override
            public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                }
            }
        });

    }

    /**
     * 确认导入
     */
    @OnClick(R.id.choosestudent_comfirm)
    public void onComfirm() {
        List<StudentBean> choosenstudentBeans = new ArrayList<>();
        for (StudentBean s : studentBeans) {
            if (s.isChosen)
                choosenstudentBeans.add(s);
        }
        if (choosenstudentBeans.size() == 0) {
            Toast.makeText(App.AppContex, "没有选择学员", Toast.LENGTH_SHORT).show();
            return;
        }

        String s = new Gson().toJson(choosenstudentBeans);
        QcCloudClient.getApi().postApi
                .qcPostCreatStudents(App.coachid, new PostStudents(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcResponse -> {
                    if (qcResponse.status == ResponseResult.SUCCESS) {
                        Toast.makeText(App.AppContex, "添加成功", Toast.LENGTH_SHORT).show();
                        setResult(400);
                        this.finish();
                    } else {
                        Toast.makeText(App.AppContex, "添加失败", Toast.LENGTH_SHORT).show();
                    }
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

            holder.itemStudentPhonenum.setText("联系电话:" + studentBean.phoneStr);

            if (studentBean.isChosen) {
                holder.itemCheckbox.setChecked(true);
            } else holder.itemCheckbox.setChecked(false);

            if (studentBean.isTag) {
                holder.itemStudentAlpha.setText(studentBean.head);
                holder.itemStudentAlpha.setVisibility(View.VISIBLE);
            } else holder.itemStudentAlpha.setVisibility(View.GONE);
            Glide.with(App.AppContex).load(R.drawable.img_default_male).asBitmap().into(new CircleImgWrapper(holder.itemStudentHeader, App.AppContex));

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
