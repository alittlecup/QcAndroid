package cn.qingchengfit.staffkit.views.gym.site;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.custom.DialogSheet;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import javax.inject.Inject;

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
 * Created by Paper on 16/5/10 2016.
 */
public class SiteDetailFragment extends BaseFragment implements SiteDetailView {

  CommonInputView name;
  CommonInputView count;
  CommonInputView usage;
  RelativeLayout btnDel;

  @Inject SiteDetailPresenter presenter;
  @Inject SerPermisAction serPermisAction;
  View denyLayout;
  Toolbar toolbar;
  TextView toolbarTitile;

  private Space mCurSpace;
  private DialogSheet sheet;

  public static SiteDetailFragment newInstance(Space space) {

    Bundle args = new Bundle();
    args.putParcelable("space", space);
    SiteDetailFragment fragment = new SiteDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mCurSpace = getArguments().getParcelable("space");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_site_detail, container, false);
    name = (CommonInputView) view.findViewById(R.id.name);
    count = (CommonInputView) view.findViewById(R.id.count);
    usage = (CommonInputView) view.findViewById(R.id.usage);
    btnDel = (RelativeLayout) view.findViewById(R.id.btn_del);
    denyLayout = (View) view.findViewById(R.id.deny_layout);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.deny_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDeny();
      }
    });
    view.findViewById(R.id.usage).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onUseage();
      }
    });
    view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDel();
      }
    });

    initToolbar(toolbar);

    boolean eP = serPermisAction.checkAll(PermissionServerUtils.STUDIO_LIST_CAN_CHANGE);
    denyLayout.setVisibility(eP ? View.GONE : View.VISIBLE);

    presenter.attachView(this);

    name.setContent(mCurSpace.getName());
    count.setContent(mCurSpace.getCapacity());
    String us = "";
    if (mCurSpace.is_support_private() && mCurSpace.is_support_team()) {
      us = getString(R.string.no_limit);
    } else if (mCurSpace.is_support_private()) {
      us = getString(R.string.course_type_private);
    } else if (mCurSpace.is_support_team()) {
      us = getString(R.string.course_type_group);
    }
    usage.setContent(us);
    name.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        mCurSpace.setName(s.toString());
      }
    });
    count.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        mCurSpace.setCapacity(s.toString());
      }
    });
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    boolean eP = serPermisAction.checkAll(PermissionServerUtils.STUDIO_LIST_CAN_CHANGE);
    toolbarTitile.setText("场地详情");
    if (eP) {
      toolbar.inflateMenu(R.menu.menu_save);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          try {
            Integer integer = Integer.valueOf(mCurSpace.getCapacity());
            if (integer <= 0) {
              ToastUtils.show("可容纳人数至少为1");
              return false;
            }
            presenter.fixSite(mCurSpace.getId(), mCurSpace);
          } catch (Exception e) {

          }
          return false;
        }
      });
    }
  }

  public void onDeny() {
    showAlert(R.string.alert_permission_forbid);
  }

  public void onUseage() {
    if (sheet == null) {
      sheet = new DialogSheet(getContext()).addButton(getString(R.string.course_type_private),
          new View.OnClickListener() {
            @Override public void onClick(View v) {
              usage.setContent(getString(R.string.course_type_private));
              mCurSpace.setIs_support_private(true);
              mCurSpace.setIs_support_team(false);
              sheet.dismiss();
            }
          }).addButton(getString(R.string.course_type_group), new View.OnClickListener() {
        @Override public void onClick(View v) {
          usage.setContent(getString(R.string.course_type_group));
          mCurSpace.setIs_support_private(false);
          mCurSpace.setIs_support_team(true);
          sheet.dismiss();
        }
      }).addButton(getString(R.string.no_limit), new View.OnClickListener() {
        @Override public void onClick(View v) {
          usage.setContent(getString(R.string.no_limit));
          mCurSpace.setIs_support_private(true);
          mCurSpace.setIs_support_team(true);
          sheet.dismiss();
        }
      });
    }
    sheet.show();
  }

  @Override public String getFragmentName() {
    return null;
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  @Override public void onDelSucceed() {
    hideLoading();
    getActivity().onBackPressed();
  }

  @Override public void onFixSucceed() {
    hideLoading();
    ToastUtils.showS("修改成功!");
    getActivity().onBackPressed();
  }

  @Override public void onFailed() {
    hideLoading();
  }

  public void onDel() {
    if (!serPermisAction.checkAll(PermissionServerUtils.STUDIO_LIST_CAN_DELETE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    DialogUtils.instanceDelDialog(getContext(), "是否删除此场地?",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            showLoading();
            presenter.delSite(mCurSpace.getId());
          }
        }).show();
  }
}
