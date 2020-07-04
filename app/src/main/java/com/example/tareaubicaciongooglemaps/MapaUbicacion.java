package com.example.tareaubicaciongooglemaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MapaUbicacion extends AppCompatActivity {

    private EditText et_lat1, et_lon1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ubicacion);

        et_lat1 = (EditText)findViewById(R.id.txt_latitud3);
        et_lon1 = (EditText)findViewById(R.id.txt_longitud3);
    }

    //Método botón ir
    public void ir(View view){
        Intent datos = new Intent(this, MapsActivity.class);

        datos.putExtra("opcion", 1);

        datos.putExtra("latitud1", et_lat1.getText().toString());
        datos.putExtra("longitud1", et_lon1.getText().toString());
        startActivity(datos);
    }
}