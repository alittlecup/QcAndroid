package cn.qingchengfit.support.animator;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.animation.Transformation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
 * Created by Paper on 2017/6/6.
 */

public class FlipAnimation extends ViewPropertyAnimation {

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    protected final @Direction int mDirection;
    protected final boolean mEnter;

    private FlipAnimation(@Direction int direction, boolean enter, long duration) {
        mDirection = direction;
        mEnter = enter;
        setDuration(duration);
    }

    /**
     * Create new Animation.
     *
     * @param direction Direction of animation
     * @param enter true for Enter / false for Exit
     * @param duration Duration of Animation
     */
    public static @NonNull FlipAnimation create(@Direction int direction, boolean enter, long duration) {
        switch (direction) {
            case UP:
            case DOWN:
                return new VerticalFlipAnimation(direction, enter, duration);
            case LEFT:
            case RIGHT:
            default:
                return new HorizontalFlipAnimation(direction, enter, duration);
        }
    }

    @IntDef({ UP, DOWN, LEFT, RIGHT }) @Retention(RetentionPolicy.SOURCE) @interface Direction {
    }

    private static class VerticalFlipAnimation extends FlipAnimation {

        public VerticalFlipAnimation(@Direction int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mPivotX = width * 0.5f;
            mPivotY = (mEnter == (mDirection == UP)) ? 0.0f : height;
            mCameraZ = -height * 0.015f;
        }

        @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
            float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
            if (mDirection == DOWN) value *= -1.0f;
            mRotationX = value * 180.0f;
            mTranslationY = -value * mHeight;

            super.applyTransformation(interpolatedTime, t);

            // Hide entering/exiting view before/after half point.
            if (mEnter) {
                mAlpha = interpolatedTime <= 0.5f ? 0.0f : 1.0f;
            } else {
                mAlpha = interpolatedTime <= 0.5f ? 1.0f : 0.0f;
            }

            applyTransformation(t);
        }
    }

    private static class HorizontalFlipAnimation extends FlipAnimation {

        public HorizontalFlipAnimation(@Direction int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mPivotX = (mEnter == (mDirection == LEFT)) ? 0.0f : width;
            //mPivotX = width*0.5f;
            mPivotY = height * 0.5f;
            mCameraZ = -width * 0.015f;
        }

        @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
            float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
            if (mDirection == RIGHT) value *= -1.0f;
            mRotationY = -value * 180.0f;
            mTranslationX = -value * mWidth;

            super.applyTransformation(interpolatedTime, t);

            // Hide entering/exiting view before/after half point.
            if (mEnter) {
                mAlpha = interpolatedTime <= 0.5f ? 0.0f : 1.0f;
            } else {
                mAlpha = interpolatedTime <= 0.5f ? 1.0f : 0.0f;
            }

            applyTransformation(t);
        }
    }
}
