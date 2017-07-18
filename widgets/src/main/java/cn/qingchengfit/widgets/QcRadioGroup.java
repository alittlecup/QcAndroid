package cn.qingchengfit.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

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
 * Created by Paper on 2017/5/8.
 * 用于单选 但是可以全不选的状态，点击选中，再点取消
 */
public class QcRadioGroup extends LinearLayout implements CompoundButton.OnCheckedChangeListener{
  private CheckedChange checkedChange;

    public QcRadioGroup(Context context) {
        super(context);
    }

    public QcRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QcRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) public QcRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0){
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof QcCheckable){
                    ((QcCheckable) v).addCheckedChangeListener(this);
                }
            }
        }
    }

  @Override public void addView(View child, ViewGroup.LayoutParams params) {
    super.addView(child, params);
    if (child instanceof QcCheckable) {
      ((QcCheckable) child).addCheckedChangeListener(this);
    }
  }

    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (getChildCount() > 0){
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof QcCheckable && !((QcCheckable) v).isOrContainCheck(buttonView)){
                    ((QcCheckable) v).setChecked(false);
                }
            }
        }
        if (checkedChange != null)
            checkedChange.onCheckedChange();

    }

    /**
     *
     * @return -1 表示没有任何选中
     */
    public int getCheckedPos(){
        if (getChildCount() > 0){
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof QcCheckable && ((QcCheckable) v).isChecked()){
                    return i;
                }
            }
        }
        return -1;
    }

    public void setCheckPos(int pos){
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof QcCheckable){
                ((QcCheckable) v).setChecked(false);
            }
        }
        if (getChildCount() > pos && pos >= 0){
            View v = getChildAt(pos);
            if (v instanceof QcCheckable ){
                ((QcCheckable) v).setChecked(true);
            }
        }
    }

    public void clearCheck(){
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof QcCheckable){
                ((QcCheckable) v).setChecked(false);
            }
        }
    }

    public CheckedChange getCheckedChange() {
        return checkedChange;
    }

    public void setCheckedChange(CheckedChange checkedChange) {
        this.checkedChange = checkedChange;
    }
    public interface CheckedChange{
        void onCheckedChange();
    }

}
