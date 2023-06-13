package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

//  TextViews for showing data
    protected TextView historyDataView , currentDataView , dynamicDataView;
    protected Button Ac;
    //tracks a user enter second value after entering a operator
    protected boolean secondValueEntering;
    // this variables tracks that A user enters a fresh input.
    protected boolean firstValueEntering = true;
    protected int operationType;
    //the variable "ActualValue" is used to obtain the final number by taking a single input number at a time.
    // The formula used is (ActualValue * 10 + userEnteredNumber), where userEnteredNumber represents the number
    // entered by the user. By applying this formula iteratively, the final number is obtained.
    public double ActuallValue = 0.0;
    // The variable "previousActual" is used to update the value based on user input.
    // For example, if the user has already entered 10 and then presses the "+" button, and subsequently enters 1,
    // the displayed value will automatically evaluate to 11. However, if the user then enters 2 after the previous 1,
    // the total will be 23, which is incorrect. To address this,
    // we use the variable "previousActual" to keep track of the previous value of "actual".
    // This allows us to reverse the calculation and subtract 1 from the previous value before adding 12. By doing this,
    // we obtain the correct result of 22.

    protected double previousActuall = 0.0;

//    in this variable I am taking the updated calculation from dynamicView TextView.
    protected double previousValue = 0.0;

    //  ClickedIdentifier Class methods reference variables.
    //  this class giving us which of the digit or operator or funciton clicked.
    //  self created class for simply the code.
    ClickIdentifier click;

    Toolbar toolbar;


    //I have created a parent class called "Reset" that serves as the base class for two child classes: "CalculatorReset" and "TemperatureReset". These inner classes are designed to assist in resetting variables within specific classes.
    //
    //In order to achieve this, I have declared two reference variables.
    // The first variable is of type "Calculator"
    // and it helps me reset the variables and reduce the amount of code within a single file.
    // The second variable is the parent class variable, which allows me to initialize the child variables as needed.
    //go to setIds() method for see the initialization.
    Reset resetParent;
    Reset.ResetCalculator resetCalculator;

    DecimalFormat decimalFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setIds();
        setToolbar();

        decimalFormat = new DecimalFormat("#.###");
    }



    //on click method which will execute when someone click on the digits/Buttons of the ui.
    public void onClickDigit(View view){
        vibrate();
        int value = click.whichDigitClicked(view);
//      checking if the user enterd an valid button... because I didn't write functionality for Real constants. or "." button
        if(value != -1){
            if(firstValueEntering){
                Ac.setText("C");
                currentDataView.append(String.valueOf(value));
                dynamicDataView.append(String.valueOf(value));
            }
            else if(secondValueEntering){
                previousActuall = ActuallValue;
                ActuallValue = ActuallValue*10 + value;
                previousValue = Double.parseDouble(dynamicDataView.getText().toString());
                currentDataView.append(String.valueOf(value));

                switch (operationType){
                    case 1:
                        dynamicDataView.setText(String.valueOf(decimalFormat.format((previousValue-previousActuall)+ActuallValue)));
                        break;

                    case 2:
                        dynamicDataView.setText(String.valueOf(decimalFormat.format((previousValue+previousActuall)-ActuallValue)));
                        break;
                    case 3:
                        if(previousActuall == 0)
                            dynamicDataView.setText(String.valueOf(decimalFormat.format(previousValue*ActuallValue)));
                        else
                            dynamicDataView.setText(String.valueOf(decimalFormat.format((previousValue/previousActuall)*ActuallValue)));
                        break;
                    case 4:
//                  if user enter 0
                        if(value == 0){
                            Toast.makeText(this, "divisibility by 0 is not possible", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(previousActuall == 0)
                                dynamicDataView.setText(String.valueOf(decimalFormat.format(previousValue/ActuallValue)));
                            else
                                dynamicDataView.setText(String.valueOf(decimalFormat.format((previousValue * previousActuall) / ActuallValue)));
                        }
                        break;
                }
            }
        }
        else{
            Toast.makeText(this, "Sorry but I didn't write the functionality for \".\" right now", Toast.LENGTH_SHORT).show();
        }

    }

    // on click method will call when someone click on the functionalities like backspace , Ac , =
    public void onClickFunctions(View view){
        vibrate();
        int value = click.whichFunctionalityClicked(view);

        switch (value){
            case 1:
                Ac.setText("AC");
                resetCalculator.resetCalculatorForNewCalculation();
                if(currentDataView.getText().toString().equals("")){
                    historyDataView.setText("");
                }else{
                    currentDataView.setText("");
                    dynamicDataView.setText("");
                }
                break;
            case 2:
                break;
            case 3:
                if(!currentDataView.getText().toString().equals("") && !dynamicDataView.getText().toString().equals("")){
                    historyDataView.append("\n"+currentDataView.getText().toString()+"\n"+"="+dynamicDataView.getText().toString());
                    currentDataView.setText("");
                    dynamicDataView.setText("");
                    resetCalculator.resetCalculatorForNewCalculation();
                }
        }
    }

    // on click method when someone click on + , - , * , % , /
    public void onClickOperations(View view){
        vibrate();
        int value = click.whichOperationClicked(view);
        switch (value){
            case 1:
                currentDataView.append("+");
                break;
            case 2:
                currentDataView.append("-");
                break;
            case 3:
                currentDataView.append("*");
                break;
            case 4:
                currentDataView.append("/");
                break;
            case 5:
                previousValue = Double.parseDouble(dynamicDataView.getText().toString());
                currentDataView.setText(String.valueOf(decimalFormat.format(previousValue/100.0)));
                dynamicDataView.setText(String.valueOf(decimalFormat.format(previousValue/100.0)));
                return;
        }

        resetCalculator.resetCalculatorAfterEnteringNewOperator(value);
    }

    private void setIds(){
        historyDataView  = findViewById(R.id.historyTextView);
        currentDataView = findViewById(R.id.currentView);
        dynamicDataView = findViewById(R.id.dynamicView);
        Ac = findViewById(R.id.ACButon);

        //scroll bar effect
        historyDataView.setMovementMethod(new ScrollingMovementMethod());
        //class which is giving us button number.
        click = new ClickIdentifier();
        toolbar = findViewById(R.id.toolBar);
        //reset class which have methods to reset variables and classes when need.
        resetParent = new Reset(MainActivity.this);
        resetCalculator = resetParent.new ResetCalculator();
    }






//  section for toolbar edits and other things like: changing activities and vibration

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.bottom_options , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent inten = null;
        switch (item.getItemId()) {
            case R.id.calculatorIcon:
                inten = new Intent(this, MainActivity.class);
                finish();
                break;
            case R.id.tempCal:
                inten = new Intent(this, TemperatureConverter.class);
                finish();
                break;
            case R.id.more:
                inten = new Intent(this , MoreCalculators.class);
                finish();
                break;
        }
        startActivity(inten);

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setTitle("Calculator");
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }
}