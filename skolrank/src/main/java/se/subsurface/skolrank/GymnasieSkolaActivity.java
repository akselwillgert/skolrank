package se.subsurface.skolrank;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import de.greenrobot.event.EventBus;
import se.subsurface.skolrank.model.Skola;

import static se.subsurface.skolrank.model.Skola.COLUMN_LASAR;
import static se.subsurface.skolrank.model.Skola.COLUMN_gbp_for_elever_med_examen;
import static se.subsurface.skolrank.model.Skola.COLUMN_program;

public class GymnasieSkolaActivity extends SkolaActivity {
    //  private static final String TAG = GymnasieSkolaActivity.class.getName();
    private LineChart gradesChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymnasie_skola);
        initCommonUi();

        //GradeChart is unique for Gymnasie
        gradesChart = (LineChart) findViewById(R.id.grades_chart);
        styleChart(gradesChart);
    }

    void initColorMap() {
        colorMap.put("BF Barn- och fritidsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.amber_400));
        colorMap.put("Barn- och fritidsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.amber_400));

        colorMap.put("BP Byggprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.brown_300));
        colorMap.put("Bygg- och anläggningsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.brown_600));
        colorMap.put("VVS- och fastighetsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.brown_800));

        colorMap.put("EC Elprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.cyan_100));
        colorMap.put("EN Energiprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.cyan_300));
        colorMap.put("El- och energiprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.cyan_600));

        colorMap.put("ES Estetiska programmet", ContextCompat.getColor(getApplicationContext(), R.color.indigo_400));
        colorMap.put("Estetiska programmet", ContextCompat.getColor(getApplicationContext(), R.color.indigo_400));

        colorMap.put("SP Samhällsvetenskapsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.blue_900));
        colorMap.put("Ekonomiprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.blue_600));
        colorMap.put("Samhällsvetenskapsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.blue_400));
        colorMap.put("Humanistiska programmet", ContextCompat.getColor(getApplicationContext(), R.color.blue_200));

        colorMap.put("FP Fordonsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.green_900));
        colorMap.put("Fordons- och transportprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.green_900));

        colorMap.put("HP Handels- och administrationsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.orange_600));
        colorMap.put("Handels- och administrationsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.orange_600));

        colorMap.put("HR Hotell- och restaurangprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.lime_500));
        colorMap.put("LP Livsmedelsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.lime_900));
        colorMap.put("Hotell- och turismprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.lime_500));
        colorMap.put("Restaurang- och livsmedelsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.lime_900));

        colorMap.put("HV Hantverksprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.purple_500));
        colorMap.put("Hantverksprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.purple_500));

        colorMap.put("IB International baccalaureate", ContextCompat.getColor(getApplicationContext(), R.color.deep_orange_600));
        colorMap.put("International Baccaleurate", ContextCompat.getColor(getApplicationContext(), R.color.deep_orange_600));

        colorMap.put("MP Medieprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.pink_200));
        colorMap.put("IP Industriprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.pink_400));
        colorMap.put("Industritekniska programmet", ContextCompat.getColor(getApplicationContext(), R.color.pink_400));

        colorMap.put("NV Naturvetenskapsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.red_800));
        colorMap.put("Naturvetenskapsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.red_800));

        colorMap.put("NP Naturbruksprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.light_green_600));
        colorMap.put("Naturbruksprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.light_green_600));

        colorMap.put("OP Omvårdnadsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.light_blue_400));
        colorMap.put("Vård- och omsorgsprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.light_blue_400));

        colorMap.put("SM Specialutformat program", ContextCompat.getColor(getApplicationContext(), R.color.black));
        colorMap.put("IV Individuellt program", ContextCompat.getColor(getApplicationContext(), R.color.black));


        colorMap.put("TE Teknikprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.dark_purple_600));
        colorMap.put("Teknikprogrammet", ContextCompat.getColor(getApplicationContext(), R.color.dark_purple_600));

        colorMap.put("Riksrekryterande utbildningar", ContextCompat.getColor(getApplicationContext(), R.color.teal_400));


        colorMap.put("Högskoleförberedande program", ContextCompat.getColor(getApplicationContext(), R.color.teal_400));
        colorMap.put("Yrkesprogram", ContextCompat.getColor(getApplicationContext(), R.color.teal_400));
        colorMap.put("Nationella program", ContextCompat.getColor(getApplicationContext(), R.color.teal_400));

        //Common grund+ gymnasie
        colorMap.put(getResources().getString(R.string.educated_parents) + "(%)", ContextCompat.getColor(getApplicationContext(), R.color.skolrank_blue));
        colorMap.put(getResources().getString(R.string.foreign_parents) + "(%)", ContextCompat.getColor(getApplicationContext(), R.color.skolrank_red));
        colorMap.put(getResources().getString(R.string.girls) + "(%)", ContextCompat.getColor(getApplicationContext(), R.color.skolrank_pink));
        colorMap.put(getResources().getString(R.string.num_students), ContextCompat.getColor(getApplicationContext(), R.color.skolrank_yellow));


    }

    private void setGradeData(Cursor cursor) {
        LineData gradeData = new LineData();
        for (int y = 2006; y < 2015; y++) {
            gradeData.addXValue(y + "");
        }
        float minGradeY = 20;
        float maxGradeY = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex(COLUMN_LASAR));
            String program = cursor.getString(cursor.getColumnIndex(COLUMN_program));
            double avggrade = cursor.getDouble(cursor.getColumnIndex(COLUMN_gbp_for_elever_med_examen));

            if (avggrade <= 0)
                continue;       //ignore these values. 0, -1
            if (avggrade < minGradeY)
                minGradeY = (float) avggrade;
            if (avggrade > maxGradeY)
                maxGradeY = (float) avggrade;
            //  int qualified = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_med_grundl_behorighet));
            addEntry(program, year, avggrade, gradeData);
        }
        gradesChart.setData(gradeData);

        YAxis left = gradesChart.getAxis(YAxis.AxisDependency.LEFT);
        left.setAxisMaxValue(maxGradeY + 0.5f);
        left.setStartAtZero(false);
        left.setAxisMinValue(minGradeY);
        left.setDrawGridLines(false);
        left.setDrawLabels(false);

        //hide right axis
        YAxis right = gradesChart.getAxisRight();
        right.setDrawLabels(false);

        gradesChart.setDescription("");

        gradesChart.invalidate();
    }


    @Override
    public void onEventBackgroundThread(Integer enhetskod) {
        LineData percentageData = new LineData();
        LineData numStudentsData = new LineData();

        for (int y = 2006; y < 2015; y++) {
            percentageData.addXValue(y + "");
            numStudentsData.addXValue(y + "");
        }

        Cursor cursor = AppController.getInstance().getGymnasieDatabase().getSkola(enhetskod);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex(COLUMN_LASAR));
            Skola skola = new Skola(cursor, SchoolUtils.GYMNASIE, false);
            addDefaultEntries(skola.educatedParents, skola.foregin, skola.numStudents, skola.girls, percentageData, numStudentsData, year);
        }
        cursor.moveToPrevious();
        Skola skola = new Skola(cursor, 1, false);

        updateNumStudentsChart(numStudentsData);
        updatePercentageChart(percentageData);


        Cursor programCursor = AppController.getInstance().getGymnasieDatabase().getProgram(enhetskod);
        setGradeData(programCursor);
        EventBus.getDefault().post(skola);
    }

    @Override
    public void onEventMainThread(Skola skola) {
        super.onEventMainThread(skola);
        setVisibileIfData(gradesChart);
    }
}