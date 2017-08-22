//package cn.qingchengfit.views.fragments;
//
//import android.content.Context;
//import android.support.v4.view.MotionEventCompat;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.webkit.WebView;
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
// * Created by Paper on 16/8/9.
// */
//public class TouchyWebView extends WebView {
//
//    public TouchyWebView(Context context) {
//        super(context);
//    }
//
//    public TouchyWebView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public TouchyWebView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override public boolean onTouchEvent(MotionEvent event) {
//
//        //Check is required to prevent crash
//        if (MotionEventCompat.findPointerIndex(event, 0) == -1) {
//            return super.onTouchEvent(event);
//        }
//
//        if (event.getPointerCount() >= 2) {
//            requestDisallowInterceptTouchEvent(true);
//        } else {
//            requestDisallowInterceptTouchEvent(false);
//        }
//
//        return super.onTouchEvent(event);
//    }
//
//    //    @Override
//    //    public boolean onTouchEvent(MotionEvent event){
//    //        requestDisallowInterceptTouchEvent(true);
//    //        return super.onTouchEvent(event);
//    //    }
//}