package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;

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

    public ClassRecordViewHolder(View itemView) {
        super(itemView);
        yearTextView = (TextView) itemView.findViewById(R.id.classrecord_year);

        itemStatementDetailHeaderdivierView = (View) itemView.findViewById(R.id.item_statement_detail_headerdivier);
        itemStatementDetailBottomdivierView = (View) itemView.findViewById(R.id.item_statement_detail_bottomdivier);
        itemStatementDetailDayTextView = (TextView) itemView.findViewById(R.id.item_statement_detail_day);
        itemStatementDetailMonthTextView = (TextView) itemView.findViewById(R.id.item_statement_detail_month);
        itemStatementDetailDateLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_statement_detail_date);
        itemStatementDetailPicImageView = (ImageView) itemView.findViewById(R.id.item_statement_detail_pic);
        itemStatementDetailNameTextView = (TextView) itemView.findViewById(R.id.item_statement_detail_name);
        itemStatementDetailContentTextView = (TextView) itemView.findViewById(R.id.item_statement_detail_content);

    }
}
