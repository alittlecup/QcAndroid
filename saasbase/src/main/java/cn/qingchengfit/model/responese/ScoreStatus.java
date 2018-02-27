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
 * //Created by yangming on 16/12/28.
 */

public class ScoreStatus {

    /**
     * module : {"shop_id":1,"score":false}
     */

    private ModuleBean module;

    public ModuleBean getModule() {
        return module;
    }

    public void setModule(ModuleBean module) {
        this.module = module;
    }

    public static class ModuleBean {
        /**
         * shop_id : 1
         * score : false
         */

        private String shop_id;
        private boolean score;
        private boolean checkin;

        public boolean isCheckin() {
            return checkin;
        }

        public void setCheckin(boolean checkin) {
            this.checkin = checkin;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public boolean isScore() {
            return score;
        }

        public void setScore(boolean score) {
            this.score = score;
        }
    }
}
