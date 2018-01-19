package cn.qingchengfit.pos;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest @RunWith(AndroidJUnit4.class) public class PosLogin {

  @Rule public ActivityTestRule<SplashActivity> mActivityTestRule =
    new ActivityTestRule<>(SplashActivity.class);

  @Test public void posLogin() throws InterruptedException {
    wait(2000);
    ViewInteraction appCompatEditText = onView(allOf(withId(R.id.edit_login_phone),
      childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")), 2), 2),
      isDisplayed()));
    appCompatEditText.perform(click());

    ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.edit_login_phone),
      childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")), 2), 2),
      isDisplayed()));
    appCompatEditText2.perform(replaceText("15123358198"), closeSoftKeyboard());

    ViewInteraction appCompatTextView = onView(allOf(withId(R.id.btn_get_code), withText("获取验证码"),
      childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")), 2), 3),
      isDisplayed()));
    appCompatTextView.perform(click());
    wait(2000);
    ViewInteraction textView = onView(allOf(withId(R.id.btn_get_code), withText("49s"),
      childAtPosition(
        childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 2), 3),
      isDisplayed()));
    //textView.check(matches(withText("%ds")));

    ViewInteraction appCompatEditText3 = onView(allOf(withId(R.id.edit_login_auth_code),
      childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")), 3), 2),
      isDisplayed()));
    appCompatEditText3.perform(replaceText("000000"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(allOf(withId(R.id.login_btn), withText("登录"),
      childAtPosition(childAtPosition(withId(R.id.frag_login), 0), 4), isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction linearLayout =
      onView(allOf(childAtPosition(childAtPosition(withId(R.id.frag_main), 0), 1), isDisplayed()));
    linearLayout.check(matches(isDisplayed()));
  }

  private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher,
    final int position) {

    return new TypeSafeMatcher<View>() {
      @Override public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(
          ((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
