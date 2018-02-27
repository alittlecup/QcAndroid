//package cn.qingchengfit.staffkit.views.course;
//
//import CourseBody;
//import cn.qingchengfit.model.base.Brand;
//import cn.qingchengfit.model.base.CoachService;
//import CourseType;
//import CourseTypes;
//import cn.qingchengfit.model.responese.QcResponseData;
//import cn.qingchengfit.staff.App;
//import cn.qingchengfit.staff.LoginTools;
//import cn.qingchengfit.staff.RxUnitTestTools;
//import cn.qingchengfit.staffkit.BuildConfig;
//import cn.qingchengfit.staffkit.rest.RestRepository;
//import cn.qingchengfit.utils.GymUtils;
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import javax.inject.Inject;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.robolectric.RobolectricGradleTestRunner;
//import org.robolectric.annotation.Config;
//import org.robolectric.shadows.ShadowLog;
//import rx.observers.TestSubscriber;
//
//import static org.mockito.Matchers.anyListOf;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 16/8/19.
// */
//@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, application = App.class)
//public class CourseDetailPresenterTest {
//    @Inject Brand brand;
//    @Inject CoachService coachService;
//    @Inject CourseDetailPresenter presenter;
//    @Inject AddCoursePresenter addCoursePresenter;
//    @Inject RestRepository restRepository;
//
//    CourseDetailPresenter.CourseDetailView view;
//    AddCoursePresenter.AddCourseView addCourseView;
//    private String ssid, staffid;
//
//    static void setFinalStatic(Field field, Object newValue) throws Exception {
//        field.setAccessible(true);
//        Field modifiersField = Field.class.getDeclaredField("modifiers");
//        modifiersField.setAccessible(true);
//        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//        field.set(null, newValue);
//    }
//
//    @Before public void setUp() throws Exception {
//        ShadowLog.stream = System.out;
//        view = mock(CourseDetailPresenter.CourseDetailView.class);
//        addCourseView = mock(AddCoursePresenter.AddCourseView.class);
//        ssid = LoginTools.getSession().session_id;
//        staffid = LoginTools.getSession().staff.getId();
//        presenter.attachView(view);
//        addCoursePresenter.attachView(addCourseView);
//        RxUnitTestTools.openRxTools();
//    }
//
//    //    public void
//
//    @Test public void testQueryDetail() throws Exception {
//        presenter.queryDetail("137");
//        ArgumentCaptor<CourseType> captor = ArgumentCaptor.forClass(CourseType.class);
//        verify(view).setCourseInfo(captor.capture());
//        verify(view).setJacket(anyListOf(String.class));
//        CourseType courseDetail = captor.getValue();
//        Assert.assertEquals(courseDetail.getId(), "137");
//    }
//
//    @Test public void testDelCourse() throws Exception {
//        addCoursePresenter.addCourse(staffid, new CourseBody.Builder().capacity(6)
//            .is_private(1)
//            .length(3600)
//            .min_users(1)
//            .name("自动生成")
//            .model(coachService.getModel())
//            .id(coachService.getId())
//            .build());
//        verify(addCourseView).onSuccess();
//        TestSubscriber<QcResponseData<CourseTypes>> subscriber = new TestSubscriber<>();
//        restRepository.getGet_api().qcGetCourses(staffid, GymUtils.getParams(coachService, brand), 1).toBlocking().subscribe(subscriber);
//        QcResponseData<CourseTypes> list = subscriber.getOnNextEvents().get(0);
//        Assert.assertEquals(list.getStatus(), 200);
//        String id = null;
//        for (int i = 0; i < list.data.courses.size(); i++) {
//            if (list.data.courses.get(i).getName().equalsIgnoreCase("自动生成")) id = list.data.courses.get(i).getId();
//        }
//        Assert.assertNotNull(id);
//        presenter.delCourse(staffid, id);
//        verify(view).onDelSuccess();
//    }
//
//    @Test public void testJudgeDel() throws Exception {
//
//    }
//}