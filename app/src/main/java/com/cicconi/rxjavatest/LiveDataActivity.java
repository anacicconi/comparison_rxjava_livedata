package com.cicconi.rxjavatest;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import io.reactivex.disposables.Disposable;

public class LiveDataActivity extends AppCompatActivity {

    private final static String TAG = LiveDataActivity.class.getSimpleName();

    int currentValue = 1;
    private MutableLiveData<Integer> data;

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);

        tvData = findViewById(R.id.tv_data);

        data = new MutableLiveData<>(currentValue);

        populateUI();
    }

    private void populateUI() {
        Log.i(TAG, "Populating LiveData UI - started");

        data.observe(this, value -> {
            if(null != value) {
                Log.i(TAG, "New LiveData value received: " + value);
                tvData.setText(String.valueOf(value));
            }
        });

        Log.i(TAG, "Populating LiveData UI - finished");
    }

    public void addData(View v) {
        currentValue = currentValue + 1;
        data.postValue(currentValue);

        // Advantages:
        // No need to call the method populateUI again because the "data.observe" checks for updates on the LiveData
        // LiveData is lifecycle aware so once the activity is resumed it sends the latest data received
        // Also, no need to unsubscribe it because it already knows when the activity is destroyed
        // LiveData listens to changes already on a background thread so no need to change it

        // Downsides:
        // LiveData alone does not survive configuration changes so it will be reset if the phone is rotated (needs a viewmodel)
        // Not easy to handle errors or transform data before passing to UI
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_observable) {
            Intent observableIntent = new Intent(this, ObservableActivity.class);
            startActivity(observableIntent);

            return true;
        }

        if (id == R.id.action_subject) {
            Intent subjectIntent = new Intent(this, SubjectActivity.class);
            startActivity(subjectIntent);

            return true;
        }

        if (id == R.id.action_livedata) {
            Intent observableIntent = new Intent(this, LiveDataActivity.class);
            startActivity(observableIntent);

            return true;
        }

        if (id == R.id.action_observable_livedata) {
            Intent observableIntent = new Intent(this, ObservableLiveDataActivity.class);
            startActivity(observableIntent);

            return true;
        }

        if (id == R.id.action_live_data_reactive_streams) {
            Intent observableIntent = new Intent(this, LiveDataReactiveStreamsActivity.class);
            startActivity(observableIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
