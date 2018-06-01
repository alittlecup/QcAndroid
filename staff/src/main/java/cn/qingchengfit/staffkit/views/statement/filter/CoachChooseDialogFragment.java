package cn.qingchengfit.staffkit.views.statement.filter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.StringAdapter;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.MeasureUtils;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/6/28 2016.
 */
public class CoachChooseDialogFragment extends BaseDialogFragment implements CoachChooseView {

	RecyclerView recyclerview;
    List<String> stringList = new ArrayList<>();
    @Inject CoachChoosePresenter presenter;
    private List<Staff> coaches = new ArrayList<>();
    private StringAdapter adapter;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_coach, container, false);
      recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

      delegatePresenter(presenter, this);
        adapter = new StringAdapter(stringList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (coaches != null && coaches.size() > pos) {
                    RxBus.getBus().post(coaches.get(pos));
                }
                dismiss();
            }
        });
        presenter.queryCoach();
        return view;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = MeasureUtils.dpToPx(245f, getResources());
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        return dialog;
    }

    @Override public void onCoaches(List<Staff> coaches) {
        Staff coach = new Staff("全部教练", "", "", 0);
        coach.id = "-1";
        coaches.add(coach);
        this.coaches = coaches;

        stringList.clear();
        for (int i = 0; i < coaches.size(); i++) {
            stringList.add(coaches.get(i).username);
        }
        adapter.notifyDataSetChanged();
    }
}
