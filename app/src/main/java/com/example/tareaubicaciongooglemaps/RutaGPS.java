package com.example.tareaubicaciongooglemaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RutaGPS extends AppCompatActivity {

    private EditText et_lat3, et_lon3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_g_p_s);

        et_lat3 = (EditText)findViewById(R.id.txt_latitud3);
        et_lon3 = (EditText)findViewById(R.id.txt_longitud3);
    }

    //Método para enviar los datos
    public void siguiente3(View view){
        Intent pag_sig3 = new Intent(this, MapsActivity.class);

        pag_sig3.putExtra("opcion", 3);

        pag_sig3.putExtra("latitud3", et_lat3.getText().toString());
        pag_sig3.putExtra("longitud3", et_lon3.getText().toString());
        startActivity(pag_sig3);
    }
}