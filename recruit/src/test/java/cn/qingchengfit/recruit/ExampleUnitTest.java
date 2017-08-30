package cn.qingchengfit.recruit;

import cn.qingchengfit.recruit.model.ResumeHome;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() throws Exception {

    ResumeHome.Builder b = new ResumeHome.Builder();
    assertTrue(!b.build().checkResumeCompleted());
    b.username("xxx");
    assertTrue(!b.build().checkResumeCompleted());
    b.avatar("xxx");
    assertTrue(!b.build().checkResumeCompleted());
    b.birthday("1909-19-10");
    assertTrue(!b.build().checkResumeCompleted());
    b.brief_description("xxxx");
    assertTrue(!b.build().checkResumeCompleted());


  }
}