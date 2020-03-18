package com.example.zoudiy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zoudiy.interfaces.SimpleCountDownTimer;
import com.example.zoudiy.utils.Preference;
import com.example.zoudiy.R;
import com.example.zoudiy.utils.RetrofitClient;
import com.example.zoudiy.models.OtpResponse;
import com.goodiebag.pinview.Pinview;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity implements SimpleCountDownTimer.OnCountDownListener {

    EditText MobileNo;
    TextView Didnt;
    Pinview pin6;
    public String mobileNo,getotp;

    private final SimpleCountDownTimer simpleCountDownTimer = new SimpleCountDownTimer(0, 10, 1, this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);

        pin6 = (Pinview) findViewById(R.id.pinview);
        pin6.setTextColor(Color.parseColor("#1E645F"));
        pin6.setPinViewEventListener(new Pinview.PinViewEventListener() {

            public void onDataEntered(Pinview pinview, boolean fromUser) {
                getotp= pinview.getValue();
                if( getotp.length() == 6 )
                {
                    mobileNo =getIntent().getStringExtra("Mobile_no");
                    Call<OtpResponse> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .Verifyotp(mobileNo,getotp);
                    call.enqueue(new Callback<OtpResponse>() {
                        @Override
                        public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                            try {
                                if(response.body().getSuccess()==true)
                                {
                                    OtpResponse otpResponse = response.body();
                                    String token=otpResponse.getData().getToken();
                                    Preference.setAccessToken(Login.this,token);
                                    Intent intent=new Intent(Login.this,Signup.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(Login.this,"Invalid OTP",Toast.LENGTH_LONG).show();
                                    pin6.clearValue();
                                }
                                //Toast.makeText(Login.this, s, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<OtpResponse> call, Throwable t) {
                            Log.d("Failure",t.toString());
                            //Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        Didnt= (TextView)findViewById(R.id.no_otp);
        Didnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        // yourMethod();
//                        simpleCountDownTimer.start(false);
//                    }
//                }, 10000);
                        Toast.makeText(Login.this, "Otp Resend Successfully", Toast.LENGTH_LONG).show();

                        // This method will be executed once the timer is over
                        Intent intent= getIntent();

                        mobileNo = intent.getStringExtra("Mobile_no");
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .Sendotp(mobileNo);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    if(response.isSuccessful()==true) {
                                        String s = response.body().string();
                                        Log.d("Success", s);
                                    }
                                    else{
                                        Toast.makeText(Login.this, "Wait for 10 second", Toast.LENGTH_LONG).show();
                                    }
                                    //Toast.makeText(Login.this, s, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("Failure",t.toString());
                                //Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        finish();
            }
        });


    }

    @Override
    public void onCountDownActive(String time) {
        Didnt.post(() -> Didnt.setText(time));

        Toast.makeText(this, "Seconds = " + simpleCountDownTimer.getSecondsTillCountDown() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCountDownFinished() {
        Didnt.post(() -> {
            Didnt.setText("Otp send again");
        });
    }
}
