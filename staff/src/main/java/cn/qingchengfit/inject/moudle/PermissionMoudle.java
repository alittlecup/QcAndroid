package cn.qingchengfit.inject.moudle;

import dagger.Module;
import dagger.Provides;
import java.util.HashMap;

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
 * Created by Paper on 16/1/28 2016.
 */

@Module public class PermissionMoudle {
    private HashMap<Integer, HashMap<String, Boolean>> permission;

    public PermissionMoudle(HashMap<Integer, HashMap<String, Boolean>> p) {
        this.permission = p;
    }

    @Provides HashMap<Integer, HashMap<String, Boolean>> providePermission() {
        return permission;
    }
}
