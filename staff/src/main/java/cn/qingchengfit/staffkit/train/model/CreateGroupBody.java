package cn.qingchengfit.staffkit.train.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/3/28.
 */

public class CreateGroupBody {

    private String gym_id;
    private String name;
    private String competition_id;
    private List<Integer> user_ids = new ArrayList<>();

    public String getGym_id() {
        return gym_id;
    }

    public void setGym_id(String gym_id) {
        this.gym_id = gym_id;
    }

    public String getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(String competition_id) {
        this.competition_id = competition_id;
    }

    public List<Integer> getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(List<Integer> user_ids) {
        this.user_ids = user_ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
