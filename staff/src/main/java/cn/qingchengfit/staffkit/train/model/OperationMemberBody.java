package cn.qingchengfit.staffkit.train.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/3/28.
 */

public class OperationMemberBody {

    private String name;
    private List<String> user_ids = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUseridList() {
        return user_ids;
    }

    public void setUseridList(List<String> useridList) {
        this.user_ids = useridList;
    }
}
