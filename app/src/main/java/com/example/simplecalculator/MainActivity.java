package com.example.simplecalculator;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private EditText n1EditText, n2EditText, resEditText;
    private Button addButton, subButton, divButton, mulButton, piButton, clearButton;
    LogicService logicService;
    ProgressBar progressBar;
    boolean mBound = false;
    String WRONG_NUMBER_FORMAT = "Zły format liczby";

    private ServiceConnection logicConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LogicService.LocalBinder binder = (LogicService.LocalBinder) service;
            logicService = binder.getService();
            mBound = true;
            Toast.makeText(MainActivity.this, "Logic Service Connected!",
                    Toast.LENGTH_SHORT).show();
        }
        public void onServiceDisconnected(ComponentName className) {
            logicService = null;
            mBound = false;
            Toast.makeText(MainActivity.this, "Logic Service Disconnected!",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        n1EditText = (EditText) findViewById(R.id.n1EditText);
        n2EditText = (EditText) findViewById(R.id.n2EditText);
        addButton = (Button) findViewById(R.id.addButton);
        subButton = (Button) findViewById(R.id.subButton);
        mulButton = (Button) findViewById(R.id.mulButton);
        divButton = (Button) findViewById(R.id.divButton);
        piButton = (Button) findViewById(R.id.piButton);
        resEditText = (EditText) findViewById(R.id.resEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        clearButton = (Button) findViewById(R.id.clearButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            this.bindService(new Intent(MainActivity.this,LogicService.class),
                    logicConnection, Context.BIND_AUTO_CREATE);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            mBound = false;
            this.unbindService(logicConnection);
        }
    }

    /**
    //uwzględnić wyjątki np puste pole edycyjne lub niepoprawnie wprowadzona liczba
    * */
    private boolean validateInputs(EditText editTextInput1, EditText editTextInput2, boolean division){
        String n1 = editTextInput1.getText().toString();
        String n2 = editTextInput2.getText().toString();
        try {
            Double.parseDouble(n1);
            Double.parseDouble(n2);
        } catch(NumberFormatException e) {
            return false;
        }
        if(division && n2.equals("0")){
            return false;
        }
        return !n1.isEmpty() && !n2.isEmpty();
    }

    private double convertEditTextToDouble(EditText editTextInput) {
        String stringInput = editTextInput.getText().toString();
        return  Double.parseDouble(stringInput);
    }

    private void setResult(double result) {
        resEditText.setText(String.valueOf(result));
    }

    private void setResultWrongInput() {
        resEditText.setText(WRONG_NUMBER_FORMAT);
        resEditText.setHighlightColor(100);
    }

    public void addOnClick(View v) {
        System.out.println("Add clicked");

        if (mBound) {
            if(validateInputs(n1EditText, n2EditText, false)) {
                double n1 = convertEditTextToDouble(n1EditText);
                double n2 = convertEditTextToDouble(n2EditText);
                double result = logicService.add(n1, n2);
                setResult(result);
            } else {
                setResultWrongInput();
            }
        }
    }

    public void subOnClick(View v) {
        System.out.println("Sub clicked");
        if (mBound) {
            if(validateInputs(n1EditText, n2EditText, false)) {
                double n1 = convertEditTextToDouble(n1EditText);
                double n2 = convertEditTextToDouble(n2EditText);
                double result = logicService.sub(n1, n2);
                setResult(result);
            } else {
                setResultWrongInput();
            }
        }
    }

    public void mulOnClick(View v) {
        System.out.println("Mul clicked");
        if (mBound) {
            if(validateInputs(n1EditText, n2EditText, false)) {
                double n1 = convertEditTextToDouble(n1EditText);
                double n2 = convertEditTextToDouble(n2EditText);
                double result = logicService.mul(n1, n2);
                setResult(result);
            } else {
                setResultWrongInput();
            }
        }
    }

    public void divOnClick(View v) {
        System.out.println("Div clicked");
        if (mBound) {
            if(validateInputs(n1EditText, n2EditText, true)) {
                double n1 = convertEditTextToDouble(n1EditText);
                double n2 = convertEditTextToDouble(n2EditText);
                double result = logicService.div(n1, n2);
                setResult(result);
            } else {
                setResultWrongInput();
            }
        }
    }

    /**
     *  Liczba Pi powinna być wpisana po pola odnoszącego się do liczby nr 1.*/
    public void countPIOnClick(View v) {
        System.out.println("Count PI clicked");
        progressBar.setProgress(0);
        new PiComputeTask(progressBar, n1EditText).execute();
    }

    public void clearOnClick(View v) {
        System.out.println("Clear clicked");
        if (mBound) {
            n1EditText.setText("");
            n2EditText.setText("");
            resEditText.setText("");
            progressBar.setProgress(0);
        }
    }
}

