package com.example.andysetiawan.qrcodescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.net.URL;

public class ProductDetail extends AppCompatActivity {


    boolean doubleBackToExitPressedOnce = false;

    TextView txtProductName, txtQty, txtDescription;
    ImageView imgProduct;
    Button btnBackProduct;
    String ProductCode;
    RequestQueue requestQueue;
    String url;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Initialize
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtQty = (TextView) findViewById(R.id.txtQty);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        btnBackProduct = (Button) findViewById(R.id.btnBackProductDetail);


        Intent toProductDetail = getIntent();
        Bundle b = toProductDetail.getExtras();
        ProductCode = getIntent().getExtras().getString("ProductCode");

        url = "https://www.eannovate.com/api/api_intern.php?action=get_product_data&barcode="+ProductCode;

        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest product = new JsonObjectRequest(Request.Method.GET, url,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            //the response JSON Object
                            //and converts them into javascript objects
                            String title = data.getString("title");
                            String desc = data.getString("desc");
                            String qty = data.getString("qty");
                            final String image = data.getString("image");

                            txtProductName.setText(title);
                            txtDescription.setText(desc);
                            txtQty.setText(qty);

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    try {
                                        InputStream in = new URL(image).openStream();
                                        bmp = BitmapFactory.decodeStream(in);
                                    } catch (Exception e) {
                                        // log error
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    if (bmp != null)
                                        imgProduct.setImageBitmap(bmp);
                                }

                            }.execute();

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




        btnBackProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backMainMenu = new Intent(ProductDetail.this, MainActivity.class);
                startActivity(backMainMenu);

                ProductDetail.this.finish();
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


}
