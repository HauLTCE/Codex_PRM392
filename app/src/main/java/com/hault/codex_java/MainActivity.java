package com.hault.codex_java;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.hault.codex_java.util.ThemeUtils;
import com.hault.codex_java.ui.world.WorldListFragment;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.applyTheme(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new WorldListFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ThemeUtils.shouldRecreateActivity(this)) {
            recreate();
        }
    }
}