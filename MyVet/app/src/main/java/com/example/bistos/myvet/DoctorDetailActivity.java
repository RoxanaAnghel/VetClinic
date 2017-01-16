package com.example.bistos.myvet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * An activity representing a single Doctor detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link DoctorListActivity}.
 */
public class DoctorDetailActivity extends AppCompatActivity {
    private Button editName;
    private TextView nameTextView;
    private Button editType;
    private TextView typeTextView;
    private Realm realm;
    private boolean hasExtras;
    private String oldname;
    private String oldtype;
    private String name;
    private String type;
    private PieChart mChart;
    private String[] animalTypes = {"Dog", "Cat", "Rabbit", "Snake", "Hamster", "Chameleon"};
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myRef.setValue("buna");
        setContentView(R.layout.activity_doctor_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        nameTextView = (TextView) findViewById(R.id.name);
        typeTextView = (TextView) findViewById(R.id.type);
        hasExtras = getIntent().getExtras() != null;
        name = getIntent().getStringExtra(DoctorListActivity.EXTRA_NAME);
        oldname = name;
        type = getIntent().getStringExtra(DoctorListActivity.EXTRA_TYPE);
        oldtype = type;
        if (hasExtras) {
            toolbar.setTitle(name);
            nameTextView.setText(name);
            typeTextView.setText(type);
        }
        setSupportActionBar(toolbar);
        realm = Realm.getInstance(this);


        mChart = (PieChart) findViewById(R.id.pieLayout);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(40f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        setData(animalTypes.length, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(12f);
        editName = (Button) findViewById(R.id.edit_name);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateEditNameDialog(name);
            }
        });

        editType = (Button) findViewById(R.id.edit_type);
        editType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAnimalTypePicker();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(DoctorDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(DoctorDetailFragment.ARG_ITEM_ID));
            DoctorDetailFragment fragment = new DoctorDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.doctor_detail_container, fragment)
                    .commit();
        }
    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> entries = new ArrayList<>();
        int[] everyTypeCountArray = getEveryTypeCount();
        for (int i = 0; i < count; i++) {
            if (everyTypeCountArray[i] > 0) {
                entries.add(new PieEntry((everyTypeCountArray[i] * range), animalTypes[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(10f);
        dataSet.setSelectionShift(10f);

        // add colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        mChart.invalidate();
    }

    private void generateEditNameDialog(String petNameText) {
        final Dialog pickerDialog = new Dialog(DoctorDetailActivity.this, android.R.style.Theme_Holo_Light_Dialog);
        pickerDialog.setTitle("Set pets name");
        pickerDialog.setContentView(R.layout.name_dialog_layout);
        final EditText petNameEditText = (EditText) pickerDialog.findViewById(R.id.pet_name_edit_text);
        if (petNameText != null && !petNameText.isEmpty()) {
            petNameEditText.setText(petNameText);
        }
        Button setButton = (Button) pickerDialog.findViewById(R.id.set);
        Button cancelButton = (Button) pickerDialog.findViewById(R.id.cancel);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = petNameEditText.getText().toString();
                nameTextView.setText(name);
                pickerDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.dismiss(); // dismiss the dialog
            }
        });
        pickerDialog.show();
    }

    public void generateAnimalTypePicker() {
        //create dialog
        final Dialog pickerDialog = new Dialog(DoctorDetailActivity.this, android.R.style.Theme_Holo_Light_Dialog);
        pickerDialog.setTitle("Choose the type of animal");
        pickerDialog.setContentView(R.layout.picker_dialog);
        Button setButton = (Button) pickerDialog.findViewById(R.id.set);
        Button cancelButton = (Button) pickerDialog.findViewById(R.id.cancel);

        //add picker to the dialog
        final NumberPicker picker = (NumberPicker) pickerDialog.findViewById(R.id.numberPicker);
        picker.setMinValue(0);
        picker.setMaxValue(5);
        picker.setDisplayedValues(animalTypes);
        picker.setWrapSelectorWheel(false);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = animalTypes[picker.getValue()];
                typeTextView.setText(type);
                pickerDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.dismiss(); // dismiss the dialog
            }
        });
        pickerDialog.show();
    }

    int[] getEveryTypeCount() {
        int[] myList = new int[10];
        for (int i = 0; i < animalTypes.length; i++) {
            int nr = realm.where(Animal.class).equalTo("type", animalTypes[i]).findAll().size();
            myList[i] = nr;
        }
        return myList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_menu, menu);
        if (!hasExtras) {
            menu.getItem(1).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                NavUtils.navigateUpTo(this, new Intent(this, DoctorListActivity.class));
                break;
            }

            case R.id.menu_item_save: {
                if (!TextUtils.isEmpty(nameTextView.getText().toString()) && !TextUtils.isEmpty(typeTextView.getText().toString())) {
                    Animal animal;
                    //animal exists-> update details, otherwise create new object
                    if (!hasExtras) {
                        realm.beginTransaction();
                        animal = realm.createObject(Animal.class);
                        animal.setName(nameTextView.getText().toString());
                        animal.setType(typeTextView.getText().toString());
                        realm.commitTransaction();
                        finish();
                    } else {
                        realm.beginTransaction();
                        animal = realm.where(Animal.class).equalTo("name", oldname).equalTo("type", oldtype).findFirst();
                        animal.setName(nameTextView.getText().toString());
                        animal.setType(typeTextView.getText().toString());
                        realm.commitTransaction();
                        finish();
                    }
                } else {
                    Toast.makeText(DoctorDetailActivity.this, "One or more fields are empty!", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.menu_item_delete: {
                realm.beginTransaction();
                Animal animal = realm.where(Animal.class).equalTo("name", oldname).equalTo("type", oldtype).findFirst();
                if (animal != null) {
                    animal.removeFromRealm();
                }
                realm.commitTransaction();
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
