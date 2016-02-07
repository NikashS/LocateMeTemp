package silentassassins.locatemetemp;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MapsActivity extends FragmentActivity implements LocationProvider.LocationCallback {

    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationRequest mLocationRequest1;
    private LocationProvider mLocationProvider;
    String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocationRequest1 = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mLocationProvider = new LocationProvider(this, this);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng;
        String longlat;
        String[] longlat1;
        longlat = ParseUser.getCurrentUser().getString("Location");
        if(longlat!=null&&longlat.length()>2) {
            double currentLongitude1, currentLatitude1;
            longlat1 = longlat.split(",");
            currentLongitude1 = Double.parseDouble(longlat1[0]);
            currentLatitude1 = Double.parseDouble(longlat1[1]);
            if (currentLatitude != currentLatitude1 || currentLongitude != currentLongitude1) {
                ParseUser.getCurrentUser().put("changed", "true");
                Toast.makeText(this, "Changed", Toast.LENGTH_LONG).show();
            }
        }
        ParseUser.getCurrentUser().put("Location", Double.toString(currentLatitude) + "," + Double.toString(currentLongitude));
        ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
        try{
            ParseUser users = queryUser.get(ParseUser.getCurrentUser().getString("track"));
            longlat1 = users.getString("Location").split(",");
            currentLatitude = Double.parseDouble(longlat1[0]);
            currentLongitude = Double.parseDouble(longlat1[1]);
            latLng = new LatLng(currentLatitude, currentLongitude);
            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title( users.getUsername()+"'s Location"));
        }
        catch (ParseException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
         currentLatitude = location.getLatitude();
         currentLongitude = location.getLongitude();
        latLng = new LatLng(currentLatitude, currentLongitude);
        mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
