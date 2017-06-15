package cn.qingchengfit.views.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import cn.qingchengfit.widgets.R;

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
 * Created by Paper on 2017/6/14.
 */
public class RichTxtFragment extends BaseFragment {

    EditText et;
    //@BindView(R2.id.btn_insert_img) CompatTextView btnInsertImg;
    //@BindView(R2.id.btn_comfirm) TextView btnComfirm;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_rich_txt, container, false);
        //unbinder = ButterKnife.bind(this, view);
        et = new EditText(getContext());
        et.setGravity(Gravity.TOP);
        et.setBackgroundResource(R.color.white);
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        et.setPadding(padding, padding, padding, padding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            et.setTextAppearance(R.style.QcTextStyleMediumDark);
        } else {
            et.setTextAppearance(getContext(), R.style.QcTextStyleMediumDark);
        }
        et.setHint("用人单位更关注求职者的专业能力、沟通能力和形象气质，建议从这方面来描述");
        return et;
    }

    @Override public String getFragmentName() {
        return RichTxtFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    public void insertImg(String url, String filePath) {
        String tempUrl = "<img src=\"" + url + "\" />";
        SpannableString spannableString = new SpannableString(tempUrl);
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(filePath, options);
        //options.inSampleSize = options.outWidth / et.getWidth() <= 0? 1: options.outWidth / et.getWidth();
        //options.inScaled = true;
        //options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        //BitmapDrawable d = new BitmapDrawable(getResources(),bitmap);
        ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);

        spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将选择的图片追加到EditText中光标所在位置
        int index = et.getSelectionStart();
        // 获取光标所在位置
        Editable edit_text = et.getEditableText();
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {
            edit_text.insert(index, spannableString);
        }
        edit_text.insert(index + spannableString.length(), "\n");
        //bitmap.recycle();
    }
}
