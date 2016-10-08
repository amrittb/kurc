package np.edu.ku.kurc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                canProceedAfterTimer = true;

                if(SplashActivity.this.isActivityInForeground) {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
            }
        }, SPLASH_TIMER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        isActivityInForeground = true;

        if(canProceedAfterTimer) {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
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
}
