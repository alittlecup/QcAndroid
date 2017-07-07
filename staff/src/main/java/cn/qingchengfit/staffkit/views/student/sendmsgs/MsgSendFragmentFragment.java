package cn.qingchengfit.staffkit.views.student.sendmsgs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.body.ShortMsgBody;
import cn.qingchengfit.model.responese.ShortMsg;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.presenters.ShortMsgPresentersPresenter;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.custom.DialogSheet;
import cn.qingchengfit.staffkit.views.custom.QcTagContainerLayout;
import cn.qingchengfit.staffkit.views.custom.QcTagView;
import cn.qingchengfit.utils.ToastUtils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

import static cn.qingchengfit.staffkit.views.ChooseActivity.CHOOSE_MULTI_STUDENTS;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/3/15.
 */
@FragmentWithArgs public class MsgSendFragmentFragment extends BaseFragment implements ShortMsgPresentersPresenter.MVPView {
    @Arg(required = false) String msgid;

    @BindView(R.id.layout_tags) QcTagContainerLayout layoutTags;
    @BindView(R.id.et_content) EditText etContent;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.tv_left) TextView tvLeft;
    @BindView(R.id.tv_sms_count) TextView tvSmsCount;
    @BindView(R.id.layout_send_hint) RelativeLayout layoutSendHint;

    String smsBegin =
        "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020";
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject ShortMsgPresentersPresenter presenter;

    private List<QcStudentBean> chosenStudent = new ArrayList<>();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_send, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        if (!TextUtils.isEmpty(msgid)) {
            showLoadingTrans();
            presenter.queryShortMsgDetail(msgid);
            toolbarTitile.setText("编辑草稿");
        }
        TextView textView = new TextView(getContext());
        textView.setText("收件人:");
        layoutTags.setHeaderView(textView);
        layoutTags.setOnTagClickListener(new QcTagView.OnTagClickListener() {
            @Override public void onTagClick(int position, String text) {
                layoutTags.removeTag(position);
                if (position > 0 && position < chosenStudent.size() + 1) chosenStudent.remove(position - 1);
            }

            @Override public void onTagLongClick(int position, String text) {

            }

            @Override public void onTailClick() {
                Intent toChooseStudent = new Intent(getContext(), ChooseActivity.class);
                toChooseStudent.putExtra("to", CHOOSE_MULTI_STUDENTS);
                toChooseStudent.putExtra("open", true);
                DirtySender.studentList.clear();
                DirtySender.studentList.addAll(chosenStudent);
                startActivityForResult(toChooseStudent, 11);
            }
        });
        etContent.setText(smsBegin);
        etContent.setSelection(etContent.getText().length());
        etContent.requestFocus();

        RxTextView.textChangeEvents(etContent).subscribe(new Action1<TextViewTextChangeEvent>() {
            @Override public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                if (textViewTextChangeEvent.text().length() < smsBegin.length()) {
                    etContent.setText(smsBegin);
                    etContent.setSelection(etContent.getText().length());
                }
                if ((textViewTextChangeEvent.text().toString().trim().length() + 7) > 70) {
                    layoutSendHint.setVisibility(View.VISIBLE);
                } else {
                    layoutSendHint.setVisibility(View.GONE);
                }
                tvSmsCount.setText((textViewTextChangeEvent.text().toString().trim().length() + 7) + "");
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarTitile.setText("新建群发短信");
        toolbar.setNavigationContentDescription(R.string.common_cancel);
        toolbar.inflateMenu(R.menu.menu_send);
        tvLeft.setText("取消");
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString().trim()) && (chosenStudent == null || chosenStudent.size() == 0)) {
                    getActivity().onBackPressed();
                    return;
                }

                //点击取消
                DialogSheet.builder(getContext()).addButton("保存为草稿", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        saveMsg();
                    }
                }).addButton("不保存", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                }).show();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == android.R.id.home) {
                    //点击取消
                    DialogSheet.builder(getContext()).addButton("保存为草稿", new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            saveMsg();
                        }
                    }).addButton("不保存", new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            getActivity().onBackPressed();
                        }
                    }).show();
                } else if (item.getItemId() == R.id.action_send) {
                    sendMsg();
                }
                return false;
            }
        });
    }

    @Override public void onDestroyView() {

        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return MsgSendFragmentFragment.class.getName();
    }

    /**
     * 点击添加学员
     */
    @OnClick(R.id.btn_add) public void onClickAdd() {
        Intent toChooseStudent = new Intent(getContext(), ChooseActivity.class);
        toChooseStudent.putExtra("to", CHOOSE_MULTI_STUDENTS);
        //toChooseStudent.putExtra("")
        DirtySender.studentList.clear();
        DirtySender.studentList.addAll(chosenStudent);
        startActivityForResult(toChooseStudent, 11);
    }

    /**
     * 发送短信
     */
    public void sendMsg() {
        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            ToastUtils.show("请填写短信内容");
            return;
        }
        if (chosenStudent == null || chosenStudent.size() == 0) {
            ToastUtils.show("请选择发送会员");
            return;
        }
        String ids = "";
        for (int i = 0; i < chosenStudent.size(); i++) {
            if (i < chosenStudent.size() - 1) {
                ids = TextUtils.concat(ids, chosenStudent.get(i).getId(), ",").toString();
            } else {
                ids = TextUtils.concat(ids, chosenStudent.get(i).getId()).toString();
            }
        }

        presenter.sendMsg(new ShortMsgBody.Builder().content(etContent.getText().toString().trim()).send(1).user_ids(ids).build());
    }

    /**
     * 保存为草稿
     */
    public void saveMsg() {
        String ids = "";
        for (int i = 0; i < chosenStudent.size(); i++) {
            if (i < chosenStudent.size() - 1) {
                ids = TextUtils.concat(ids, chosenStudent.get(i).getId(), ",").toString();
            } else {
                ids = TextUtils.concat(ids, chosenStudent.get(i).getId()).toString();
            }
        }
        if (!TextUtils.isEmpty(msgid)) {
            presenter.updateShortMsg(
                new ShortMsgBody.Builder().content(etContent.getText().toString().trim()).user_ids(ids).message_id(msgid).build());
        } else {
            presenter.sendMsg(new ShortMsgBody.Builder().content(etContent.getText().toString().trim()).send(0).user_ids(ids).build());
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                //List<QcStudentBean> students = (ArrayList) data.getParcelableArrayListExtra("student");
                List<String> s = new ArrayList<>();
                if (DirtySender.studentList != null) {
                    for (int i = 0; i < DirtySender.studentList.size(); i++) {
                        s.add(DirtySender.studentList.get(i).getUsername());
                    }
                }
                chosenStudent.clear();
                chosenStudent.addAll(DirtySender.studentList);
                DirtySender.studentList.clear();

                layoutTags.removeAllTags();
                layoutTags.setTags(s);
            }
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void onShortMsgList(List<ShortMsg> list) {

    }

    @Override public void onShortMsgDetail(ShortMsg detail) {
        hideLoadingTrans();
        if (detail != null) {
            if (detail.users != null) {
                chosenStudent.clear();
                chosenStudent.addAll(detail.users);

                List<String> s = new ArrayList<>();
                if (chosenStudent != null) {
                    for (int i = 0; i < chosenStudent.size(); i++) {
                        s.add(chosenStudent.get(i).getUsername());
                    }
                }
                layoutTags.removeAllTags();
                layoutTags.setTags(s);
            }
            if (detail.content != null) {
                etContent.setText(smsBegin + detail.content);
            }
        }
    }

    @Override public void onPostSuccess() {
        getActivity().onBackPressed();
        ToastUtils.show(R.drawable.vector_hook_white, "发送成功");
    }

    @Override public void onPutSuccess() {
        getActivity().onBackPressed();
        ToastUtils.show("保存成功");
    }

    @Override public void onDelSuccess() {

    }
}
