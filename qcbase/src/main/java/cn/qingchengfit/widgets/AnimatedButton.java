/*
 * Copyright (c) 2015, Jaewe Heo. All rights reserved.
 * Use of this source code is governed by a BSD-style
 * license that can be found in the LICENSE file.
 */

package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

public class AnimatedButton extends AppCompatImageButton {

  private static final int[] mAttr = { android.R.attr.background };
  protected boolean checked;
  private Drawable offDrawable;
  private Drawable onDrawable;

  public AnimatedButton(Context context) {
    super(context);
    //        init(context);
  }

  public AnimatedButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AnimatedButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedButton);
    TypedArray b = context.obtainStyledAttributes(attrs, mAttr);
    Resources r = context.getResources();
    final int idon = a.getResourceId(R.styleable.AnimatedButton_ab_drawable_on, -1);
    final int idoff = a.getResourceId(R.styleable.AnimatedButton_ab_drawable_off, -1);
    final int idbg = b.getResourceId(0, -1);
    if (idbg != -1) {
      setBackgroundDrawable(ContextCompat.getDrawable(getContext(), idbg));
    } else {
      setBackgroundResource(R.color.transparent);
    }
    offDrawable = ContextCompat.getDrawable(getContext(), idoff);
    onDrawable = ContextCompat.getDrawable(getContext(), idon);

    int color = a.getColor(R.styleable.AnimatedButton_ab_color, Color.BLACK);
    DrawableCompat.setTint(offDrawable, color);
    DrawableCompat.setTint(onDrawable, color);
    a.recycle();
    b.recycle();

    setScaleType(ScaleType.CENTER_INSIDE);

    //        if (isLollipop()) {
    setImageDrawable(checked ? onDrawable : offDrawable);
    //        } else {
    //            setImageDrawable(checked ? onDrawable : offDrawable);
    //        }
  }

  //    protected boolean isLollipop() {
  //        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  //    }

  /**
   * Change the checked state of the view to the inverse of its current state
   */
  public void toggle() {
    checked = !checked;
    setImageDrawable(checked ? onDrawable : offDrawable);

    Drawable drawable = getDrawable();
    if (drawable instanceof Animatable) {
      Animatable animatable = (Animatable) drawable;
      if (animatable.isRunning()) {
        animatable.stop();
      }
      animatable.start();
    }
  }

  /**
   * Returns the checked state.
   *
   * @return The checked state.
   */
  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }
}
