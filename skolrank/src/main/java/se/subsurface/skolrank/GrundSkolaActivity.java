package se.subsurface.skolrank;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import de.greenrobot.event.EventBus;
import se.subsurface.skolrank.model.Skola;

import static se.subsurface.skolrank.model.Skola.COLUMN_LASAR;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_flickor_arskurs_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_genomsnittligt_meritvarde_16;

public class GrundSkolaActivity extends SkolaActivity {
    private static final String TAG = GrundSkolaActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grund_skola);

        initCommonUi();
    }

    @Override
    void initColorMap() {
        colorMap.put(getResources().getString(R.string.avg_grade) + "(poäng)", ContextCompat.getColor(getApplicationContext(), R.color.black));
        colorMap.put(getResources().getString(R.string.qualified_students) + "(%)", ContextCompat.getColor(getApplicationContext(), R.color.skolrank_green));

        //Common grund+ gymnasie
        colorMap.put(getResources().getString(R.string.educated_parents) + "(%)", ContextCompat.getColor(getApplicationContext(), R.color.skolrank_blue));
        colorMap.put(getResources().getString(R.string.foreign_parents) + "(%)", ContextCompat.getColor(getApplicationContext(), R.color.skolrank_red));
        colorMap.put(getResources().getString(R.string.girls) + "(%)", ContextCompat.getColor(getApplicationContext(), R.color.skolrank_pink));
        colorMap.put(getResources().getString(R.string.num_students), ContextCompat.getColor(getApplicationContext(), R.color.skolrank_yellow));
    }



    @Override
    public void onEventBackgroundThread(Integer enhetskod) {
        Log.e(TAG, "onEventBackgroundThread enhetskod=" + enhetskod);
        Cursor cursor = AppController.getInstance().getGrundDatabase().getGrundskola(enhetskod);
        cursor.moveToFirst();

        LineData percentageData = new LineData();
        LineData numStudentsData = new LineData();

        for (int y = 2006; y < 2015; y++) {
            percentageData.addXValue(y + "");
            numStudentsData.addXValue(y + "");
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex(COLUMN_LASAR));

            int numStudents = cursor.getInt(cursor.getColumnIndex(COLUMN_elever_arskurs_1_9));
            int grade = cursor.getInt(cursor.getColumnIndex(COLUMN_genomsnittligt_meritvarde_16));
            int educated = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9));
            int immigrant = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9));
            int girls = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_flickor_arskurs_1_9));
            int graduated = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen));

            if (grade != 0 && grade != -1) {
                addEntry(getResources().getString(R.string.avg_grade) + "(poäng)", year, grade, percentageData);
            }

            if (graduated != 0 && graduated != -1)
                addEntry(getResources().getString(R.string.qualified_students) + "(%)", year, graduated, percentageData);

            addDefaultEntries(educated, immigrant, numStudents, girls, percentageData, numStudentsData, year);
        }


        LineDataSet avgGradeDataSet = percentageData.getDataSetByLabel(getResources().getString(R.string.avg_grade) + "(poäng)", false);
        if (avgGradeDataSet != null) {
            avgGradeDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            avgGradeDataSet.enableDashedLine(10f, 5f, 0f);

        }

        LineDataSet graduatedDataSet = percentageData.getDataSetByLabel(getResources().getString(R.string.qualified_students) + "(%)", false);
        if (graduatedDataSet != null) {
            graduatedDataSet.enableDashedLine(10f, 5f, 0f);
        }

        updateNumStudentsChart(numStudentsData);

        YAxis right = percentageChart.getAxis(YAxis.AxisDependency.RIGHT);
        right.setAxisMaxValue(320);
        right.setAxisMinValue(80);
        updatePercentageChart(percentageData);

        cursor.moveToPrevious();
        Skola skola = new Skola(cursor, 0, false);


        EventBus.getDefault().post(skola);
    }


}