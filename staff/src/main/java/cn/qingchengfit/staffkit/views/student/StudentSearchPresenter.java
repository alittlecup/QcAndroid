package cn.qingchengfit.staffkit.views.student;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.widgets.AlphabetView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/20.
 */
public class StudentSearchPresenter extends BasePresenter {

    public PresenterView view;
    private Subscription spFilter;

    @Inject public StudentSearchPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spFilter != null) spFilter.unsubscribe();
    }

    public void filter(String keyword) {
        if (spFilter != null) spFilter.unsubscribe();
        spFilter = StudentAction.newInstance()
            .getStudentByKeyWord(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<QcStudentBean>>() {
                @Override public void call(List<QcStudentBean> qcStudentBeen) {
                    handleData(qcStudentBeen);
                }
            })

        ;
    }

    private void handleData(List<QcStudentBean> data) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (QcStudentBean student : data) {
            StudentBean bean = new StudentBean();
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.systemUrl = "后台无数据";
            bean.id = student.getId();
            bean.color = "";
            bean.support_shop = student.getSupport_gym();
            bean.support_shop_ids = student.getSupoort_gym_ids();

            //bean.brandid = brand_id;
            bean.joined_at = TextUtils.isEmpty(student.join_at()) ? "" : student.join_at();
            //bean.modelid = gymid;
            //bean.model = gymmodel;
            if (TextUtils.isEmpty(student.getHead()) || !AlphabetView.Alphabet.contains(student.getHead())) {
                bean.head = "~";
            } else {
                bean.head = student.getHead().toUpperCase();
            }
            if (!curHead.equalsIgnoreCase(bean.head)) {
                bean.setIsTag(true);
                curHead = bean.head;
            }

            StringBuffer sb = new StringBuffer();
            sb.append("手机：").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            studentBeanList.add(bean);
        }
        view.onFilterStudentList(studentBeanList);
    }

    public interface PresenterView extends PView {
        void onFilterStudentList(List<StudentBean> list);
    }
}