package cn.qingchengfit.model.body;

import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.Time_repeat;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 16/5/4 2016.
 */
public class ArrangeBatchBody {

    public String teacher_id;
    public String course_id;
    public String shop_id;
    public String from_date;
    public String to_date;
    public int max_users;
    public List<String> spaces;
    public ArrayList<Rule> rules;
    public ArrayList<Time_repeat> time_repeats;
    public String batch_id;
    public boolean is_free = false;
}
