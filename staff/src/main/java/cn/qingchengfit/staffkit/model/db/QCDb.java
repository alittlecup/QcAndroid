package cn.qingchengfit.staffkit.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import cn.qingchengfit.model.common.Card;
import rx.functions.Func1;

public class QCDb {
    public QCDb() {
    }

    /**
     * 会员卡
     */
    public static abstract class CardTable {
        // 表名
        public static final String TABLE_NAME = "Card";

        // 表字段
        public static final String ID = "id";
        public static final String COLOR = "color";
        public static final String CARD_TPL_ID = "card_tpl_id";
        public static final String TYPE_NAME = "type_name";
        public static final String BALANCE = "balance";
        public static final String BUNDLEUSERS = "bundleUsers";
        public static final String DESCRIPTION = "description";
        public static final String NAME = "name";
        public static final String CHECK_VALID = "check_valid";
        public static final String VALID_FROM = "valid_from";
        public static final String VALID_TO = "valid_to";
        public static final String TYPE = "type";
        public static final String BRAND_ID = "brand_id";
        public static final String IS_LOCKED = "is_locked";
        public static final String IS_ACTIVE = "is_active";
        public static final String SUPPORTIDS = "supportIds";
        public static final String PRICE = "price";
        public static final String CARD_NO = "card_no";
        public static final String START = "start";
        public static final String END = "end";
        public static final String TOTAL_ACCOUNT = "total_account";
        public static final String TOTAL_TIMES = "total_times";

        // 建表语句
        public static final String CREATE = "CREATE TABLE "
            + TABLE_NAME
            + " ("
            + ID
            + " TEXT PRIMARY KEY,"
            + COLOR
            + " TEXT,"
            + CARD_TPL_ID
            + " TEXT,"
            + TYPE_NAME
            + " TEXT,"
            + BALANCE
            + " REAL,"
            + BUNDLEUSERS
            + " TEXT,"
            + DESCRIPTION
            + " TEXT,"
            + NAME
            + " TEXT,"
            + CHECK_VALID
            + " INT,"
            + VALID_FROM
            + " TEXT,"
            + VALID_TO
            + " TEXT,"
            + TYPE
            + " INT,"
            + BRAND_ID
            + " TEXT,"
            + IS_LOCKED
            + " INT,"
            + IS_ACTIVE
            + " INT,"
            + SUPPORTIDS
            + " TEXT,"
            + PRICE
            + " REAL,"
            + CARD_NO
            + " TEXT,"
            + START
            + " TEXT,"
            + END
            + " TEXT,"
            + TOTAL_ACCOUNT
            + " REAL,"
            + TOTAL_TIMES
            + " REAL"
            + " ); ";
        // 响应式的查询,根据表中的row生成一个对象
        static Func1<Cursor, Card> CARD_MAPPER = new Func1<Cursor, Card>() {
            @Override public Card call(Cursor cursor) {
                return toCardMapper(cursor);
            }
        };

        // 对象转字段,放入表中
        public static ContentValues toContentValues(Card card) {
            ContentValues values = new ContentValues();
            values.put(ID, card.getId());
            values.put(COLOR, card.getColor());
            values.put(CARD_TPL_ID, card.getCard_tpl_id());
            values.put(TYPE_NAME, card.getType_name());
            values.put(BALANCE, card.getBalance());
            values.put(BUNDLEUSERS, card.getBundleUsers());
            values.put(DESCRIPTION, card.getDescription());
            values.put(NAME, card.getName());
            values.put(CHECK_VALID, card.isCheck_valid());
            values.put(VALID_FROM, card.getValid_from());
            values.put(VALID_TO, card.getValid_to());
            values.put(TYPE, card.getType());
            values.put(BRAND_ID, card.getBrand_id());
            values.put(IS_LOCKED, card.is_locked());
            values.put(IS_ACTIVE, card.is_active());
            values.put(SUPPORTIDS, card.getSupportIds());
            values.put(PRICE, card.getPrice());
            values.put(CARD_NO, card.getCard_no());
            values.put(TOTAL_ACCOUNT, card.getTotal_account());
            values.put(TOTAL_TIMES, card.getTotal_times());
            return values;
        }

        public static Card toCardMapper(Cursor cursor) {
            if (cursor.getCount() == 0) {
                return null;
            }
            Card card = new Card();
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ID));
            card.setId(id);
            String color = cursor.getString(cursor.getColumnIndexOrThrow(COLOR));
            card.setColor(color);
            String card_tpl_id = cursor.getString(cursor.getColumnIndexOrThrow(CARD_TPL_ID));
            card.setCard_tpl_id(card_tpl_id);
            String type_name = cursor.getString(cursor.getColumnIndexOrThrow(TYPE_NAME));
            card.setType_name(type_name);
            Float balance = cursor.getFloat(cursor.getColumnIndexOrThrow(BALANCE));
            card.setBalance(balance);
            String bindleusers = cursor.getString(cursor.getColumnIndexOrThrow(BUNDLEUSERS));
            card.setBundleUsers(bindleusers);
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION));
            card.setDescription(description);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
            card.setName(name);
            int check_valid = cursor.getInt(cursor.getColumnIndexOrThrow(CHECK_VALID));
            if (check_valid == 1) {
                card.setCheck_valid(true);
            } else {
                card.setCheck_valid(false);
            }

            String valid_from = cursor.getString(cursor.getColumnIndexOrThrow(VALID_FROM));
            card.setValid_from(valid_from);
            String valid_to = cursor.getString(cursor.getColumnIndexOrThrow(VALID_TO));
            card.setValid_to(valid_to);
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(TYPE));
            card.setType(type);
            String brand_id = cursor.getString(cursor.getColumnIndexOrThrow(BRAND_ID));
            card.setBrand_id(brand_id);
            int is_locked = cursor.getInt(cursor.getColumnIndexOrThrow(IS_LOCKED));
            if (is_locked == 1) {
                card.setIs_locked(true);
            } else {
                card.setIs_locked(false);
            }

            int is_active = cursor.getInt(cursor.getColumnIndexOrThrow(IS_ACTIVE));
            if (is_active == 1) {
                card.setIs_active(true);
            } else {
                card.setIs_active(false);
            }

            String supportids = cursor.getString(cursor.getColumnIndexOrThrow(SUPPORTIDS));
            card.setSupportIds(supportids);
            Float price = cursor.getFloat(cursor.getColumnIndexOrThrow(PRICE));
            card.setPrice(price);
            String card_no = cursor.getString(cursor.getColumnIndexOrThrow(CARD_NO));
            card.setCard_no(card_no);
            Float total_account = cursor.getFloat(cursor.getColumnIndexOrThrow(TOTAL_ACCOUNT));
            card.setTotal_account(total_account);
            Float total_times = cursor.getFloat(cursor.getColumnIndexOrThrow(TOTAL_TIMES));
            card.setTotal_times(total_times);
            return card;
        }
    }
}
