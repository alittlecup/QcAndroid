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

    private ToolbarModel(Builder builder) {
        setTitle(builder.title);
        setMenu(builder.menu);
        setListener(builder.listener);
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
        toolbar.getMenu().clear();
        if (0 != menu) {
            toolbar.inflateMenu(menu);
        }

    }

    @BindingAdapter("menuListener")
    public static void setToolbarListener(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }

    public static final class Builder {
        private String title;
        private int menu;
        private Toolbar.OnMenuItemClickListener listener;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder menu(int val) {
            menu = val;
            return this;
        }

        public Builder listener(Toolbar.OnMenuItemClickListener val) {
            listener = val;
            return this;
        }

        public ToolbarModel build() {
            return new ToolbarModel(this);
        }
    }
}
