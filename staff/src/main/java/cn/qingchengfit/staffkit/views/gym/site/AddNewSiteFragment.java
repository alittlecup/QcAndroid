package cn.qingchengfit.staffkit.views.gym.site;

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
import android.widget.Button;
import android.widget.TextView;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.AddSiteEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.custom.DialogSheet;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/29 2016.
 */
public class AddNewSiteFragment extends BaseDialogFragment {

  Toolbar toolbar;
  CommonInputView name;
  CommonInputView count;
  CommonInputView usage;
  Button btn;
  TextView toolbarTitile;
  @Inject RestRepository restRepository;
  @Inject GymWrapper gymWrapper;
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
    setStyle(DialogFragment.STYLE_NORMAL, R.style.QcAppTheme);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_new_site, container, false);
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

    toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        AddNewSiteFragment.this.dismiss();
      }
    });
    toolbarTitile.setText(getString(R.string.title_add_new_site));
    name.addTextWatcher(textChange);
    count.addTextWatcher(textChange);
    return view;
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
      }).addButton(getString(R.string.no_limit), new View.OnClickListener() {
        @Override public void onClick(View v) {
          usage.setContent(getString(R.string.no_limit));
          isSupportPrivate = true;
          isSupportTeam = true;
          sheet.dismiss();
        }
      });
    }
    sheet.show();
  }

  public void onComfirm() {
    if (TextUtils.isEmpty(count.getContent()) || TextUtils.isEmpty(name.getContent())) {
      DialogUtils.showAlert(getContext(), "请完善信息");
      return;
    }
    try {
      if (Integer.parseInt(count.getContent()) <= 0) {
        DialogUtils.showAlert(getContext(), "可容纳人数至少为1");
        return;
      }
    } catch (Exception e) {
    }
    //是用户新建 还是
    if (getArguments().getInt("type") == Configs.INIT_TYPE_GUIDE) {
      SystemInitBody body = (SystemInitBody) App.caches.get("init");
      body.spaces.add(
          new Space(name.getContent(), count.getContent(), isSupportPrivate, isSupportTeam));
      getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
          IntentUtils.instanceStringIntent(name.getContent()));
      this.dismiss();
    } else if (getArguments().getInt("type") == Configs.INIT_TYPE_ADD) {
      Space space =
          new Space(name.getContent(), count.getContent(), isSupportPrivate, isSupportTeam);
      restRepository.getPost_api()
          .qcCreateSpace(App.staffId, gymWrapper.id(), gymWrapper.model(), "", space)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
              if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                RxBus.getBus().post(new AddSiteEvent());
                dismiss();
              } else {
                ToastUtils.show(qcResponse.getMsg());
              }
            }
          }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {

            }
          });
    } else if (getArguments().getInt("type") == Configs.INIT_TYPE_CHOOSE) {
      Space space =
          new Space(name.getContent(), count.getContent(), isSupportPrivate, isSupportTeam);
      restRepository.getPost_api()
          .qcCreateSpace(App.staffId, gymWrapper.id(), gymWrapper.model(), null, space)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
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
