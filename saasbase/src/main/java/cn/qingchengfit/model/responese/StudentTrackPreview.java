package cn.qingchengfit.model.responese;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/11.
 */

public class StudentTrackPreview {

    /**
     * "created_users_count": 12,      # 新注册用户总数
     * "following_users_count": 6,     # 跟进中用户总数
     * "member_users_count": 21,       # 会员总数
     * "all_users_count": 39,          # 总用户数
     * "new_created_users_count": 1,   # 今日新增新注册用户数
     * "new_following_users_count": 2, # 今日新增跟进中用户数
     * "new_member_users_count": 2     # 今日新增会员数
     */

    public int created_users_count;
    public int following_users_count;
    public int member_users_count;
    public int all_users_count;
    public int new_created_users_count;
    public int new_following_users_count;
    public int new_member_users_count;

    public int getCreated_users_count() {
        return created_users_count;
    }

    public void setCreated_users_count(int created_users_count) {
        this.created_users_count = created_users_count;
    }

    public int getFollowing_users_count() {
        return following_users_count;
    }

    public void setFollowing_users_count(int following_users_count) {
        this.following_users_count = following_users_count;
    }

    public int getMember_users_count() {
        return member_users_count;
    }

    public void setMember_users_count(int member_users_count) {
        this.member_users_count = member_users_count;
    }

    public int getNew_following_users_count() {
        return new_following_users_count;
    }

    public void setNew_following_users_count(int new_following_users_count) {
        this.new_following_users_count = new_following_users_count;
    }

    public int getAll_users_count() {
        return all_users_count;
    }

    public void setAll_users_count(int all_users_count) {
        this.all_users_count = all_users_count;
    }

    public int getNew_member_users_count() {
        return new_member_users_count;
    }

    public void setNew_member_users_count(int new_member_users_count) {
        this.new_member_users_count = new_member_users_count;
    }
}
