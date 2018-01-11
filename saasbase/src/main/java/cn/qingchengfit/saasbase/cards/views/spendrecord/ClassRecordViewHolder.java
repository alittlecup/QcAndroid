package cn.qingchengfit.saasbase.cards.views.spendrecord;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;

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
 * Created by Paper on 15/11/20 2015.
 */
public class ClassRecordViewHolder extends RecyclerView.ViewHolder {

    View itemStatementDetailHeaderdivierView;
    View itemStatementDetailBottomdivierView;
    TextView itemStatementDetailDayTextView;
    TextView itemStatementDetailMonthTextView;
    LinearLayout itemStatementDetailDateLinearLayout;
    ImageView itemStatementDetailPicImageView;
    TextView itemStatementDetailNameTextView;
    TextView itemStatementDetailContentTextView;
    TextView yearTextView;
    RelativeLayout headerlayout;
    TextView monthdata;
    TextView spendaccount;

    public ClassRecordViewHolder(View itemView) {
        super(itemView);
        yearTextView = (TextView) itemView.findViewById(R.id.classrecord_year);
        headerlayout = (RelativeLayout) itemView.findViewById(R.id.header_layout);
        monthdata = (TextView) itemView.findViewById(R.id.month_data);
        spendaccount = (TextView) itemView.findViewById(R.id.account);

        itemStatementDetailHeaderdivierView = (View) itemView.findViewById(R.id.item_statement_detail_headerdivier);
        itemStatementDetailBottomdivierView = (View) itemView.findViewById(R.id.item_statement_detail_bottomdivier);
        itemStatementDetailDayTextView = (TextView) itemView.findViewById(R.id.item_statement_detail_day);
        itemStatementDetailMonthTextView = (TextView) itemView.findViewById(R.id.item_statement_detail_month);
        itemStatementDetailDateLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_statement_detail_date);
        itemStatementDetailPicImageView = (ImageView) itemView.findViewById(R.id.item_statement_detail_pic);
        itemStatementDetailNameTextView = (TextView) itemView.findViewById(R.id.item_statement_detail_name);
        itemStatementDetailContentTextView = (TextView) itemView.findViewById(R.id.item_statement_time_shop);
    }
}
