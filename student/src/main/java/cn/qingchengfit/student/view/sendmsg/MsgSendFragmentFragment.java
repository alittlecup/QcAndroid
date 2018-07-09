package cn.qingchengfit.student.view.sendmsg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.widget.QcTagContainerLayout;
import cn.qingchengfit.saascommon.widget.QcTagView;
import cn.qingchengfit.student.BuildConfig;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.ShortMsg;
import cn.qingchengfit.student.bean.ShortMsgBody;
import cn.qingchengfit.student.routers.StudentParamsInjector;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;


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
@Leaf(module = "student",path = "/student/msgsend")
public class MsgSendFragmentFragment extends SaasCommonFragment
    implements ShortMsgPresentersPresenter.MVPView {
  @Need String msgid;

  QcTagContainerLayout layoutTags;
  EditText etContent;
  Toolbar toolbar;
  TextView toolbarTitile;
  TextView tvLeft;
  TextView tvSmsCount;
  RelativeLayout layoutSendHint;

  String smsBegin =
      "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020";
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShortMsgPresentersPresenter presenter;

  private List<QcStudentBean> chosenStudent = new ArrayList<>();
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StudentParamsInjector.inject(this);
  }


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_msg_send, container, false);
    layoutTags = (QcTagContainerLayout) view.findViewById(R.id.layout_tags);
    etContent = (EditText) view.findViewById(R.id.et_content);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    tvLeft = (TextView) view.findViewById(R.id.tv_left);
    tvSmsCount = (TextView) view.findViewById(R.id.tv_sms_count);
    layoutSendHint = (RelativeLayout) view.findViewById(R.id.layout_send_hint);
    view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickAdd();
      }
    });

    setToolbar(toolbar);
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
        // TODO: 2018/7/8 choose staff
        //Intent toChooseStudent = new Intent(getContext(), ChooseActivity.class);
        //toChooseStudent.putExtra("to", CHOOSE_MULTI_STUDENTS);
        //toChooseStudent.putExtra("open", true);
        //DirtySender.studentList.clear();
        //DirtySender.studentList.addAll(chosenStudent);
        //startActivityForResult(toChooseStudent, 11);
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
        if ((textViewTextChangeEvent.text().toString().length() - 24) > 59) {
          layoutSendHint.setVisibility(View.VISIBLE);
        } else {
          layoutSendHint.setVisibility(View.GONE);
        }
        tvSmsCount.setText((textViewTextChangeEvent.text().toString().length() - 24) + "");
      }
    });
    return view;
  }

  private void setToolbar(@NonNull Toolbar toolbar) {
    toolbarTitile.setText("新建群发短信");
    toolbar.setNavigationContentDescription(R.string.common_cancel);
    toolbar.inflateMenu(R.menu.menu_send);
    // TODO: 2018/7/8  取消
    tvLeft.setText("取消");
    tvLeft.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (TextUtils.isEmpty(etContent.getText().toString().trim()) && (chosenStudent == null
            || chosenStudent.size() == 0)) {
          getActivity().onBackPressed();
          return;
        }

        //点击取消
        DialogSheet.builder(getContext()).addButton("保存为草稿", R.color.text_black,new View.OnClickListener() {
          @Override public void onClick(View v) {
            saveMsg();

          }
        }).addButton("不保存",R.color.text_black, new View.OnClickListener() {
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
          DialogSheet.builder(getContext()).addButton("保存为草稿",R.color.text_black, new View.OnClickListener() {
            @Override public void onClick(View v) {
              saveMsg();
            }
          }).addButton("不保存",R.color.text_black, new View.OnClickListener() {
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
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup && this.isfitSystemPadding()) {
      ((ViewGroup)toolbar.getParent()).setPadding(0,
          MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
      RelativeLayout.LayoutParams layoutParams =
          (RelativeLayout.LayoutParams)tvLeft.getLayoutParams();
      layoutParams.setMargins(0, MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
      tvLeft.setLayoutParams(layoutParams);
    }

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
  public void onClickAdd() {
    // TODO: 2018/7/8  choosestaff
    //Intent toChooseStudent = new Intent(getContext(), ChooseActivity.class);
    //toChooseStudent.putExtra("to", CHOOSE_MULTI_STUDENTS);
    //DirtySender.studentList.clear();
    //DirtySender.studentList.addAll(chosenStudent);
    //startActivityForResult(toChooseStudent, 11);
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

    presenter.sendMsg(
        new ShortMsgBody.Builder().content(etContent.getText().toString().substring(24))
            .send(1)
            .user_ids(ids)
            .build());
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
          new ShortMsgBody.Builder().content(etContent.getText().toString().trim())
              .user_ids(ids)
              .message_id(msgid)
              .build());
    } else {
      presenter.sendMsg(new ShortMsgBody.Builder().content(etContent.getText().toString().trim())
          .send(0)
          .user_ids(ids)
          .build());
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
    ToastUtils.show(R.drawable.vd_success_tick_black, "发送成功");
  }

  @Override public void onPutSuccess() {
    getActivity().onBackPressed();
    ToastUtils.show("保存成功");
  }

  @Override public void onDelSuccess() {

  }
  @Override protected void routeTo(String uri, Bundle bd, boolean b) {
    if (BuildConfig.RUN_AS_APP) {
      if (!uri.startsWith("/")) {
        uri = "/" + uri;
      }
      if (this.getActivity() instanceof BaseActivity) {
        this.routeTo(Uri.parse(BuildConfig.PROJECT_NAME
            + "://"
            + ((BaseActivity) this.getActivity()).getModuleName()
            + uri), bd, b);
      }
    } else {
      super.routeTo(uri, bd, b);
    }
  }
}
