package cn.qingchengfit.staffkit.views.student.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.usecase.bean.SignInImg;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

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
 * Created by Paper on 16/8/29.
 */
public class SignInImageItem extends AbstractFlexibleItem<SignInImageItem.SignInImageVH> {

    private SignInImg signInImg;

    public SignInImageItem(SignInImg signInImg) {
        this.signInImg = signInImg;
    }

    public SignInImg getSignInImg() {
        return signInImg;
    }

    public void setSignInImg(SignInImg signInImg) {
        this.signInImg = signInImg;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_fix_img;
    }

    @Override public SignInImageVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new SignInImageVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SignInImageVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext()).load(PhotoUtils.getSmall(signInImg.photo)).into(holder.oldImg);
        holder.fixTime.setText(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(signInImg.created_at)));
        holder.fixStaff.setText(signInImg.created_by == null ? "" : signInImg.created_by.getUsername());
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class SignInImageVH extends FlexibleViewHolder {
        @BindView(R.id.fix_time) TextView fixTime;
        @BindView(R.id.fix_staff) TextView fixStaff;
        @BindView(R.id.old_img) ImageView oldImg;

        public SignInImageVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
