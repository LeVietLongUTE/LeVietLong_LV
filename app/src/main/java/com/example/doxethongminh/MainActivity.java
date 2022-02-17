package com.example.doxethongminh;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.doxethongminh.Model.User;
import com.example.doxethongminh.fragment.CaidatFragment;
import com.example.doxethongminh.fragment.DsXeFragment;
import com.example.doxethongminh.fragment.ThongkeFragment;
import com.example.doxethongminh.fragment.TimkiemFragment;
import com.example.doxethongminh.fragment.XeratheoveFragment;
import com.example.doxethongminh.fragment.chontheoveFragment;
import com.example.doxethongminh.fragment.xeraFragment;
import com.example.doxethongminh.fragment.xevaoFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_CHONTHEOVE = 0;
    private static final int FRAGMENT_XERACHONTHEOVE = 1;
    private static final int FRAGMENT_XEVAO = 2;
    private static final int FRAGMENT_XERA = 3;

    private static final int FRAGMENT_DSXE = 4;
    private static final int FRAGMENT_THONGKE = 5;
    private static final int FRAGMENT_HDSD = 6;
    private static final int FRAGMENT_CAIDAT = 7;
    private int mCurrentFragment = FRAGMENT_CHONTHEOVE;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;


    TextView welcom;
    FirebaseUser user;
    DatabaseReference reference;
    String UserID;
    Button btndangxuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        btndangxuat = findViewById(R.id.btndangxuat);
        welcom = findViewById(R.id.welcom);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();
        reference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if (userprofile != null) {
                    String username = userprofile.username;
                    welcom.setText("Xin chào  " + username + "!");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btndangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, login.class));
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

//        //xử lý khi vào mặt định là Fragment chọn theo vé
        replaceFragment(new chontheoveFragment());
        navigationView.getMenu().findItem(R.id.Nav_chontheove).setChecked(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Nav_chontheove) {
            //xử lý logic khi không phải fragment chọn theo vé thì replace quay lại
            toolbar.setTitle("xe vào chọn Theo Vé");

            if (mCurrentFragment != FRAGMENT_CHONTHEOVE) {

                replaceFragment(new chontheoveFragment());
                mCurrentFragment = FRAGMENT_CHONTHEOVE;
            }

        }
        if (id == R.id.Nav_xeratheove) {
            //xử lý logic khi không phải fragment chọn theo vé thì replace quay lại
            toolbar.setTitle("xe ra chọn Theo Vé");

            if (mCurrentFragment != FRAGMENT_XERACHONTHEOVE) {
                replaceFragment(new XeratheoveFragment());
                mCurrentFragment = FRAGMENT_XERACHONTHEOVE;
            }

        } else if (id == R.id.Nav_xevao) {
            toolbar.setTitle("Xe Vào");
            if (mCurrentFragment != FRAGMENT_XEVAO) {

                replaceFragment(new xevaoFragment());
                mCurrentFragment = FRAGMENT_XEVAO;
            }

        } else if (id == R.id.Nav_xera) {
            toolbar.setTitle("Xe Ra");
            if (mCurrentFragment != FRAGMENT_XERA) {

                replaceFragment(new xeraFragment());

                mCurrentFragment = FRAGMENT_XERA;

            }

        } else if (id == R.id.Nav_dsxe) {
            toolbar.setTitle("Danh sách xe");
            if (mCurrentFragment != FRAGMENT_DSXE) {

                replaceFragment(new DsXeFragment());

                mCurrentFragment = FRAGMENT_DSXE;
            }
        } else if (id == R.id.Nav_thongke) {
            toolbar.setTitle("Thống kê");
            if (mCurrentFragment != FRAGMENT_THONGKE) {

                replaceFragment(new ThongkeFragment());

                mCurrentFragment = FRAGMENT_THONGKE;
            }
        } else if (id == R.id.Nav_timkiem) {
            toolbar.setTitle("Hướng dẫn sử dụng");
            if (mCurrentFragment != FRAGMENT_HDSD) {

                replaceFragment(new TimkiemFragment());

                mCurrentFragment = FRAGMENT_HDSD;

            }
        } else if (id == R.id.Nav_caidat) {

            toolbar.setTitle("Cài đặt");
            if (mCurrentFragment != FRAGMENT_CAIDAT) {

                replaceFragment(new CaidatFragment());

                mCurrentFragment = FRAGMENT_CAIDAT;
            }

        }

        //khi đang ở chọn theo vé mà tiếp tục chọn theo vé thì nó đóng menu
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    //xử lý sự kiện menu đang mở click nút quay lại thì đóng menu, nếu ko xử lý ấn nút quay lại thì sẽ thoát app
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //
//    // xử lý Fragment truyền vào giao diện home fragment
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_Fragment, fragment);
        fragmentTransaction.commit();
    }


}
