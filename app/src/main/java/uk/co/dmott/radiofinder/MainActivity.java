package uk.co.dmott.radiofinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by david on 20/10/15.
 */
public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    public final static String MUSIC_SEARCHES_SYMBOL = "co.uk.dmott.mynewradiofinder1.SEARCHES";
    private static final int PERMISSION_REQUEST_NETWORK_STATE = 0;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int PERMISSION_REQUEST_INTERNET = 0;



    public enum NetworkStatus {
      UNKNOWN,AVAILABLE,NOTAVAILABLE
    }

    private SharedPreferences musicSearchesEntered; // cache of previous searches

    private TableLayout musicTableScrollView; //Scrollview for the matches returned from pullparser

    private EditText musicEditText; // optional text field for specifying string to search for in the synopsis
    private Spinner genreSpinner; // spinner for choosing the music genre to search for

    private View mLayout;

    private NetworkStatus networkState = NetworkStatus.UNKNOWN;

    private String searchPattern;


    Button enterMusicSearchButton; //Button to add the search pattern to cache and search in xmlpullparser

    Context mainContext;
    HashMap<String, String> genresMap = new HashMap<String, String>();

    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    public static ArrayList<String> channelpointer = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.tableLayout); // main layout

        musicEditText = (EditText) findViewById(R.id.searchScheduleEditText);
        genreSpinner = (Spinner) findViewById(R.id.musicGenreSpinner);

        musicSearchesEntered = getSharedPreferences(MUSIC_SEARCHES_SYMBOL, MODE_PRIVATE);

        musicTableScrollView = (TableLayout) findViewById(R.id.musicSearchTableScrollView);

        mainContext = this;

        genresMap.clear(); // map the names put in the spinner to the string used in the search
        genresMap.put("classicpopandrock", "Classic Pop & Rock");
        genresMap.put("Classic Pop & Rock", "classicpopandrock");

        genresMap.put("hiphoprnbanddancehall", "HipHop, R&B & Dance");
        genresMap.put("HipHop, R&B & Dance", "hiphoprnbanddancehall");

        genresMap.put("classical", "Classical");
        genresMap.put("Classical", "classical");

        genresMap.put("jazzandblues", "Jazz & Blues");
        genresMap.put("Jazz & Blues", "jazzandblues");

        genresMap.put("country", "Country");
        genresMap.put("Country", "country");

        genresMap.put("popandchart", "Pop & Chart");
        genresMap.put("Pop & Chart", "popandchart");

        genresMap.put("danceandelectronica", "Dance & Electronica");
        genresMap.put("Dance & Electronica", "danceandelectronica");

        genresMap.put("rockandindie", "Rock & Indie");
        genresMap.put("Rock & Indie", "rockandindie");

        genresMap.put("desi", "Desi");
        genresMap.put("Desi", "desi");

        genresMap.put("soulandreggae", "Soul & Raggae");
        genresMap.put("Soul & Raggae", "soulandreggae");


        genresMap.put("easylisteningsoundtracksandmusicals", "Easy Listening,Sound tracks,Musicals");
        genresMap.put("Easy Listening,Sound tracks,Musicals", "easylisteningsoundtracksandmusicals");


        genresMap.put("world", "World");
        genresMap.put("World", "world");

        genresMap.put("folk", "Folk");
        genresMap.put("Folk", "folk");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button goFindMusicButton = (Button) findViewById(R.id.enterSearchScheduleButton);
        goFindMusicButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newSearchString1 = genreSpinner.getSelectedItem().toString();
                String newSearchString2 = genresMap.get(newSearchString1);

                newSearchString2 += "/" + musicEditText.getText().toString();

                musicEditText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(musicEditText.getWindowToken(), 0);


                saveSearchkString(newSearchString2);

            }

        });
        if (getResources().getConfiguration().orientation != 1) {

            musicTableScrollView.removeAllViews();
            deleteAllSaveSearchkString();

            updateSavedSearchList(null);
        } else {
            musicTableScrollView.removeAllViews();
            updateSavedSearchList(null);

        }


    }

    private void deleteAllSaveSearchkString()
    {
        SharedPreferences.Editor preferencesEditor = musicSearchesEntered.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_help1:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showHelp()
    {
        // show the help screen

        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        //intent.putExtra(MUSIC_SEARCHES_SYMBOL, searchScheduleString);
        startActivity(intent);

    }






    private void saveSearchkString(String newSearchString)
    {
        String isTheSearchStringNew = musicSearchesEntered.getString(newSearchString, null);


        if (isTheSearchStringNew == null)
        {
            SharedPreferences.Editor preferencesEditor = musicSearchesEntered.edit();
            preferencesEditor.putString(newSearchString, newSearchString);
            preferencesEditor.apply();
            updateSavedSearchList(newSearchString);
        }
        else
        {
            Toast.makeText(this, R.string.already_stored, Toast.LENGTH_SHORT).show();

           // if (isNetworkAvailable(mainContext)) {
            //    Intent intent = new Intent(MainActivity.this, ScheduleListActivity.class);
           //     intent.putExtra(MUSIC_SEARCHES_SYMBOL, newSearchString);
            //    startActivity(intent);
           // }
           // else
           // {
           //     Toast.makeText(mainContext, "You have no network available", Toast.LENGTH_SHORT).show();

           // }
            searchPattern =   newSearchString;
            GotoScheduleListActivityIfNetworkAvailable(mainContext);

        }

    }

    // added so that now the user adds the search genre and goes to find in on operation
    private void insertSearchStringInScrollViewAndClick(String searchString, int arrayIndex)
    {

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newSearchScheduleRow = inflater.inflate(R.layout.music_search_row, null);

        TextView newSearchScheduleTextView = (TextView)newSearchScheduleRow.findViewById(R.id.musicSearchTextView);

        newSearchScheduleTextView.setText(searchString);

        Button scheduleFindButton = (Button)newSearchScheduleRow.findViewById(R.id.musicSearchButton);
        scheduleFindButton.setOnClickListener(getScheduleActivityListener);

        Button deleteMeButton = (Button)newSearchScheduleRow.findViewById(R.id.deleteButton);
        deleteMeButton.setOnClickListener(deleteScheduleActivityListener);

        musicTableScrollView.addView(newSearchScheduleRow, arrayIndex);
        newSearchScheduleRow.findViewById(R.id.musicSearchButton).performClick();
    }





    private void insertSearchStringInScrollView(String searchString, int arrayIndex)
    {

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newSearchScheduleRow = inflater.inflate(R.layout.music_search_row, null);

        TextView newSearchScheduleTextView = (TextView)newSearchScheduleRow.findViewById(R.id.musicSearchTextView);

        newSearchScheduleTextView.setText(searchString);

        Button scheduleFindButton = (Button)newSearchScheduleRow.findViewById(R.id.musicSearchButton);
        scheduleFindButton.setOnClickListener(getScheduleActivityListener);

        Button deleteMeButton = (Button)newSearchScheduleRow.findViewById(R.id.deleteButton);
        deleteMeButton.setOnClickListener(deleteScheduleActivityListener);

        musicTableScrollView.addView(newSearchScheduleRow, arrayIndex);
        //newSearchScheduleRow.findViewById(R.id.musicSearchButton).performClick();
    }



    private void updateSavedSearchList(String newSearchString)
    {
        String[] searchStrings = musicSearchesEntered.getAll().keySet().toArray(new String[0]);
        Arrays.sort(searchStrings, String.CASE_INSENSITIVE_ORDER);
        if (newSearchString != null)
        {
            insertSearchStringInScrollViewAndClick(newSearchString, Arrays.binarySearch(searchStrings, newSearchString));

        } else
        {
            for (int i=0; i < searchStrings.length; i++)
            {
                insertSearchStringInScrollView(searchStrings[i], i);
            }

        }
    }
    public View.OnClickListener getScheduleActivityListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            //musicSearchTextView


            View SearchScheduleRow = (TableRow)v.getParent();
            SearchScheduleRow.setBackgroundColor(getResources().getColor(R.color.aero_blue));
            TextView searchScheduleTextView = (TextView)SearchScheduleRow.findViewById(R.id.musicSearchTextView);
            String searchScheduleString = searchScheduleTextView.getText().toString();
            searchPattern =   searchScheduleString;

            GotoScheduleListActivityIfNetworkAvailable(mainContext);

            //if (isNetworkAvailable(mainContext))
            //{
      //          Intent intent = new Intent(MainActivity.this, ScheduleListActivity.class);
      //          intent.putExtra(MUSIC_SEARCHES_SYMBOL, searchScheduleString);
       //         startActivity(intent);
            //}
            //else
            //{
            //    Toast.makeText(mainContext, "You have no network available", Toast.LENGTH_SHORT).show();
            //}


        }



    };


    public void GotoScheduleListActivityIfNetworkAvailable(final Context context) {
        // permission     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
/**
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start network state preview
            Snackbar.make(mLayout,
                    "Access Network State avaiable. Getting status",
                    Snackbar.LENGTH_SHORT).show();
            if (getNetworkStatus(mainContext) == true)
            {
                Snackbar.make(mLayout,
                        "Network is available",
                        Snackbar.LENGTH_SHORT).show();
                        startGetScheduleActivity();
            }
            else
            {
                Snackbar.make(mLayout,
                        "Network is not available",
                        Snackbar.LENGTH_SHORT).show();
            }
        } else {
            // Permission is missing and must be requested.
            requestNetworkStatePermission();
        }
**/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start network state preview
            Snackbar.make(mLayout,
                    "Internet available.",
                    Snackbar.LENGTH_SHORT).show();
            if (getNetworkStatus(mainContext) == true)
            {
                Snackbar.make(mLayout,
                        "Network/Internet is available",
                        Snackbar.LENGTH_SHORT).show();
                startGetScheduleActivity();
            }
            else
            {
                Snackbar.make(mLayout,
                        "Network/Internet is not available",
                        Snackbar.LENGTH_SHORT).show();
            }
        } else {
            // Permission is missing and must be requested.
            requestNetworkStatePermission();
        }





    }

    private void startGetScheduleActivity()
    {

        String localSearchPattern = searchPattern;
        System.out.println("Starting GetSchedule");

        Intent intent = new Intent(MainActivity.this, ScheduleListActivity.class);
        intent.putExtra(MUSIC_SEARCHES_SYMBOL, searchPattern);
        startActivity(intent);
    }





    private boolean getNetworkStatus(Context ctxt)
    {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) ctxt.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        //return true;
    }


    private void requestNetworkStatePermission() {
        // Permission has not been granted and must be requested.

        /**
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_NETWORK_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Permission Access Network State is Required.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                            PERMISSION_REQUEST_NETWORK_STATE);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission Access Network State is not available. Requesting permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSION_REQUEST_NETWORK_STATE);
        }

         **/
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Permission Internet is Required.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.INTERNET},
                            PERMISSION_REQUEST_INTERNET);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Internet is not available. Requesting permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_INTERNET);
        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        /**
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_NETWORK_STATE) {
            // Request for network state  permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Snackbar.make(mLayout, "Permission Access Network State was granted. Get status.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                if (getNetworkStatus(mainContext) == true)
                    startGetScheduleActivity();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Permission Access Network State was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
         **/

        if (requestCode == PERMISSION_REQUEST_INTERNET) {
            // Request for network state  permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Snackbar.make(mLayout, "Permission Internet was granted. Get status.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                if (getNetworkStatus(mainContext) == true)
                    startGetScheduleActivity();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Permission Internet was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)


    }



    public View.OnClickListener deleteScheduleActivityListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            View SearchScheduleRow = (TableRow)v.getParent();
            int indx = musicTableScrollView.indexOfChild(SearchScheduleRow);
            System.out.println("remove index + " + indx);
            TextView keyStringTV = (TextView)SearchScheduleRow.findViewById(R.id.musicSearchTextView);
            String keyString = keyStringTV.getText().toString();
            System.out.println("remove string+ " + keyString);
            deleteScheduleAtIndex(indx,keyString);


        }



    };


    private void deleteScheduleAtIndex(int index, String keyToRemove)
    {
        musicTableScrollView.removeViewAt(index);

        SharedPreferences.Editor editor = musicSearchesEntered.edit();
        editor.remove( keyToRemove);
        editor.apply();
    }



    //@Override
    /**
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

**/







}
