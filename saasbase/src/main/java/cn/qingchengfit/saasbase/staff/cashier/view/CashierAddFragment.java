package cn.qingchengfit.saasbase.staff.cashier.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;




import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;

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
 * Created by Paper on 2017/10/11.
 */
@Leaf(module = "staff" ,path = "/cashier/add/")
public class CashierAddFragment extends BaseFragment {

	Toolbar toolbar;
	TextView toolbarTitle;
	CommonInputView civName;
	CommonInputView civPhoneNum;
	CommonInputView civGender;


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cashier_add, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    civName = (CommonInputView) view.findViewById(R.id.civ_name);
    civPhoneNum = (CommonInputView) view.findViewById(R.id.civ_phoneNum);
    civGender = (CommonInputView) view.findViewById(R.id.civ_gender);
    view.findViewById(R.id.civ_gender).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onViewClicked();
      }
    });

    initToolbar(toolbar);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
  }

  @Override public String getFragmentName() {
    return CashierAddFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

 public void onViewClicked() {
    DialogList.builder(getContext()).list(getResources().getStringArray(R.array.gender_list), new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 2017/10/11 新增收银员接口
      }
    }).show();
  }
}
