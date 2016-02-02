package se.subsurface.skolrank;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import de.greenrobot.event.EventBus;
import se.subsurface.skolrank.model.Skola;

public class MapsActivity extends AbstractMainActivity implements OnMapReadyCallback {
    private final static String TAG = "MapsActivity";
    private static final int CLUSTER_MIN_SIZE = 2;
    private static final int CLUSTER_MAX_SIZE = 5;
    private ClusterManager<SkolMarker> mClusterManager;
    private GoogleMap mMap;
    private boolean zoomToMarkers = false;
    private CameraPosition savedPosition;
    private Cluster<SkolMarker> clickedCluster;
    private Marker clickedMarker;
    //   private CameraPosition cameraPosition;

    @Override
    void restartLoader() {
        Log.e(TAG, "restartLoader()");
        updateMarkers(true);
    }

    private void updateMarkers(boolean zoomToMarkers) {
        mProgressBar.setVisibility(View.VISIBLE);
        mNumSchoolTV.setVisibility(View.GONE);
        LatLngBounds bounds;
        if (!zoomToMarkers) {
            bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        } else {
            bounds = null;
        }
        EventBus.getDefault().post(new UpdateMarkers(zoomToMarkers, bounds));
    }

    private Cursor getCursor(MainActivity.CompareBy compareBy, String filterString, int schoolType, int year, LatLngBounds bounds) {
        Log.e(TAG, "loadInBackground ENTER");
        Cursor cursor;
        if (schoolType == 0) {
            cursor = AppController.getInstance().getGrundDatabase().getCity(GrundDatabase.getSortString(compareBy), filterString, year, bounds);
        } else {
            cursor = AppController.getInstance().getGymnasieDatabase().getSkola(compareBy, filterString, year, bounds);
        }
        return cursor;

    }

    @Override
    int getContentView() {
        return R.layout.activity_maps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "onCreate mmap=" + mMap);
        Intent intent = getIntent();
        savedPosition = intent.getParcelableExtra(SchoolUtils.MAP_POSITION);
        Log.e(TAG, "onCreate savedPosition=" + savedPosition);
        showDialog();
    }

    private void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Skolrank - Karta");
        //View helperView = LayoutInflater.from(this).inflate(R.layout.helper, null);
        dialog.setContentView(R.layout.helper);
        Skola skola = new Skola(0, 0, "Abc 123 Skolan", "Stockholm", Skola.Huvudman.KOM, 256, 227.3d, 20, 55, 61, 80, 200, SchoolUtils.GRUND, 123, 2014);
        View sampleMarker = dialog.findViewById(R.id.sample_marker);
//sampleMarker.setBackground(new BubbleDrawable(getResources()));
        SchoolUtils.getMarkerView(skola, this, sampleMarker);

        Button dialogButton = (Button) dialog.findViewById(R.id.helper_button);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        googleMap.setMyLocationEnabled(true);
//        mMap.setMyLocationEnabled(true);
        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new SkolMarkerRenderer());
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<SkolMarker>() {
            @Override
            public boolean onClusterItemClick(SkolMarker skolMarker) {
                ActivityUtil.openSkolaActivity(MapsActivity.this, skolMarker.skola.enhetskod, mSchoolType);
                return false;
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Log.e(TAG, "cameraPosition" + cameraPosition.target);
                Log.e(TAG, "onCameraChange() clickedCluster=" + clickedCluster + ", clickedMaker=" + clickedMarker);
                if (clickedCluster == null && clickedMarker == null) {
                    if (!zoomToMarkers) {
                        Log.e(TAG, "do update");
                        updateMarkers(false);
                    } else {
                        Log.e(TAG, "dont update");
                    }
                } else if (clickedCluster != null && clickedCluster.getSize() > CLUSTER_MAX_SIZE) {
                    //trick to not reload markers if window is moved due to center on marker
                    float zoom = mMap.getCameraPosition().zoom + 2f;
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(clickedCluster.getPosition(), zoom);
                    mMap.animateCamera(yourLocation);

                }
                clickedCluster = null;
                clickedMarker = null;
                zoomToMarkers = false;
                //MapsActivity.this.cameraPosition = cameraPosition;
                mClusterManager.onCameraChange(cameraPosition);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clickedMarker = marker;
                mClusterManager.onMarkerClick(marker);
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(
                new MyCustomAdapterForClusters());
        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<SkolMarker>() {
                    @Override
                    public boolean onClusterClick(Cluster<SkolMarker> cluster) {
                        clickedCluster = cluster;

                        Log.e(TAG, "onClusterClick clickedCluster=" + clickedCluster);
                        return false;
                    }
                });


        Log.e(TAG, "onMapReady savedPosition=" + savedPosition);
        if (savedPosition != null) {
            zoomToMarkers = false;
            MapsActivity.this.zoomToMarkers = false;
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(savedPosition));

        } else {
            updateMarkers(true);
        }
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Called in the background thread
    @SuppressWarnings("unused")
    public void onEventBackgroundThread(UpdateMarkers event) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Cursor cursor = getCursor(mSortComparator, mSearchQuery, mSchoolType, year, event.bounds);

        mClusterManager.clearItems();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


            Skola skola = new Skola(cursor, mSchoolType, false);
            SkolMarker sm = new SkolMarker(skola);
            if (skola.xpos != 0) {
                builder.include(sm.position);
                mClusterManager.addItem(sm);

            }
        }
        int numSchools = cursor.getCount();
        cursor.close();
        LatLngBounds bounds = null;
        try {
            bounds = builder.build();
        } catch (IllegalStateException e) {
            Log.i(TAG, "No markers, Cursor empty or positions missing");
        }
        Log.e(TAG, "onEventBackgroundThread numSchools=" + numSchools + ", event.zoomToMarkers=" + event.zoomToMarkers + ", bounds=" + bounds);
        EventBus.getDefault().post(new UpdateMap(numSchools, event.zoomToMarkers, bounds));
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(UpdateMap event) {
        Log.e(TAG, "onEventMainThread UpdateMap=" + event);
        updateStatus(event.numSchools);

        if (event.zoomToMarkers) {
            MapsActivity.this.zoomToMarkers = true;

            Log.e(TAG, "bounds=" + event.bounds);
            int padding = (int) getResources().getDimension(R.dimen.map_bounds); // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(event.bounds, padding);
            mMap.moveCamera(cu);
        }
        FrameLayout mapFrame = (FrameLayout) findViewById(R.id.map_frame);
        mapFrame.setVisibility(View.VISIBLE);

        mClusterManager.cluster();
    }

    private class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            LinearLayout clusterListView = new LinearLayout(MapsActivity.this);

            int sdk = Build.VERSION.SDK_INT;

            if (clickedCluster.getSize() <= CLUSTER_MAX_SIZE && clickedCluster.getSize() > CLUSTER_MIN_SIZE) {

                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    clusterListView.setBackgroundDrawable(new BubbleDrawable(getResources()));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    clusterListView.setBackground(new BubbleDrawable(getResources()));
                }

                clusterListView.setOrientation(LinearLayout.VERTICAL);
                for (SkolMarker skolMarker : clickedCluster.getItems()) {
                    View clusterItemView = getLayoutInflater().inflate(R.layout.detailed_marker, clusterListView, false);
                    SchoolUtils.getMarkerView(skolMarker.skola, MapsActivity.this, clusterItemView);
                    clusterListView.addView(clusterItemView);
                }
            }
            return clusterListView;
        }
    }

    private class SkolMarkerRenderer extends DefaultClusterRenderer<SkolMarker> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        private final LinearLayout clusterListView;

        public SkolMarkerRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);

            //Cluster
            clusterListView = new LinearLayout(getApplicationContext());
            clusterListView.setOrientation(LinearLayout.VERTICAL);
            mClusterIconGenerator.setContentView(clusterListView);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<SkolMarker> cluster, MarkerOptions markerOptions) {
            if (cluster.getSize() == CLUSTER_MIN_SIZE) {
                clusterListView.removeAllViews();
                for (SkolMarker skolMarker : cluster.getItems()) {
                    View clusterItemView = getLayoutInflater().inflate(R.layout.detailed_marker, clusterListView, false);
                    SchoolUtils.getMarkerView(skolMarker.skola, MapsActivity.this, clusterItemView);
                    clusterListView.addView(clusterItemView);
                }
                Bitmap icon = mClusterIconGenerator.makeIcon();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .anchor(mIconGenerator.getAnchorU(), mIconGenerator.getAnchorV());
            } else {
                super.onBeforeClusterRendered(cluster, markerOptions);
            }
        }

        @Override
        protected void onBeforeClusterItemRendered(SkolMarker skolMarker, MarkerOptions markerOptions) {
            @SuppressLint("InflateParams") View clusterItemView = getLayoutInflater().inflate(R.layout.detailed_marker, null);
            SchoolUtils.getMarkerView(skolMarker.skola, MapsActivity.this, clusterItemView);
            mIconGenerator.setContentView(clusterItemView);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
                    .anchor(mIconGenerator.getAnchorU(), mIconGenerator.getAnchorV());

        }

        protected String getClusterText(int bucket) {
            String text;
            if (bucket <= 0) {
                text = "-";
            } else {
                text = bucket + "";
            }
            return text;
        }

        protected int getBucket(Cluster<SkolMarker> cluster) {
            int bucket = 0;
            int numStudents = -1;
            switch (MapsActivity.super.mSortComparator) {

                case GRADE:
                    int totalGrades = 0;
                    for (SkolMarker m : cluster.getItems()) {
                        if (m.skola.betyg != -1) {
                            numStudents += m.skola.numStudents;
                            totalGrades += m.skola.numStudents * m.skola.betyg;
                        }
                    }
                    bucket = (totalGrades / numStudents);
                    break;
                case NUM_STUDENTS:
                    for (SkolMarker m : cluster.getItems()) {
                        bucket += m.skola.numStudents;
                    }
                    break;
                case NAME:
                    break;
                case QUALIFIED_FOR_HIGH_SCHOOL:
                    int qualified = 0;
                    for (SkolMarker m : cluster.getItems()) {
                        if (m.skola.qualified != -1) {
                            numStudents += m.skola.numStudents;
                            qualified += 0.01 * m.skola.numStudents * m.skola.qualified;
                        }
                    }
                    bucket = (100 * qualified / numStudents);
                    break;
                case EDUCATED_PARENTS:
                    int educatedParents = 0;
                    for (SkolMarker m : cluster.getItems()) {
                        if (m.skola.educatedParents != -1) {
                            numStudents += m.skola.numStudents;
                            educatedParents += 0.01 * m.skola.numStudents * m.skola.educatedParents;
                        }
                    }
                    bucket = (100 * educatedParents / numStudents);
                    break;
                case GIRLS:
                    int girls = 0;
                    for (SkolMarker m : cluster.getItems()) {
                        if (m.skola.girls != -1) {
                            numStudents += m.skola.numStudents;
                            girls += 0.01 * m.skola.numStudents * m.skola.girls;
                        }
                    }
                    bucket = (100 * girls / numStudents);
                    break;
                case FOREIGN_PARENTS:
                    int foreignStudents = 0;
                    for (SkolMarker m : cluster.getItems()) {
                        if (m.skola.foregin != -1) {
                            numStudents += m.skola.numStudents;
                            foreignStudents += 0.01 * m.skola.numStudents * m.skola.foregin;
                        }
                    }

                    bucket = (100 * foreignStudents / numStudents);
                    break;
            }

            return bucket;
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            return cluster.getSize() >= CLUSTER_MIN_SIZE;
        }

    }

    private class UpdateMarkers {
        public final boolean zoomToMarkers;
        public final LatLngBounds bounds;

        public UpdateMarkers(boolean zoomToMarkers, LatLngBounds bounds) {
            this.zoomToMarkers = zoomToMarkers;
            this.bounds = bounds;

        }
    }

    private class UpdateMap {
        public final boolean zoomToMarkers;
        public final LatLngBounds bounds;
        public final int numSchools;

        public UpdateMap(int numSchools, boolean zoomToMarkers, LatLngBounds bounds) {
            this.zoomToMarkers = zoomToMarkers;
            this.bounds = bounds;
            this.numSchools = numSchools;
        }

        @Override
        public String toString() {
            return "UpdateMap{" +
                    "zoomToMarkers=" + zoomToMarkers +
                    ", bounds=" + bounds +
                    ", numSchools=" + numSchools +
                    '}';
        }
    }
}