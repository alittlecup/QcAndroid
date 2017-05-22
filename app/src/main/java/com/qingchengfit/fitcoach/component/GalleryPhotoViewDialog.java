package com.qingchengfit.fitcoach.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.qingchengfit.utils.BitmapUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.ViewPaperAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class GalleryPhotoViewDialog extends Dialog
    implements ViewPager.OnPageChangeListener, View.OnClickListener, PhotoViewAttacher.OnPhotoTapListener {
    ViewPaperAdapter viewPaperAdapter;
    ViewPager viewPager;
    View progress;
    LinearLayout tagPanel;
    List<View> pagerListView = new ArrayList<View>();
    int selectedIndex = 0;
    Button btn;
    List<String> urls = new ArrayList<>();

    public GalleryPhotoViewDialog(Context context) {
        super(context, R.style.theme_dialog_fullscreen);
        setContentView(R.layout.dialog_gallery_photoview);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tagPanel = (LinearLayout) findViewById(R.id.ViewPagerTagPanel);
        btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = viewPager.getCurrentItem();
                Glide.with(context).load(urls.get(pos)).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        new AsyncTask<String, String, String>() {
                            @Override protected String doInBackground(String... params) {
                                //                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ UUID.randomUUID()+".png");
                                //                                OutputStream os = null;
                                //                                try {
                                //                                    os = new FileOutputStream(file);
                                //                                } catch (FileNotFoundException e) {
                                //
                                //                                }
                                //                                resource.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                BitmapUtils.saveImageToGallery(context, resource);
                                return "";
                            }

                            @Override protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                ToastUtils.showDefaultStyle("图片保存至相册");
                            }
                        }.execute();
                    }
                });
            }
        });
    }

    public void setImage(String... url) {
        selectedIndex = 0;
        urls = Arrays.asList(url);
        pagerListView.clear();
        tagPanel.removeAllViews();
        viewPager.removeAllViews();
        for (String u : url) {
            PhotoView image = new PhotoView(this.getContext());
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            image.setScaleType(ImageView.ScaleType.CENTER);

            pagerListView.add(image);
            Glide.with(App.AppContex).load(u).into(image);
            //    		ImageLoader.getInstance().displayImage(u, image,mDisplayImageOption);
            ImageView tag = new ImageView(this.getContext());
            //    		tag.setImageResource(R.drawable.icon_pager_tag);
            tag.setPadding(12, 0, 0, 0);
            tagPanel.addView(tag);
            image.setOnClickListener(this);
            image.setOnPhotoTapListener(this);
        }
        viewPaperAdapter = new ViewPaperAdapter(pagerListView);
        viewPager.setAdapter(viewPaperAdapter);
        viewPager.setOnPageChangeListener(this);
        tagPanel.getChildAt(0).setSelected(true);
    }

    public void setImage(List<String> url) {
        urls = url;
        selectedIndex = 0;

        pagerListView.clear();
        tagPanel.removeAllViews();
        viewPager.removeAllViews();
        for (String u : url) {
            PhotoView image = new PhotoView(this.getContext());
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            image.setScaleType(ImageView.ScaleType.CENTER);

            pagerListView.add(image);
            Glide.with(App.AppContex).load(u).into(image);
            //    		ImageLoader.getInstance().displayImage(u, image,mDisplayImageOption);
            ImageView tag = new ImageView(this.getContext());
            tag.setImageResource(R.drawable.gallery_tag_dot);
            tag.setPadding(12, 0, 0, 0);
            tagPanel.addView(tag);
            image.setOnClickListener(this);
            image.setOnPhotoTapListener(this);
        }
        viewPaperAdapter = new ViewPaperAdapter(pagerListView);
        viewPager.setAdapter(viewPaperAdapter);
        viewPager.setOnPageChangeListener(this);
        tagPanel.getChildAt(0).setSelected(true);
    }

    public void setSelected(int index) {
        if (index < viewPaperAdapter.getCount()) {
            viewPager.setCurrentItem(index);
            //			tagPanel.getChildAt(index).setSelected(true);
        }
    }

    @Override public void onPageScrollStateChanged(int arg0) {
    }

    @Override public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override public void onPageSelected(int index) {

        tagPanel.getChildAt(selectedIndex).setSelected(false);
        tagPanel.getChildAt(index).setSelected(true);
        selectedIndex = index;
    }

    @Override public void onClick(View v) {
        //        this.dismiss();
    }

    @Override public void onPhotoTap(View view, float x, float y) {
        this.dismiss();
    }
}