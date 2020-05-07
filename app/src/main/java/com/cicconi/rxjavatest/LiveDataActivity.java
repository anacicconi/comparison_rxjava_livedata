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
    private MutableLiveData<Integer> data = new MutableLiveData<>(currentValue);

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);

        tvData = findViewById(R.id.tv_data);

        populateUI();
    }

    private void populateUI() {
        Log.i(TAG, "Populating LiveData UI - started");

        data.observe(this, value -> {
            if(null != value) {
                tvData.setText(String.valueOf(value));
            }
        });

        Log.i(TAG, "Populating LiveData UI - finished");
    }

    public void addData(View v) {
        currentValue = currentValue + 1;
        data.postValue(currentValue);

        // No need to call the method populateUI again because the "data.observe" checks for updates on the LiveData
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

        return super.onOptionsItemSelected(item);
    }
}
