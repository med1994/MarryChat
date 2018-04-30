package mohamed.com.marrychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    DatabaseReference logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout= FirebaseDatabase.getInstance().getReference().child("users").child("online");



        // Firebase

        mAuth = FirebaseAuth.getInstance();

        //Toolbar setting
        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Marry Chat");
        // View pager
        mViewPager=(ViewPager)findViewById(R.id.Tabpager);
        mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout=(TabLayout)findViewById(R.id.main_tabs_id);
        mTabLayout.setupWithViewPager(mViewPager);
        FirebaseCrash.log("Activity created");
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            logout.setValue(false);
            sendtostart();

        }
    }

    private void sendtostart() {
        startActivity(new Intent(MainActivity.this, StartActivity.class));
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()== R.id.main_logout){
            mAuth.signOut();
            sendtostart();
        }
        if (item.getItemId()== R.id.main_Account_Setting){
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));

        }
        if (item.getItemId()== R.id.main_all_user){
            startActivity(new Intent(MainActivity.this,UsersActivity.class));


        }


        return true;
    }
}
