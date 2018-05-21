package cn.qingchengfit.staffkit.views.student.sendmsgs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.ToggleButton;
import cn.qingchengfit.model.body.ShortMsgBody;
import cn.qingchengfit.model.responese.ShortMsg;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.presenters.ShortMsgPresentersPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/3/14.
 */
@FragmentWithArgs public class ShortMsgDetailFragment extends BaseFragment
    implements ShortMsgPresentersPresenter.MVPView {

  @Arg ShortMsg shortMsg;

  TextView tvStudents;
  TextView tvSaveTime;
  ImageView imgSend;
  TextView tvSenderName;
  TextView tvSenderPhone;
  LinearLayout layoutSender;
  TextView tvContent;
  LinearLayout layoutOpreate;
  String smsBegin;
  @Inject ShortMsgPresentersPresenter presenter;

  String msgId;
  Toolbar toolbar;
  TextView toolbarTitile;
  TextView labelSaveTime;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_send_msg_detail, container, false);
    tvStudents = (TextView) view.findViewById(R.id.tv_students);
    tvSaveTime = (TextView) view.findViewById(R.id.tv_save_time);
    imgSend = (ImageView) view.findViewById(R.id.img_send);
    tvSenderName = (TextView) view.findViewById(R.id.tv_sender_name);
    tvSenderPhone = (TextView) view.findViewById(R.id.tv_sender_phone);
    layoutSender = (LinearLayout) view.findViewById(R.id.layout_sender);
    tvContent = (TextView) view.findViewById(R.id.tv_content);
    layoutOpreate = (LinearLayout) view.findViewById(R.id.layout_opreate);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    labelSaveTime = (TextView) view.findViewById(R.id.label_save_time);
    smsBegin = getString(R.string.qc_sms_begin);
    view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ShortMsgDetailFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ShortMsgDetailFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ShortMsgDetailFragment.this.onClick(v);
      }
    });
    ToggleButton toggleButton = view.findViewById(R.id.tg_show_students);
    toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        showAllStudents(isChecked);
      }
    });
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    msgId = shortMsg.id;
    layoutSender.setVisibility(shortMsg.status == 2 ? View.GONE : View.VISIBLE);
    layoutOpreate.setVisibility(shortMsg.status == 2 ? View.VISIBLE : View.GONE);
    labelSaveTime.setText(shortMsg.status == 2 ? "保存时间: " : "发送时间: ");
    onShortMsgDetail(shortMsg);
    presenter.queryShortMsgDetail(msgId);
    tvContent.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        ClipboardManager cmb =
            (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        String copyStr = tvContent.getText().toString().trim();
        if (copyStr.contains(smsBegin)) {
          copyStr = copyStr.replace(smsBegin, "");
        }
        cmb.setPrimaryClip(ClipData.newPlainText("qingcheng", copyStr));
        ToastUtils.show("已复制到剪切板");
        return true;
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(shortMsg.status == 2 ? "草稿详情" : "短信详情");
  }

  @Override public String getFragmentName() {
    return ShortMsgDetailFragment.class.getName();
  }

  @Override public void onShowError(String e) {
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }

  @Override public void onShortMsgList(List<ShortMsg> list) {

  }

  @Override public void onShortMsgDetail(ShortMsg detail) {
    shortMsg = detail;
    tvContent.setText(smsBegin + detail.content);
    tvStudents.setText(detail.title);

    if (detail.created_by != null) {
      tvSenderName.setText(detail.created_by.getUsername());
      tvSenderPhone.setText(detail.created_by.getPhone());
      Glide.with(getContext())
          .load(detail.created_by.getAvatar())
          .asBitmap()
          .placeholder(detail.created_by.gender == 0 ? R.drawable.default_manage_male
              : R.drawable.default_manager_female)
          .into(new CircleImgWrapper(imgSend, getContext()));
    }
    if (!TextUtils.isEmpty(detail.created_at)) {
      tvSaveTime.setText(
          DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(detail.created_at)));
    }
  }

  @Override public void onPostSuccess() {
    ToastUtils.show(R.drawable.vector_hook_white, "发送成功");
  }

  @Override public void onPutSuccess() {

  }

  @Override public void onDelSuccess() {
    getActivity().onBackPressed();
    ToastUtils.show("保存成功");
  }

  public void showAllStudents(boolean checked) {
    tvStudents.setText(
        checked ? Html.fromHtml(shortMsg.getMultiStudentsHtml()) : shortMsg.getShortTitle());
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_del:
        DialogUtils.instanceDelDialog(getContext(), "确定删除此草稿？",
            new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                presenter.delShortMsg(msgId);
              }
            }).show();

        break;
      case R.id.btn_edit:
        getFragmentManager().beginTransaction()
            .replace(R.id.frag, new MsgSendFragmentFragmentBuilder().msgid(msgId).build())
            .addToBackStack("")
            .commitAllowingStateLoss();
        break;
      case R.id.btn_send:
        if (TextUtils.isEmpty(shortMsg.content)) {
          onShowError(R.string.err_shortmsg_content_null);
          return;
        }
        if (shortMsg.users == null || shortMsg.users.size() == 0) {
          onShowError(R.string.err_shortmsg_to_null);
          return;
        }
        String ids = "";
        for (int i = 0; i < shortMsg.users.size(); i++) {
          if (i < shortMsg.users.size() - 1) {
            ids = TextUtils.concat(ids, shortMsg.users.get(i).getId(), ",").toString();
          } else {
            ids = TextUtils.concat(ids, shortMsg.users.get(i).getId()).toString();
          }
        }
        String copyStr = tvContent.getText().toString().trim();
        if (copyStr.contains(smsBegin)) {
          copyStr.replace(smsBegin, "");
        }
        presenter.sendMsg(
            new ShortMsgBody.Builder().content(copyStr).send(1).user_ids(ids).build());
        break;
    }
  }
}
