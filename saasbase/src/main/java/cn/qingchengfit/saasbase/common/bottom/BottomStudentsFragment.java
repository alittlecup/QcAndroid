package cn.qingchengfit.saasbase.common.bottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.items.DelStudentItem;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * Created by Paper on 2017/3/15.
 */
public class BottomStudentsFragment extends BottomSheetDialogFragment
  implements FlexibleAdapter.OnItemClickListener {
  public static final int CHOOSE_STUDENT = 0;
  public static final int CHAT_FRIENDS = 1;
  protected CommonFlexAdapter adapter;
  int type;
	protected TextView tvStudCount;
	TextView tvClearAll;
	RecyclerView recycleview;
  List<AbstractFlexibleItem> datas = new ArrayList<>();

  private BottomStudentsListener listener;

  public static BottomStudentsFragment newInstance(int type) {
    Bundle args = new Bundle();
    args.putInt("t", type);
    BottomStudentsFragment fragment = new BottomStudentsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) type = getArguments().getInt("t", 0);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view =
      inflater.inflate(R.layout.dialog_fragment_allotsale_show_selected, container, false);
    tvStudCount = (TextView) view.findViewById(R.id.tv_stud_count);
    tvClearAll = (TextView) view.findViewById(R.id.tv_clear_all);
    recycleview = (RecyclerView) view.findViewById(R.id.recycleview);
    view.findViewById(R.id.tv_clear_all).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        BottomStudentsFragment.this.onClick();
      }
    });

    recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recycleview.addItemDecoration(
      new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    adapter = new CommonFlexAdapter(datas, this);
    recycleview.setAdapter(adapter);
    if (adapter != null && tvStudCount != null) {
      adapter.notifyDataSetChanged();
      tvStudCount.setText(getString(R.string.qc_allotsale_select, datas.size()));
    }
    return view;
  }

  public <T extends Personage> void setDatas(List<T> qcStudentBeen) {
    if (qcStudentBeen != null) {
      datas.clear();
      for (int i = 0; i < qcStudentBeen.size(); i++) {
        datas.add(new DelStudentItem(qcStudentBeen.get(i)));
      }
      if (adapter != null && tvStudCount != null) {
        adapter.notifyDataSetChanged();
        showTitle();
      }
    }
  }

  @Override public boolean onItemClick(int position) {
    adapter.removeItem(position);
    if (adapter.getItemCount() == 0) {
      dismiss();
    }
    showTitle();
    return true;
  }

 public void onClick() {
    DirtySender.studentList.clear();
    datas.clear();
    adapter.clear();
    dismiss();
  }

  protected void showTitle() {
    tvStudCount.setText(getString(
      type == CHOOSE_STUDENT ? R.string.qc_allotsale_select : R.string.qc_chat_friend_select,
      datas.size()));
  }

  @Override public void dismiss() {
    super.dismiss();
  }

  @Override public void onDestroyView() {
    if (listener != null) {
      List<Personage> ret = new ArrayList<>();
      for (int i = 0; i < adapter.getItemCount(); i++) {
        if (adapter.getItem(i) instanceof DelStudentItem) {
          ret.add(((DelStudentItem) adapter.getItem(i)).getUser());
        }
      }
      listener.onBottomStudents(ret);
    }
    super.onDestroyView();
  }

  public BottomStudentsListener getListener() {
    return listener;
  }

  public void setListener(BottomStudentsListener listener) {
    this.listener = listener;
  }

  public interface BottomStudentsListener {
    void onBottomStudents(List<Personage> list);
  }
}
