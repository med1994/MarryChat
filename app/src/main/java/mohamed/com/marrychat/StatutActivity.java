package mohamed.com.marrychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatutActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputLayout textStatus;
    private Button btn_change_statuts;
    //Firbase
    DatabaseReference mDatabase;
    FirebaseUser user;
    private ProgressDialog mProgressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statut);
        mToolbar=(Toolbar)findViewById(R.id.status_appBar);

        //Firebase
        user= FirebaseAuth.getInstance().getCurrentUser();
        String cerrentuser =user.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(cerrentuser);



        //actionbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Statut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textStatus=(TextInputLayout)findViewById(R.id.change_statut_text);
        btn_change_statuts=(Button)findViewById(R.id.btn_status_change_id);

        btn_change_statuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newstatut=textStatus.getEditText().getText().toString();
                //progress dialog
                mProgressDialog=new ProgressDialog(StatutActivity.this);
                mProgressDialog.setTitle("Changing statut");
                mProgressDialog.setMessage("Please wait to change your status");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                mDatabase.child("statut").setValue(newstatut).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            startActivity(new Intent(StatutActivity.this, SettingsActivity.class));
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"there are some problem", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }
}
