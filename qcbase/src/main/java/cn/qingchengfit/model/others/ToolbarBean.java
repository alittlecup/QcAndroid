package cn.qingchengfit.model.others;

import android.support.v7.widget.Toolbar;
import android.view.View;

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
 * Created by Paper on 16/4/1 2016.
 */
public class ToolbarBean {
    public String title;
    public boolean showRight;
    public View.OnClickListener onClickListener;
    public int menu;
    public Toolbar.OnMenuItemClickListener listener;

    public ToolbarBean(String title, boolean showRight, View.OnClickListener onClickListener, int menu,
        Toolbar.OnMenuItemClickListener listener) {
        this.title = title;
        this.showRight = showRight;
        this.onClickListener = onClickListener;
        this.menu = menu;
        this.listener = listener;
    }

    private ToolbarBean(Builder builder) {
        title = builder.title;
        showRight = builder.showRight;
        onClickListener = builder.onClickListener;
        menu = builder.menu;
        listener = builder.listener;
    }

    public static final class Builder {
        private String title;
        private boolean showRight;
        private View.OnClickListener onClickListener;
        private int menu;
        private Toolbar.OnMenuItemClickListener listener;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder showRight(boolean val) {
            showRight = val;
            return this;
        }

        public Builder onClickListener(View.OnClickListener val) {
            onClickListener = val;
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

        public ToolbarBean build() {
            return new ToolbarBean(this);
        }
    }
}
