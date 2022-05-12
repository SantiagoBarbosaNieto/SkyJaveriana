package co.edu.javeriana.innovacion.skyjaveriana.login_register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import co.edu.javeriana.innovacion.skyjaveriana.FirestoreDB_Keys;
import co.edu.javeriana.innovacion.skyjaveriana.MainActivity;
import co.edu.javeriana.innovacion.skyjaveriana.R;

public class Login extends AppCompatActivity {
    TextView txt_recuperarLogin, txt_crearCuenta;
    EditText etxt_email, etxt_pass;
    Button btn_iniciar;
    ProgressBar progressBar;

    FirebaseAuth fAuth;
    FirebaseFirestore fDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txt_recuperarLogin = findViewById(R.id.txt_recuperar_login);
        txt_crearCuenta = findViewById(R.id.txt_crearCuenta_login);
        btn_iniciar = findViewById(R.id.btn_iniciar_login);
        etxt_email = findViewById(R.id.etxt_email_login);
        etxt_pass = findViewById(R.id.etxt_pass_login);
        progressBar = findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();
        fDb = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            etxt_email.setText(extras.getString("email"));
            //The key argument here must match that used in the other activity
        }


        //On clicks
        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


        txt_crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irARegistro();
            }
        });

        txt_recuperarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irARecuperarPassword();
            }
        });
    }


    private void login()
    {
        String email = etxt_email.getText().toString();
        String pass = etxt_pass.getText().toString();

        boolean err = false;
        if(TextUtils.isEmpty(email))
        {
            err = true;
            etxt_email.setError(getString(R.string.err_email_vacio));
        }
        if(TextUtils.isEmpty(pass))
        {
            err = true;
            etxt_pass.setError(getString(R.string.err_pass_vacio));
        }
        if(err)
            return;

        progressBar.setVisibility(View.VISIBLE);

        fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful())
                {
                    System.out.println(fAuth.getCurrentUser().getEmail() + "   ++++    " + fAuth.getCurrentUser().isEmailVerified());
                    if(!fAuth.getCurrentUser().isEmailVerified())
                    {
                        Toast.makeText(Login.this, getString(R.string.msg_noVerificado), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, VerificarCuenta.class);
                        intent.putExtra("email", email);
                        intent.putExtra("pass", pass);
                        startActivity(intent);
                        return;
                    }
                    String uId = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fDb.collection(FirestoreDB_Keys.USERS.toString()).document(uId);
                    documentReference.addSnapshotListener(Login.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            boolean admin = value.getBoolean(FirestoreDB_Keys.ADMIN.toString());
                            if(admin)
                            {
                                //TODO mandar a vista admin
                                Toast.makeText(Login.this, "Login exitoso.. Vista admin no implementada", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Login.this, getString(R.string.ex_login), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(Login.this, "Ocurrio un error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void irARegistro()
    {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    private void irARecuperarPassword()
    {
        Intent intent = new Intent(Login.this, ResetPassword.class);
        intent.putExtra("email", etxt_email.getText().toString());
        startActivity(intent);
    }

}