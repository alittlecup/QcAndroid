package cn.qingchengfit.saasbase.course.course.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.network.body.CourseBody;
import cn.qingchengfit.saasbase.course.course.presenters.EditCoursePresenter;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

@Leaf(module = "course",path = "/edit/")
public class EditCourseFragment extends SaasBaseFragment implements EditCoursePresenter.EditCourseView {

    @BindView(R2.id.edit_hint) TextView editHint;
    @BindView(R2.id.suit_gyms_hint) TextView suitGymsHint;
    @BindView(R2.id.suit_gyms_edit) CommonInputView suitGymsEdit;
    @BindView(R2.id.suit_gyms_rv) RecyclerView suitGymsRv;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
    @Inject EditCoursePresenter mPresenter;

    @Need public CourseType courseType;

    private Fragment mCourseInfoFragment;
    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (mCourseInfoFragment instanceof CourseBaseInfoEditFragment) {
                CourseType courseDetail = ((CourseBaseInfoEditFragment) mCourseInfoFragment).getCourse();
                if (courseDetail != null) {
                    CourseBody body = new CourseBody.Builder().length(courseDetail.getLength())
                        .is_private(courseDetail.is_private() ? 1 : 0)
                        .capacity(courseDetail.getCapacity())
                        .min_users(courseDetail.getMin_users())
                        .photo(courseDetail.getPhoto())
                        .name(courseDetail.getName())
                        //                            .shop_ids(supportIds)
                        .plan_id(courseDetail.getPlan() == null ? null
                            : (courseDetail.getPlan().getId() == null ? null : courseDetail.getPlan().getId() + ""))
                        .build();
                    showLoading();
                    mPresenter.editCourse(courseDetail.getId(), body);
                }
            }
            return true;
        }
    };

    public static EditCourseFragment newInstance(CourseType course) {

        Bundle args = new Bundle();
        args.putParcelable("c", course);
        EditCourseFragment fragment = new EditCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(mPresenter, this);
        mPresenter.judgePermission(courseType);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("编辑基本信息");
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    @Override public String getFragmentName() {
        return EditCourseFragment.class.getName();
    }


    @Override public void showBaseInfoHint(String hint) {
        if (TextUtils.isEmpty(hint)) {
            editHint.setVisibility(View.GONE);
        } else {
            editHint.setVisibility(View.VISIBLE);
            editHint.setText(hint);
        }
    }

    @Override public void showSuitGymHint(String hint) {
        if (TextUtils.isEmpty(hint)) {
            suitGymsHint.setVisibility(View.GONE);
        } else {
            suitGymsHint.setVisibility(View.VISIBLE);
            suitGymsHint.setText(hint);
        }
    }

    @Override public void editBaseInfo(CourseType courseDetail) {
        toolbarTitile.setText("编辑基本信息");
        toolbar.inflateMenu(R.menu.menu_compelete);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        mCourseInfoFragment = CourseBaseInfoEditFragment.newInstance(courseDetail);
        getChildFragmentManager().beginTransaction().replace(R.id.course_layout, mCourseInfoFragment).commit();
    }

    @Override public void showbaseInfo(CourseType courseDetail) {
        mCourseInfoFragment = CourseBaseInfoShowFragment.newInstance(courseDetail);
        getChildFragmentManager().beginTransaction().replace(R.id.course_layout, mCourseInfoFragment).commit();
    }

    @Override public void editSuitGym(CourseType courseDetail) {
        suitGymsEdit.setVisibility(View.VISIBLE);
        suitGymsEdit.setContent(String.format(Locale.CHINA, "%d家", courseDetail.getShops().size()));
    }

    @OnClick(R2.id.suit_gyms_edit) public void editSuitGyms() {

        ArrayList<String> stringList = new ArrayList<>();
        if (courseType != null) {
            stringList.addAll(courseType.getShopIdList());
            // TODO: 2017/11/30 选择场馆
            //MutiChooseGymFragment.start(this, false, stringList,
            //    courseDetail.is_private() ? PermissionServerUtils.PRISETTING_CAN_CHANGE : PermissionServerUtils.TEAMSETTING_CAN_CHANGE, 1);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    String ids = "";
                    ArrayList<Shop> shops = data.getParcelableArrayListExtra(IntentUtils.RESULT);
                    if (shops != null) {
                        for (int i = 0; i < shops.size(); i++) {
                            if (i < shops.size() - 1) {
                                ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
                            } else {
                                ids = TextUtils.concat(ids, shops.get(i).id).toString();
                            }
                        }
                        CourseType courseDetail = (CourseType) getArguments().getParcelable("c");
                        suitGymsEdit.setTag(shops.size() + "家");
                        if (courseDetail != null) {
                            showLoading();
                            mPresenter.editCourseShops(courseDetail.getId(), ids);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override public void showSuitGym(CourseType courseDetail) {
        suitGymsRv.setVisibility(View.VISIBLE);
        List<AbstractFlexibleItem> d = new ArrayList<>();
        for (int i = 0; i < courseDetail.getShops().size(); i++) {
            d.add(new SimpleTextItemItem(courseDetail.getShops().get(i).name));
        }
        CommonFlexAdapter adapter = new CommonFlexAdapter(d);
        suitGymsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        suitGymsRv.setAdapter(adapter);
    }

    @Override public void onSucceed() {
        hideLoading();
        popBack();
    }

    @Override public void onFailed(String err) {
        hideLoading();
        ToastUtils.show(err);
    }

    @Override public void onSucceedShops() {
        hideLoading();
        ToastUtils.show("修改所属场馆成功!");
        suitGymsEdit.setContent((String) suitGymsEdit.getTag());
    }
}
