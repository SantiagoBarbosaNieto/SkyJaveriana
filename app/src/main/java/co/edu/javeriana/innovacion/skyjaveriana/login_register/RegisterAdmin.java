package co.edu.javeriana.innovacion.skyjaveriana.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import co.edu.javeriana.innovacion.skyjaveriana.FirestoreDB_Keys;
import co.edu.javeriana.innovacion.skyjaveriana.MainActivity;
import co.edu.javeriana.innovacion.skyjaveriana.R;

public class RegisterAdmin extends AppCompatActivity {

    EditText etxt_nombreEmpresa, etxt_nombreEncargado, etxt_email, etxt_pass;
    Button btn_registrarse;
    CheckBox ch_promo, ch_tyc;
    TextView txt_login;
    ProgressBar progressBar;

    FirebaseAuth fAuth;
    FirebaseFirestore fDb;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        etxt_nombreEmpresa = findViewById(R.id.etxt_nombreEmpresa_registerAdmin);
        etxt_nombreEncargado = findViewById(R.id.etxt_nombreEncargado_registerAdmin);
        etxt_email = findViewById(R.id.etxt_email_registerAdmin);
        etxt_pass = findViewById(R.id.etxt_pass_registerAdmin);

        btn_registrarse = findViewById(R.id.btn_registrarse_registerAdmin);
        btn_registrarse.setEnabled(false);

        ch_promo = findViewById(R.id.ch_promo_registerAdmin);
        ch_tyc = findViewById(R.id.ch_tyc_registerAdmin);

        txt_login = findViewById(R.id.txt_irLogin_registerAdmin);
        progressBar = findViewById(R.id.progressBar_registrerAdmin);
        progressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();
        fDb = FirebaseFirestore.getInstance();

        //OnClicks
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irALogin();
            }
        });

        ch_tyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ch_tyc.isChecked())
                {
                    btn_registrarse.setEnabled(true);
                }
                else
                {
                    btn_registrarse.setEnabled(false);
                }
            }
        });
    }

    private void register()
    {
        String nombreEmpresa = etxt_nombreEmpresa.getText().toString();
        String nombre = etxt_nombreEncargado.getText().toString();
        String email = etxt_email.getText().toString();
        String pass = etxt_pass.getText().toString();

        boolean promo = ch_promo.isChecked();

        boolean err = false;
        if(TextUtils.isEmpty(nombreEmpresa)) {
            err = true;
            etxt_nombreEmpresa.setError(getString(R.string.err_nombre_vacio));
        }
        if(TextUtils.isEmpty(nombre)) {
            err = true;
            etxt_nombreEncargado.setError(getString(R.string.err_nombre_vacio));
        }
        if(TextUtils.isEmpty(email)) {
            err = true;
            etxt_email.setError(getString(R.string.err_email_vacio));
        }
        if(TextUtils.isEmpty(pass)) {
            err = true;
            etxt_pass.setError(getString(R.string.err_pass_vacio));
        }
        else if(pass.length() < 6 ) {
            err = true;
            etxt_pass.setError(getString(R.string.err_pass_corto));
        }

        if(err)
            return;

        progressBar.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    userId = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fDb.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put(FirestoreDB_Keys.ADMIN.toString(), true);
                    user.put(FirestoreDB_Keys.NOMBRE.toString(), nombre);
                    user.put(FirestoreDB_Keys.EMAIL.toString(), email);
                    user.put(FirestoreDB_Keys.PROMOS.toString(), promo);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterAdmin.this, getString(R.string.ex_registro), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterAdmin.this, MainActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("pass", pass);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterAdmin.this, "Ocurrio un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterAdmin.this, "Ocurrio un error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void irALogin()
    {
        Intent intent = new Intent(RegisterAdmin.this, Login.class);
        startActivity(intent);
    }
}