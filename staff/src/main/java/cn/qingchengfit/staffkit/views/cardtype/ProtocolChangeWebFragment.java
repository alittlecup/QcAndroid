package cn.qingchengfit.staffkit.views.cardtype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.views.fragments.WebFragment;
import java.io.Serializable;

/**
 * Created by fb on 2017/11/21.
 */

public class ProtocolChangeWebFragment extends WebFragment {

  private OnActionClickListener onActionClickListener;

  public static ProtocolChangeWebFragment newInstance(String url, OnActionClickListener listener) {
    Bundle args = new Bundle();
    args.putString("url", url);
    args.putSerializable("listener", listener);
    ProtocolChangeWebFragment fragment = new ProtocolChangeWebFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null){
      onActionClickListener = (OnActionClickListener) getArguments().getSerializable("listener");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    mToobarActionTextView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onActionClickListener != null){
          onActionClickListener.onAction(v);
        }
      }
    });
    return view;
  }

  public interface OnActionClickListener extends Serializable{
    void onAction(View v);
  }

}
