package cn.qingchengfit.staffkit.views.gym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.course.CourseActivity;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/5/4 2016.
 */
public class ChooseGroupCourseFragment extends BaseDialogFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;
    @BindView(R.id.btn) Button btn;
    @Inject GymUseCase usecase;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private int mType;
    private String mChooseId;
    private Subscription sp;
    private List<CourseTypeSample> datas = new ArrayList<>();
    private CourseAdatper adatper;

    /**
     * @param type 团课/私教种类
     */

    public static void start(Fragment fragment, int requestCode, String id, int type) {
        ChooseGroupCourseFragment f = newInstance(type, id);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ChooseGroupCourseFragment newInstance(int type, String chooseid) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", chooseid);
        ChooseGroupCourseFragment fragment = new ChooseGroupCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            mChooseId = getArguments().getString("id");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_choose_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (mType == Configs.TYPE_GROUP) {
            toolbarTitile.setText("选择团课种类");
            btn.setVisibility(View.VISIBLE);
            btn.setText("新增团课种类");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent it = new Intent(getActivity(), CourseActivity.class);
                    it.putExtra(Configs.EXTRA_COURSE_TYPE, CourseActivity.ADD_GROUP_COURSE);
                    startActivityForResult(it, 1);
                }
            });
        } else {
            toolbarTitile.setText("选择私教种类");
            btn.setVisibility(View.VISIBLE);
            btn.setText("新增私教种类");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent it = new Intent(getActivity(), CourseActivity.class);
                    it.putExtra(Configs.EXTRA_COURSE_TYPE, CourseActivity.ADD_PRIVATE_COURSE);
                    startActivityForResult(it, 1);
                }
            });
        }

        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        adatper = new CourseAdatper(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(adatper);
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent it = new Intent();
                it.putExtra("course", datas.get(pos));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, it);
                dismiss();
            }
        });

        freshData();
        return view;
    }

    private void freshData() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", mType == Configs.TYPE_PRIVATE ? PermissionServerUtils.PRISETTING : PermissionServerUtils.TEAMSETTING);
        params.put("method", "get");
        sp = restRepository.getGet_api()
            .qcGetCoursesPermission(App.staffId, params, mType == Configs.TYPE_PRIVATE ? 1 : 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CourseTypeSamples>>() {
                @Override public void call(QcResponseData<CourseTypeSamples> qcResponseChooseCourses) {
                    if (qcResponseChooseCourses.getStatus() == ResponseConstant.SUCCESS) {
                        datas.clear();
                        datas.addAll(qcResponseChooseCourses.data.courses);
                        adatper.notifyDataSetChanged();
                        recycleview.setNoData(datas.size() == 0);
                    } else {
                        // ToastUtils.logHttp(qcResponseChooseCourses);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        freshData();
    }

    @Override public void onDestroyView() {
        if (sp != null) sp.unsubscribe();
        super.onDestroyView();
    }

    public class CourseAdatper extends RecyclerView.Adapter<CourseVh> implements View.OnClickListener {
        List<CourseTypeSample> datas;
        OnRecycleItemClickListener listener;

        public CourseAdatper(List<CourseTypeSample> datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public CourseVh onCreateViewHolder(ViewGroup parent, int viewType) {
            CourseVh vh = new CourseVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
            vh.itemView.setOnClickListener(this);
            return vh;
        }

        @Override public void onBindViewHolder(CourseVh holder, int position) {
            holder.itemView.setTag(position);
            CourseTypeSample c = datas.get(position);
            Glide.with(holder.itemView.getContext()).load(PhotoUtils.getSmall(c.getPhoto())).into(holder.img);
            holder.text1.setText(c.getName());
            holder.text3.setText(String.format(Locale.CHINA, "时长%d分钟", c.getLength() / 60));
            if (mChooseId != null && mChooseId.equalsIgnoreCase(c.getId())) {
                holder.righticon.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView.getContext()).load(R.drawable.ic_hook_circle).into(holder.righticon);
            } else {
                holder.righticon.setVisibility(View.GONE);
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class CourseVh extends RecyclerView.ViewHolder {
        @BindView(R.id.img) ImageView img;
        @BindView(R.id.img_foot) ImageView imgFoot;
        @BindView(R.id.text1) TextView text1;
        @BindView(R.id.texticon) ImageView texticon;
        @BindView(R.id.text2) TextView text2;
        @BindView(R.id.text3) TextView text3;
        @BindView(R.id.righticon) ImageView righticon;
        @BindView(R.id.course_layout) RelativeLayout courseLayout;

        public CourseVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
