package se.subsurface.skolrank;

import android.app.Application;


public class AppController extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = AppController.class.getName();
    private static AppController mInstance;
    private GrundDatabase grundDatabase;
    private GymnasieDatabase gymnasieDatabase;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public GrundDatabase getGrundDatabase() {
        if (grundDatabase == null) {
            grundDatabase = new GrundDatabase(this);
        }
        return this.grundDatabase;
    }

    public GymnasieDatabase getGymnasieDatabase() {
        if (gymnasieDatabase == null) {
            gymnasieDatabase = new GymnasieDatabase(this);

        }
        return gymnasieDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


}
