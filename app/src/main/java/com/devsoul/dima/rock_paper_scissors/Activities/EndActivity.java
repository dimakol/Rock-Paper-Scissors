package com.devsoul.dima.rock_paper_scissors.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.devsoul.dima.rock_paper_scissors.MainFragment;
import com.devsoul.dima.rock_paper_scissors.R;

public class EndActivity extends Activity implements MainFragment.TaskStatusCallback {

    private MainFragment mFragment;
    private TextView text;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);

        // Import font from assets
        String fontPath = "fonts/Paint Peel Initials.ttf";
        text = (TextView) findViewById(R.id.signature);
        // Font Face
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        // Applying font to text
        text.setTypeface(typeface);

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

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onPostExecute() {
        // Finishes all the activities except for FirstActivity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public void onCancelled() {

    }
}
