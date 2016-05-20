package com.example.andysetiawan.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String url, status;
    Button btnScanCode, btnFindProduct;
    EditText edtProductCode;
    boolean doubleBackToExitPressedOnce = false;
    String ProductCode;
    RequestQueue requestQueue;
    private TextView statusMessage;
    private TextView barcodeValue;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize
        btnScanCode = (Button) findViewById(R.id.btnScanCode);
        btnFindProduct = (Button) findViewById(R.id.btnFindProduct);
        edtProductCode = (EditText) findViewById(R.id.edtProductCode);

        //Action btnScanCode
        btnScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus,1);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                MainActivity.this.finish();
            }
        });

        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        //Action btnFindProduct
        btnFindProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "https://www.eannovate.com/api/api_intern.php?action=get_product_data&barcode=" + edtProductCode.getText().toString();

                JsonObjectRequest product = new JsonObjectRequest(Request.Method.GET, url,
                        // The third parameter Listener overrides the method onResponse() and passes
                        //JSONObject as a parameter
                        new Response.Listener<JSONObject>() {

                            // Takes the response from the JSON request
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    status = response.getString("status");
                                    ProductCode = edtProductCode.getText().toString();


                                        if (Integer.parseInt(status)==200){
                                            Toast.makeText(MainActivity.this,"Product has been Found", Toast.LENGTH_SHORT).show();
                                            Intent toProductDetail = new Intent(MainActivity.this, ProductDetail.class);
                                            toProductDetail.putExtra("ProductCode", ProductCode);
                                            startActivity(toProductDetail);

                                            MainActivity.this.finish();

                                        }
                                        else if (Integer.parseInt(status)==404){
                                            Toast.makeText(MainActivity.this,"Product not Found", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
                                        }

                                }
                                // Try and catch are included to handle any errors due to JSON
                                catch (JSONException e) {
                                    // If an error occurs, this prints the error to the log
                                    e.printStackTrace();
                                }
                            }
                        },
                        // The final parameter overrides the method onErrorResponse() and passes VolleyError
                        //as a parameter
                        new Response.ErrorListener() {
                            @Override
                            // Handles errors that occur due to Volley
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", "Error");
                            }
                        }
                );
                requestQueue.add(product);


            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

}


