package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.component.FullyLinearLayoutManager;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCourseManageFragment extends Fragment {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.starttime)
    CommonInputView starttime;
    @Bind(R.id.endtime)
    CommonInputView endtime;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.add)
    TextView add;
    List<CmBean> datas = new ArrayList<>();
    private CmAdapter adapter;
    private TimePeriodChooser timeDialogWindow;
    private TimeDialogWindow timeWindow;
    private DialogList dialogList;
    private String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private int mType = Configs.TYPE_GROUP;//类型 1是私教 2是团课


    public static AddCourseManageFragment newInstance() {

        Bundle args = new Bundle();

        AddCourseManageFragment fragment = new AddCourseManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddCourseManageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course_manage, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("添加团课排期");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        datas.add(new CmBean(1, new Date(), null));
        adapter = new CmAdapter();
        recyclerview.setLayoutManager(new FullyLinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        return view;
    }

    @OnClick(R.id.add)
    public void onAdd() {
        if (datas.size() > 6) {
            ToastUtils.showDefaultStyle("已达到上限");
            return;
        }
        if (mType == Configs.TYPE_PRIVATE) {
            CmBean bean = new CmBean(1, new Date(), new Date());
            datas.add(bean);
        } else {
            CmBean bean = new CmBean(1, new Date());
            datas.add(bean);
        }

        adapter.notifyDataSetChanged();
    }


    /**
     * 选择周几
     */
    public void chooseWeek(int pos) {
        if (dialogList == null) {
            dialogList = new DialogList(getContext());
            dialogList.title("请选择健身房");
            dialogList.list(weeks, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    datas.get(pos).week = position;
                    adapter.notifyItemChanged(pos);
                    dialogList.dismiss();
                }
            });
        }
        dialogList.show();
    }

    public void chooseTime(int pos) {
        if (mType == Configs.TYPE_PRIVATE) {
            if (timeWindow == null) {
                timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS);
                timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        datas.get(pos).dateStart = date;
                        adapter.notifyItemChanged(pos);
                    }
                });
            }
            timeWindow.show();

        } else {
            if (timeDialogWindow == null) {
                timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS);
                timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date start, Date end) {
                        if (start.getTime() >= end.getTime()) {
                            ToastUtils.showDefaultStyle("开始时间不能小于结束时间");
                            return;
                        }

                        datas.get(pos).dateStart = start;
                        datas.get(pos).dateEnd = end;
                        adapter.notifyItemChanged(pos);
                    }
                });
            }
            timeDialogWindow.show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class CmVh extends RecyclerView.ViewHolder {
        @Bind(R.id.text1)
        TextView text1;
        @Bind(R.id.layout1)
        RelativeLayout layout1;
        @Bind(R.id.text2)
        TextView text2;
        @Bind(R.id.layout2)
        RelativeLayout layout2;
        @Bind(R.id.delete)
        ImageView delete;

        public CmVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CmAdapter extends RecyclerView.Adapter<CmVh> {


        @Override
        public CmVh onCreateViewHolder(ViewGroup parent, int viewType) {
            CmVh vh = new CmVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_manage, parent, false));
            return vh;
        }

        @Override
        public void onBindViewHolder(CmVh holder, int position) {
            CmBean bean = datas.get(position);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            });
            holder.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //选择周几
                    chooseWeek(position);
                }
            });
            holder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //选择时间
                    chooseTime(position);
                }
            });
            if (bean.week >0)
                holder.text1.setText(weeks[bean.week]);
            if (bean.dateEnd != null) {
                holder.text2.setText(DateUtils.getTimeHHMM(bean.dateStart) + "-" + DateUtils.getTimeHHMM(bean.dateEnd));
            } else {
                holder.text2.setText(DateUtils.getTimeHHMM(bean.dateStart));
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    public class CmBean {
        public int week;
        public Date dateStart;
        public Date dateEnd;

        public CmBean(int week, Date dateStart) {
            this.week = week;
            this.dateStart = dateStart;
            this.dateEnd = null;
        }

        public CmBean(int week, Date dateStart, Date dateEnd) {
            this.week = week;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
        }
    }

}
