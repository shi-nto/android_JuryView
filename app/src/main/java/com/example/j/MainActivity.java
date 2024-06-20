package com.example.j;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.launchdarkly.eventsource.MessageEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SSEHandler {

    private YourViewModel viewModel;
    private TextView textView;
    private TextView textView1;
    private ImageView image;


    private void sendVote(int candidateNumber, int juryNumber) {
        OkHttpClient client = new OkHttpClient();

        // Create the request body with the candidate number and jury number
        RequestBody requestBody = new FormBody.Builder()
                .add("data", String.valueOf(candidateNumber))
                .add("jury", String.valueOf(juryNumber))
                .build();

        // Create the POST request
        Request request = new Request.Builder()
                .url("http://192.168.1.180:3001/face_vote")
                .post(requestBody)
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle the response
                }
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences sharedPreferences9 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String juryu = sharedPreferences9.getString("jury", "");
        Log.e("jury", juryu);

        int juryNumber = Integer.parseInt(juryu);


        ImageView image1 = findViewById(R.id.image1);
        ImageView image2 = findViewById(R.id.image2);
        image1.setOnClickListener(v -> {
            // Candidate 1 is selected
            sendVote(1, juryNumber);
        });

        image2.setOnClickListener(v -> {
            // Candidate 2 is selected
            sendVote(2, juryNumber);
        });
        // Initialize TextView
   //     textView = findViewById(R.id.textView); // Replace with your TextView id


        Button button = findViewById(R.id.scan_button);
        button.setOnClickListener(v -> {
            Log.e("hello", "Clicked With Succes");
            Intent intent = new Intent(MainActivity.this, QRScannerActivity.class);
            startActivity(intent);
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        // To retrieve values:
        SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String nameTokn = sharedPreferences1.getString("token", "");
        Log.e("token", nameTokn);

        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String nameTokn1 = sharedPreferences1.getString("jury", "");
        Log.e("Jury", nameTokn1);
        // Initialize SSEClient
        SSEClient sseClient = new SSEClient(nameTokn,  nameTokn1);
        sseClient.initSse(this,"ybfieiozfffdytnv6og", Throwable::printStackTrace);

        // Initialize ViewModel with factory
        YourViewModelFactory factory = new YourViewModelFactory(sseClient);
        viewModel = new ViewModelProvider(this, factory).get(YourViewModel.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.disconnect();
    }

    @Override
    public void onSSEConnectionOpened() {
        // Handle SSE connection opened event
    }

    @Override
    public void onSSEConnectionClosed() {
        // Handle SSE connection closed event
    }
@Override
public void onSSEEventReceived(String event, MessageEvent messageEvent) {
    runOnUiThread(() -> {
        try {


            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String jury = sharedPreferences.getString("jury", "");
            Log.e("Hello", jury);

            // Parse the JSON data
            JSONObject jsonObject = new JSONObject(messageEvent.getData());
            Log.e("json", String.valueOf(jsonObject));

            // Get the candidate details
            JSONObject candidat = jsonObject.getJSONObject("candidat");
            int id = candidat.getInt("id");
            int badge = candidat.getInt("badge");
            String name = candidat.getString("name");
            String chanson = candidat.getString("chanson");
            String image = candidat.getString("image");
            int note = candidat.getInt("note");
            String sexe = candidat.getString("sexe");
            if(sexe.equals("m"))
            {
                ImageView imag = findViewById(R.id.image1);
                imag.setImageResource(R.drawable.male);
            }else{
                ImageView imag = findViewById(R.id.image1);
                imag.setImageResource(R.drawable.female);
            }

            textView = findViewById(R.id.badge1);
            textView.setText(String.valueOf(badge));
            textView = findViewById(R.id.name1);
            textView.setText(name);
            textView = findViewById(R.id.chanson1);
            textView.setText(chanson);

            // Get the 'user' object
            JSONObject userObject = jsonObject.getJSONObject("user");
            // Get the 'name' string from the 'user' object
            String userme = userObject.getString("name");


            textView = findViewById(R.id.jury);
            textView.setText( "Salut! " + userme);
            Log.e("user", userme);


            // the candidat1
            JSONObject candidat1 = jsonObject.getJSONObject("candidat1");
            int id1 = candidat1.getInt("id");
            int badge1 = candidat1.getInt("badge");
            String name1 = candidat1.getString("name");
            String chanson1 = candidat1.getString("chanson");
            String image1 = candidat1.getString("image");
            int note1 = candidat1.getInt("note");
            String sexe1 = candidat1.getString("sexe");
            if(sexe1.equals("m"))
            {
                ImageView imag1 = findViewById(R.id.image2);
                imag1.setImageResource(R.drawable.male);
            }else {
                ImageView imag1 = findViewById(R.id.image2);
                imag1.setImageResource(R.drawable.female);
            }



            textView1 = findViewById(R.id.badge2);
            textView1.setText(String.valueOf(badge1));
            textView1 = findViewById(R.id.name2);
            textView1.setText(name1);
            textView1 = findViewById(R.id.chanson2);
            textView1.setText(chanson1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });
}


    @Override
    public void onSSEError(Throwable t) {
        // Handle SSE error with throwable
    }


}