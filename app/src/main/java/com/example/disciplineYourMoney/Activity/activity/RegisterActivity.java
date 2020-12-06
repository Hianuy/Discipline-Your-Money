package com.example.disciplineYourMoney.Activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.disciplineYourMoney.Activity.config.ConfigurationFirebase;
import com.example.disciplineYourMoney.Activity.helper.Base64Custom;
import com.example.disciplineYourMoney.Activity.model.User;
import com.example.disciplineYourMoney.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.pd.chocobar.ChocoBar;

public class RegisterActivity extends AppCompatActivity {


    private EditText     edtName, edtEmail, edtPassword;
    private Button       btRegister;
    private FirebaseAuth firebaseAuth;
    private User         user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_register );

//        getSupportActionBar().setTitle("Cadastro");

        FirebaseApp.initializeApp( this );


        btRegister       = findViewById ( R.id.btRegister );
        edtName          = findViewById ( R.id.edtName );
        edtEmail         = findViewById ( R.id.edtEmail );
        edtPassword      = findViewById ( R.id.edtPassword );


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textName      = edtName     .getText().toString();
                String textEmail     = edtEmail    .getText().toString();
                String textPassword  = edtPassword .getText().toString();
                // validação


                if (textName.isEmpty() && textEmail.isEmpty() && textPassword.isEmpty()) {
                    ChocoBar.builder().setView(view)
                            .setText("Fill in all fields")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();

                } else if (textName.isEmpty() && textEmail.isEmpty()) {
                    ChocoBar.builder().setView(view)
                            .setText("Fill in the name and email fields")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();

                } else if (textName.isEmpty() && textPassword.isEmpty()) {
                    ChocoBar.builder().setView(view)
                            .setText("Fill in the name and password fields")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();

                } else if (textEmail.isEmpty() && textPassword.isEmpty()) {
                    ChocoBar.builder().setView(view)
                            .setText("Fill in the email and password fields")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();


                } else if (textName.isEmpty() && !(textEmail.isEmpty() && textPassword.isEmpty())) {

                    ChocoBar.builder().setView(view)
                            .setText("Fill in the name field")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();
                } else if (textEmail.isEmpty() && !(textName.isEmpty() && textPassword.isEmpty())) {

                    ChocoBar.builder().setView(view)
                            .setText("Fill in the email field")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();
                } else if (textPassword.isEmpty() && !(textName.isEmpty() && textEmail.isEmpty())) {

                    ChocoBar.builder().setView(view)
                            .setText("Fill in the password field")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();
                }

                if (!textName.isEmpty()) {
                    if (!textEmail.isEmpty()) {
                        if (!textPassword.isEmpty()) {


                            user = new User();
                            user.setName(textName);
                            user.setEmail(textEmail);
                            user.setPassword(textPassword);
                            registerUser();


                        }

                    }
                }
            }


        });

    }

    public void registerUser() {
        // criar uma classe separada para recuperar automaticamente as instancias do firebase
        // quero utilizar sempre a mesma instancia nao quero criar uma nova a todo momento

        // poderiamos passar o email e senha
        // percerber que firebaseAuth(O metodo foi chamado sem eu precisar instanciar a classe) = FirebaseAuth.getInstance();


        firebaseAuth = ConfigurationFirebase.getFireBaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // verifica se deu certo
                    String  idUser = Base64Custom.encodeBase64( user.getEmail() );
                    user.setIdUser( idUser );
                    user.save();

                    ChocoBar.builder().setActivity(RegisterActivity.this)
                            .setText("Success in registering user")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .green()
                            .show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finishMyActivity();
                            // Do something after 5s = 5000ms

                        }
                    }, 1700);


                } else {

                    String exception = "";
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        exception = "Enter a stronger password!";
                        customBar(exception);


                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Enter a valid email address!";
                        customBar(exception);


                    } catch (FirebaseAuthUserCollisionException e) {
                        exception = "This email has already been used";
                        customBar(exception);

                    } catch (Exception e) {
                        exception = "Error: Check Your Connection";

                        e.printStackTrace();
                        customBarInternalError( exception );

                    }
                }
            }
        });

    }

    public void finishMyActivity() {
            finish();
    }

    public void customBar( String exception ) {
        ChocoBar.builder().setBackgroundColor(Color.parseColor("#FFB32D26"))
                .setTextSize( 12 )
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setTextTypefaceStyle(Typeface.BOLD)
                .setText( exception )
                .centerText()
                .setIcon( R.drawable.ic_exclamation_triangle_solid )
                .setActivity(RegisterActivity.this)
                .setDuration(ChocoBar.LENGTH_LONG)
                .build()
                .show();

    }

    public void customBarInternalError( String exception ) {
        ChocoBar.builder().setBackgroundColor(Color.parseColor("#FFB32D26"))
                .setTextSize( 13 )
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setTextTypefaceStyle(Typeface.BOLD)
                .setText( exception )
                .centerText()
                .setIcon( R.drawable.ic_wifi_solid )
                .setActivity(RegisterActivity.this)
                .setDuration(ChocoBar.LENGTH_LONG)
                .build()
                .show();

    }
}



