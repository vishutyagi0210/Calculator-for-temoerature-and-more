package com.example.calculator;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

public class Reset{
    private MainActivity mainActivity;
    private TemperatureConverter temperatureConverter;
    public Reset(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    public Reset(TemperatureConverter temperatureConverter){
        this.temperatureConverter = temperatureConverter;
    }


    public class ResetCalculator{
        public void resetCalculatorForNewCalculation(){
            mainActivity.ActuallValue = 0.0;
            mainActivity.previousActuall = 0.0;
            mainActivity.secondValueEntering = false;
            mainActivity.firstValueEntering = true;
            mainActivity.operationType = -1;
            mainActivity.previousValue = 0.0;
        }
        public void resetCalculatorAfterEnteringNewOperator(int value){
            mainActivity.ActuallValue = 0.0;
            mainActivity.previousActuall = 0.0;
            mainActivity.secondValueEntering = true;
            mainActivity.firstValueEntering = false;
            mainActivity.operationType = value;
        }
    }

    public class ResetTemperature{

        public void resetWhenClickOnLowerText(){
            temperatureConverter.LowerTextView.setTextColor(Color.BLACK);
            temperatureConverter.upperTextView.setTextColor(Color.GRAY);
            temperatureConverter.LowerTextView.setTextSize(40);
            temperatureConverter.upperTextView.setTextSize(30);
            temperatureConverter.touchStillInLowerText = true;
            temperatureConverter.touchStillInUpperText = false;
            temperatureConverter.ActuallValue = 0;
            temperatureConverter.eraseLowerText = true;
        }
        public void resetWhenClickOnUpperText(){
            temperatureConverter.upperTextView.setTextColor(Color.BLACK);
            temperatureConverter.LowerTextView.setTextColor(Color.GRAY);
            temperatureConverter.upperTextView.setTextSize(40);
            temperatureConverter.LowerTextView.setTextSize(30);
            temperatureConverter.touchStillInUpperText = true;
            temperatureConverter.touchStillInLowerText = false;
            temperatureConverter.ActuallValue = 0;
            temperatureConverter.eraseUpperText = true;
        }
        public void resetForNewCalculation(){
            temperatureConverter.eraseUpperText = false;
            temperatureConverter.eraseLowerText = false;
            temperatureConverter.touchStillInLowerText = false;
            temperatureConverter.touchStillInUpperText = false;
            temperatureConverter.upperTextView.setText("0");
            temperatureConverter.LowerTextView.setText("0");
        }
        public void resetTextViews(){
            temperatureConverter.upperTextView.setText("0");
            temperatureConverter.LowerTextView.setText("0");
        }
    }
}
