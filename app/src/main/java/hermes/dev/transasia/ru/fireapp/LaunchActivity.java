package hermes.dev.transasia.ru.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    private Button rxJava;
    private Button Patterns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initView();

        rxJava.setOnClickListener(v-> startActivity(new Intent(LaunchActivity.this, RxJavaActivity.class)));
        Patterns.setOnClickListener(v-> startActivity(new Intent(LaunchActivity.this, PatternActivity.class)));
    }

    private void initView() {
        rxJava = (Button) findViewById(R.id.rxJava);
        Patterns = (Button) findViewById(R.id.Patterns);
    }
}
