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

public class ObservableActivity extends AppCompatActivity {

    private final static String TAG = ObservableActivity.class.getSimpleName();

    CompositeDisposable disposable;
    Observable<Integer> justObservable;

    TextView tvData;
    Button btnAddData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable);

        disposable = new CompositeDisposable();

        tvData = findViewById(R.id.tv_data);
        btnAddData = findViewById(R.id.btn_add_data);

        justObservable = Observable.just(1, 2, 3);

        populateUI();
    }

    private void populateUI() {
        Log.i(TAG, "Populating Observable UI - started");

        Disposable observable = justObservable
            .subscribe(
                // result -> tvData.setText(result), // the one that will be set to the TextView is "three" because it's the last received
                result -> {
                    CharSequence currentText = tvData.getText();
                    String textToUpdate = String.valueOf(result);
                    tvData.setText(String.format("%s %s", currentText, textToUpdate)); // have to concatenate each time to get them all
                } ,
                Throwable::printStackTrace,
                () -> Log.i(TAG, "Populating Observable UI - finished")
            );

        disposable.add(observable);
    }

    public void addData(View v) {
        justObservable = Observable.just(4, 5, 6);
        // populateUI(): if I want the view to be updated when the observable is updated I have to do it by myself
        // calling the method again because the subscribe does not keep listening for changes
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.observable_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_subject) {
            Intent subjectIntent = new Intent(this, SubjectActivity.class);
            startActivity(subjectIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
