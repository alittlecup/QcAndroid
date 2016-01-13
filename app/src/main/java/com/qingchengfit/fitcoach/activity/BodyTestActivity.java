package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImageGridAdapter;
import com.qingchengfit.fitcoach.bean.ImageGridBean;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.FullyGridLayoutManager;
import com.qingchengfit.fitcoach.component.GalleryPhotoViewDialog;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.fragment.BodyTestFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.fragment.ModifyBodyTestFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BodyTestActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.other_data)
    LinearLayout otherData;
    private ImageGridAdapter imageGridAdapter;
    private FullyGridLayoutManager gridLayoutManager;
    private List<ImageGridBean> datas = new ArrayList<>();
    private boolean isModify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_test);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, BodyTestFragment.newInstance("", ""))
                .commit();
        datas.add(new ImageGridBean("http://zoneke-img.b0.upaiyun.com/dff71b1cca9db02065d2477e4dbd0d0f.jpg"));
        datas.add(new ImageGridBean("http://zoneke-img.b0.upaiyun.com/dff71b1cca9db02065d2477e4dbd0d0f.jpg"));
        datas.add(new ImageGridBean("http://zoneke-img.b0.upaiyun.com/dff71b1cca9db02065d2477e4dbd0d0f.jpg"));
        datas.add(new ImageGridBean("http://zoneke-img.b0.upaiyun.com/dff71b1cca9db02065d2477e4dbd0d0f.jpg"));
        imageGridAdapter = new ImageGridAdapter(datas);
        gridLayoutManager = new FullyGridLayoutManager(getApplicationContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(imageGridAdapter);
        imageGridAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (v.getId() == R.id.delete) {
                    datas.remove(pos);
                    imageGridAdapter.notifyDataSetChanged();
                } else {
                    if (pos < datas.size() - 1) {
                        GalleryPhotoViewDialog galleryPhotoViewDialog = new GalleryPhotoViewDialog(BodyTestActivity.this);
                        String[] phtoto = new String[datas.size()];
                        galleryPhotoViewDialog.setImage(datas.toArray(phtoto));
                        galleryPhotoViewDialog.show();
                    } else {
                        ChoosePictureFragmentDialog choosePictureFragmentDialog = new ChoosePictureFragmentDialog();
                        choosePictureFragmentDialog.show(getSupportFragmentManager(), "choose");
                    }
                }
            }
        });

        CommonInputView civ = new CommonInputView(this,"哈哈哈哈");
        otherData.addView(civ);
        otherData.addView(new CommonInputView(this,"2222"));
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isModify)
            getMenuInflater().inflate(R.menu.menu_text_edit, menu);
        else getMenuInflater().inflate(R.menu.menu_text_cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_eidt) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new ModifyBodyTestFragment())
                    .commit();
            isModify = true;
            invalidateOptionsMenu();
        } else if (item.getItemId() == R.id.action_cancel) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, BodyTestFragment.newInstance("", ""))
                    .commit();
            isModify = false;
            invalidateOptionsMenu();
        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return true;
    }
}
