package se.subsurface.skolrank.model;


import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Skola {
    public static final String COLUMN_LASAR = "lasar";
    public static final String COLUMN_SKOLA_ID = "skola_id";
    public static final String TABLE_SKOLA = "SKOLA";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_skola = "skola";
    public static final String COLUMN_genomsnittligt_meritvarde_16 = "genomsnittligt_meritvarde_16";
    public static final String COLUMN_huvudman = "huvudman";
    public static final String COLUMN_antal_elever = "antal_elever";
    public static final String COLUMN_kommunnamn = "kommunnamn";
    public static final String COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen = "andel_som_uppnatt_kunskapskraven_i_alla_amnen";
    public static final String COLUMN_andel_som_ej_uppnatt_kunskapskraven_i_ett_amne = "andel_som_ej_uppnatt_kunskapskraven_i_ett_amne";
    public static final String COLUMN_andel_som_ej_uppnatt_kunskapskraven_i_tva_eller_fler_amnen = "andel_som_ej_uppnatt_kunskapskraven_i_tva_eller_fler_amnen";
    public static final String COLUMN_andel_som_saknar_betyg_i_alla_amnen = "andel_som_saknar_betyg_i_alla_amnen";
    public static final String COLUMN_andel_behoriga_till_yrkesprogram = "andel_behoriga_till_yrkesprogram";
    public static final String COLUMN_andel_behoriga_till_estetiskt_program = "andel_behoriga_till_estetiskt_program";
    public static final String COLUMN_andel_behoriga_till_ekonomi_humanistiska_och_samhallsvetenskapsprogram = "andel_behoriga_till_ekonomi_humanistiska_och_samhallsvetenskapsprogram";
    public static final String COLUMN_andel_behoriga_till_naturvetenskapligt_och_tekniskt_program = "andel_behoriga_till_naturvetenskapligt_och_tekniskt_program";
    public static final String COLUMN_elever_forskoleklass = "elever_forskoleklass";
    public static final String COLUMN_andel_flickor_forskoleklass = "andel_flickor_forskoleklass";
    public static final String COLUMN_elever_arskurs_1_9 = "elever_arskurs_1_9";
    public static final String COLUMN_andel_flickor_arskurs_1_9 = "andel_flickor_arskurs";
    public static final String COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9 = "andel_elever_med_utlandsk_bakgrund_ak_1_9";
    public static final String COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9 = "andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9";
    public static final String COLUMN_elever_arskurs_1 = "elever_arskurs_1";
    public static final String COLUMN_elever_arskurs_2 = "elever_arskurs_2";
    public static final String COLUMN_elever_arskurs_3 = "elever_arskurs_3";
    public static final String COLUMN_elever_arskurs_4 = "elever_arskurs_4";
    public static final String COLUMN_elever_arskurs_5 = "elever_arskurs_5";
    public static final String COLUMN_elever_arskurs_6 = "elever_arskurs_6";
    public static final String COLUMN_elever_arskurs_7 = "elever_arskurs_7";
    public static final String COLUMN_elever_arskurs_8 = "elever_arskurs_8";
    public static final String COLUMN_elever_arskurs_9 = "elever_arskurs_9";
    public static final String COLUMN_xpos = "xpos";
    public static final String COLUMN_ypos = "ypos";
    public static final String TABLE_HUVUDMAN = "HUVUDMAN";
    public static final String COLUMN_type = "Type";
    public static final String COLUMN_seq = "Seq";
    public static final String TABLE_PROGRAM = "PROGRAM";
    public static final String COLUMN_program_namn = "program_namn";
    public static final String COLUMN_antal_elever_ar_1 = "antal_elever_ar_1";
    public static final String COLUMN_antal_elever_ar_2 = "antal_elever_ar_2";
    public static final String COLUMN_antal_elever_ar_3 = "antal_elever_ar_3";
    public static final String COLUMN_andel_kvinnor = "andel_kvinnor";
    public static final String COLUMN_andel_m_utl_bakgr = "andel_m_utl_bakgr";
    public static final String COLUMN_andel_m_hogutb_foraldrar = "andel_m_hogutb_foraldrar";
    public static final String TABLE_SKOLA_PROGRAM = "SKOLA_PROGRAM";
    public static final String COLUMN_skola_id = "skola_id";
    public static final String COLUMN_program = "program";
    public static final String COLUMN_totalt_antal = "totalt_antal";
    public static final String COLUMN_andel_med_examen = "andel_med_examen";
    public static final String COLUMN_andel_med_studiebevis = "andel_med_studiebevis";
    public static final String COLUMN_andel_med_grundl_behorighet = "andel_med_grundl_behorighet";
    public static final String COLUMN_andel_med_utokat_prog = "andel_med_utokat_prog";
    public static final String COLUMN_gbp_for_elever_med_examen_eller_studiebevis = "gbp_for_elever_med_examen_eller_studiebevis";
    public static final String COLUMN_gbp_for_elever_med_examen = "gbp_for_elever_med_examen";
    public static final String COLUMN_eng_5 = "eng_5";
    public static final String COLUMN_historia_1 = "historia_1";
    public static final String COLUMN_idrott_1 = "idrott_1";
    public static final String COLUMN_ma_1 = "ma_1";
    public static final String COLUMN_na_1 = "na_1";
    public static final String COLUMN_re_1 = "re_1";
    public static final String COLUMN_sam_1 = "sam_1";
    public static final String COLUMN_sv_1 = "sv_1";
    public static final String COLUMN_sv_andraspr_1 = "sv_andraspr_1";
    public static final String COLUMN_gymnarb = "gymnarb";
    private final static String TAG = "Skola";
    public final double xpos;
    public final double ypos;
    public final String name;
    public final String kommun;
    public final Huvudman huvudman;
    public final int antalElever;
    public final double betyg;
    public final int foregin;
    public final int educatedParents;
    public final int girls;
    public final int qualified;
    public final int numStudents;
    public final int schoolType;
    public final int enhetskod;
    public final int year;
    public int firstGrade = -1;
    public int lastGrade = -1;
    public String[] programs;
    public Double[] qualifieds;
    public Double[] grades;
    public Integer[] totaltAntals;

    public Skola(double xpos, double ypos, String name, String kommun, Huvudman huvudman, int antalElever, double betyg, int foregin, int educatedParents, int girls, int qualified, int numStudents, int schoolType, int enhetskod, int year) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.kommun = kommun;
        this.huvudman = huvudman;
        this.antalElever = antalElever;
        this.betyg = betyg;
        this.foregin = foregin;
        this.educatedParents = educatedParents;
        this.girls = girls;
        this.qualified = qualified;
        this.numStudents = numStudents;
        this.schoolType = schoolType;
        this.enhetskod = enhetskod;
        this.year = year;
    }

    public Skola(Cursor cursor, int schoolType, boolean program) {
        this.enhetskod = cursor.getInt(cursor.getColumnIndex(COLUMN_SKOLA_ID));
        this.schoolType = schoolType;
        this.xpos = cursor.getDouble(cursor.getColumnIndex(COLUMN_xpos));
        this.ypos = cursor.getDouble(cursor.getColumnIndex(COLUMN_ypos));
        this.name = cursor.getString(cursor.getColumnIndex(COLUMN_skola));
        this.kommun = cursor.getString(cursor.getColumnIndex(COLUMN_kommunnamn));
        String huvudmanString = cursor.getString(cursor.getColumnIndex(COLUMN_huvudman));
        this.huvudman = fromString(huvudmanString);
        this.antalElever = cursor.getInt(cursor.getColumnIndex(COLUMN_antal_elever));
        this.year = cursor.getInt(cursor.getColumnIndex(COLUMN_LASAR));
        if (schoolType == 0) {
            this.betyg = cursor.getDouble(cursor.getColumnIndex(COLUMN_genomsnittligt_meritvarde_16));
            this.foregin = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9));
            this.educatedParents = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9));
            this.girls = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_flickor_arskurs_1_9));
            this.qualified = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen));
            this.numStudents = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_1_9));
            setGrades(cursor);
        } else {
            this.betyg = -1;
            this.foregin = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_m_utl_bakgr));
            this.educatedParents = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_m_hogutb_foraldrar));
            this.girls = cursor.getInt(cursor.getColumnIndex(COLUMN_andel_kvinnor));
            this.qualified = -1;//cursor.getInt(cursor.getColumnIndex(COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen));
            this.numStudents = cursor.getInt(cursor.getColumnIndex(COLUMN_antal_elever));

            if (program) {
                String programString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_program));
                programs = programString.split(",");

                String qualifiedString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_andel_med_grundl_behorighet));
                qualifieds = getDouble(qualifiedString);

                String gradeString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_gbp_for_elever_med_examen));
                grades = getDouble(gradeString);

                String totaltAntalString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_totalt_antal));
                totaltAntals = getInteger(totaltAntalString);
            }
        }

    }

    private static Huvudman fromString(String s) {
        if (s.startsWith("Fri")) {
            return Huvudman.FRI;
        } else if (s.startsWith("Kom")) {
            return Huvudman.KOM;
        } else {
            return Huvudman.ANNAN;
        }
    }

    private Double[] getDouble(String csvString) {

        final String[] strings = csvString.split(",");
        List<Double> d = new ArrayList<>();
        for (String tempLongString : strings) {
            d.add(Double.valueOf(tempLongString));
        }
        return d.toArray(new Double[strings.length]);
    }

    private Integer[] getInteger(String csvString) {

        final String[] strings = csvString.split(",");
        List<Integer> d = new ArrayList<>();
        for (String tempLongString : strings) {
            d.add(Integer.valueOf(tempLongString));
        }
        return d.toArray(new Integer[strings.length]);
    }

private void setGrades(Cursor cursor) {
    //Årskurser
    int numStudentsF = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_forskoleklass));
    int numStudents1 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_1));
    int numStudents2 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_2));
    int numStudents3 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_3));
    int numStudents4 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_4));
    int numStudents5 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_5));
    int numStudents6 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_6));
    int numStudents7 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_7));
    int numStudents8 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_8));
    int numStudents9 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_elever_arskurs_9));
    if (numStudentsF > 0) {
        firstGrade = 0;
    } else if (numStudents1 > 0) {
        firstGrade = 1;
    } else if (numStudents2 > 0) {
        firstGrade = 2;
    } else if (numStudents3 > 0) {
        firstGrade = 3;
    } else if (numStudents4 > 0) {
        firstGrade = 4;
    } else if (numStudents5 > 0) {
        firstGrade = 5;
    } else if (numStudents6 > 0) {
        firstGrade = 6;
    } else if (numStudents7 > 0) {
        firstGrade = 7;
    } else if (numStudents8 > 0) {
        firstGrade = 8;
    } else if (numStudents9 > 0) {
        firstGrade = 9;
    }

    if (numStudents9 > 0) {
        lastGrade = 9;
    } else if (numStudents8 > 0) {
        lastGrade = 8;
    } else if (numStudents7 > 0) {
        lastGrade = 7;
    } else if (numStudents6 > 0) {
        lastGrade = 6;
    } else if (numStudents5 > 0) {
        lastGrade = 5;
    } else if (numStudents4 > 0) {
        lastGrade = 4;
    } else if (numStudents3 > 0) {
        lastGrade = 3;
    } else if (numStudents2 > 0) {
        lastGrade = 2;
    } else if (numStudents1 > 0) {
        lastGrade = 1;
    } else if (numStudentsF > 0) {
        lastGrade = 0;
    }
}

    public enum Huvudman {FRI, KOM, ANNAN}

}
