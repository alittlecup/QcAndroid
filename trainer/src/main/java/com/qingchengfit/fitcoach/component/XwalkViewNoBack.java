//package com.qingchengfit.fitcoach.component;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.KeyEvent;
//
//import org.xwalk.core.XWalkView;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * <p>
// * Created by Paper on 15/9/25 2015.
// */
//public class XwalkViewNoBack extends XWalkView {
//    public XwalkViewNoBack(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public XwalkViewNoBack(Context context, Activity activity) {
//        super(context, activity);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//            return false;
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
//            return false;
//        return super.dispatchKeyEvent(event);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//            return false;
//        return super.onKeyUp(keyCode, event);
//    }
//}
