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
import io.reactivex.subjects.UnicastSubject;

public class SubjectActivity extends AppCompatActivity {

    private final static String TAG = SubjectActivity.class.getSimpleName();

    CompositeDisposable disposable;
    BehaviorSubject<Integer> behaviorSubject;

    TextView tvData;
    Button btnAddData;

    int currentValue = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        disposable = new CompositeDisposable();

        tvData = findViewById(R.id.tv_data);
        btnAddData = findViewById(R.id.btn_add_data);

        behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext(currentValue);

        populateUI();
    }

    private void populateUI() {
        Log.i(TAG, "Populating Subject UI - started");

        Disposable subject = behaviorSubject
            .subscribe(
                result -> {
                    CharSequence currentText = tvData.getText();
                    String textToUpdate = String.valueOf(result);
                    tvData.setText(String.format("%s %s", currentText, textToUpdate)); // have to concatenate each time to get them all
                } ,
                Throwable::printStackTrace,
                () -> Log.i(TAG, "Populating Subject UI - finished")
            );

        disposable.add(subject);
    }

    public void addData(View v) {
        currentValue = currentValue + 1;
        behaviorSubject.onNext(currentValue);
        // no need to call populateUI again because when the subject values are update the subscribe listens to it
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subject_menu, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
