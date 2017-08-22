package cn.qingchengfit.model.common;

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
 * Created by Paper on 16/3/19 2016.
 * 跟进记录
 */
public class FollowRecord {
    public String time;
    public String follower;
    public String content;
    public List<String> imgs;

    public FollowRecord(String time, String follower, String content, List<String> imgs) {
        this.time = time;
        this.follower = follower;
        this.content = content;
        this.imgs = imgs;
    }

    //    public
}
