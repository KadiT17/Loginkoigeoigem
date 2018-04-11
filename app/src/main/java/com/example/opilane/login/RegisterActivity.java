package com.example.opilane.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText eesNimi, perekonnaNimi, epost, salasõna;
    Button btn_registreeri;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    String _eesNimi, _perekonnaNimi, _epost, _salasõna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        eesNimi = findViewById(R.id.eesnimi);
        perekonnaNimi = findViewById(R.id.perenimi);
        epost = findViewById(R.id.epost);
        salasõna = findViewById(R.id.parool);
        btn_registreeri = findViewById(R.id.btnRegistreeri);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_registreeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valideri()) {
                    String k_epost = epost.getText().toString().trim();
                    String k_salasõna = salasõna.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(k_epost, k_salasõna).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.setMessage("Andmete edastamisega läheb aega, palun kannatust");
                                        progressDialog.show();
                                        saadaEpostiKinnitus();
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        }
                                    }
                                }
                            });
                }
            }

        });
    }

    private boolean valideri() {
        boolean tulemus = false;
        _eesNimi = eesNimi.getText().toString();
        _perekonnaNimi = perekonnaNimi.getText().toString();
        _epost = epost.getText().toString();
        _salasõna = salasõna.getText().toString();
        if (_eesNimi.isEmpty() || _perekonnaNimi.isEmpty() || _epost.isEmpty() || _salasõna.isEmpty()) {
            teade("Täida kõik väljad");
        } else {
            tulemus = true;
        }
        return tulemus;
    }

    private void saadaEpostiKinnitus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        saadaKasutajaAndmed();
                        teade("Registreerimine õnnestus, teile saadeti kinnitus email!");
                        finish();
                        firebaseAuth.signOut(); //logid välja, et saaksid valideerida ennast ning uuesti sisse logida siis
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        teade("Kinnitus emaili ei saadetud!");
                    }

                }
            });
        }

    }

    private void saadaKasutajaAndmed() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfileData userProfileData = new UserProfileData(_eesNimi, _perekonnaNimi, _epost);
        databaseReference.setValue(userProfileData);
    }

    public void teade(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
