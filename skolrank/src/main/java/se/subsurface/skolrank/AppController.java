package se.subsurface.skolrank;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class AppController extends Application {
    public boolean showHidden = false;
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


    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG

            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }

    public void sendEvent(Tracker tracker, AbstractMainActivity.CompareBy compareBy, String sortString) {
        Log.e(TAG, "sendEvent");
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("CompareBy")
                .setAction(compareBy.name())
                .build());
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("sortString")
                .setAction(sortString)
                .build());

    }
}
