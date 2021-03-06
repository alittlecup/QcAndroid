package com.qingchengfit.fitcoach.activity;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.qingchengfit.bean.StudentBean;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Contact;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.PhoneFuncUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.StudentCompare;
import com.qingchengfit.fitcoach.component.AlphabetView;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.LoadingDialog;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.AddStudentBean;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ChooseStudentActivity extends BaseActivity {

	Toolbar toolbar;
	RecyclerView choosestudentRv;
	CheckBox choosestudentAll;
	TextView choosestudentChooseNum;
	TextView choosestudentTotalNum;
	TextView choosestudentComfirm;
	AlphabetView alphabetview;
    List<StudentBean> studentBeans = new ArrayList<>(); //通讯录中所有联系人
	TextView toolbarTitle;

    private LinearLayoutManager mLinearLayoutManager;
    private StudentAdapter studentAdapter;
    private int chosenCount = 0;
    private HashMap<String, Integer> alphabetSort = new HashMap<>();
    private LoadingDialog loadingDialog;
    private CoachService mCoachService;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_choose_student);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        choosestudentRv = (RecyclerView) findViewById(R.id.choosestudent_rv);
        choosestudentAll = (CheckBox) findViewById(R.id.choosestudent_all);
        choosestudentChooseNum = (TextView) findViewById(R.id.choosestudent_choose_num);
        choosestudentTotalNum = (TextView) findViewById(R.id.choosestudent_total_num);
        choosestudentComfirm = (TextView) findViewById(R.id.choosestudent_comfirm);
        alphabetview = (AlphabetView) findViewById(R.id.alphabetview);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        findViewById(R.id.choosestudent_comfirm).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onComfirm();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        toolbarTitle.setText("导入通讯录");
        mCoachService = getIntent().getParcelableExtra("service");
        mLinearLayoutManager = new LinearLayoutManager(this);
        choosestudentRv.setLayoutManager(mLinearLayoutManager);
        studentAdapter = new StudentAdapter(studentBeans);
        choosestudentRv.setAdapter(studentAdapter);
        studentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                //点击某人,修改选中状态
                StudentBean s = studentBeans.get(pos);
                if (s.isChosen) {
                    s.isChosen = false;
                    chosenCount--;
                } else {
                    s.isChosen = true;
                    chosenCount++;
                }
                if (chosenCount == studentBeans.size()) {
                    choosestudentAll.setChecked(true);
                } else {
                    choosestudentAll.setChecked(false);
                }
                studentAdapter.notifyItemChanged(pos);
                choosestudentChooseNum.setText(Integer.toString(chosenCount));
            }
        });
        ShowLoading("正在获取联系人信息");
        new RxPermissions(this).request(Manifest.permission.READ_CONTACTS).subscribe(new Action1<Boolean>() {
            @Override public void call(Boolean aBoolean) {
                if (aBoolean) {
                    Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
                        @Override public void call(Subscriber<? super String> subscriber) {
                            List<Contact> contacts = PhoneFuncUtils.initContactList(ChooseStudentActivity.this);
                            studentBeans.clear();
                            for (Contact contact : contacts) {
                                StudentBean studentBean = new StudentBean();
                                studentBean.phone = contact.getPhone();
                                studentBean.username = contact.getUsername();
                                if (AlphabetView.Alphabet.contains(contact.getSortKey())) {
                                    studentBean.head = contact.getSortKey();
                                } else {
                                    studentBean.head = "~";
                                }
                                studentBean.headerPic = contact.getHeader();
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
                                } else {
                                    bean.isTag = false;
                                }
                            }

                            subscriber.onNext("");
                            subscriber.onCompleted();
                            //                subscriber.onNext(contacts);
                        }
                    });

                  observable.observeOn(AndroidSchedulers.mainThread())
                      .onBackpressureBuffer()
                      .subscribeOn(Schedulers.io())
                      .subscribe(new Observer<String>() {
                        @Override public void onCompleted() {
                            loadingDialog.dismiss();
                        }

                        @Override public void onError(Throwable e) {
                            loadingDialog.dismiss();
                        }

                        @Override public void onNext(String s) {
                            choosestudentTotalNum.setText("/" + studentBeans.size() + "人");
                            studentAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    ChooseStudentActivity.this.finish();
                }
            }
        },new HttpThrowable());

        choosestudentAll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //                choosestudentAll.setChecked(!choosestudentAll.isChecked());
                boolean isChecked = choosestudentAll.isChecked();
                //全选 和 全不选
                for (StudentBean s : studentBeans) {
                    s.isChosen = isChecked;
                }
                if (isChecked) //修改选中人数
                {
                    chosenCount = studentBeans.size();
                } else {
                    chosenCount = 0;
                }
                choosestudentChooseNum.setText(Integer.toString(chosenCount));
                studentAdapter.notifyDataSetChanged();
            }
        });

        alphabetview.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                } else {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get("~"), 0);
                }
            }
        });
    }

    public void ShowLoading(String content) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this,content);
        }
        loadingDialog.show();
    }

    /**
     * 确认导入
     */
 public void onComfirm() {
        List<AddStudentBean> choosenstudentBeans = new ArrayList<>();
        for (StudentBean s : studentBeans) {
            if (s.isChosen) {
              choosenstudentBeans.add(new AddStudentBean(s.username, s.phone, s.gender, null));
            }
        }
        if (choosenstudentBeans.size() == 0) {
            Toast.makeText(App.AppContex, "没有选择学员", Toast.LENGTH_SHORT).show();
            return;
        }

        //        String s = new Gson().toJson(choosenstudentBeans);
        //        LogUtil.e("student:"+s);
        ShowLoading("正在导入,请稍后...");
        if (mCoachService == null) {
            return;
        }
        TrainerRepository.getStaticTrainerAllApi().qcAddStudents(App.coachid, new PostStudents(choosenstudentBeans), GymUtils.getParams(mCoachService))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcResponse>() {
                @Override public void onCompleted() {
                    loadingDialog.dismiss();
                }

                @Override public void onError(Throwable e) {
                    loadingDialog.dismiss();
                }

                @Override public void onNext(QcResponse qcResponse) {
                    if (qcResponse.status == ResponseResult.SUCCESS) {
                        Toast.makeText(App.AppContex, "添加成功", Toast.LENGTH_SHORT).show();
                        setResult(400);
                        ChooseStudentActivity.this.finish();
                    } else {
                        Toast.makeText(App.AppContex, qcResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    /**
     * recycle adapter
     */
    public static class StudentsHolder extends RecyclerView.ViewHolder {
	ImageView itemStudentHeader;
	TextView itemStudentName;
	TextView itemStudentPhonenum;
	CheckBox itemCheckbox;
	TextView itemStudentAlpha;
	View itemStudentDivder;

        public StudentsHolder(View itemView) {
            super(itemView);
            itemStudentHeader = (ImageView) itemView.findViewById(R.id.item_student_header);
            itemStudentName = (TextView) itemView.findViewById(R.id.item_student_name);
            itemStudentPhonenum = (TextView) itemView.findViewById(R.id.item_student_phonenum);
            itemCheckbox = (CheckBox) itemView.findViewById(R.id.item_checkbox);
            itemStudentAlpha = (TextView) itemView.findViewById(R.id.item_student_alpha);
            itemStudentDivder = (View) itemView.findViewById(R.id.item_student_divider);

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

        @Override public StudentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            StudentsHolder holder =
                new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_choose, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(StudentsHolder holder, int position) {
            holder.itemView.setTag(position);
            StudentBean studentBean = datas.get(position);

            holder.itemStudentName.setText(studentBean.username);

            holder.itemStudentPhonenum.setText("手机:" + studentBean.phone);

            if (studentBean.isChosen) {
                holder.itemCheckbox.setChecked(true);
            } else {
                holder.itemCheckbox.setChecked(false);
            }

            if (studentBean.isTag) {
                if (TextUtils.equals("~", studentBean.head)) {
                    holder.itemStudentAlpha.setText("#");
                } else {
                    holder.itemStudentAlpha.setText(studentBean.head);
                }
                holder.itemStudentAlpha.setVisibility(View.VISIBLE);
            } else {
                holder.itemStudentAlpha.setVisibility(View.GONE);
            }

            if (position < datas.size() - 2 && !TextUtils.equals(studentBean.head, datas.get(position + 1).head)) {
                holder.itemStudentDivder.setVisibility(View.GONE);
            } else {
                holder.itemStudentDivder.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(studentBean.headerPic)) {
                Glide.with(App.AppContex)
                    .load(R.drawable.ic_default_head_nogender)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, App.AppContex));
            } else {
                Glide.with(App.AppContex)
                    .load(Uri.parse(studentBean.headerPic))
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, App.AppContex));
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }
}
