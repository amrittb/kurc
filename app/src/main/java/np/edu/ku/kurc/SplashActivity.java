package np.edu.ku.kurc;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import np.edu.ku.kurc.auth.AuthManager;
import np.edu.ku.kurc.auth.LoginActivity;
import np.edu.ku.kurc.common.AsyncCallback;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Category;
import np.edu.ku.kurc.models.collection.CategoryCollection;
import np.edu.ku.kurc.network.api.ServiceFactory;
import np.edu.ku.kurc.network.api.services.CategoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    /**
     * Splash Screen Timer.
     */
    public static final int SPLASH_TIMER = 5000;

    /**
     * Checks if the activity is in foreground.
     */
    public boolean isActivityInForeground;

    /**
     * Checks if the timer has ended.
     */
    private boolean timerEnded;

    /**
     * Checks if we can proceed after timer.
     */
    private boolean appInitiated;

    /**
     * AuthManager Instance.
     */
    private AuthManager authManager;

    private CardView appSetupCard;
    private View appSetupProgress;
    private TextView appSetupMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        KenBurnsView heroImage = (KenBurnsView) findViewById(R.id.hero_bg);

        RandomTransitionGenerator generator = new RandomTransitionGenerator(Const.HERO_IMAGE_TRANSACTION_DURATION, new AccelerateDecelerateInterpolator());
        heroImage.setTransitionGenerator(generator);
        heroImage.restart();

        appSetupCard = (CardView) findViewById(R.id.app_setup_card);
        appSetupProgress =  appSetupCard.findViewById(R.id.app_setup_progress);
        appSetupMessage = (TextView) appSetupCard.findViewById(R.id.app_setup_message);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                timerEnded = true;

                if(SplashActivity.this.isActivityInForeground && SplashActivity.this.appInitiated) {
                    startApp();
                }
            }
        }, SPLASH_TIMER);

        authManager = new AuthManager(this);

        initApp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        isActivityInForeground = true;

        if(appInitiated && timerEnded) {
            startApp();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        isActivityInForeground = false;

        if(appInitiated) {
            finish();
        }
    }

    /**
     * Starts the application by loading correct activity.
     */
    private void startApp() {
        if(authManager.member() == null) {
            startActivity(new Intent(this,LoginActivity.class));
        } else {
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }

    /**
     * Initializes App.
     */
    private void initApp() {
        Category category = new Category();

        int categoryCount = category.count(getApplicationContext());

        if(categoryCount <= 0) {
            setupApp();
        } else {
            appInitiated = true;
        }
    }

    /**
     * Sets up the app.
     */
    public void setupApp() {
        appSetupCard.setVisibility(View.VISIBLE);

        fetchCategories();
    }

    /**
     * Fetches categories from server.
     */
    private void fetchCategories() {
        Call<CategoryCollection> call = ServiceFactory.makeService(CategoryService.class).getCategories();

        call.enqueue(new Callback<CategoryCollection>() {
            @Override
            public void onResponse(Call<CategoryCollection> call, Response<CategoryCollection> response) {
                consumeCategories(response.body());
            }

            @Override
            public void onFailure(Call<CategoryCollection> call, Throwable t) {
                consumeCategories(null);
            }
        });
    }

    /**
     * Consumes Categories collection.
     *
     * @param categories Categories Collection.
     */
    private void consumeCategories(CategoryCollection categories) {
        if(categories != null) {
            categories.saveAll(getApplicationContext(), new AsyncCallback<Category, Void, Void>() {

                @Override
                public void onSetup() {
                    appSetupMessage.setText(R.string.message_app_setup_almost_done);
                }

                @Override
                public void onSuccess(Void result) {
                    appInitiated = true;

                    if(timerEnded) {
                        startApp();
                    }
                }

                @Override
                public void onFailure() {
                    showSetupError(R.string.error_global);
                }
            });
        } else {
            showSetupError(R.string.error_network);
        }
    }

    /**
     * Shows App setup Error message.
     *
     * @param errorMessageResource Error Message Resource.
     */
    private void showSetupError(int errorMessageResource) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_not_interested_black_48dp);

        appSetupProgress.setVisibility(View.GONE);
        appSetupMessage.setText(errorMessageResource);
        appSetupMessage.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }
}
