package com.example.calculator;

import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ClickIdentifier{
    public int whichDigitClicked(View view){
        Button btn = (Button) view;

        switch(btn.getId()){
            case R.id.zeroButton:
                return 0;
            case R.id.oneButton:
                return 1;
            case R.id.twoButton:
                return 2;
            case R.id.threeButton:
                return 3;
            case R.id.fourButton:
                return 4;
            case R.id.fiveButton:
                return 5;
            case R.id.sixButton:
                return 6;
            case R.id.sevenButton:
                return 7;
            case R.id.eightButton:
                return 8;
            case R.id.nine:
                return 9;

        }
        return -1;
    }

    public int whichOperationClicked(View view){
        Button btn = (Button) view;

        switch (btn.getId()){
            case R.id.plusButton:
                return 1;
            case R.id.minusButton:
                return 2;
            case R.id.multiplyButton:
                return 3;
            case R.id.divideButton:
                return 4;
            case R.id.moduloButton:
                return 5;
            default:
                return -1;
        }
    }

    public int whichFunctionalityClicked(View view){
        Button btn = (Button) view;

        switch (btn.getId()){
            case R.id.ACButon:
                return 1;
            case R.id.backSpaceButton:
                return 2;
            case R.id.evaluateButton:
                return 3;
            default:
                return -1;
        }
    }
}
