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
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LiveDataReactiveStreamsActivity extends AppCompatActivity {

    private final static String TAG = LiveDataReactiveStreamsActivity.class.getSimpleName();

    int currentValue = 1;
    Flowable<Integer> flowable;
    private LiveData<Integer> data;

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);

        tvData = findViewById(R.id.tv_data);

        // LiveDataReactiveStreams only work with flowables
        flowable = Flowable.just(currentValue);
        data = LiveDataReactiveStreams.fromPublisher(flowable);

        populateUI();
    }

    private void populateUI() {
        data.observe(this, value -> {
            if(null != value) {
                Log.i(TAG, "New LiveData value received: " + value);
                tvData.setText(String.valueOf(value));
            }
        });
    }

    public void addData(View v) {
        currentValue = currentValue + 1;
        flowable = Flowable.just(currentValue);

        //TODO: is it possible to add more values to LiveData after LiveDataReactiveStreams.fromPublisher?
        // Not directly to LiveData but to the flowable that would be used by LiveData (as on a real network call
        // the data wouldn't be set directly to LiveData)
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
