package cn.qingchengfit.staff;

import cn.qingchengfit.model.responese.Login;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.rest.RestRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import rx.observers.TestSubscriber;

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
 * Created by Paper on 16/7/1 2016.
 */
@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class) public class MockTestApi {
    RestRepository restRepository;
    Login loginData;

    @Before public void setUp() {
        loginData = LoginTools.getSession();
        restRepository = new RestRepository(loginData.session_id);
    }

    @Test public void MocksTestStudent() {
        TestSubscriber<QcResponseData<Students>> subscriber = new TestSubscriber<>();
        System.out.println("test_api:" + loginData.staff.getId());
        restRepository.getGet_api().qcGetAllStudents(loginData.staff.getId(), "166", "staff_gym").toBlocking().subscribe(subscriber);
        QcResponseData<Students> simpleStudents = subscriber.getOnNextEvents().get(0);
        Assert.assertNotNull(simpleStudents);
        Assert.assertNotNull(simpleStudents.data);
        Assert.assertNotNull(simpleStudents.data.users);
        System.out.println(simpleStudents.getStatus());
    }
}
