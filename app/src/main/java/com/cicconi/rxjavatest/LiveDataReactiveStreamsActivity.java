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

        // useless but proves that it's not possible to update the value of an observable that was converted into a livedata
        flowable = Flowable.just(currentValue);

        // Advantages:
        // Easy to handle error cases or transforming the data before sending it to the livedata

        // Downsides:
        // if I want the livedata to be updated when the observable is updated I have to create a new livedata using
        // LiveDataReactiveStreams.fromPublisher(flowable) and observe again

        // By default, observables subscribe on main thread so if this was a heavy task I would need to change it manually
        // Observables are not aware of lifecycle events so they don't send data again if the activity is resumed and
        // also we have to dispose them manually on onDestroy

        // **** This is a useless case in the way I'm handling it here but interesting to understand how to convert
        // an observable into a livedata once
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
