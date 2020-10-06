package com.MaJnr.ruffier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.MaJnr.testruffier.R;

public class FirstUseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_viewpager);

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonDone) {
            this.finish();
        } else if (view.getId() == R.id.moreInfo) {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            startActivity(intent);
        }
    }
}
