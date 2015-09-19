package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.SearchInterface;
import com.qingchengfit.fitcoach.fragment.SearchFragment;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/17 2015.
 */
public class SearchActivity extends AppCompatActivity implements SearchInterface {

    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        int type = getIntent().getIntExtra("type", SearchFragment.TYPE_GYM);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.search_fraglayout, SearchFragment.newInstance(getIntent().getIntExtra("type", type)))
                .commit();

    }

    @Override
    public void onSearchResult(int id, String name) {
        Intent it = new Intent();
        it.putExtra("id", id);
        it.putExtra("name", name);
        setResult(100, it);
        this.finish();
    }
}
