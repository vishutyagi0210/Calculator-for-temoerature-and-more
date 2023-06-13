package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.view.ContentInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class TemperatureConverter extends AppCompatActivity{

    int ActuallValue = 0;
    TextView upperTextView , LowerTextView;
    Toolbar toolbar;
    Button divide , minus , plus , multiply , Ac , backspace , modulo , evaluate;

    boolean touchStillInUpperText , touchStillInLowerText;
    boolean eraseUpperText , eraseLowerText;

    Spinner lowerSpinner , upperSpinner;

    ClickIdentifier click;


    //same relation Reset class contain resetTemperature class which have some methods which
    // helping us through out the programme to reset the variables on time.
    Reset resetParent;
    Reset.ResetTemperature resetTemperature;

    DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_converter);

        setIds();
        setToolBar();
//      in temperature we don't need + , - , and other operations so, I am setting the visibility to be false.
//      yes, we are reusing our digits and function xml.
        setVisibilitys();

        decimalFormat = new DecimalFormat("#.###");


        upperTextView.setOnClickListener(new UpperTextFunctions());
        LowerTextView.setOnClickListener(new LowerTextFunctions());

//I dont want this functionality right now.
//        //setting up spinners.
//        ArrayAdapter<CharSequence> upperSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.tempSpinnerOptions , android.R.layout.simple_spinner_dropdown_item);
//        upperSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        upperSpinner.setAdapter(upperSpinnerAdapter);

//        upperSpinner.setOnItemSelectedListener(new UpperSpinnerFunctionality());
        //lower functionality didn't have this type of functionality.


    }

    public void setVisibilitys(){
        divide.setVisibility(View.INVISIBLE);
        minus.setVisibility(View.INVISIBLE);
        multiply.setVisibility(View.INVISIBLE);
        plus.setVisibility(View.INVISIBLE);
        modulo.setVisibility(View.INVISIBLE);
    }



    //onclick method whcih didig click
    public void onClickDigit(View view){
        vibrate();
        //in this variable I am taking which digit is clicked and then perform actions accordingly
        int value = click.whichDigitClicked(view);

        // if it is -1 that means "." is clicked so, right now I didn't handle this scenario.
        if(value != -1){
            //getting the values from spinner. like which option is selected.
            int lowerSpinnerValue = getLowerSpinnerValue();
            int upperSpinnerValue = getUpperSpinnerValue();

            // if a person try to enter a vlaue when already in actually value contains a negative
            //so, here for appending we use else method.
            if(ActuallValue >= 0)
                ActuallValue = ActuallValue*10+value;
            else
                ActuallValue = ActuallValue*10-value;
            if(touchStillInUpperText){
                if(eraseUpperText){
                    //if user enter into the text and this container contains a ans,
                    // then first we erase it with the help of eraseLowerText variable which is updated every time
                    // when someone click on TextViews.
                    eraseUpperText = false;
                    upperTextView.setText("");
                }
                upperTextView.append(String.valueOf(value));
                if(upperSpinnerValue == 1 && lowerSpinnerValue == 0){
                    setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(CtoF())));
                }
                else if(upperSpinnerValue == 0 && lowerSpinnerValue == 1){
                    setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(FtoC())));
                }
                else{
                    setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(ActuallValue)));
                }
            }
            if(touchStillInLowerText){
                if(eraseLowerText){
                    //if user enter into the text and this container contains a ans,
                    // then first we erase it with the help of eraseLowerText variable which is updated every time
                    // when someone click on TextViews.
                    LowerTextView.setText("");
                    eraseLowerText = false;
                }
                LowerTextView.append(String.valueOf(value));
                if(lowerSpinnerValue == 1 && upperSpinnerValue == 0){
                    setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(CtoF())));
                }
                else if(lowerSpinnerValue == 0 && upperSpinnerValue == 1){
                    setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(FtoC())));
                }
                else{
                    setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(ActuallValue)));
                }
            }
        }else{
            Toast.makeText(this, "this functionality is not activated yet.", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickFunctions(View view){
        vibrate();
        int value = click.whichFunctionalityClicked(view);
        int lowerSpinnerValue = getLowerSpinnerValue();
        int upperSpinnerValue = getUpperSpinnerValue();

        switch (value){
            case 1:
                resetTemperature.resetForNewCalculation();
                break;
            case 2:
                String currentText = upperTextView.getText().toString();
                double updatedValue;
                ActuallValue/=10;
                if(touchStillInUpperText){
                    //first checking if the string is not empty and textview contains something.
                    if (!currentText.isEmpty() && upperTextView.length() > 1) {
                        String updatedText = currentText.substring(0, currentText.length() - 1);
                        //taking the new value from into double variable for evaluating the new ans.
                        updatedValue = Double.parseDouble(updatedText);
                        //setting updated string
                        setTextOnTextView(upperTextView , updatedText);

                        //setting the ans after checking the spinner values.
                        if(upperSpinnerValue == 1 && lowerSpinnerValue == 0){
                            setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(CtoF())));
                        }
                        else if(upperSpinnerValue == 0 && lowerSpinnerValue == 1){
                            setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(FtoC())));
                        }
                        else{
                            setTextOnTextView(LowerTextView , "="+updatedText);
                        }
                    }
                    else{
                        resetTemperature.resetTextViews();
                    }
                }
                else{
                    currentText = LowerTextView.getText().toString();
                    if (!currentText.isEmpty() && LowerTextView.length() > 1) {
                        String updatedText = currentText.substring(0, currentText.length() - 1);
                        updatedValue = Double.parseDouble(updatedText);
                        setTextOnTextView(LowerTextView , updatedText);


                        if(upperSpinnerValue == 1 && lowerSpinnerValue == 0){
                            setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(FtoC())));
                        }
                        else if(upperSpinnerValue == 0 && lowerSpinnerValue == 1){
                            setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(CtoF())));
                        }
                        else{
                            setTextOnTextView(upperTextView , "="+updatedText);
                        }

                    }
                    else{
                        resetTemperature.resetTextViews();
                    }
                }
                break;
            case 3:
                // when someone click on  +/- button so, show - in front of text and evaluating new ans.
                if(touchStillInLowerText){
                    if(lowerSpinnerValue == 1 && upperSpinnerValue == 0){
                        setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(minusCtoF())));
                        setTextOnTextView(LowerTextView , String.valueOf(decimalFormat.format(-ActuallValue)));
                    }else if(lowerSpinnerValue == 0 && upperSpinnerValue == 1){
                        setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(minusFtoC())));
                        setTextOnTextView(LowerTextView , String.valueOf(decimalFormat.format(-ActuallValue)));
                    }else{
                        setTextOnTextView(upperTextView , String.valueOf("="+decimalFormat.format(-ActuallValue)));
                        setTextOnTextView(LowerTextView , String.valueOf(decimalFormat.format(-ActuallValue)));
                    }
                }
                else{
                    if(lowerSpinnerValue == 1 && upperSpinnerValue == 0){
                        setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(minusFtoC())));
                        setTextOnTextView(upperTextView , String.valueOf(decimalFormat.format(-ActuallValue)));

                    }else if(lowerSpinnerValue == 0 && upperSpinnerValue == 1){
                        setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(minusCtoF())));
                        setTextOnTextView(upperTextView , String.valueOf(decimalFormat.format(-ActuallValue)));
                    }else{
                        setTextOnTextView(LowerTextView , String.valueOf("="+decimalFormat.format(-ActuallValue)));
                        setTextOnTextView(upperTextView , String.valueOf(decimalFormat.format(-ActuallValue)));
                    }
                }
                ActuallValue = -ActuallValue;
                break;
        }
    }





    public void setIds(){
        upperTextView = findViewById(R.id.tempCalUpperTextView);
        LowerTextView = findViewById(R.id.tempCalLowerTextView);
        divide = findViewById(R.id.divideButton);
        minus = findViewById(R.id.minusButton);
        plus = findViewById(R.id.plusButton);
        multiply = findViewById(R.id.multiplyButton);
        Ac = findViewById(R.id.ACButon);
        backspace = findViewById(R.id.backSpaceButton);
        modulo = findViewById(R.id.moduloButton);
        evaluate = findViewById(R.id.evaluateButton);
        evaluate.setText("+/-");
        lowerSpinner = findViewById(R.id.lowerSpinner);
        upperSpinner = findViewById(R.id.upperSpinner);
        toolbar = findViewById(R.id.toolBar);
        click = new ClickIdentifier();
        resetParent = new Reset(TemperatureConverter.this);
        resetTemperature = resetParent.new ResetTemperature();
    }

    public void setTextOnTextView(TextView textView , String data){
        textView.setText(data);
    }

    public double FtoC(){
        return ((ActuallValue - 32) * (5.0/9.0));
    }

    public double CtoF(){
        return ((ActuallValue * (9.0/5.0)) + 32);
    }

    public double minusCtoF(){
       return ((-ActuallValue * (9.0/5.0)) + 32);
    }

    public double minusFtoC(){
        return ((-ActuallValue - 32) * (5.0/9.0));
    }



    private class UpperTextFunctions implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            vibrate();
            resetTemperature.resetWhenClickOnUpperText();
        }
    }

    private class LowerTextFunctions implements View.OnClickListener{

        @Override
        public void onClick(View v){
            vibrate();
            resetTemperature.resetWhenClickOnLowerText();
        }
    }

    public int getUpperSpinnerValue(){
        String str = upperSpinner.getSelectedItem().toString();

        if(str.equals("Celsius"))
            return 1;
        else
            return 0;
    }

    public int getLowerSpinnerValue(){
        String str = lowerSpinner.getSelectedItem().toString();

        if(str.equals("Celsius"))
            return 1;
        else
            return 0;
    }



    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.bottom_options , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent inten = null;
        if(item.getItemId() == R.id.calculatorIcon){
            inten = new Intent(this , MainActivity.class);
            finish();
        }
        else if(item.getItemId() == R.id.tempCal){
            inten = new Intent(this , TemperatureConverter.class);
            finish();
        }
        else{
            inten = new Intent(this , MoreCalculators.class);
            finish();
        }
        startActivity(inten);

        return super.onOptionsItemSelected(item);
    }

    public void setToolBar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle("Temperature calculator");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.WHITE);
    }


    private class UpperSpinnerFunctionality implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String currentSelect = parent.getItemAtPosition(position).toString();
            int lowerSpinnerSelected = getLowerSpinnerValue();
            if(currentSelect.equals("Celsius") && lowerSpinnerSelected == 0){
                LowerTextView.setText(String.valueOf((decimalFormat.format((ActuallValue * (9.0/5.0)) + 32))));
            }
            else if(currentSelect.equals("Fahrenheit") && lowerSpinnerSelected == 1){
                LowerTextView.setText(String.valueOf(decimalFormat.format((ActuallValue - 32) * (5.0/9.0))));
            }
            else{
                LowerTextView.setText(String.valueOf(decimalFormat.format(ActuallValue)));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}