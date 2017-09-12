package cn.qingchengfit.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.model.common.ShareExtends;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import cn.qingchengfit.widgets.R;
import com.bumptech.glide.Glide;

/**
 * Created by fb on 2017/9/12.
 */

public class ShareDialogExtendLayout extends LinearLayout {

  private ImageView imgIcon;
  private TextView title;
  private ImageView imgRightArrow;
  private TextView content;
  private ShareExtends shareExtends;
  private OnClickExtendListener listener;

  public ShareDialogExtendLayout(Context context) {
    super(context);
    init(context);
  }

  public ShareDialogExtendLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public ShareDialogExtendLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void setListener(OnClickExtendListener listener) {
    this.listener = listener;
  }

  //@Override protected void onFinishInflate() {
  //  super.onFinishInflate();
  //  imgIcon = (ImageView)findViewById(R.id.img_fun_share);
  //  title = (TextView)findViewById(R.id.text_fun_name_share);
  //  imgRightArrow = (ImageView)findViewById(R.id.img_right_arrow_share);
  //  content = (TextView)findViewById(R.id.text_content_share_extends);
  //  this.setOnClickListener(new OnClickListener() {
  //    @Override public void onClick(View v) {
  //      if (listener != null){
  //        listener.onSelectExtend(shareExtends.key);
  //      }
  //    }
  //  });
  //}

  private void init(Context context) {
    View view = inflate(context, R.layout.item_share_dialog_extends, this);
    imgIcon = (ImageView) view.findViewById(R.id.img_fun_share);
    title = (TextView) view.findViewById(R.id.text_fun_name_share);
    imgRightArrow = (ImageView) view.findViewById(R.id.img_right_arrow_share);
    content = (TextView) view.findViewById(R.id.text_content_share_extends);
    this.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null) {
          listener.onSelectExtend(shareExtends);
        }
      }
    });
  }

  public void setExtend(Context context, ShareExtends shareExtends) {
    this.shareExtends = shareExtends;
    Glide.with(context).load(shareExtends.icon).into(imgIcon);
    title.setText(shareExtends.title);
    if (shareExtends.type.equals(ShareDialogFragment.SHARE_TYPE_INFO)) {
      content.setVisibility(View.VISIBLE);
      content.setText(shareExtends.desc);
      imgRightArrow.setVisibility(GONE);
    } else {
      content.setVisibility(View.GONE);
      imgRightArrow.setVisibility(View.VISIBLE);
    }
    invalidate();
  }

  public interface OnClickExtendListener {
    void onSelectExtend(ShareExtends shareExtends);
  }
}
