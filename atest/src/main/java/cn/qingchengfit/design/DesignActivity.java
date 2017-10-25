package cn.qingchengfit.design;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import cn.qingchengfit.testmodule.R;

/**
 * Created by fb on 2017/10/24.
 */

public class DesignActivity extends AppCompatActivity {

  private Button btnColor;
  private Button btnText;
  private Button btnEmpty;
  private Button btnWidget;
  private Button btnCell;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_design);
    btnColor = (Button) findViewById(R.id.btn_color);
    btnText = (Button) findViewById(R.id.btn_text);
    btnEmpty = (Button) findViewById(R.id.btn_empty);
    btnWidget = (Button) findViewById(R.id.btn_widget);
    btnCell = (Button) findViewById(R.id.btn_cell);
    btnText.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(TextActivity.class);
      }
    });
    btnColor.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(ColorActivity.class);
      }
    });
    btnEmpty.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(EmptyActivity.class);
      }
    });
    btnWidget.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(ButtonActivity.class);
      }
    });
    btnCell.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        start(CommonInputActivity.class);
      }
    });
  }

  private void start(Class<? extends Activity> activity){
    startActivity(new Intent(DesignActivity.this, activity));
  }

}
