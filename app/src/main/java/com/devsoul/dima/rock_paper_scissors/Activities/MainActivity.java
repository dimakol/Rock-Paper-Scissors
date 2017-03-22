package com.devsoul.dima.rock_paper_scissors.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.devsoul.dima.rock_paper_scissors.MainFragment;
import com.devsoul.dima.rock_paper_scissors.R;

public class MainActivity extends AppCompatActivity implements MainFragment.TaskStatusCallback
{

    private MainFragment mFragment;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Finish the Activity
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        // Font
        String fontPath = "fonts/Road_Rage.otf";

        TextView text1 = (TextView) findViewById(R.id.title1);
        TextView text2 = (TextView) findViewById(R.id.title2);
        TextView text3 = (TextView) findViewById(R.id.title3);

        // Font Face
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);

        // Applying font
        text1.setTypeface(typeface);
        text2.setTypeface(typeface);
        text3.setTypeface(typeface);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (savedInstanceState != null) {
            int progress = savedInstanceState.getInt("progress_value");
            mProgressBar.setProgress(progress);
        }

        android.app.FragmentManager mMgr = getFragmentManager();
        mFragment = (MainFragment) mMgr
                .findFragmentByTag(MainFragment.TAG_START_FRAGMENT);

        if (mFragment == null) {
            mFragment = new MainFragment();
            mMgr.beginTransaction()
                    .add(mFragment, MainFragment.TAG_START_FRAGMENT)
                    .commit();
        }

        if (mFragment != null)
            mFragment.startBackgroundTask();
    }

    /**
     * This method is called before an activity may be killed Store info in
     * bundle if required.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("progress_value", mProgressBar.getProgress());
    }

    // Background task Callbacks

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onPostExecute() {
        if (mFragment != null)
            mFragment.updateExecutingStatus(false);
        // Move to menu activity
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCancelled() {

    }
}
