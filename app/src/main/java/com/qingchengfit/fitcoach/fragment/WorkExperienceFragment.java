package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.paper.paperbaselibrary.component.GridViewInScroll;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.BaseInfoBean;
import com.qingchengfit.fitcoach.component.TagGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkExperienceFragment extends Fragment {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private RecordComfirmAdapter adapter;

    public WorkExperienceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
        ButterKnife.bind(this, view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecordComfirmAdapter(new ArrayList<>());
        recyclerview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public static class WorkExperienceVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_time)
        TextView itemTime;
        @Bind(R.id.item_studio_name)
        TextView itemStudioName;
        @Bind(R.id.item_studio_comfirm)
        TextView itemStudioComfirm;
        @Bind(R.id.item_studio_classes)
        GridViewInScroll itemStudioClasses;
        @Bind(R.id.item_tag_group)
        TagGroup itemTagGroup;
        @Bind(R.id.item_studio_expaned)
        ToggleButton itemExpaned;

        public WorkExperienceVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LogUtil.e("hahhahahah");
            itemExpaned.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    itemStudioClasses.setVisibility(View.VISIBLE);
                    itemTagGroup.setVisibility(View.VISIBLE);
                } else {
                    itemTagGroup.setVisibility(View.GONE);
                    itemStudioClasses.setVisibility(View.GONE);
                }
            });

        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_work_classes.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.item_workclass_name)
        TextView itemWorkclassName;
        @Bind(R.id.item_workclass_num)
        TextView itemWorkclassNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class RecordComfirmAdapter extends RecyclerView.Adapter<WorkExperienceVH> {


        private List<BaseInfoBean> datas;

        public RecordComfirmAdapter(List datas) {
            this.datas = datas;
        }

        @Override
        public WorkExperienceVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WorkExperienceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workexperience, null));
        }

        @Override
        public void onBindViewHolder(WorkExperienceVH holder, int position) {

            holder.itemTagGroup.setTags("非常好", "长得帅", "一般般嘛");
            List<String> aaaas = new ArrayList<>();
            aaaas.add("12");
            aaaas.add("12");
            aaaas.add("12");
            aaaas.add("12");
            GridInScrollAdapter adapter1 = new GridInScrollAdapter(aaaas);
            holder.itemStudioClasses.setAdapter(adapter1);

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    class ClassBean {
        String name;
        String classNum;
        String studentNum;

        public ClassBean(String name, String classNum, String studentNum) {
            this.name = name;
            this.classNum = classNum;
            this.studentNum = studentNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassNum() {
            return classNum;
        }

        public void setClassNum(String classNum) {
            this.classNum = classNum;
        }

        public String getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(String studentNum) {
            this.studentNum = studentNum;
        }
    }

    class GridInScrollAdapter extends BaseAdapter {
        List data;

        public GridInScrollAdapter(List data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_classes, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            return view;
        }


    }
}
