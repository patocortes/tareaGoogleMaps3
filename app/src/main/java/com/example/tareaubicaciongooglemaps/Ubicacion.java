package com.example.tareaubicaciongooglemaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Ubicacion extends AppCompatActivity {

    private EditText et_lat_ini, et_lon_ini, et_lat_has, et_lon_has;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        et_lat_ini = (EditText)findViewById(R.id.txt_lat);
        et_lon_ini = (EditText)findViewById(R.id.txt_lon);
        et_lat_has = (EditText)findViewById(R.id.txt_lat2);
        et_lon_has = (EditText)findViewById(R.id.txt_lon2);
    }

    //Método para enviar los datos
    public void siguiente(View view){
        Intent pag_sig = new Intent(this, MapsActivity.class);

        pag_sig.putExtra("opcion", 2);

        pag_sig.putExtra("latitud_desde", et_lat_ini.getText().toString());
        pag_sig.putExtra("longitud_desde", et_lon_ini.getText().toString());
        pag_sig.putExtra("latitud_hasta", et_lat_has.getText().toString());
        pag_sig.putExtra("longitud_hasta", et_lon_has.getText().toString());
        startActivity(pag_sig);
    }
}
