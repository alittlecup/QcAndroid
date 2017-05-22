package com.qingchengfit.fitcoach.http.bean;

import java.util.ArrayList;
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
 * Created by Paper on 15/9/17 2015.
 */
public class QcEvaluateResponse extends QcResponse {

    /**
     * data : {"evaluate":{"course_score":5,"total_count":0,"coach_score":5},"tags":[{"count":"1.2k","username":"减脂"},{"count":"902","username":"增肌"}]}
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * evaluate : {"course_score":5,"total_count":0,"coach_score":5}
         * tags : [{"count":"1.2k","username":"减脂"},{"count":"902","username":"增肌"}]
         */

        private EvaluateEntity evaluate;
        private List<TagsEntity> impression;

        public EvaluateEntity getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(EvaluateEntity evaluate) {
            this.evaluate = evaluate;
        }

        public List<TagsEntity> getTags() {
            return impression;
        }

        public void setTags(List<TagsEntity> tags) {
            this.impression = tags;
        }

        public QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity getHomeEvaluate() {
            QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity evaluate = new QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity();
            evaluate.setCoach_score(getEvaluate().coach_score);
            evaluate.setCourse_score(getEvaluate().course_score);
            evaluate.setTotal_count(getEvaluate().total_count);
            return evaluate;
        }

        //        public QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys getHomeTags() {
        //            QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys tagsEntitys = new QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys();
        //            List<QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys.TagsEntity> tags = new ArrayList<>();
        //
        //            for (int i = 0; i < getTags().size(); i++) {
        //                QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys.TagsEntity tagsEntity = new QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys.TagsEntity();
        //                tagsEntity.setCount(getTags().get(i).count);
        //                tagsEntity.setName(getTags().get(i).username);
        //                tags.add(tagsEntity);
        //            }
        //            tagsEntitys.setTags(tags);
        //            return tagsEntitys;
        //        }
        public String[] getTagArray() {
            List<String> list = new ArrayList<>();
            for (TagsEntity tag : impression) {
                StringBuffer sb = new StringBuffer();
                sb.append(tag.getName());
                sb.append("  (");
                sb.append(tag.getCount());
                sb.append(")");
                list.add(sb.toString());
            }
            String[] ret = list.toArray(new String[list.size()]);
            return ret;
        }

        public static class EvaluateEntity {
            /**
             * course_score : 5.0
             * total_count : 0
             * coach_score : 5.0
             */

            private double course_score;
            private int total_count;
            private double coach_score;

            public double getCourse_score() {
                return course_score;
            }

            public void setCourse_score(double course_score) {
                this.course_score = course_score;
            }

            public int getTotal_count() {
                return total_count;
            }

            public void setTotal_count(int total_count) {
                this.total_count = total_count;
            }

            public double getCoach_score() {
                return coach_score;
            }

            public void setCoach_score(double coach_score) {
                this.coach_score = coach_score;
            }
        }

        public static class TagsEntity {
            /**
             * count : 1.2k
             * username : 减脂
             */

            private String count;
            private String comment;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getName() {
                return comment;
            }

            public void setName(String name) {
                this.comment = name;
            }
        }
    }
}
