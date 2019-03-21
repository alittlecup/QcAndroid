package cn.qingchengfit.gym.gymconfig.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.gym.responsitory.network.GymApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import cn.qingchengfit.widgets.CommonInputView;
import javax.inject.Inject;
import rx.functions.Action1;

public class AddNewSiteFragment extends BaseDialogFragment {

  Toolbar toolbar;
  CommonInputView name;
  CommonInputView count;
  CommonInputView usage;
  Button btn;
  TextView toolbarTitile;
  @Inject QcRestRepository restRepository;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  private DialogSheet sheet;
  private boolean isSupportPrivate = true;
  private boolean isSupportTeam = true;
  private TextChange textChange = new TextChange();

  public static void start(Fragment fragment, int requestCode, int type) {
    BaseDialogFragment f = newInstance(type);
    f.setTargetFragment(fragment, requestCode);
    f.show(fragment.getFragmentManager(), "");
  }

  public static AddNewSiteFragment newInstance(int type) {
    Bundle args = new Bundle();
    AddNewSiteFragment fragment = new AddNewSiteFragment();
    args.putInt("type", type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.gy_fragment_add_new_site, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    name = (CommonInputView) view.findViewById(R.id.name);
    count = (CommonInputView) view.findViewById(R.id.count);
    usage = (CommonInputView) view.findViewById(R.id.usage);
    btn = (Button) view.findViewById(R.id.btn);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.usage).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onUseage();
      }
    });
    view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onComfirm();
      }
    });

    initToolbar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    toolbarTitile.setText("新增场地");
    name.addTextWatcher(textChange);
    count.addTextWatcher(textChange);
    SensorsUtils.trackScreen(      this.getClass().getCanonicalName());
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
  }

  public void onUseage() {
    if (sheet == null) {
      sheet = new DialogSheet(getContext()).addButton(getString(R.string.course_type_private),
          new View.OnClickListener() {
            @Override public void onClick(View v) {
              usage.setContent(getString(R.string.course_type_private));
              isSupportPrivate = true;
              isSupportTeam = false;
              sheet.dismiss();
            }
          }).addButton(getString(R.string.course_type_group), new View.OnClickListener() {
        @Override public void onClick(View v) {
          usage.setContent(getString(R.string.course_type_group));
          isSupportPrivate = false;
          isSupportTeam = true;
          sheet.dismiss();
        }
      }).addButton("不限", new View.OnClickListener() {
        @Override public void onClick(View v) {
          usage.setContent("不限");
          isSupportPrivate = true;
          isSupportTeam = true;
          sheet.dismiss();
        }
      });
    }
    sheet.show();
  }

  @SuppressWarnings("unused") public void onComfirm() {
    Space space = new Space(name.getContent(), count.getContent(), isSupportPrivate, isSupportTeam);
    restRepository.createRxJava1Api(GymApi.class)
        .qcCreateSpace(loginStatus.staff_id(), gymWrapper.id(), gymWrapper.model(), null, space)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcResponse) {
            if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
              getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                  new Intent());
              dismiss();
            } else {
              ToastUtils.show(qcResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {

          }
        });
  }

  @Override public void onDestroyView() {
    AppUtils.hideKeyboard(getActivity());
    super.onDestroyView();
  }

  class TextChange implements TextWatcher {

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      btn.setEnabled(
          (!TextUtils.isEmpty(name.getContent())) && !TextUtils.isEmpty(count.getContent()));
    }
  }
}
