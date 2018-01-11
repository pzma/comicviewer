package pzm.comicviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    public EditText title, url, feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        title = (EditText) findViewById(R.id.titleRequestText);
        url = (EditText) findViewById(R.id.urlRequestText);
        feedback = (EditText) findViewById(R.id.feedbackRequestText);
    }

    protected void sendEmail(View v) {

        String body = "";
        body += "Request Title:\n";
        body += title.getText().toString();
        body += "Request URL:\n";
        body += url.getText().toString();
        body += "Feedback:\n";
        body += feedback.getText().toString();


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"peteyzeepablo@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Request: ComicViewer");
        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
