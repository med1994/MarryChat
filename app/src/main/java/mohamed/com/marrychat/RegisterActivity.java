package mohamed.com.marrychat;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout mDisplay_name;
    private TextInputLayout memail;
    private TextInputLayout mpassword;
    private Button mCreat_Accbtn;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    //firebase data base object
    private DatabaseReference mDatabase;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //instance sur la connxion
        mAuth = FirebaseAuth.getInstance();
        mDisplay_name =(TextInputLayout)findViewById(R.id.Reg_Display_Name);
        memail=(TextInputLayout)findViewById(R.id.Reg_email);
        mpassword=(TextInputLayout)findViewById(R.id.Reg_password);
        mCreat_Accbtn=(Button)findViewById(R.id.Reg_creat_account);
        //Tool bar
        mToolbar=(Toolbar) findViewById(R.id.registre_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Creat Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progresDialog
        mProgressDialog =new ProgressDialog(this);

        mCreat_Accbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Display_name =mDisplay_name.getEditText().getText().toString();
                String email=memail.getEditText().getText().toString();
                String password=mpassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(Display_name)||!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){
                    mProgressDialog.setTitle("Registring user");
                    mProgressDialog.setMessage("Please wait while we create your account");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    Reg_methode(Display_name,email,password);
                }


            }

          
        });


    }

    private void Reg_methode(final String display_name, String email, String password) {
  mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
              // instance sur l'utlisateur qui est conncté
             FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

              HashMap<String,String> hashuser =new HashMap<>();
              hashuser.put("Displayname",display_name);
              hashuser.put("statut","I m using lapichat");
              hashuser.put("image","default");
              hashuser.put("thum_imge","default");
              mDatabase.setValue(hashuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          mProgressDialog.dismiss();
                          Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                          finish();
                      }
                  }
              });


          } else {
              mProgressDialog.hide();
              Toast.makeText(getApplicationContext(), "Cannot sgin in,Please check the form and try agin.", Toast.LENGTH_LONG).show();
          }
      }
  });
    }
}
