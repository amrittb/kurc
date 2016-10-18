package np.edu.ku.kurc;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import np.edu.ku.kurc.auth.AuthManager;
import np.edu.ku.kurc.models.Member;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer =  (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        selectFirstNavItem();
        initHeaderWithMember();
    }

    /**
     * Selects First Navigation item.
     */
    private void selectFirstNavItem() {
        navigationView.post(new Runnable() {

            @Override
            public void run() {
                navigationView.getMenu().getItem(0).setChecked(true);
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
            }
        });
    }

    /**
     * Initializes header with member.
     */
    private void initHeaderWithMember() {
        AuthManager manager = new AuthManager(this);

        Member member = manager.member();

        if(member == null) {
            member = Member.getGuestMember();
        }

        setHeaderForMember(member);
    }

    /**
     * Sets Header for given member.
     *
     * @param member Member details to be set.
     */
    private void setHeaderForMember(Member member) {
        View header = navigationView.getHeaderView(0);
        TextView nameView = (TextView) header.findViewById(R.id.placeholder_member_name);
        TextView infoView = (TextView) header.findViewById(R.id.placeholder_member_info);

        nameView.setText(member.name);
        infoView.setText(member.email);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        toolbar.setTitle(item.getTitle());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
