package cn.qingchengfit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.items.DelStudentItem;
import cn.qingchengfit.model.base.Personage;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
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
@FragmentWithArgs public class BottomStudentsFragment extends BottomSheetDialogFragment implements FlexibleAdapter.OnItemClickListener {
    public static final int CHOOSE_STUDENT = 0;
    public static final int CHAT_FRIENDS = 1;
  @BindView(R.id.tv_stud_count) protected TextView tvStudCount;
  protected CommonFlexAdapter adapter;
    @Arg(required = false) int type;
    @BindView(R.id.tv_clear_all) TextView tvClearAll;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    List<AbstractFlexibleItem> datas = new ArrayList<>();
    Unbinder unbinder;
    private BottomStudentsListener listener;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_allotsale_show_selected, container, false);
        unbinder = ButterKnife.bind(this, view);
        recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        //recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
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
              if (qcStudentBeen.get(i) instanceof Personage) {
                datas.add(new DelStudentItem((Personage) qcStudentBeen.get(i)));
              }
            }
            if (adapter != null && tvStudCount != null) {
                adapter.notifyDataSetChanged();
              showTitle();
            }
        }
    }

  protected void showTitle() {
    tvStudCount.setText(getString(
        type == CHOOSE_STUDENT ? R.string.qc_allotsale_select : R.string.qc_chat_friend_select,
        datas.size()));
  }

    @Override public boolean onItemClick(int position) {
        adapter.removeItem(position);
      showTitle();
      if (adapter.getItemCount() == 0) {
            dismiss();
        }
        return true;
    }

    @OnClick(R.id.tv_clear_all) public void onClick() {
        datas.clear();
        dismiss();
    }

    @Override public void dismiss() {

        super.dismiss();
    }

    @Override public void onDestroy() {

        if (listener != null) {
            List<Personage> ret = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i) instanceof DelStudentItem) {
                    ret.add(((DelStudentItem) datas.get(i)).getUser());
                }
            }
            listener.onBottomStudents(ret);
        }

        super.onDestroy();
    }

    @Override public void onDestroyView() {
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
