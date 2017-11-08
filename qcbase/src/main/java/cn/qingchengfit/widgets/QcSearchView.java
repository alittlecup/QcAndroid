package cn.qingchengfit.widgets;

import android.content.Context;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

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
 * Created by Paper on 2017/11/3.
 */

public class QcSearchView extends LinearLayoutCompat implements CollapsibleActionView {

  EditText etSearch;
  ImageView btnClose;

  public QcSearchView(Context context) {
    super(context);
    initView(context);
  }

  public QcSearchView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView(context);
  }

  public QcSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context);
  }

  private void initView(Context context){
    ViewGroup vg = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_action_search,null);
    etSearch = (EditText)vg.findViewById(R.id.et_search);
    btnClose = (ImageView)vg. findViewById(R.id.btn_close);
    btnClose.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        closeSearch();
      }
    });
    addView(vg);
  }

  void closeSearch(){
    if (TextUtils.isEmpty(etSearch.getText())){
      clearFocus();
      setVisibility(GONE);
    }else {
      etSearch.setText("");
    }
  }

  public void setSearchTextChangeListener(TextWatcher tw){
    etSearch.addTextChangedListener(tw);
  }

  public void setHint(String hint){
    etSearch.setHint(hint);
  }





  @Override public void onActionViewExpanded() {

  }

  @Override public void onActionViewCollapsed() {

  }
}
