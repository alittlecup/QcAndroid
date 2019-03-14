package cn.qingchengfit.staffkit.views.gym.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.adapter.GymMoreAdapter;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.DrawableUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

import static cn.qingchengfit.saascommon.qrcode.views.QRActivity.MODULE_MANAGE_COACH;

public class GymFuntionItem extends AbstractFlexibleItem<GymFuntionItem.GymFuntionVH>
    implements ISectionable<GymFuntionItem.GymFuntionVH, FunHeaderItem> {

  public static final int STATUS_REMOVE = 1;
  public static final int STATUS_ADD = 2;

  private GymFuntion mGymFuntion;

  private Context mContext;

  private FunHeaderItem mFunHeaderItem;

  public GymFuntionItem(GymFuntion gymFuntion) {
    mGymFuntion = gymFuntion;
  }

  public GymFuntionItem(GymFuntion gymFuntion, FunHeaderItem i) {
    mGymFuntion = gymFuntion;
    this.mFunHeaderItem = i;
  }

  public GymFuntion getGymFuntion() {
    if (mGymFuntion == null) {
      mGymFuntion = new GymFuntion.Builder().img(0).text(R.string.none)

          .moduleName("").build();
    }
    return mGymFuntion;
  }

  public void setGymFuntion(GymFuntion gymFuntion) {
    mGymFuntion = gymFuntion;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_gym_function;
  }

  @Override public GymFuntionVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new GymFuntionVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, GymFuntionVH holder, int position,
      List payloads) {
    if (mGymFuntion.getImg() != 0) {
      holder.funImg.setVisibility(View.VISIBLE);
      holder.funImg.setImageResource(mGymFuntion.getImg());
    } else {
      holder.funImg.setVisibility(View.INVISIBLE);
    }
    holder.funTxt.setText(mGymFuntion.getText());
    if (adapter instanceof GymMoreAdapter) {
      if (((GymMoreAdapter) adapter).getStatus() == 0 || mGymFuntion.getImg() == 0) {
        if (GymFunctionFactory.getModuleStatus(mGymFuntion.getModuleName()) > 0
            && !(boolean) ((CommonFlexAdapter) adapter).getTag("isPro")) {
          holder.statusIcon.setVisibility(View.VISIBLE);
          holder.statusIcon.setImageResource(R.drawable.ic_pro_green);
        } else {
          holder.statusIcon.setVisibility(View.GONE);
        }
        GridLayoutManager.LayoutParams lp =
            (GridLayoutManager.LayoutParams) holder.rootView.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        holder.rootView.setLayoutParams(lp);
      } else if (((GymMoreAdapter) adapter).getStatus() == STATUS_REMOVE) {
        holder.statusIcon.setVisibility(View.VISIBLE);
        holder.statusIcon.setImageResource(R.drawable.ic_moudule_manage_remove);

        GridLayoutManager.LayoutParams lp =
            (GridLayoutManager.LayoutParams) holder.rootView.getLayoutParams();
        lp.setMargins(0, 0, 1, 1);
        holder.rootView.setLayoutParams(lp);
      } else if (((GymMoreAdapter) adapter).getStatus() == STATUS_ADD) {
        holder.statusIcon.setVisibility(View.VISIBLE);
        if (((GymMoreAdapter) adapter).mOtherDatas.contains(GymFuntionItem.this)) {
          holder.statusIcon.setImageResource(R.drawable.ic_moudule_manage_added);
        } else {
          holder.statusIcon.setImageResource(R.drawable.ic_moudule_manage_add);
        }
        GridLayoutManager.LayoutParams lp =
            (GridLayoutManager.LayoutParams) holder.rootView.getLayoutParams();
        lp.setMargins(0, 0, 1, 1);
        holder.rootView.setLayoutParams(lp);
      }
    }
    holder.notSetting.setVisibility(mGymFuntion.isNotSetting() ? View.VISIBLE : View.GONE);
    holder.notSetting.setText("未设置");
    if (MODULE_MANAGE_COACH.equals(mGymFuntion.getModuleName())) {
      if (mGymFuntion.isNotSetting()) {
        holder.notSetting.setText("邀请");
        Drawable drawable = DrawableUtils.tintDrawable(holder.notSetting.getContext(),
            R.drawable.bg_fill_red_slide_white, R.color.success_green);
        holder.notSetting.setBackground(drawable);
      } else {
        holder.notSetting.setVisibility(View.GONE);
      }
    }
  }

  @Override public boolean equals(Object o) {

    if (o instanceof GymFuntionItem) {
      return ((GymFuntionItem) o).mGymFuntion.getModuleName()
          .toLowerCase()
          .equalsIgnoreCase(this.mGymFuntion.getModuleName());
    } else {
      return false;
    }
  }

  @Override public FunHeaderItem getHeader() {
    return mFunHeaderItem;
  }

  @Override public void setHeader(FunHeaderItem header) {
    this.mFunHeaderItem = header;
  }

  public class GymFuntionVH extends FlexibleViewHolder {
    ViewGroup rootView;
    ImageView funImg;
    TextView funTxt;
    ImageView statusIcon;
    TextView notSetting;

    public GymFuntionVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      rootView = (ViewGroup) view.findViewById(R.id.root_view);
      funImg = (ImageView) view.findViewById(R.id.fun_img);
      funTxt = (TextView) view.findViewById(R.id.fun_txt);
      statusIcon = (ImageView) view.findViewById(R.id.status_icon);
      notSetting = view.findViewById(R.id.tv_not_set);
    }
  }
}