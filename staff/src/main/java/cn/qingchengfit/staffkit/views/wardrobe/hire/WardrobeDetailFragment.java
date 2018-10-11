package cn.qingchengfit.staffkit.views.wardrobe.hire;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventBackPress;
import cn.qingchengfit.staffkit.rxbus.event.EventLongHire;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobeActivity;
import cn.qingchengfit.staffkit.views.wardrobe.edit.WardrobeEditFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * Created by Paper on 16/8/29.
 */
public class WardrobeDetailFragment extends BaseFragment {
  public static Locker locker;
  TextView name;
  TextView region;
  private Locker mLocker;
  private Fragment mCurFragment;
  private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {
      if (item.getItemId() == R.id.action_edit) {
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), WardrobeEditFragment.newInstance(mLocker))
            .addToBackStack(getFragmentName())
            .commit();
      }
      return true;
    }
  };

  public static WardrobeDetailFragment newInstance(Locker locker) {

    Bundle args = new Bundle();
    args.putParcelable("l", locker);
    WardrobeDetailFragment fragment = new WardrobeDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLocker = getArguments().getParcelable("l");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_hire_wardrobe, container, false);
    name = (TextView) view.findViewById(R.id.name);
    region = (TextView) view.findViewById(R.id.region);
    setToolbar(view);
    name.setText(mLocker.name);
    region.setText(mLocker.region.name);
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag, WardrobeShortHireFragment.newInstance(mLocker))
        .commit();
    if (mCurFragment != null) {
      getChildFragmentManager().beginTransaction()
          .replace(R.id.frag, mCurFragment)
          .addToBackStack(getFragmentName())
          .commit();
      if (getActivity() instanceof WardrobeActivity) {
        ((WardrobeActivity) getActivity()).setHasSecondView(true);
      }
    }

    RxBusAdd(EventLongHire.class).subscribe(new Action1<EventLongHire>() {
      @Override public void call(EventLongHire eventLongHire) {
        mCurFragment = WardrobeLongHireFragment.newInstance(mLocker);
        getChildFragmentManager().beginTransaction()
            .replace(R.id.frag, mCurFragment)
            .addToBackStack(getFragmentName())
            .commit();
        if (getActivity() instanceof WardrobeActivity) {
          ((WardrobeActivity) getActivity()).setHasSecondView(true);
        }
      }
    });
    RxBusAdd(EventBackPress.class).subscribe(new Action1<EventBackPress>() {
      @Override public void call(EventBackPress eventBackPress) {
        getChildFragmentManager().popBackStackImmediate();
        if (getActivity() instanceof WardrobeActivity) {
          ((WardrobeActivity) getActivity()).setHasSecondView(false);
        }
      }
    });
    if (locker != null) {
      mLocker = locker;
      name.setText(mLocker.name);
      region.setText(mLocker.region.name);
      locker = null;
    }
    return view;
  }

  private void setToolbar(View root) {
    Toolbar toolbar = root.findViewById(R.id.toolbar);
    TextView textView = root.findViewById(R.id.toolbar_title);
    textView.setText("租用更衣柜");
    toolbar.inflateMenu(R.menu.menu_icon_edit);
    toolbar.setOnMenuItemClickListener(listener);
    initToolbar(toolbar);
  }

  @Override public String getFragmentName() {
    return WardrobeDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

    if (getActivity() instanceof WardrobeActivity) {
      ((WardrobeActivity) getActivity()).setHasSecondView(false);
    }
  }
}
