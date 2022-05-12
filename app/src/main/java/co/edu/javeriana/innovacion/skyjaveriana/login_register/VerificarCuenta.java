package co.edu.javeriana.innovacion.skyjaveriana.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.javeriana.innovacion.skyjaveriana.BuildConfig;
import co.edu.javeriana.innovacion.skyjaveriana.R;

public class VerificarCuenta extends AppCompatActivity {

    String pass, email;
    TextView txt_email, txt_login;
    Button btn_reenviar;

    FirebaseAuth fAuth;
    public int counter;
    private int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(BuildConfig.DEBUG)
        {
            time = 20;
        }
        else
        {
            time = 60;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_cuenta);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            pass = extras.getString("pass");
            //The key argument here must match that used in the other activity
        }

        txt_email = findViewById(R.id.txt_email_verificarCuenta);
        txt_email.setText(email);
        txt_login = findViewById(R.id.txt_irLogin_verificarCuenta);

        btn_reenviar = findViewById(R.id.btn_reenviar_verificarCuenta);

        fAuth = FirebaseAuth.getInstance();

        //OnClicks
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irALogin();
            }
        });

        btn_reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reenviar();
            }
        });

        reenviar();
    }

    public void irALogin()
    {
        fAuth.signOut();
        Intent intent = new Intent(VerificarCuenta.this, Login.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void reenviar()
    {
        fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fAuth.getCurrentUser();
                System.out.println(user.getEmail() + " ... " + user.isEmailVerified() );
                if(user.isEmailVerified())
                {
                    Toast.makeText(VerificarCuenta.this, getString(R.string.msg_yaVerificado_verificarCuenta), Toast.LENGTH_LONG).show();
                    irALogin();
                    return;
                }
                else
                {
                    enviarEmail();
                    btn_reenviar.setEnabled(false);
                    counter = time;
                    new CountDownTimer(time*1000, 1000){
                        public void onTick(long millisUntilFinished){
                            btn_reenviar.setText(getString(R.string.btn_reenviar_verificarCuenta) + " (" +String.valueOf(counter) + ")");
                            counter--;
                        }
                        public  void onFinish(){
                            btn_reenviar.setEnabled(true);
                            btn_reenviar.setText(getString(R.string.btn_reenviar_verificarCuenta));
                        }
                    }.start();
                }
            }
        });
    }

    public void enviarEmail()
    {
        FirebaseUser user = fAuth.getCurrentUser();
        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(VerificarCuenta.this, getString(R.string.msg_emailEnviado_verificarCuenta), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerificarCuenta.this, "Ocurrio un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error: " + e.getMessage());
            }
        });
    }
}