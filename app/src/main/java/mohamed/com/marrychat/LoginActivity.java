package mohamed.com.marrychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    //the bar that have the flach
    private Toolbar mToolbar;

    private TextInputLayout mLoginemail;
    private TextInputLayout mLoginpassword;
    private Button mLogin;
    private ProgressDialog mLoginProgress;
    //Firebase authentification
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginProgress=new ProgressDialog(this);
        //instance sur la connxion
        mAuth = FirebaseAuth.getInstance();
        mUserDB= FirebaseDatabase.getInstance().getReference().child("users");




        //Toolbar
        mToolbar=(Toolbar) findViewById(R.id.registre_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Java utils
        mLoginemail=(TextInputLayout)findViewById(R.id.email_log);
        mLoginpassword=(TextInputLayout)findViewById(R.id.password_log);
        mLogin=(Button)findViewById(R.id.Login_btn);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendre le valeur de champs
                String email=mLoginemail.getEditText().getText().toString();
                String password=mLoginpassword.getEditText().getText().toString();
                if (!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){
                    mLoginProgress.setTitle("Loggin In");
                    mLoginProgress.setMessage("Please wait while we check your Caridentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    Loginuser(email ,password);
                }
            }
        });

    }

    private void Loginuser(String email, String password) {
        //conxion avec un mail et mot de pass
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //terminer le progress

                        String current_user_id=mAuth.getCurrentUser().getUid();

                        String device_Token= FirebaseInstanceId.getInstance().getToken();
                        mUserDB.child(current_user_id).child("device Token").setValue(device_Token).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //creat a new task and clear all the previus task
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else {
                        mLoginProgress.hide();
                        Toast.makeText(getApplicationContext(), "Cannot sgin in,Please check the form and try agin.", Toast.LENGTH_LONG).show();
                    }

            }
        });

    }

}
