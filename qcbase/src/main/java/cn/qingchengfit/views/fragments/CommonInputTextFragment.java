package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.widgets.R;

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
 * Created by Paper on 2017/7/21.
 */
public class CommonInputTextFragment extends BaseFragment {

	Toolbar toolbar;
	protected TextView toolbarTitle;
	protected EditText et;
  public String title;
  public String hint;
  public String content;

  public static CommonInputTextFragment newInstance(String title, String content, String hint) {
    Bundle args = new Bundle();
    args.putString("t", title);
    args.putString("c", content);
    args.putString("h", hint);
    CommonInputTextFragment fragment = new CommonInputTextFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      title = getArguments().getString("t", "");
      hint = getArguments().getString("h", "");
      content = getArguments().getString("c");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_common_input_txt, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    et = (EditText) view.findViewById(R.id.et);
    view.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onImgCloseClicked();
      }
    });
    view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnClicked();
      }
    });

    initToolbar(toolbar);
    return view;
  }

  @Override public boolean isBlockTouch() {
    return true;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(title);
    et.setText(content);
    et.setHint(hint);
  }

  @Override protected void onFinishAnimation() {
    AppUtils.showKeyboard(getContext(), et);
  }

  @Override public String getFragmentName() {
    return CommonInputTextFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if(getActivity() != null){
      AppUtils.hideKeyboard(getActivity());
    }
  }

 public void onImgCloseClicked() {
    et.setText("");
  }

 public void onBtnClicked() {
    RxBus.getBus().post(new EventTxT.Builder().txt(et.getText().toString().trim()).build());
    getFragmentManager().popBackStackImmediate();
  }
}
