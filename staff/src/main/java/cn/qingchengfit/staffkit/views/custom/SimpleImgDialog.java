package cn.qingchengfit.staffkit.views.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import com.bumptech.glide.Glide;
import uk.co.senab.photoview.PhotoView;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/13 2016.
 */
public class SimpleImgDialog extends BaseDialogFragment {

	PhotoView photoview;

    public static SimpleImgDialog newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        SimpleImgDialog fragment = new SimpleImgDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_img, container, false);
      photoview = (PhotoView) view.findViewById(R.id.photoview);

      photoview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(getActivity()).load(getArguments().getString("url")).placeholder(R.drawable.img_loadingimage).into(photoview);
        //        photoview.setScale(photoview.getMinimumScale());
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
