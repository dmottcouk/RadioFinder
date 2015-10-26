package uk.co.dmott.radiofinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by david on 23/10/15.
 */
public class ScheduleEntryDetailScreen extends AppCompatActivity {

    private String strtTime;
    private String endTime;
    private String strtDate;
    private String endDate;
    private String start;
    private String end;
    private boolean isTomorrow;

    private String displayTitlesTitle;
    private String shortSynopsis;
    private String serviceTypeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.schedule_list_view);
        setContentView(R.layout.list_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent theIntent = getIntent();

        start = theIntent.getStringExtra("startTime");
        end = theIntent.getStringExtra("endTime");

        strtTime = start.substring(11, 16);
        endTime = end.substring(11, 16);

        strtDate = start.substring(8, 10);
        endDate = end.substring(8, 10);



        Calendar rightNow = Calendar.getInstance();
        int calDate = rightNow.get(Calendar.DATE);


        if (Integer.parseInt(strtDate) == calDate)
        {
            isTomorrow = false;
        }
        else
        {
            isTomorrow = true;
        }



        displayTitlesTitle = theIntent.getStringExtra("displayTitlesTitle");
        shortSynopsis = theIntent.getStringExtra("shortSynopsis");
        serviceTypeTitle = theIntent.getStringExtra("serviceTypeTitle");
        String mediaType = theIntent.getStringExtra("mediaType");


        ImageView iconVw = (ImageView) findViewById(R.id.detailsMediaTypeImageView);


        if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio3"))
        {

            iconVw.setImageResource(R.drawable.radio3);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio6"))
        {

            iconVw.setImageResource(R.drawable.radio6);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("cymru"))
        {

            iconVw.setImageResource(R.drawable.cymru);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio2"))
        {

            iconVw.setImageResource(R.drawable.radio2);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio1"))
        {

            iconVw.setImageResource(R.drawable.radio1);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio4"))
        {

            iconVw.setImageResource(R.drawable.radio4);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("bbc4"))
        {

            iconVw.setImageResource(R.drawable.bbc4);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("bbcasian"))
        {

            iconVw.setImageResource(R.drawable.asian);
        }
        else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("scotland"))
        {

            iconVw.setImageResource(R.drawable.scot);
        }


        TextView startEndTV = (TextView) findViewById(R.id.detailsTimeTextView);
        String startEndStr = strtTime + "-" + endTime;
        startEndTV.setText(startEndStr);

        TextView titleTV = (TextView) findViewById(R.id.detailsTitleTextView);
        titleTV.setText(displayTitlesTitle);

        TextView synopsisTV = (TextView) findViewById(R.id.detailsOverviewTextView);
        synopsisTV.setText(shortSynopsis);


        TextView serviceTV = (TextView) findViewById(R.id.detailsServiceTitleTextView);
        serviceTV.setText("Service : "+ serviceTypeTitle );


        TextView mediaTV = (TextView) findViewById(R.id.detailsMediaTypeTextView);
        mediaTV.setText("Media : "+ mediaType);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
