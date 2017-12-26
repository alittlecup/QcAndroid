package cn.qingchengfit.staffkit.views.allotsales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshStudent;
import cn.qingchengfit.staffkit.views.FilterCommonFragment;
import cn.qingchengfit.staffkit.views.custom.CustomSwipeRefreshLayout;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.student.StudentAdapter;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.AlphabetView;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import com.tubb.smrv.SwipeMenuRecyclerView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

import static cn.qingchengfit.staffkit.views.allotsales.MultiModifyFragmentBuilder.newMultiModifyFragment;

public class SaleDetailFragment extends FilterCommonFragment
    implements SaleDetailPresenter.PresenterView, View.OnClickListener {

  public LinearLayoutManager mLinearLayoutManager;
  // 学员list
  public List<StudentBean> datas = new ArrayList<>();
  // 未筛选的数据
  public List<StudentBean> originStuList = new ArrayList<>();
  @Inject SaleDetailPresenter presenter;
  @Inject StaffWrapper staffWrapper;
  @Inject StaffWrapper saler;
  @BindView(R.id.ll_add_stu) LinearLayout llAddStu;
  @BindView(R.id.ll_sort) LinearLayout llSort;
  @BindView(R.id.myhome_appBar) AppBarLayout myhomeAppBar;
  @BindView(R.id.smrv_sale) SwipeMenuRecyclerView smrvSale;
  @BindView(R.id.swipe) CustomSwipeRefreshLayout swipe;
  @BindView(R.id.scroll_root) CoordinatorLayout scrollRoot;
  Unbinder unbinder;
  @BindView(R.id.alphabetview) AlphabetView alphabetview;
  private DetailStudentAdapter studentAdapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_allotsale_saledetail, container, false);
    setView(view);
    super.onCreateView(inflater, container, savedInstanceState);
    if (saler.getStaff() == null) return view;
    mCallbackActivity.setToolbar(TextUtils.isEmpty(saler.getStaff().username) ? getString(
        R.string.qc_allotsale_sale_detail_notitle)
            : getString(R.string.qc_allotsale_sale_detail_title, saler.getStaff().username), false,
        null, TextUtils.isEmpty(saler.getStaff().username) ? R.menu.menu_multi_allot
            : R.menu.menu_multi_modify, new Toolbar.OnMenuItemClickListener() {

          @Override public boolean onMenuItemClick(MenuItem item) {
            // 跳转批量修改
            MultiModifyFragment multiModifyFragment =
                newMultiModifyFragment(saler.getStaff(), filter,
                    TextUtils.isEmpty(saler.getStaff().username)
                        ? MultiModifyFragment.TYPE_MULTI_ALLOT
                        : MultiModifyFragment.TYPE_MULTI_MODIFY);
            multiModifyFragment.originList = originStuList;
            multiModifyFragment.students = datas;
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), multiModifyFragment)
                .addToBackStack(null)
                .commit();
            return false;
          }
        });

    initDI();
    initView();
    showLoadingTrans();

    if (getActivity() instanceof AllotSalesActivity) {
      // TODO:  2017/3/29 重写吧   没勇气
      if (((AllotSalesActivity) getActivity()).filterFragment != null) {
        ((AllotSalesActivity) getActivity()).filterFragment.setFromPage(2);
      }
    }

    RxBusAdd(EventFreshStudent.class).subscribe(new Action1<EventFreshStudent>() {
      @Override public void call(EventFreshStudent eventFreshStudent) {
        freshData();
      }
    });

    presenter.queryStudent(App.staffId, filter, saler.getStaff().id);
    presenter.getSellerUsers(saler.getStaff().id);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onDrawerClosed(View drawerView) {
    //        filterFragment.resetView(filter);
    super.onDrawerClosed(drawerView);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void initDI() {
    delegatePresenter(presenter, this);
  }

  private void initView() {
    studentAdapter = new DetailStudentAdapter(datas);
    mLinearLayoutManager = new LinearLayoutManager(getActivity());

    smrvSale.setLayoutManager(mLinearLayoutManager);
    smrvSale.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    smrvSale.setAdapter(studentAdapter);
    swipe.setColorSchemeResources(R.color.colorPrimary);
    swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        presenter.queryStudent(App.staffId, filter, saler.getStaff().id);
      }
    });
    smrvSale.setAdapter(studentAdapter);
    // 是否启用 item 滑动菜单
    if (TextUtils.isEmpty(saler.getStaff().username)) {
      studentAdapter.setSwipeEnable(false);
    } else {
      studentAdapter.setSwipeEnable(true);
    }

    // 添加名下会员
    if (TextUtils.isEmpty(saler.getStaff().username)) {
      llAddStu.setVisibility(View.GONE);
    } else {
      llAddStu.setOnClickListener(this);
    }

    // 侧边索引点击监听,关闭item的左滑菜单
    alphabetview.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return false;
      }
    });
    // 侧边索引选中监听,list滚动到指定位置
    alphabetview.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
      @Override public void onChange(int position, String s) {
        if ("#".equals(s)) s = "~";
        mLinearLayoutManager.scrollToPositionWithOffset(
            studentAdapter.getPositionForSection(s.charAt(0)), 0);
      }
    });

    //        freshData();

    tvSortRegister.setOnClickListener(this);
    tvSortAlpha.setOnClickListener(this);
    tvSortFilter.setOnClickListener(this);
  }

  public void filterSaleConfirm(StudentFilterEvent event) {
    filter = event.filter;
    getActivity().runOnUiThread(new Runnable() {
      @Override public void run() {
        ((AllotSalesActivity) getActivity()).drawer.closeDrawers();
        showLoading();
        onClickFilterUIChange();
        freshData();
      }
    });
  }

  @OnClick public void onClick(View v) {
    // 添加名下会员
    switch (v.getId()) {
      case R.id.ll_add_stu:
        MultiModifyFragment fragment =
            newMultiModifyFragment(saler.getStaff(), filter, MultiModifyFragment.TYPE_ADD_STUDENT);
        fragment.originList = originStuList;
        fragment.students = datas;
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), fragment)
            .addToBackStack("saleDetail")
            .commit();
        break;
      case R.id.tv_sort_alpha:
        sortType = SORT_TYPE_ALPHA;
        //onClickSortUIChange();
        sortData(datas);
        studentAdapter.notifyDataSetChanged();
        break;
      case R.id.tv_sort_register:
        sortType = SORT_TYPE_REGISTER;
        //onClickSortUIChange();
        sortData(datas);
        studentAdapter.notifyDataSetChanged();
        break;
      case R.id.tv_sort_filter:
        if (getActivity() instanceof AllotSalesActivity) {
          ((AllotSalesActivity) getActivity()).drawer.openDrawer(GravityCompat.END);
        }
        break;
    }
  }

  private void freshData() {
    presenter.queryStudent(App.staffId, filter,
        saler.getStaff() != null ? saler.getStaff().id : null);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  @Override public void onStudentList(List<StudentBean> list) {
    hideLoading();
    hideLoadingTrans();
    swipe.setRefreshing(false);
    datas.clear();
    datas.addAll(list);
    sortData(datas);
    studentAdapter.notifyDataSetChanged();
  }

  @Override public void onShowError(String e) {
    hideLoading();
    ToastUtils.show(e);
  }

  @Override public void onRemoveSucess(int position) {
    studentAdapter.getData().remove(position);
    studentAdapter.notifyItemRemoved(position);
  }

  @Override public void stopRefresh() {
    hideLoading();
    hideLoadingTrans();
  }

  @Override public void onGetOrigin(List<StudentBean> originList) {
    originStuList.clear();
    originStuList.addAll(originList);
  }

  public class DetailStudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
      implements View.OnClickListener {
    List<StudentBean> datas;
    boolean swipeEnable = true;//是否启用滑动菜单

    private int TYPE_FOOTER = 0;
    private int TYPE_NORMAL = 1;

    public DetailStudentAdapter(List<StudentBean> d) {
      this.datas = d;
    }

    public List<StudentBean> getData() {
      return datas;
    }

    public void setSwipeEnable(boolean swipeEnable) {
      this.swipeEnable = swipeEnable;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      RecyclerView.ViewHolder holder;

      if (viewType == TYPE_NORMAL) {
        holder = new MyBindingViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_allotsale_detail_student, parent, false));
        RxBusAdd(EventFreshStudent.class).subscribe(new Action1<EventFreshStudent>() {
          @Override public void call(EventFreshStudent eventFreshStudent) {
            freshData();
          }
        });
        ((MyBindingViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
            StudentBean bean = (StudentBean) view.getTag();
            it.putExtra("student", bean);
            startActivity(it);
          }
        });
        // 注册 event 刷新列表
        RxBusAdd(EventFreshStudent.class).subscribe(new Action1<EventFreshStudent>() {
          @Override public void call(EventFreshStudent eventFreshStudent) {
            freshData();
          }
        });
        return holder;
      } else {
        holder = new StudentAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_total_count_footer, parent, false));
        // 注册 event 刷新列表
        RxBusAdd(EventFreshStudent.class).subscribe(new Action1<EventFreshStudent>() {
          @Override public void call(EventFreshStudent eventFreshStudent) {
            freshData();
          }
        });
        return holder;
      }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
      if (datas.size() == 0) return;
      if (holder instanceof MyBindingViewHolder) {
        holder.itemView.setTag(datas.get(position));
        StudentBean studentBean = datas.get(position);

        // 销售names
        if (studentBean.sellers == null || studentBean.sellers.size() == 0) {
          ((MyBindingViewHolder) holder).itemPersonDesc.setText(
              holder.itemView.getContext().getString(R.string.qc_allotsale_sale, " "));
        } else {
          ((MyBindingViewHolder) holder).itemPersonDesc.setText(holder.itemView.getContext()
              .getString(R.string.qc_allotsale_sale,
                  StringUtils.sellersNames(studentBean.sellers)));
        }

        ((MyBindingViewHolder) holder).itemPersonName.setText(studentBean.getUsername());
        ((MyBindingViewHolder) holder).itemPersonPhonenum.setText(studentBean.getPhone());
        //((MyBindingViewHolder)holder).itemPersonHeaderLoop.setBackgroundDrawable(
        //    new LoopView(TextUtils.isEmpty(studentBean.color) ? "#00000000" : studentBean.color));
        if (studentBean.gender) {//男
          ((MyBindingViewHolder) holder).itemPersonGender.setImageResource(
              R.drawable.ic_gender_signal_male);
        } else {
          ((MyBindingViewHolder) holder).itemPersonGender.setImageResource(
              R.drawable.ic_gender_signal_female);
        }
        if (studentBean.isTag) {
          ((MyBindingViewHolder) holder).itemStudentDetailAlpha.setVisibility(View.VISIBLE);
          ((MyBindingViewHolder) holder).itemStudentDetailAlpha.setText(getItem(position).head);
        } else {
          ((MyBindingViewHolder) holder).itemStudentDetailAlpha.setVisibility(View.GONE);
        }

        StringUtils.studentStatus(((MyBindingViewHolder) holder).status, studentBean.status);
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(studentBean.avatar))
            .asBitmap()
            .placeholder(R.drawable.ic_default_head_nogender)
            .error(R.drawable.ic_default_head_nogender)
            .into(new CircleImgWrapper(((MyBindingViewHolder) holder).itemPersonHeader,
                holder.itemView.getContext()));
      } else {
        StudentAdapter.ItemViewHolder itemViewHolder;
        itemViewHolder = (StudentAdapter.ItemViewHolder) holder;
        itemViewHolder.tvTotalCount.setText(datas.size() + "名会员");
      }
    }

    public int getPositionForSection(char section) {
      for (int i = 0; i < getItemCount() - 1; i++) {
        String sortStr = datas.get(i).head;
        char firstChar = sortStr.toUpperCase().charAt(0);
        if (firstChar == section) {
          return i;
        }
      }
      return -1;
    }

    public StudentBean getItem(int position) {
      if (position == datas.size()) {
        position--;
      }
      return datas.get(position);
    }

    @Override public int getItemCount() {
      if (datas.size() != 0) {
        return datas.size() + 1;
      }
      return 0;
    }

    @Override public int getItemViewType(int position) {
      if (position == datas.size()) return TYPE_FOOTER;
      return TYPE_NORMAL;
    }

    @Override public void onClick(View v) {
      // 跳转会员详情
      Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
      StudentBean bean = (StudentBean) v.getTag();
      it.putExtra("student", bean);
      startActivity(it);
    }
  }

  public class MyBindingViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_student_detail_alpha) TextView itemStudentDetailAlpha;
    @BindView(R.id.item_person_header) ImageView itemPersonHeader;
    @BindView(R.id.item_person_name) TextView itemPersonName;
    @BindView(R.id.item_person_gender) ImageView itemPersonGender;
    @BindView(R.id.tv_referrer_count) TextView tvReferrerCount;
    @BindView(R.id.item_person_phonenum) TextView itemPersonPhonenum;
    @BindView(R.id.item_person_desc) TextView itemPersonDesc;
    @BindView(R.id.status) TextView status;
    @BindView(R.id.sml) LinearLayout sml;

    public MyBindingViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
