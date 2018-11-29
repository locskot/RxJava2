package hermes.dev.transasia.ru.fireapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class LaunchActivity extends AppCompatActivity {

    private Button rxJava;
    private Button Patterns;
    private Button dialogs;

    private static final String TAG = "tag__";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initView();
        SwitchCompat switchNightMode = findViewById(R.id.switch_night_mode);
        rxJava.setOnClickListener(v -> startActivity(new Intent(LaunchActivity.this, RxJavaActivity.class)));
        Patterns.setOnClickListener(v -> startActivity(new Intent(LaunchActivity.this, PatternActivity.class)));
        dialogs.setOnClickListener(v -> {
            FabDialog fabDialog = new FabDialog();
            fabDialog.showNow(getSupportFragmentManager(), "rag");
//            startActivity(new Intent(LaunchActivity.this, OpacityActivity.class));
        });





//        rxJava.setOnClickListener(v -> switchNightMode.setChecked(false));
//        Patterns.setOnClickListener(v -> switchNightMode.setChecked(true));


        int[][] states = new int[][]{
//                new int[] { android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[]{android.R.attr.state_checked}, new int[]{-android.R.attr.state_checked},
//                new int[] { android.R.attr.state_pressed},
//                new int[] {-android.R.attr.state_pressed},
//                new int[] { android.R.attr.state_activated},
//                new int[] {-android.R.attr.state_activated},

        };

        int[] colors = new int[]{getResources().getColor(R.color.transasia_primary), getResources().getColor(R.color.colorAccent),
//                getResources().getColor(R.color.transasia_primary),
//                getResources().getColor(R.color.maps_card_view_background),
//                getResources().getColor(R.color.transasia_primary),
//                getResources().getColor(R.color.maps_card_view_background),
//                getResources().getColor(R.color.transasia_primary),
//                getResources().getColor(R.color.maps_card_view_background),
        };

        switchNightMode.setTrackTintList(new ColorStateList(states, colors));


        Log.d(TAG, "onCreate: ");
    }

    private void initView() {
        rxJava = (Button) findViewById(R.id.rxJava);
        Patterns = (Button) findViewById(R.id.Patterns);
        dialogs = (Button) findViewById(R.id.dialog);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
