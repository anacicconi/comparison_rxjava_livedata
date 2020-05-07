package com.cicconi.rxjavatest;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ObservableActivity extends AppCompatActivity {

    private final static String TAG = ObservableActivity.class.getSimpleName();

    int currentValue = 1;

    CompositeDisposable disposable;
    Observable<Integer> observable;

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable);

        tvData = findViewById(R.id.tv_data);

        disposable = new CompositeDisposable();
        observable = Observable.just(currentValue);

        populateUI();
    }

    private void populateUI() {
        Log.i(TAG, "Populating Observable UI - started");

        Disposable disposableObservable = observable
            .subscribe(
                result -> tvData.setText(String.valueOf(result)),
                Throwable::printStackTrace,
                () -> Log.i(TAG, "Populating Observable UI - finished")
            );

        disposable.add(disposableObservable);
    }

    public void addData(View v) {
        currentValue = currentValue + 1;
        observable = Observable.just(currentValue);

        // if I want the view to be updated when the observable is updated I have to do it by myself
        // calling the method again because the "observable.subscribe" does not keep listening for changes
        // In other words, I have to subscribe again each time the observable changes
        // populateUI();
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

        return super.onOptionsItemSelected(item);
    }
}
