package cn.qingchengfit.model.responese;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/1/16.
 */

public class GymFuntion {

    private String moduleName;
    @StringRes private int text;
    @DrawableRes private int img;

    private int gymGrade;
    private boolean isGymPro;

    private GymFuntion(Builder builder) {
        setModuleName(builder.moduleName);
        setText(builder.text);
        setImg(builder.img);
        setGymGrade(builder.gymGrade);
        isGymPro = builder.isGymPro;
    }

    public int getGymGrade() {
        return gymGrade;
    }

    public void setGymGrade(int gymGrade) {
        this.gymGrade = gymGrade;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isGymPro() {
        return isGymPro;
    }

    public void setGymPro(boolean gymPro) {
        isGymPro = gymPro;
    }

    public static final class Builder {
        private String moduleName;
        private int text;
        private int img;
        private int gymGrade;
        private boolean isGymPro;

        public Builder() {
        }

        public Builder moduleName(String val) {
            moduleName = val;
            return this;
        }

        public Builder text(int val) {
            text = val;
            return this;
        }

        public Builder img(int val) {
            img = val;
            return this;
        }

        public Builder gymGrade(int val) {
            gymGrade = val;
            return this;
        }

        public Builder isGymPro(boolean val) {
            isGymPro = val;
            return this;
        }

        public GymFuntion build() {
            return new GymFuntion(this);
        }
    }
}
