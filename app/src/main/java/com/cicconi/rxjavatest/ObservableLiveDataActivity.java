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
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ObservableLiveDataActivity extends AppCompatActivity {

    private final static String TAG = ObservableLiveDataActivity.class.getSimpleName();

    int currentValue = 1;
    CompositeDisposable disposable;
    Observable<Integer> observable;
    private MutableLiveData<Integer> data;

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);

        tvData = findViewById(R.id.tv_data);

        disposable = new CompositeDisposable();
        observable = Observable.just(currentValue);

        // The LiveData starts empty because it will receive the values from the observable
        data = new MutableLiveData<>();

        populateUI();
        updateDataValue();
    }

    private void populateUI() {
        data.observe(this, value -> {
            if(null != value) {
                Log.i(TAG, "New LiveData value received: " + value);
                tvData.setText(String.valueOf(value));
            }
        });
    }

    private void updateDataValue() {
        Log.i(TAG, "Populating LiveData UI - started");

        Disposable disposableObservable = observable
            .subscribe(
                result -> data.postValue(result),
                Throwable::printStackTrace,
                () -> Log.i(TAG, "Populating LiveData UI - finished")
            );

        disposable.add(disposableObservable);
    }

    public void addData(View v) {
        currentValue = currentValue + 1;
        observable = Observable.just(currentValue);

        //TODO: is it possible to make LiveData listens to changes on observable without calling the subscribe again?
        updateDataValue();
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
