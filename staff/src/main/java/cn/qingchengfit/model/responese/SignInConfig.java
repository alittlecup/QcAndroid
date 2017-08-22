package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class SignInConfig {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("configs") public List<Config> configs;
    }

    public class Config {

        @SerializedName("name") private String name;
        @SerializedName("editable") private Boolean editable;
        @SerializedName("readable") private Boolean readable;
        @SerializedName("value") private Object value;
        @SerializedName("group_name") private String groupName;
        @SerializedName("priority") private Integer priority;
        @SerializedName("shop_id") private Integer shopId;
        @SerializedName("key") private String key;
        @SerializedName("id") private Integer id;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The editable
         */
        public Boolean getEditable() {
            return editable;
        }

        /**
         * @param editable The editable
         */
        public void setEditable(Boolean editable) {
            this.editable = editable;
        }

        /**
         * @return The readable
         */
        public Boolean getReadable() {
            return readable;
        }

        /**
         * @param readable The readable
         */
        public void setReadable(Boolean readable) {
            this.readable = readable;
        }

        public Object getValue() {
            return value;
        }

        /**
         * @return The value
         */

        public void setValue(Object value) {
            this.value = value;
        }

        public Integer getValueInt() {
            if (value instanceof Double) {
                return ((Double) value).intValue();
            } else {
                return 0;
            }
        }
        /**
         * @param value The value
         */

        /**
         * @return The groupName
         */
        public String getGroupName() {
            return groupName;
        }

        /**
         * @param groupName The group_name
         */
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        /**
         * @return The priority
         */
        public Integer getPriority() {
            return priority;
        }

        /**
         * @param priority The priority
         */
        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        /**
         * @return The shopId
         */
        public Integer getShopId() {
            return shopId;
        }

        /**
         * @param shopId The shop_id
         */
        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        /**
         * @return The key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return The id
         */
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Integer id) {
            this.id = id;
        }
    }
}
