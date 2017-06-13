//package cn.qingchengfit.staffkit;
//
//import android.test.ActivityInstrumentationTestCase2;
//import android.widget.TextView;
//
//import com.robotium.solo.Solo;
//
//import cn.qingchengfit.staffkit.views.login.LoginActivity;
//
//public class BrandTest extends ActivityInstrumentationTestCase2 {
//    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "cn.qingchengfit.staffkit.views.MainActivity";
//    private Solo solo;
//
//    private static Class<?> launcherActivityClass;
//
//    static {
//        try {
//            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public BrandTest(){
//        super(launcherActivityClass);
//    }
//    public BrandTest(String pkg, Class<LoginActivity> activityClass) {
//        super(pkg, activityClass);
//    }
//
//    public BrandTest(Class<LoginActivity> activityClass) {
//        super(activityClass);
//    }
//
//
//    public void setUp() throws Exception {
//        super.setUp();
//        solo = new Solo(getInstrumentation());
//        getActivity();
//    }
//
//    @Override
//    public void tearDown() throws Exception {
////        solo.finishOpenedActivities();
//        super.tearDown();
//    }
//
//    public void testRun()  throws Exception{
//        //Wait for activity: 'com.example.ExampleActivty'
//        solo.unlockScreen();
//        solo.waitForActivity("MainActivity", 2000);
//        TextView titleBrand = solo.getText(R.id.toolbar_titile);
//        solo.clickOnView(titleBrand);
//        solo.waitForActivity("ChooseBrandActivity", 2000);
//        boolean find = solo.searchText("Move");
////        RecyclerView rv = (RecyclerView)solo.getCurrentActivity().findViewById(R.id.recycleview);
//        solo.clickInRecyclerView(3);
////        solo.waitForActivity("MainActivity", 2000);
//
//
//        boolean b = solo.waitForActivity("MainActivity", 2000);
//        assertTrue("msg",b);
//
//
//    }
//}
