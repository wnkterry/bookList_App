package com.example.kathy.booklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<bookInfo>> {
    /*
      * URL for Google Books API
      */
    private static final String GOOGLE_BOOKS_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=4";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private ProgressBar mProgress;

    /**
     * Constant value for the bookInfo loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOKLISTING_LOADER_ID = 1;

    /**
     * Adapter for the list of bookInfos
     */
    private BookListAdapter mAdapter;

    public static final String LOG_TAG = BookListActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);


    //Set progress spinner visible in bookInfo_activity.xml
        mProgress = (ProgressBar) findViewById(R.id.loading_spinner);
        mProgress.setVisibility(View.VISIBLE);


        // Find a reference to the {@link ListView} in the layout
        ListView bookInfoListView = (ListView) findViewById(R.id.list);


        //Find reference to set layout to empty state Textview
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookInfoListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of bookInfos as input
        mAdapter = new BookListAdapter(this, new ArrayList<bookInfo>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookInfoListView.setAdapter(mAdapter);


        Button cbutton = (Button)this.findViewById(R.id.search_button);
        EditText title = (EditText)this.findViewById(R.id.search_viewer);


        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        bookInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current bookInfo that was clicked on
                bookInfo currentbookInfo = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookInfoUri = Uri.parse(currentbookInfo.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookInfoUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected == true) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOKLISTING_LOADER_ID, null, this);
        } else {
            //Turn off spinner
            mProgress.setVisibility(View.GONE);

            // Set empty state text to display "No bookInfos found."
            mEmptyStateTextView.setText(R.string.No_Connection);

        }

    }

    @Override
    public Loader<List<bookInfo>> onCreateLoader(int i, Bundle bundle) {
        return new BookListLoader(this, GOOGLE_BOOKS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<bookInfo>> loader, List<bookInfo> bookInfos) {

        //Turn off spinner
        mProgress.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.empty);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (bookInfos != null && !bookInfos.isEmpty()) {
            mAdapter.addAll(bookInfos);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<bookInfo>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


}


