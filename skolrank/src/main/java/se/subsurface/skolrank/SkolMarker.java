package se.subsurface.skolrank;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import se.subsurface.skolrank.model.Skola;

class SkolMarker implements ClusterItem {
    final LatLng position;
    final Skola skola;

    public SkolMarker(Skola skola) {
        this.position = new LatLng(skola.xpos, skola.ypos);
        this.skola = skola;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

}
