package hermes.dev.transasia.ru.fireapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    private Button rxJava;
    private Button Patterns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initView();
        SwitchCompat switchNightMode = (SwitchCompat) findViewById(R.id.switch_night_mode);
        rxJava.setOnClickListener(v-> startActivity(new Intent(LaunchActivity.this, RxJavaActivity.class)));
        Patterns.setOnClickListener(v-> startActivity(new Intent(LaunchActivity.this, PatternActivity.class)));

//        rxJava.setOnClickListener(v -> switchNightMode.setChecked(false));
//        Patterns.setOnClickListener(v -> switchNightMode.setChecked(true));



        int[][] states = new int[][] {
//                new int[] { android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] { android.R.attr.state_checked},
                new int[] {-android.R.attr.state_checked},
//                new int[] { android.R.attr.state_pressed},
//                new int[] {-android.R.attr.state_pressed},
//                new int[] { android.R.attr.state_activated},
//                new int[] {-android.R.attr.state_activated},

        };

        int[] colors = new int[] {
                getResources().getColor(R.color.transasia_primary),
                getResources().getColor(R.color.colorAccent),
//                getResources().getColor(R.color.transasia_primary),
//                getResources().getColor(R.color.maps_card_view_background),
//                getResources().getColor(R.color.transasia_primary),
//                getResources().getColor(R.color.maps_card_view_background),
//                getResources().getColor(R.color.transasia_primary),
//                getResources().getColor(R.color.maps_card_view_background),
        };

        switchNightMode.setTrackTintList(new ColorStateList(states, colors));
    }

    private void initView() {
        rxJava = (Button) findViewById(R.id.rxJava);
        Patterns = (Button) findViewById(R.id.Patterns);
    }
}
