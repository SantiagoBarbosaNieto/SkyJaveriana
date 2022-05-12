package co.edu.javeriana.innovacion.skyjaveriana.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import co.edu.javeriana.innovacion.skyjaveriana.R;

public class ResetPassword extends AppCompatActivity {

    EditText etxt_email;
    Button btn_enviar, btn_login;
    TextView txt_crearCuenta;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etxt_email = findViewById(R.id.etxt_email_resetPass);
        btn_enviar = findViewById(R.id.btn_enviar_resetPass);
        btn_login = findViewById(R.id.btn_login_resetPass);
        txt_crearCuenta = findViewById(R.id.txt_crearCuenta_resetPass);
        progressBar = findViewById(R.id.progressBar_resetPass);
        progressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            etxt_email.setText(extras.getString("email"));
            //The key argument here must match that used in the other activity
        }

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar();
            }
        });

        txt_crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irARegister();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irALogin();
            }
        });
    }

    private void enviar()
    {
        progressBar.setVisibility(View.VISIBLE);
        String email = etxt_email.getText().toString();
        boolean err = false;
        if(TextUtils.isEmpty(email)) {
            etxt_email.setError(getString(R.string.err_email_vacio));
            err = true;
        }

        if(err)
            return;

        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ResetPassword.this, getString(R.string.ex_resetPass), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ResetPassword.this, getString(R.string.err_resetPass) + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void irARegister()
    {
        //TODO añadir info al intent - el email ingresado para el reset password
        Intent intent = new Intent(ResetPassword.this, Register.class);
        startActivity(intent);
    }

    private void irALogin()
    {
        //TODO añadir info al intent - el email ingresado para el reset password
        Intent intent = new Intent(ResetPassword.this, Login.class);
        intent.putExtra("email", etxt_email.getText().toString());
        startActivity(intent);
    }
}