package cn.qingchengfit.staffkit.rxbus.event;

import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
 * Created by Paper on 16/3/7 2016.
 */
public class EventToolbar {
    String title;
    View.OnClickListener listener;
    boolean showRight;
    @MenuRes int menu;
    Toolbar.OnMenuItemClickListener menuClick;

    public EventToolbar(String title) {
        this.title = title;
    }

    public EventToolbar(@NonNull String title, View.OnClickListener listener, boolean showRight, int menu) {
        this.title = title;
        this.listener = listener;
        this.showRight = showRight;
        this.menu = menu;
    }

    private EventToolbar(Builder builder) {
        setTitle(builder.title);
        setListener(builder.listener);
        setShowRight(builder.showRight);
        setMenu(builder.menu);
        setMenuClick(builder.menuClick);
    }

    public Toolbar.OnMenuItemClickListener getMenuClick() {
        return menuClick;
    }

    public void setMenuClick(Toolbar.OnMenuItemClickListener menuClick) {
        this.menuClick = menuClick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public boolean isShowRight() {
        return showRight;
    }

    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public static final class Builder {
        private String title;
        private View.OnClickListener listener;
        private boolean showRight;
        private int menu;
        private Toolbar.OnMenuItemClickListener menuClick;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder listener(View.OnClickListener val) {
            listener = val;
            return this;
        }

        public Builder showRight(boolean val) {
            showRight = val;
            return this;
        }

        public Builder menu(int val) {
            menu = val;
            return this;
        }

        public Builder menuClick(Toolbar.OnMenuItemClickListener val) {
            menuClick = val;
            return this;
        }

        public EventToolbar build() {
            return new EventToolbar(this);
        }
    }
}
