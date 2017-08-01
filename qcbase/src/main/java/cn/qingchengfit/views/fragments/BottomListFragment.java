package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

import static eu.davidea.flexibleadapter.SelectableAdapter.MODE_SINGLE;

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
 * Created by Paper on 2016/12/9.
 */

public class BottomListFragment extends BottomSheetDialogFragment
    implements FlexibleAdapter.OnItemClickListener {

  protected TextView tvtitle;
  protected RecyclerView recycleview;
  protected TextView btnComfirm;
  protected ImageView btnClose;

  protected FlexibleAdapter mFlexibleAdapter;
  protected List<AbstractFlexibleItem> mDatas = new ArrayList<>();
  protected int mChooseMode = MODE_SINGLE;
  private String title;
  private ComfirmChooseListener listener;
  private int selectedPos = -1;

  public static BottomListFragment newInstance(String title) {
    Bundle args = new Bundle();
    args.putString("title", title);
    BottomListFragment fragment = new BottomListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static BottomListFragment newInstance(String title, int mChooseMode) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putInt("chooseMode", mChooseMode);
    BottomListFragment fragment = new BottomListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) title = getArguments().getString("title");
  }

  public int getSelectedPos() {
    return selectedPos;
  }

  public void setSelectedPos(int selectedPos) {
    this.selectedPos = selectedPos;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_bottom_list, container, false);
    tvtitle = (TextView) view.findViewById(R.id.title);
    btnComfirm = (TextView) view.findViewById(R.id.btn_comfirm);
    btnClose = (ImageView) view.findViewById(R.id.btn_close);
    recycleview = (RecyclerView) view.findViewById(R.id.recycleview);
    recycleview.setHasFixedSize(true);
    recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recycleview.addItemDecoration(new QcLeftRightDivider(getContext(), 1, 0, 50, 0));
    if (getArguments().containsKey("chooseMode")) {
      mChooseMode = getArguments().getInt("chooseMode", MODE_SINGLE);
    }
    mFlexibleAdapter = new FlexibleAdapter(mDatas, this);
    mFlexibleAdapter.setMode(mChooseMode);
    if (selectedPos >= 0) mFlexibleAdapter.toggleSelection(selectedPos);
    recycleview.setAdapter(mFlexibleAdapter);
    tvtitle.setText(TextUtils.isEmpty(title) ? getTitle() : title);
    btnComfirm.setVisibility(mChooseMode == MODE_SINGLE ? View.GONE : View.VISIBLE);
    btnClose.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    btnComfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null) {
          List<IFlexible> ret = new ArrayList<>();
          for (int j = 0; j < mFlexibleAdapter.getSelectedPositions().size(); j++) {
            ret.add(mFlexibleAdapter.getItem(mFlexibleAdapter.getSelectedPositions().get(j)));
          }
          listener.onComfirmClick(ret, mFlexibleAdapter.getSelectedPositions());
        }
        dismiss();
      }
    });
    //BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(((View) view.getParent()));
    //bottomSheetBehavior.setPeekHeight(MeasureUtils.getScreenHeight(getResources())/3);
    return view;
  }

  public void loadData(List<AbstractFlexibleItem> ds) {
    mDatas.clear();
    mDatas.addAll(ds);
    if (mFlexibleAdapter != null) mFlexibleAdapter.notifyDataSetChanged();
  }

  public Object getListener() {
    return listener;
  }

  public void setListener(ComfirmChooseListener listener) {
    this.listener = listener;
  }

  public String getTitle() {
    return getString(R.string.please_choose);
  }

  @Override public boolean onItemClick(int i) {
    mFlexibleAdapter.toggleSelection(i);
    mFlexibleAdapter.notifyItemChanged(i);
    if (mChooseMode == MODE_SINGLE && listener != null) {
      List<IFlexible> ret = new ArrayList<>();
      ret.add(mFlexibleAdapter.getItem(i));
      listener.onComfirmClick(ret, mFlexibleAdapter.getSelectedPositions());
      dismiss();
    }
    return false;
  }

  public interface ComfirmChooseListener {
    void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos);
  }
}
