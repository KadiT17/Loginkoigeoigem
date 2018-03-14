package com.example.opilane.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.

public class LoginActivity extends AppCompatActivity {

    EditText epost, salasõna;
    Button btn_login;
    TextView registreeri; unustatud; katsed;

    int loendaja = 3;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        epost = findViewById(R.id.epost);
        btn_login = findViewById(R.id.login);
        salasõna = findViewById(R.id.salasõna);
        registreeri = findViewById(R.id.registreeri);
        unustatud = findViewById(R.id.unustatud);
        katsed = findViewById(R.id.katsed);
    }
}
