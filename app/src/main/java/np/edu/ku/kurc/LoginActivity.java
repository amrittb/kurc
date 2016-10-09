package np.edu.ku.kurc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;

import java.io.IOException;

import np.edu.ku.kurc.auth.AuthManager;
import np.edu.ku.kurc.auth.MemberAuthHandler;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.fragments.LoginDialog;
import np.edu.ku.kurc.models.Member;
import np.edu.ku.kurc.qrcode.KURCQrCodeProcessor;
import np.edu.ku.kurc.utils.Metrics;

public class LoginActivity extends AppCompatActivity implements KURCQrCodeProcessor.MemberProcessObserver,
        MemberAuthHandler,
        View.OnClickListener {

    private CameraSource cameraSource;

    private AuthManager authManager;

    private SurfaceView qrCameraView;
    private TextView openScannerText;
    private ImageButton openScannerBtn;
    private FrameLayout cameraViewContainer;

    private KURCQrCodeProcessor qrProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authManager = new AuthManager(this);

        qrCameraView = (SurfaceView) findViewById(R.id.qrCameraView);
        openScannerText = (TextView) findViewById(R.id.openScannerText);
        openScannerBtn = (ImageButton) findViewById(R.id.openScannerBtn);
        cameraViewContainer = (FrameLayout) findViewById(R.id.cameraViewContainer);

        Button guestLoginBtn = (Button) findViewById(R.id.loginAsGuestBtn);

        guestLoginBtn.setOnClickListener(this);
        openScannerBtn.setOnClickListener(this);

        initializeQrCodeScanner();
    }

    /**
     * Initializes Qr Code Scanner.
     */
    private void initializeQrCodeScanner() {
        BarcodeDetector qrCodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, qrCodeDetector)
                .setRequestedPreviewSize(Const.QR_PREVIEW_SIZE, Const.QR_PREVIEW_SIZE)
                .build();

        qrProcessor = new KURCQrCodeProcessor(this);

        qrCodeDetector.setProcessor(qrProcessor);

        resizeCameraViewSize();
    }

    /**
     * Resize Camera View for better fit.
     */
    private void resizeCameraViewSize() {
        cameraViewContainer.post(new Runnable() {

            @Override
            public void run() {
                int size = getOptimalCameraViewSize();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cameraViewContainer.getLayoutParams();
                params.width = size;
                params.height = size;

                cameraViewContainer.setLayoutParams(params);
            }
        });
    }

    /**
     * Returns optimal camera view size.
     *
     * @return optimal camera view size.
     */
    private int getOptimalCameraViewSize() {
        int width = qrCameraView.getWidth();
        int height = qrCameraView.getHeight();

        int size = width;

        if (height < width) {
            size = height;
        }

        int maxSize = (int) Metrics.dipToPixels(this, Const.QR_VIEW_MAX_SIZE_DP);
        if (maxSize < size) {
            size = maxSize;
        }

        return size;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openScannerBtn:
                openQrCodeScanner();
                break;
            case R.id.loginAsGuestBtn:
                handleLogin(Member.getGuestMember());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Const.PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openQrCodeScanner();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Opens QR Code scanner.
     */
    private void openQrCodeScanner() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Const.PERMISSION_REQUEST_CAMERA);
                return;
            }
            cameraSource.start(qrCameraView.getHolder());
            syncCameraViewWithSource();

            openScannerBtn.setVisibility(View.INVISIBLE);
            openScannerText.setVisibility(View.INVISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Syncs camera view with source.
     */
    private void syncCameraViewWithSource() {
        qrCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    /**
     * Processes member for login.
     *
     * @param member Member to be processed.
     */
    @Override
    public void processMember(Member member) {
        Bundle bundle = new Bundle();
        bundle.putString("member",new Gson().toJson(member));

        Fragment loginDialog = new LoginDialog();
        loginDialog.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(loginDialog,"login_dialog").commit();
    }

    /**
     * Handles member login.
     *
     * @param member Member to be logged in.
     */
    @Override
    public void handleLogin(Member member) {
        if(authManager.attemptLogin(member)) {
            startActivity(new Intent(this,MainActivity.class));
            finish();

            Toast.makeText(this,"Yay! One more member.",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"You have me invalid credentials",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Cancels member login.
     * @param member Member found to be cancelled.
     */
    @Override
    public void cancelLogin(Member member) {
        qrProcessor.reset();
    }
}
