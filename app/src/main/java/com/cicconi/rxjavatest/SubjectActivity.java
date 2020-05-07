package com.cicconi.rxjavatest;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;

public class SubjectActivity extends AppCompatActivity {

    private final static String TAG = SubjectActivity.class.getSimpleName();

    int currentValue = 1;

    CompositeDisposable disposable;
    Observable<Integer> observable;
    ReplaySubject<Integer> subject;

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        tvData = findViewById(R.id.tv_data);

        disposable = new CompositeDisposable();
        observable = Observable.just(currentValue);
        subject = ReplaySubject.create();

        observable.subscribe(subject);

        populateUI();
    }

    private void populateUI() {
        Log.i(TAG, "Populating Subject UI - started");

        Disposable disposableSubject = subject
            .subscribe(
                result -> {
                    Log.i(TAG, String.format("result: %s", result));
                    tvData.setText(String.valueOf(result));
                } ,
                Throwable::printStackTrace,
                () -> Log.i(TAG, "Populating Subject UI - finished")
            );

        disposable.add(disposableSubject);
    }

    public void addData(View v) {
        currentValue = currentValue + 1;
        // TODO: I can make the subject subscribes to the values of the observable but how to send more values later?
        //observable = Observable.just(currentValue);
        //subject.onNext(currentValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.dispose();
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
