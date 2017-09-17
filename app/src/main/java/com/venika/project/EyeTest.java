package com.venika.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EyeTest extends AppCompatActivity {
    private TextView mTestText;
    private Button mOkay, mNotOkay;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_test);

        mTestText = (TextView) findViewById(R.id.testText);

        mOkay = (Button) findViewById(R.id.okayBtn);
        mNotOkay = (Button) findViewById(R.id.notokayBtn);

        mOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter == 0 ){
                    mTestText.setTextSize(getResources().getDimension(R.dimen.textsize1));
                }
                else if(counter == 1){
                    mTestText.setTextSize(getResources().getDimension(R.dimen.textsize2));
                }
                else if(counter == 2){
                    mTestText.setTextSize(getResources().getDimension(R.dimen.textsize3));
                }
                else if(counter == 3){
                    mTestText.setTextSize(getResources().getDimension(R.dimen.textsize4));
                }
                else if(counter >= 4){
                    mTestText.setTextSize(getResources().getDimension(R.dimen.textsize5));
                    Toast.makeText(EyeTest.this, "Eyes are okay :)", Toast.LENGTH_SHORT).show();
                }
                counter++;
            }
        });

        mNotOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter < 4){
                    Toast.makeText(EyeTest.this, "Eyes are not okay :(", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EyeTest.this, "Eyes are okay :)", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
