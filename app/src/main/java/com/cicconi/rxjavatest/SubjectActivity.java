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
    ReplaySubject<Integer> subject;

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        tvData = findViewById(R.id.tv_data);

        disposable = new CompositeDisposable();
        subject = ReplaySubject.create();
        subject.onNext(currentValue);

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
        subject.onNext(currentValue);

        // Advantages:
        // It works like a LiveData because we just need to add a new data to the stream and the text is
        // automatically updated (subscribe is done only once)

        // Downsides:
        // Just like observables, they subscribe on main thread so if this was a heavy task I would need to change it manually
        // Subjects are not aware of lifecycle events so they don't send data again if the activity is resumed and
        // also we have to dispose them manually on onDestroy

        // This case is interesting when the data comes from a non observable because we can convert it into an observable
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
