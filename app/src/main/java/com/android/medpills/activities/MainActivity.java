package com.android.medpills.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.medpills.Model.PillAlarm;
import com.android.medpills.Model.PillsContainer;
import com.android.medpills.R;
import com.android.medpills.adapters.MyMedcineRecyclerViewAdapter;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView_todaysDay;
    private RecyclerView recyclerView_Pills;
    private MyMedcineRecyclerViewAdapter mRecyclerAdapter;
    private LinearLayout linearLayout_noPills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MedPills");
        setUpViews();
        bindData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setUpViews() {
        textView_todaysDay=(TextView) findViewById(R.id.textView_todaysDay);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String todayString = new SimpleDateFormat("EEE, MMM d").format(today);
        textView_todaysDay.setText(todayString);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToAddActivity();
            }
        });

        linearLayout_noPills=(LinearLayout) findViewById(R.id.linearLayout_noPillsYet);
        recyclerView_Pills=(RecyclerView)findViewById(R.id.recyclerView_pills);
        recyclerView_Pills.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerAdapter = new MyMedcineRecyclerViewAdapter();
        recyclerView_Pills.setAdapter(mRecyclerAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            redirectToAllMyPillsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void redirectToAddActivity() {
        Intent intent = new Intent(this, AddPillActivity.class);
        startActivity(intent);
        finish();
    }


    public void redirectToAllMyPillsActivity() {
        Intent intent = new Intent(this, AllMyPillsActivity.class);
        startActivity(intent);
        finish();
    }
    private void bindData() {
        PillsContainer pillsContainer = new PillsContainer();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        List<PillAlarm> pillAlarms = Collections.emptyList();

        try {
            pillAlarms = pillsContainer.gellAllTheAlarms(this, day);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(pillAlarms.size() != 0) {
            linearLayout_noPills.setVisibility(View.GONE);
            mRecyclerAdapter.setItems(pillAlarms);
            mRecyclerAdapter.notifyDataSetChanged();
        } else
        {
            linearLayout_noPills.setVisibility(View.VISIBLE);
        }
    }
}
