package np.edu.ku.kurc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import np.edu.ku.kurc.auth.AuthManager;

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
     * Checks if we can proceed after timer.
     */
    private boolean canProceedAfterTimer = false;

    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                canProceedAfterTimer = true;

                if(SplashActivity.this.isActivityInForeground) {
                    startApp();
                }
            }
        }, SPLASH_TIMER);

        authManager = new AuthManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        isActivityInForeground = true;

        if(canProceedAfterTimer) {
            startApp();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        isActivityInForeground = false;

        if(canProceedAfterTimer) {
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
}
