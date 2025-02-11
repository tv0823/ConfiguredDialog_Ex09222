package com.example.configureddialog_ex09222;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AlertDialog.Builder abd;

    Button geometricalBtn, mathematicalBtn;
    EditText FirstNumberEt, numDEt;
    int math;

    LinearLayout myDialog;

    ListView firstTwentyLv;
    TextView firstNumTv, numDTv, placeTv, sumToNumTV;
    double firstNum, numD, seqSum;
    String[] seqArr;
    double[] sumValuesArr;

    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTwentyLv = findViewById(R.id.firstTwentyLv);
        firstNumTv = findViewById(R.id.firstNumTv);
        numDTv = findViewById(R.id.numD);
        placeTv = findViewById(R.id.placeTv);
        sumToNumTV = findViewById(R.id.sumToNumTV);

        seqSum = 0;
        seqArr = new String[20];
        sumValuesArr = new double[20];

        adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, seqArr);
    }

    public void DataEnter(View view) {
        myDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_get_values, null);
        geometricalBtn = myDialog.findViewById(R.id.geometricalBtn);
        mathematicalBtn = myDialog.findViewById(R.id.mathematicalBtn);

        FirstNumberEt = myDialog.findViewById(R.id.FirstNumberEt);
        numDEt = myDialog.findViewById(R.id.numDEt);

        abd = new AlertDialog.Builder(this);

        abd.setView(myDialog);
        abd.setTitle("Information");
        abd.setMessage("Please enter sequence information:");
        abd.setPositiveButton("Finish", myClick);
        abd.setNegativeButton("Cancel", myClick);
        abd.setNeutralButton("Reset", myClick);

        abd.setCancelable(false);

        AlertDialog ad = abd.create();
        ad.show();
    }

    public void resetItems()
    {
        FirstNumberEt.setText("");
        numDEt.setText("");

        FirstNumberEt.setVisibility(View.VISIBLE);
        numDEt.setVisibility(View.VISIBLE);
    }

    public String bigNumSimplifier(double value){
        String scientificNotation = String.format("%.4e", value);
        String[] parts = scientificNotation.split("e");
        double base = Double.parseDouble(parts[0]) / 10.0;
        int exponent = Integer.parseInt(parts[1]) + 1;

        return String.format("%.4f * 10^%d", base, exponent);
    }

    public void calcArrValues() {
        seqSum = 0;
        for (int i = 0; i < 20; i++) {
            double currentValue;

            if (math == 1) {
                currentValue = firstNum + i * numD;
            } else {
                currentValue = firstNum * Math.pow(numD, i);
            }

            if ((currentValue > 1000000) || (currentValue < -1000000)) {
                seqArr[i] = bigNumSimplifier(currentValue);
            } else if(currentValue > -1 && currentValue < 1) {
                seqArr[i] = currentValue + "";
            } else{
                seqArr[i] = String.format("%.2f", currentValue);
            }

            seqSum += currentValue;
            sumValuesArr[i] = seqSum;
        }
    }

    DialogInterface.OnClickListener myClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                String first = FirstNumberEt.getText().toString();
                String strNumD = numDEt.getText().toString();

                if(first.isEmpty() || strNumD.isEmpty()) {
                    Toast.makeText(MainActivity.this, "One or more input is empty", Toast.LENGTH_SHORT).show();
                } else if ((first.equals("+.") || first.equals("+") || first.equals("-.") || first.equals("-") || first.equals(".")) || (strNumD.equals("+.") || strNumD.equals("+") || strNumD.equals("-.") || strNumD.equals("-") || strNumD.equals("."))) {
                    Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                } else if((math == 0) && (first.equals("0"))){
                    Toast.makeText(MainActivity.this, "Geometrical seq can't start with 0", Toast.LENGTH_SHORT).show();
                } else if(strNumD.equals("0")){
                    Toast.makeText(MainActivity.this, "multiplier and difference can't be 0", Toast.LENGTH_SHORT).show();
                }
                else {
                    firstTwentyLv.setVisibility(View.VISIBLE);
                    firstNumTv.setVisibility(View.VISIBLE);
                    numDTv.setVisibility(View.VISIBLE);

                    firstNum = Float.parseFloat(first);
                    numD = Float.parseFloat(strNumD);

                    calcArrValues();

                    firstTwentyLv.setOnItemClickListener(MainActivity.this);
                    firstTwentyLv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                    firstTwentyLv.setAdapter(adp);

                    firstNumTv.setText("X1 = " + String.format("%.2f", firstNum));
                    numDTv.setText("d = " + String.format("%.2f", numD));

                    placeTv.setText("");
                    sumToNumTV.setText("");
                }
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            } else if (which == DialogInterface.BUTTON_NEUTRAL) {
                firstTwentyLv.setVisibility(View.INVISIBLE);
                firstNumTv.setVisibility(View.INVISIBLE);
                numDTv.setVisibility(View.INVISIBLE);
                placeTv.setVisibility(View.INVISIBLE);
                sumToNumTV.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        firstNumTv.setVisibility(View.VISIBLE);
        numDTv.setVisibility(View.VISIBLE);
        placeTv.setVisibility(View.VISIBLE);
        sumToNumTV.setVisibility(View.VISIBLE);

        placeTv.setText("n = " + (pos+1));

        if ((sumValuesArr[pos] > 1000000) || (sumValuesArr[pos] < -1000000)) {
            sumToNumTV.setText("Sn: " + bigNumSimplifier(sumValuesArr[pos]));
        } else if(sumValuesArr[pos] > -1 && sumValuesArr[pos] < 1) {
            sumToNumTV.setText("Sn: " + sumValuesArr[pos]);
        } else{
            sumToNumTV.setText("Sn: " + String.format("%.2f", sumValuesArr[pos]));
        }
    }

    public void mathematical(View view) {
        math = 1;
        numDEt.setHint("Enter difference");
        resetItems();
    }

    public void geometrical(View view) {
        math = 0;
        numDEt.setHint("Enter multiplier");
        resetItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menuCred) {
            Intent si = new Intent(this, CreditsActivity.class);
            startActivity(si);
        }

        return true;
    }
}