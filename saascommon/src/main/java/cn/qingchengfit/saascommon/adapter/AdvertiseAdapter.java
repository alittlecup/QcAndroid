package cn.qingchengfit.saascommon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.model.AdvertiseInfo;
import cn.qingchengfit.saascommon.views.AdvertiseDialog;
import cn.qingchengfit.utils.PhotoUtils;

public class AdvertiseAdapter extends PagerAdapter {
    private Context context;
    private List<AdvertiseInfo> list = new ArrayList<>();
    private LinkedList<View> mCaches = new LinkedList<>();
    private AdvertiseDialog.DialogClickListener listener;

    public AdvertiseAdapter(Context context, AdvertiseDialog.DialogClickListener listener, List<AdvertiseInfo> list) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (mCaches.size() > 0) {
            mCaches.clear();
        }
        container.removeView((View) object);
        mCaches.add((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        ViewHolder viewHolder = null;
        if (mCaches.size() == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_advertise, null);
            viewHolder = new ViewHolder();
            viewHolder.advertiseIv = view.findViewById(R.id.iv_advertise);
            view.setTag(viewHolder);
        } else {
            view = mCaches.removeFirst();
            viewHolder = (ViewHolder) view.getTag();
        }
        AdvertiseInfo advertiseInfo = list.get(position);
        Glide.with(context)
                .load(advertiseInfo.photo_url)
                .asBitmap()
                .into(viewHolder.advertiseIv);

        viewHolder.advertiseIv.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClickListener(list.get(position));
            }
        });
        container.addView(view);
        return view;
    }

    private class ViewHolder {
        ImageView advertiseIv;
    }
}
