package cn.qingchengfit.staffkit.views.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeListFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * Created by Paper on 15/11/19 2015.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments;
    FragmentManager fm;

    HashMap<Long, Fragment> tags = new HashMap<>();

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fs) {
        super(fm);
        this.fragments = fs;
        this.fm = fm;
        for (int i = 0; i < fs.size(); i++) {
            if (fs.get(i) instanceof WardrobeListFragment) {
                tags.put(((WardrobeListFragment) fs.get(i)).getRegionId(), fs.get(i));
            }
        }
    }

    public Fragment findByTag(Long tag) {
        return tags.get(tag);
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);

        return fragment;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (position >= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }

    public void clearItem(ViewPager viewPager) {
        if (fragments.size() > 0) {
            for (int i = 0; i < fragments.size(); i++) {
                destroyItem(viewPager, i, fragments.get(i));
            }
        }
    }

    @Override public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override public int getCount() {
        return fragments.size();
    }

    @Override public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override public CharSequence getPageTitle(int position) {
        Fragment f = fragments.get(position);
        if (f instanceof TitleFragment) {
            return ((TitleFragment) f).getTitle();
        } else {
            return "";
        }
    }
}

