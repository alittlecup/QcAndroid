package cn.qingchengfit.widgets;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    //@Test
    //public void useAppContext() throws Exception {
    //    // Context of the app under test.
    //    Context appContext = InstrumentationRegistry.getTargetContext();
    //
    //    assertEquals("cn.qingchengfit.widgets.test", appContext.getPackageName());
    //}

  @Test
  public void testGetHost(){
    Uri uri = Uri.parse("qcstaff://test/hahah/?id=1");
    String s =cn.qingchengfit.utils.Utils.getUriHostPath(uri);
    assertEquals(s,"test/hahah");

  }


}
