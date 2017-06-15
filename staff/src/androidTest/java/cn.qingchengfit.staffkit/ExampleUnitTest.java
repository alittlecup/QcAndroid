//package cn.qingchengfit.staffkit;
//
//import android.support.design.widget.TextInputLayout;
//import android.test.ActivityInstrumentationTestCase2;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.robotium.solo.Solo;
//
//import cn.qingchengfit.staffkit.views.login.LoginActivity;
//
//public class ExampleUnitTest  extends ActivityInstrumentationTestCase2 {
//    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "cn.qingchengfit.staffkit.views.login.LoginActivity";
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
//    public ExampleUnitTest(){
//        super(launcherActivityClass);
//    }
//    public ExampleUnitTest(String pkg, Class<LoginActivity> activityClass) {
//        super(pkg, activityClass);
//    }
//
//    public ExampleUnitTest(Class<LoginActivity> activityClass) {
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
//        solo.waitForActivity("LoginActivity", 2000);
//        //Clear the EditText editText1
//        EditText phone = ((TextInputLayout) solo.getView(R.id.login_phone_num)).getEditText();
//        EditText pw = ((TextInputLayout) solo.getView(R.id.login_phone_verity)).getEditText();
//        solo.clearEditText(phone);
//        solo.enterText(phone, "18618304202");
//        solo.clearEditText(pw);
//        solo.enterText(pw, "admin123");
//        Button btn = (Button) solo.getView(R.id.login_btn);
//        solo.clickOnView(btn);
//        solo.waitForActivity("LoginActivity", 1000);
////        solo.searchText("会员卡种类");
//        boolean b = solo.waitForActivity("MainActivity", 2000);
//        assertTrue("msg",b);
//    }
//}
