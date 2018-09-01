package com.example.mlabsystem2.dialerfinal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Pop extends AppCompatActivity  implements View.OnClickListener {
    TextView tv ,tv1;
    String number;

    Button call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        tv=(TextView)findViewById(R.id.tv);
        tv1=(TextView)findViewById(R.id.tv1);
        call=(Button)findViewById(R.id.call);
        tv1.setText(getIntent().getStringExtra("number"));
        number= (String) tv1.getText();
        call.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:" + number));
        startActivity(call);
    }
    @Override
    public  void onBackPressed(){
        Intent intent=new Intent(this,Dialerfinal.class);
        startActivity(intent);
    }
}
