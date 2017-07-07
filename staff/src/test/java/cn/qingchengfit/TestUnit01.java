//package cn.qingchengfit;
//
//import cn.qingchengfit.model.common.Rule;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.Assert;
//import org.junit.Test;
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
// * Created by Paper on 2016/12/30.
// */
//
//public class TestUnit01 {
//
//    @Test public void testRules() {
//        List<Rule> rules1 = new ArrayList<>();
//        rules1.add(new Rule.Builder().card_tpl_id("1").build());
//        rules1.add(new Rule.Builder().card_tpl_id("2").build());
//        rules1.add(new Rule.Builder().card_tpl_id("3").build());
//        rules1.add(new Rule.Builder().card_tpl_id("4").build());
//        List<Rule> rules2 = new ArrayList<>();
//        rules2.add(new Rule.Builder().card_tpl_id("1").build());
//        rules2.add(new Rule.Builder().card_tpl_id("2").build());
//        rules2.add(new Rule.Builder().card_tpl_id("5").build());
//        rules2.add(new Rule.Builder().card_tpl_id("6").build());
//        rules1.removeAll(rules2);
//        if (rules1.size() > 0) {
//            rules2.addAll(rules1);
//        }
//        Assert.assertEquals(rules2.size(), 6);
//    }
//}
