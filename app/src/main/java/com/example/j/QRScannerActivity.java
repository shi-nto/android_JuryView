package com.example.j;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QRScannerActivity extends AppCompatActivity {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://192.168.1.180:3001/juryOption")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("Data", responseData);

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray juryArray = jsonObject.getJSONArray("jury");

                        RadioButton radioButton1 = findViewById(R.id.radio_button1);
                        RadioButton radioButton2 = findViewById(R.id.radio_button2);
                        RadioButton radioButton3 = findViewById(R.id.radio_button3);
                        RadioButton radioButton4 = findViewById(R.id.radio_button4);
                        RadioButton radioButton5 = findViewById(R.id.radio_button5);

                        radioButton1.setText(juryArray.getJSONObject(0).getString("name"));
                        radioButton2.setText(juryArray.getJSONObject(1).getString("name"));
                        radioButton3.setText(juryArray.getJSONObject(2).getString("name"));
                        radioButton4.setText(juryArray.getJSONObject(3).getString("name"));
                        radioButton5.setText(juryArray.getJSONObject(4).getString("name"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button1) {
                    Log.d("RadioButton", "1");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("jury", "0");
                    myEdit.commit();
                } else if (checkedId == R.id.radio_button2) {
                    Log.d("RadioButton", "2");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("jury", "1");
                    myEdit.commit();
                } else if (checkedId == R.id.radio_button3) {
                    Log.d("RadioButton", "3");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("jury", "2");
                    myEdit.commit();
                } else if (checkedId == R.id.radio_button4) {
                    Log.d("RadioButton", "4");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("jury", "3");
                    myEdit.commit();
                } else if (checkedId == R.id.radio_button5) {
                    Log.d("RadioButton", "5");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("jury", "4");
                    myEdit.commit();
                }
            }
        });

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
           @Override
public void barcodeResult(BarcodeResult result) {
    String scanResult = result.getText();
    Log.d("QRScanner", "Scan result: " + scanResult);

    Intent intent = new Intent(QRScannerActivity.this, MainActivity.class);
    intent.putExtra("scanResult", scanResult);

    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
    SharedPreferences.Editor myEdit = sharedPreferences.edit();
    myEdit.putString("token",  scanResult);
    myEdit.commit();
    startActivity(intent);
    finish();
}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}