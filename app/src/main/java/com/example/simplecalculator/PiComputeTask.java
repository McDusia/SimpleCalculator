package com.example.simplecalculator;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ProgressBar;

public class PiComputeTask extends AsyncTask<Void, Integer, Double> {

    private ProgressBar progressBar;
    private EditText n1EditText;

    PiComputeTask(ProgressBar _progressBar, EditText _n1EditText){
        progressBar = _progressBar;
        n1EditText = _n1EditText;
    }

    protected Double doInBackground(Void... voids) {
        int n = 10000000;
        int k = 0;
        double x, y;
        for (int i = 0; i < n; i++) {
            x = Math.random();
            y = Math.random();

            if (x * x + y * y <= 1) {
                k++;
            }

            if (i % 1000 == 0) {
                publishProgress(i*100/n);
            }
        }
        return 4. * k / n;
    }

    protected void onPostExecute(Double result) {
        n1EditText.setText(String.valueOf(result));
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }
}