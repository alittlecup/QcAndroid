package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.bean.ShopCourse;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/12/29 2015.
 */
public class CourseView extends RelativeLayout {

    ImageView img;
    TextView text1;
    ImageView texticon;
    TextView text2;
    TextView text3;
    ImageView righticon;

    private ShopCourse course;

    public CourseView(Context context) {
        super(context);
        init(context);
    }

    public CourseView(Context context, ShopCourse course) {
        super(context);
        this.course = course;
        init(context);
    }

    public CourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.layout_course, this, true);
        LogUtil.e("xxxx");
    }

    @Override protected void onFinishInflate() {
        LogUtil.e("course .");
        super.onFinishInflate();

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        img = (ImageView) findViewById(R.id.img);
        righticon = (ImageView) findViewById(R.id.righticon);
        LogUtil.e("course .......");
        if (course != null) {
            LogUtil.e("course != null");
            Glide.with(App.AppContex).load(course.image_url).into(img);
            text1.setText(course.name);
            text2.setText("时长" + course.length + "min");
            text3.setText("累计" + course.course_count + "节,服务" + course.service_count + "人次");
            if (course.is_private) {
                righticon.setVisibility(VISIBLE);
            } else {
                righticon.setVisibility(GONE);
            }
        } else {
            LogUtil.e("course == null");
        }
    }
}
