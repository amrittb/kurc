package np.edu.ku.kurc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import np.edu.ku.kurc.auth.AuthManager;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.fragments.HomeFragment;
import np.edu.ku.kurc.posts.ExtendedPostsPresenter;
import np.edu.ku.kurc.posts.PostsFragment;
import np.edu.ku.kurc.models.Category;
import np.edu.ku.kurc.models.Member;
import np.edu.ku.kurc.models.collection.CategoryCollection;
import np.edu.ku.kurc.posts.data.PostsLocalDataSource;
import np.edu.ku.kurc.posts.data.PostsRemoteDataSource;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private FragmentManager fragmentManager;
    private HashMap<String, Fragment> fragmentMap = new HashMap<>();

    private CategoryCollection categories;

    private PostsRepository postsRepository;

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
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        populateMenuItems(navigationView.getMenu());
        initHeaderWithMember();

        fragmentManager = getSupportFragmentManager();

        postsRepository = PostsRepository.getInstance(new PostsLocalDataSource(getApplicationContext()),
                                                        new PostsRemoteDataSource(getApplicationContext()));

    }

    @Override
    protected void onStart() {
        super.onStart();

        postsRepository.registerReceivers();
    }

    @Override
    protected void onStop() {
        super.onStop();

        postsRepository.unregisterReceivers();
    }

    /**
     * Populates Menu Items from database.
     *
     * @param menu Menu instance.
     * @TODO Move fetching of categories in another thread.
     */
    private void populateMenuItems(Menu menu) {
        categories = (CategoryCollection) (new Category()).all(getApplicationContext());

        for(Category category: categories) {
            MenuItem item = menu.add(R.id.menu_group_main,category.id,2,category.name);
            item.setIcon(category.getMenuIcon());
            item.setCheckable(true);
        }

        selectFirstNavItem();
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

        switch (id) {
            case R.id.nav_home:
                swapHomeFragment();
                break;
            case R.id.nav_logout:
                logout();
                break;
            default:
                if(categories != null) {
                    Category category = categories.findById(id);
                    if(category != null) {
                        swapPostsFragment(category.slug);
                    }
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Logs out the authenticated member.
     */
    private void logout() {
        AuthManager manager = new AuthManager(this);

        manager.logout();

        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    /**
     * Swaps Home Fragment.
     */
    private void swapHomeFragment() {
        swapFragment(HomeFragment.instance(postsRepository),Const.FRAGMENT_TAG_HOME);
    }

    /**
     * Swaps Posts Fragment in the fragment container.
     *
     * @param categorySlug Category slug of the post category.
     */
    private void swapPostsFragment(String categorySlug) {
        String tag = getFragmentTag(categorySlug);
        PostsFragment postsFragment = getPostsFragment(categorySlug);

        postsFragment.setPresenter(new ExtendedPostsPresenter(postsRepository, postsFragment));

        swapFragment(postsFragment,tag);
    }

    /**
     * Swaps a fragment.
     *
     * @param fragment  Fragment to be swapped out.
     * @param tag       Tag of the fragment.
     */
    private void swapFragment(Fragment fragment, String tag) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(fragmentByTag != null && fragmentByTag.isAdded()) {
            hideAllFragments(transaction);

            transaction.show(fragmentByTag);
        } else {
            transaction.add(R.id.content_main, fragment,tag);

            fragmentMap.put(tag,fragment);
        }

        transaction.commit();
    }

    /**
     * Hides all fragments.
     *
     * @param transaction Transaction from which the fragments are to be hidden.
     */
    private void hideAllFragments(FragmentTransaction transaction) {
        for(Fragment f: fragmentMap.values()) {
            if(f.isAdded()) {
                transaction.hide(f);
            }
        }
    }

    /**
     * Returns PostsFragment with correct category.
     *
     * @param categorySlug Category slug for which the posts fragment is to be loaded.
     * @return              PostsFragment instance.
     */
    @NonNull
    private PostsFragment getPostsFragment(String categorySlug) {
        return PostsFragment.instance(categorySlug);
    }

    /**
     * Returns Fragment tag for given category slug.
     *
     * @param categorySlug Category slug for which tag is to be returned.
     * @return              Category fragment tag.
     */
    @NonNull
    private String getFragmentTag(String categorySlug) {
        return categorySlug.toUpperCase() + "_" + "FRAGMENT";
    }
}
