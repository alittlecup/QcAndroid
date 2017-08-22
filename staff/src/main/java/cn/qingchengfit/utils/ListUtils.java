//package cn.qingchengfit.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2017/3/30.
// */
//
//public class ListUtils {
//    public static void test() {
//
//    }
//
//    /**
//     * List 类型转换  好累
//     */
//    public static <T, G> List<T> transerList(List<T> to, List<G> list) {
//        to = new ArrayList<>();
//        if (list != null) {
//            for (int i = 0; i < list.size(); i++) {
//                G g = list.get(i);
//                to.add((T) g);
//            }
//        }
//        return to;
//    }
//}
