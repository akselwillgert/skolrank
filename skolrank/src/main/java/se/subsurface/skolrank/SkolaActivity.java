package se.subsurface.skolrank;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import se.subsurface.skolrank.model.Skola;

import static se.subsurface.skolrank.model.Skola.COLUMN_SKOLA_ID;

abstract public class SkolaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "SkolaActivity";

    final Map<String, Integer> colorMap = new HashMap<>();
    LineChart percentageChart;
    private LineChart numStudentsChart;
    private GoogleMap mMap;
    private View chartContainer;
    private int enhetskod;
    private ProgressBar mProgressBar;
    private Button browserButton;
    private int minNumStudentsY = 20;
    private int maxNumStudentsY = 0;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                showMapActivity();
            }
        });
        EventBus.getDefault().register(this);
        chartContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "");
        EventBus.getDefault().post(enhetskod);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setupMap(Skola skola) {
        @SuppressLint("InflateParams") View singleMarker = getLayoutInflater().inflate(R.layout.detailed_marker, null);
        IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());

        ImageView mImageView = (ImageView) singleMarker.findViewById(R.id.kommun_shield);
        TextView nameTV = (TextView) singleMarker.findViewById(R.id.skolnamn);
        mIconGenerator.setContentView(singleMarker);
        GoneTextView avgGradeTV = (GoneTextView) singleMarker.findViewById(R.id.avg_grade);
        GoneTextView foreignTV = (GoneTextView) singleMarker.findViewById(R.id.foreign);
        GoneTextView educatedTV = (GoneTextView) singleMarker.findViewById(R.id.educated_parents);
        GoneTextView girlsTV = (GoneTextView) singleMarker.findViewById(R.id.girls);
        nameTV.setText(SchoolUtils.formatSchoolName(skola.name, 12));
        avgGradeTV.setTextOrGone(skola.betyg + "", "p");
        foreignTV.setTextOrGone(skola.foregin + "", "%");
        educatedTV.setTextOrGone(skola.educatedParents + "", "%");
        mImageView.setImageResource(SchoolUtils.getIconResourceId(skola, this));
        if (skola.girls > 60 || skola.girls < 40) {
            girlsTV.setTextOrGone(skola.girls + "", "%");
        }
        LatLng skolaPosition = new LatLng(skola.xpos, skola.ypos);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(skolaPosition)
                .icon(BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon()));
        Log.e(TAG, "markerOptions=" + markerOptions);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(skolaPosition, 15));

    }

    void styleChart(Chart chart) {
        chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        chart.getLegend().setWordWrapEnabled(true);
    }

    void initCommonUi() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.skolrank_toolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        browserButton = (Button) findViewById(R.id.btn_open_in_browser);

        numStudentsChart = (LineChart) findViewById(R.id.chart_numStudents);
        percentageChart = (LineChart) findViewById(R.id.percentage_chart);
        styleChart(numStudentsChart);
        styleChart(percentageChart);

        mProgressBar = (ProgressBar) this.findViewById(R.id.chart_progress);
        chartContainer = findViewById(R.id.chart_container);
        initColorMap();

        Intent intent = getIntent();
        enhetskod = intent.getIntExtra(COLUMN_SKOLA_ID, 0); //if it's a string you stored.
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    abstract void initColorMap();

    @SuppressWarnings("unused")
    abstract public void onEventBackgroundThread(Integer enhetskod);

    void addEntry(String lineName, int year, double value, LineData data) {
        //    Log.e(TAG, "program=" + program + " year=" + year + " grade=" + grade);
        if (value <= 0)
            return;
        LineDataSet dataSet = data.getDataSetByLabel(lineName, true);
        if (dataSet == null) {
            dataSet = new LineDataSet(new ArrayList<Entry>(), lineName);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            Log.e(TAG, "linename=" + lineName);
            int color = colorMap.get(lineName);

            applyStyle(dataSet, color);
            data.addDataSet(dataSet);
            dataSet.setValueFormatter(new DecimalFormater());
        }
        int xIndex = 0;
        for (String x : data.getXVals()) {
            if (x.equals(year + "")) {
                break;
            }
            xIndex++;
        }

        Entry entry = new Entry((float) value, xIndex);
        dataSet.addEntryOrdered(entry);

    }

    private void applyStyle(LineDataSet set, int color) {
        set.setColor(color);
        set.setCircleColor(color);
        set.setLineWidth(1f);
        set.setCircleSize(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setFillAlpha(65);
        set.setFillColor(color);
        set.setDrawCubic(true);
    }

    void updatePercentageChart(LineData percentageData) {
        //Percentage + grades chart
        YAxis right = percentageChart.getAxis(YAxis.AxisDependency.RIGHT);
        //  right.setAxisMaxValue(100);
        right.setDrawGridLines(false);
        right.setDrawLabels(false);

        YAxis left = percentageChart.getAxis(YAxis.AxisDependency.LEFT);
        left.setAxisMaxValue(100 * 1.1f);
        left.setAxisMinValue(0);
        left.setDrawGridLines(false);
        left.setDrawLabels(false);
        percentageChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        percentageChart.setDescription("");
        percentageChart.setData(percentageData);
        percentageChart.getLegend().setWordWrapEnabled(true);
        percentageChart.invalidate();
    }

    void updateNumStudentsChart(LineData numStudentsData) {

        YAxis numStudentsLeft = numStudentsChart.getAxisLeft();
        numStudentsLeft.setDrawGridLines(false);
        numStudentsLeft.setDrawLabels(false);
        numStudentsLeft.setAxisMinValue(minNumStudentsY);
        numStudentsLeft.setAxisMaxValue(maxNumStudentsY + maxNumStudentsY * 0.2f);

        YAxis numStudentsRight = numStudentsChart.getAxisRight();
        numStudentsRight.setDrawGridLines(false);
        numStudentsRight.setDrawLabels(false);

        numStudentsChart.setData(numStudentsData);
        numStudentsChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        numStudentsChart.setDescription("");
        numStudentsChart.invalidate();
    }

    private void initButton(String name, String kommun) {
        final String browserQuery = name + ", " + kommun;
        assert getSupportActionBar() != null;
        getSupportActionBar().setSubtitle(name);

        //bind the browser button

        browserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, browserQuery);
                if (intent.resolveActivity(SkolaActivity.this.getPackageManager()) != null) {
                    SkolaActivity.this.startActivity(intent);
                }
            }
        });
    }


    void addDefaultEntries(int educated, int immigrant, int numStudents, int girls, LineData percentageData, LineData numStudentsData, int year) {
        if (educated != -1) {
            String lineName = getResources().getString(R.string.educated_parents) + "(%)";
            addEntry(lineName, year, educated, percentageData);
        }

        if (immigrant > 0) {
            String lineName = getResources().getString(R.string.foreign_parents) + "(%)";
            addEntry(lineName, year, immigrant, percentageData);
        }

        if (numStudents > 0) {
            String lineName = getResources().getString(R.string.num_students);
            addEntry(lineName, year, numStudents, numStudentsData);
            if (numStudents < minNumStudentsY)
                minNumStudentsY = numStudents;
            if (numStudents > maxNumStudentsY)
                maxNumStudentsY = numStudents;
        }

        if (girls > 0) {
            String lineName = getResources().getString(R.string.girls) + "(%)";
            addEntry(lineName, year, girls, percentageData);
        }

    }

    void setVisibileIfData(LineChart chart) {
        chart.setVisibility(View.GONE);

        LineData data = chart.getLineData();
        if (data != null && data.getDataSets() != null && !data.getDataSets().isEmpty()) {
            chart.setVisibility(View.VISIBLE);
        }
    }

    private void showMapActivity() {
        Intent myIntent = new Intent(this, MapsActivity.class);

        myIntent.putExtra(SchoolUtils.MAP_POSITION, mMap.getCameraPosition());
        //myIntent.putExtra(COLUMN_SKOLA_ID, enhetskod); //Optional parameters
        this.startActivity(myIntent);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(Skola skola) {
        Log.e(TAG, "onEventMainThread skola.year=" + skola.year);
        chartContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        setVisibileIfData(percentageChart);
        setVisibileIfData(numStudentsChart);
        setupMap(skola);
        initButton(skola.name, skola.kommun);

    }

    private class DecimalFormater implements ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return new BigDecimal(Float.toString(value)).stripTrailingZeros().toPlainString();

        }
    }
}
