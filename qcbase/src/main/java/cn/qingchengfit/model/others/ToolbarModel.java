package cn.qingchengfit.model.others;

import android.databinding.BindingAdapter;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class ToolbarModel {
    private String title;

    public ToolbarModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private @MenuRes int menu;
    private Toolbar.OnMenuItemClickListener listener;

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public int getMenu() {
        return menu;
    }

    public Toolbar.OnMenuItemClickListener getListener() {
        return listener;
    }

    public void setListener(Toolbar.OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @BindingAdapter("menu")
    public static void setToolbarMenu(Toolbar toolbar, @MenuRes int menu) {
        if (0 != menu) {
            toolbar.inflateMenu(menu);
        }

    }

    @BindingAdapter("menuListener")
    public static void setToolbarListener(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }



}
