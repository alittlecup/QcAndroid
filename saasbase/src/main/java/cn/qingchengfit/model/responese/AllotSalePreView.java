package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Staff;
import java.util.List;

/**
 * Created by yangming on 16/10/19.
 */

public class AllotSalePreView {

    /**
     * count : 1
     * users : ["毛云涛"]
     * Seller : {"username":"陈驰远","id":"3"}
     */

    private String count;
    /**
     * username : 陈驰远
     * id : 3
     */

    private Staff seller;
    private List<String> users;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Staff getSeller() {
        return seller;
    }

    public void setSeller(Staff seller) {
        this.seller = seller;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
