package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.views.fragments.BaseListFragment;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/9/29.
 */

public class CardBindStudentsFragment extends BaseListFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  //@BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  //@BindView(R.id.tb_searchview_et) EditText tbSearchviewEt;
  //@BindView(R.id.tb_searchview_clear) ImageView tbSearchviewClear;
  //@BindView(R.id.tb_searchview_cancle) Button tbSearchviewCancle;
  //@BindView(R.id.searchview) LinearLayout searchview;
  //@BindView(R.id.layout_root) LinearLayout layoutRoot;

  private cn.qingchengfit.saasbase.cards.bean.Card card;

  public static CardBindStudentsFragment newInstance(
      cn.qingchengfit.saasbase.cards.bean.Card card) {
    Bundle args = new Bundle();
    args.putParcelable("card",card);
    CardBindStudentsFragment fragment = new CardBindStudentsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null){
      card = getArguments().getParcelable("card");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LinearLayout ll =
        (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, container, false);
    View v = super.onCreateView(inflater, container, savedInstanceState);
    ll.addView(v, 1);
    unbinder = ButterKnife.bind(this, ll);
    initToolbar(toolbar);
    initData();
    return ll;
  }

  private void initData() {
    List<StudentItem> studentItems = new ArrayList<>();
    for (QcStudentBean qcStudentBean : card.getUsers()) {
      studentItems.add(new StudentItem(qcStudentBean));
    }
    setDatas(studentItems,1);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(R.string.t_card_bind_student);
    toolbar.getMenu().clear();
    toolbar.getMenu().add("修改").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        // TODO: 2017/9/29 绑定会员
        return true;
      }
    });
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "";
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
