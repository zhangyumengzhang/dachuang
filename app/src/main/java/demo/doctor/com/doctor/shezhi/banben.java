package demo.doctor.com.doctor.shezhi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import demo.doctor.com.doctor.R;
import demo.doctor.com.doctor.shouye.setFragment;

public class banben extends AppCompatActivity {
    TextView queding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banben);
        queding=findViewById(R.id.tv_queding);
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(banben.this, setFragment.class);
                startActivity(i);
            }
        });
    }
}
