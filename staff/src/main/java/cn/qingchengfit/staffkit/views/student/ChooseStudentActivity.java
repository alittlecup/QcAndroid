package cn.qingchengfit.staffkit.views.student;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.common.Contact;
import cn.qingchengfit.model.responese.AddStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.PhoneFuncUtils;
import cn.qingchengfit.utils.StudentCompare;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.AlphabetView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseStudentActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.choosestudent_rv) RecyclerView choosestudentRv;
    @BindView(R.id.choosestudent_all) CheckBox choosestudentAll;
    @BindView(R.id.choosestudent_choose_num) TextView choosestudentChooseNum;
    @BindView(R.id.choosestudent_total_num) TextView choosestudentTotalNum;
    @BindView(R.id.choosestudent_comfirm) TextView choosestudentComfirm;
    @BindView(R.id.alphabetview) AlphabetView alphabetview;
    List<StudentBean> studentBeans = new ArrayList<>(); //通讯录中所有联系人

    private LinearLayoutManager mLinearLayoutManager;
    private StudentAdapter studentAdapter;
    private int chosenCount = 0;
    private HashMap<String, Integer> alphabetSort = new HashMap<>();
    private MaterialDialog loadingDialog;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_choose_student);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("导入通讯录");
        mLinearLayoutManager = new LinearLayoutManager(this);
        choosestudentRv.setLayoutManager(mLinearLayoutManager);
        choosestudentRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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
                    studentBean.avatar = contact.getHeader();
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

        observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
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
            loadingDialog = new MaterialDialog.Builder(this).content("请稍后").progress(true, 0).cancelable(false).build();
        }
        if (content != null) loadingDialog.setContent(content);
        loadingDialog.show();
    }

    /**
     * 确认导入
     */
    @OnClick(R.id.choosestudent_comfirm) public void onComfirm() {
        List<AddStudentBean> choosenstudentBeans = new ArrayList<>();
        for (StudentBean s : studentBeans) {
            if (s.isChosen) {
                choosenstudentBeans.add(new AddStudentBean(s.username, s.phone, s.gender ? 1 : 0));
            }
        }
        if (choosenstudentBeans.size() == 0) {
            Toast.makeText(this, "没有选择学员", Toast.LENGTH_SHORT).show();
            return;
        }

        ShowLoading("正在导入,请稍后...");
        //        QcCloudClient.getApi().postApi
        //                .qcAddStudents(App.coachid, new PostStudents(choosenstudentBeans))
        //                .subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Subscriber<QcResponse>() {
        //                    @Override
        //                    public void onCompleted() {
        //                        loadingDialog.dismiss();
        //                    }
        //
        //                    @Override
        //                    public void onError(Throwable e) {
        //                        loadingDialog.dismiss();
        //                    }
        //
        //                    @Override
        //                    public void onNext(QcResponse qcResponse) {
        //                        if (qcResponse.status == ResponseResult.SUCCESS) {
        //                            Toast.makeText(App.AppContex, "添加成功", Toast.LENGTH_SHORT).show();
        //                            setResult(400);
        //                            ChooseStudentActivity.this.finish();
        //                        } else {
        //                            Toast.makeText(App.AppContex, "添加失败", Toast.LENGTH_SHORT).show();
        //                        }
        //                    }
        //                });

    }

    /**
     * recycle adapter
     */
    public static class StudentsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_student_header) ImageView itemStudentHeader;
        @BindView(R.id.item_student_name) TextView itemStudentName;
        @BindView(R.id.item_student_phonenum) TextView itemStudentPhonenum;
        @BindView(R.id.item_checkbox) CheckBox itemCheckbox;
        @BindView(R.id.item_student_alpha) TextView itemStudentAlpha;
        //        @BindView(R.id.item_student_divider)
        //        View itemStudentDivder;

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

            //            if (position < datas.size() - 2 && !TextUtils.equals(studentBean.head, datas.get(position + 1).head)) {
            //                holder.itemStudentDivder.setVisibility(View.GONE);
            //            } else {
            //                holder.itemStudentDivder.setVisibility(View.VISIBLE);
            //            }

            if (TextUtils.isEmpty(studentBean.avatar)) {
                Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_default_head_nogender)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, holder.itemView.getContext()));
            } else {
                Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(studentBean.avatar))
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, holder.itemView.getContext()));
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
