package com.example.opilane.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    EditText epost, salasõna;
    Button btn_login;
    TextView registreeri, unustatud, katsed;

    int loendaja = 3;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        epost = findViewById(R.id.epost);
        btn_login = findViewById(R.id.btnLogin);
        salasõna = findViewById(R.id.parool);
        registreeri = findViewById(R.id.registreeri);
        unustatud = findViewById(R.id.unustatud);
        katsed = findViewById(R.id.katsed);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser kasutaja = firebaseAuth.getCurrentUser();
        if (kasutaja !=null){
            finish();
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valideeri(epost.getText() .toString() ,salasõna.getText().toString());
            }
        });
        registreeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registreerimine = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registreerimine);
            }
        });
        unustatud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void valideeri(String epost, String salasõna) {
        progressDialog.setMessage("Andmete edastamiseha lähesb aega, palun kannatust!");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(epost, salasõna).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    kontrolliEposti();
                } else {
                    teade("Sisse logimine ebaõnnestus!");
                    if (loendaja < 3) {
                        katsed.setText("Katseid on jäänud veel" + String.valueOf(loendaja));
                    }
                    if (loendaja == 0) {
                        btn_login.setEnabled(false);
                        katsed.setText("Võta ühendust administraatoriga");
                    }
                }
            }
        });
    }
        private void kontrolliEposti(){
            FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
            boolean epostiKontroll = firebaseUser.isEmailVerified();
            if(epostiKontroll){
                finish();
                startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            }
            else{
                teade("Kinnitage oma eposti aadress!");
            }
    }


    public void teade(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT) .show();
    }
}
