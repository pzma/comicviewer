package pzm.comicviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        populateTextViews();

    }

    private void populateTextViews() {
        TextView textView0 = (TextView) findViewById(R.id.textView0);
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        TextView textView8 = (TextView) findViewById(R.id.textView8);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
        TextView textView10 = (TextView) findViewById(R.id.textView10);

        textView0.setText(Html.fromHtml(getString(R.string.zerohelp)));
        textView1.setText(Html.fromHtml(getString(R.string.onehelp)));
        textView2.setText(Html.fromHtml(getString(R.string.twohelp)));
        textView3.setText(Html.fromHtml(getString(R.string.threehelp)));
        textView4.setText(Html.fromHtml(getString(R.string.fourhelp)));
        textView5.setText(Html.fromHtml(getString(R.string.fivehelp)));
        textView6.setText(Html.fromHtml(getString(R.string.sixhelp)));
        textView7.setText(Html.fromHtml(getString(R.string.sevenhelp)));
        textView8.setText(Html.fromHtml(getString(R.string.eighthelp)));
        textView9.setText(Html.fromHtml(getString(R.string.ninehelp)));
        textView10.setText(Html.fromHtml(getString(R.string.tenhelp)));

    }


}
