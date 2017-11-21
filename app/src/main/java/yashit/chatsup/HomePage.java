package yashit.chatsup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import yashit.chatsup.DataObjects.UserProfile;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference root;

    private AllMessagesAdapter arrayAdapter;
    private ArrayList<String> userList = new ArrayList<>();

    private ListView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = (ListView) findViewById(R.id.listView);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();



        root = FirebaseDatabase.getInstance().getReference().getRoot().child("users");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, userList);
        rv.setAdapter(arrayAdapter);

        root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserProfile user = snapshot.getValue(UserProfile.class);
                            String username = user.getFullName();
                            userList.add(username);
                            Toast.makeText(HomePage.this, username , Toast.LENGTH_SHORT).show();
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//        root.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                userList.clear();
//                Iterator i = dataSnapshot.getChildren().iterator();
//                int tempCounter = 0;
//                String name;
//                while (i.hasNext()){
//                    Object dsp = ((DataSnapshot) i.next()).getValue();
//                    Toast.makeText(HomePage.this, dsp.toString(), Toast.LENGTH_SHORT).show();
////                    String data = ((DataSnapshot) i.next()).getValue().toString();
////                    String[] splitData = data.split(",");
////                    //Name is printed 3rd in the list.
////                    String[] nameData = splitData[2].split("=");
////                    name = nameData[1].substring(0, nameData[1].length() - 1);
////                    userList.add(name);
////                    Toast.makeText(HomePage.this, Arrays.toString(nameData), Toast.LENGTH_SHORT).show();
////                      if(tempCounter==0) {
////                        tempCounter += 1;
////                    }
////                    else if(tempCounter==1) {
////                        Object dsp = ((DataSnapshot) i.next()).getValue();
////                        name = dsp.toString();
////                        tempCounter+=1;
////                    }
////                    else if(tempCounter==2) {
////                        tempCounter=0;
////                    }
////                    String[] keySplit = key.split("->");
////                    String name1 = keySplit[0];
////                    String name2 = keySplit[1];
////                    if(name1.equals(currentUser)) {
////                        userList.add(name2);
//
////                    }
////                    else if(name2.equals(currentUser)){
////                        userList.add(name1);
////                    }
//                }
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void signOut(View view) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            auth.signOut();
            startActivity(new Intent(HomePage.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
