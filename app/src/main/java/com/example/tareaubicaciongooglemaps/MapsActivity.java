package com.example.tareaubicaciongooglemaps;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle extras = getIntent().getExtras();

        if (extras.getInt("opcion") == 1){
            String lat1_str = getIntent().getStringExtra("latitud1");
            String lon1_str = getIntent().getStringExtra("longitud1");

            if (lat1_str.equals("") && lon1_str.equals("")){
                LatLng arica = new LatLng(-18.483621, -70.3103);
                mMap.addMarker(new MarkerOptions().position(arica).title("Universidad Santo Tomás"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arica, 18));
                Toast.makeText(this, "Por defecto se generó esta posición", Toast.LENGTH_LONG).show();
            } else {
                Float lat1_flo = Float.parseFloat(lat1_str);
                Float lon1_flo = Float.parseFloat(lon1_str);

                LatLng arica = new LatLng(lat1_flo, lon1_flo);
                mMap.addMarker(new MarkerOptions().position(arica).title("Inicio"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arica, 18));
            }
        } else if (extras.getInt("opcion") == 2){
            String lat_ini_str = getIntent().getStringExtra("latitud_desde");
            String lon_ini_str = getIntent().getStringExtra("longitud_desde");
            String lat_fin_str = getIntent().getStringExtra("latitud_hasta");
            String lon_fin_str = getIntent().getStringExtra("longitud_hasta");

            if(lat_ini_str.equals("") && lon_ini_str.equals("")){
                LatLng arica1 = new LatLng(-18.483621, -70.3103);
                mMap.addMarker(new MarkerOptions().position(arica1).title("Universidad Santo Tomás"));
                LatLng arica2 = new LatLng(-18.478705, -70.321134);
                mMap.addMarker(new MarkerOptions().position(arica2).title("Catedral San Marcos de Arica"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arica2, 18));
                String url = getRequestUrl(arica1, arica2);
                TaskResquestDirections taskResquestDirections = new TaskResquestDirections();
                taskResquestDirections.execute(url);
                Toast.makeText(this, "Por defecto se generó esta ruta", Toast.LENGTH_LONG).show();
            }else {
                Float lat_ini_flo = Float.parseFloat(lat_ini_str);
                Float lon_ini_flo = Float.parseFloat(lon_ini_str);
                Float lat_fin_flo = Float.parseFloat(lat_fin_str);
                Float lon_fin_flo = Float.parseFloat(lon_fin_str);

                // Add a marker in Sydney and move the camera
                LatLng arica1 = new LatLng(lat_ini_flo, lon_ini_flo);
                mMap.addMarker(new MarkerOptions().position(arica1).title("Inicio"));
                LatLng arica2 = new LatLng(lat_fin_flo, lon_fin_flo);
                mMap.addMarker(new MarkerOptions().position(arica2).title("Destino"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arica2, 18));

                String url = getRequestUrl(arica1, arica2);
                TaskResquestDirections taskResquestDirections = new TaskResquestDirections();
                taskResquestDirections.execute(url);
            }
            Toast.makeText(this, "Acá deveria ir el mapa de inicio y destino", Toast.LENGTH_LONG).show();
        } else if (extras.getInt("opcion") == 3){
            actualizarGPS();
            Toast.makeText(this, "Acá deveria ir el mapa con posición GPS", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Opción incorrecta", Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        actualizarUbicacion(location);
                    }
                }
            });
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
            actualizarGPS();
        }
    }

    private void actualizarUbicacion(Location location) {

            String lat_fin_str = getIntent().getStringExtra("latitud3");
            String lon_fin_str = getIntent().getStringExtra("longitud3");
            LatLng ubicacionGPS = new LatLng(location.getLatitude(), location.getLongitude());

        if (lat_fin_str.equals("") && lon_fin_str.equals("")) {

                mMap.addMarker(new MarkerOptions().position(ubicacionGPS).title("Ubicación por defecto"));
                LatLng destino = new LatLng(-18.478705, -70.321134);
                mMap.addMarker(new MarkerOptions().position(destino).title("Catedral San Marcos de Arica"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destino, 18));
                String url = getRequestUrl(ubicacionGPS, destino);
                TaskResquestDirections taskResquestDirections = new TaskResquestDirections();
                taskResquestDirections.execute(url);
                Toast.makeText(this, "Por defecto se generó esta ruta", Toast.LENGTH_LONG).show();
            } else {
                Float lat_fin_flo = Float.parseFloat(lat_fin_str);
                Float lon_fin_flo = Float.parseFloat(lon_fin_str);

                mMap.addMarker(new MarkerOptions().position(ubicacionGPS).title("Ubicación"));

                LatLng destino = new LatLng(lat_fin_flo, lon_fin_flo);
                mMap.addMarker(new MarkerOptions().position(destino).title("Destino"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destino, 18));

                String url = getRequestUrl(ubicacionGPS, destino);
                TaskResquestDirections taskResquestDirections = new TaskResquestDirections();
                taskResquestDirections.execute(url);
            }

    }

    private String getRequestUrl(LatLng origen, LatLng destino) {
        String resultado = "";

        String string_origen = "origin="+origen.latitude+","+origen.longitude;
        String string_destino = "destination="+destino.latitude+","+destino.longitude;

        String sensor = "sensor=false";
        String modo = "mode=driving";
        //String key = "key="+getString(R.string.google_maps_key);
        String key = "key=AIzaSyDZD9RE2aKOnARijujFF78ugUQzFDzWvAo";  //- key del andres, esta si me funcionó

        String param = string_origen+"&"+string_destino+"&"+sensor+"&"+modo+"&"+key;
        String salida = "json";

        resultado = "https://maps.googleapis.com/maps/api/directions/"+salida+"?"+param;
        System.out.println("RESULTADO  " + resultado);

        //https://maps.googleapis.com/maps/api/directions/json?origin=123,123&destination=456,654&sensor=false&mode=driving

        return resultado;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String linea = "";

            while ((linea = bufferedReader.readLine())!=null){
                stringBuffer.append(linea);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null)
                inputStream.close();

            httpURLConnection.disconnect();
        }

        return responseString;
    }

    public class TaskResquestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";

            try{
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for(List<HashMap<String, String>> path : lists){
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for(HashMap<String, String> point : path){
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if(polylineOptions!= null){
                mMap.addPolyline(polylineOptions);
            } else{
                Toast.makeText(getApplicationContext(), "Dirección no encontrada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
