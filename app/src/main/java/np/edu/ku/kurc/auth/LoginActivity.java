package np.edu.ku.kurc.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Field;

import np.edu.ku.kurc.MainActivity;
import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Member;
import np.edu.ku.kurc.auth.qrcode.KURCQrCodeProcessor;

public class LoginActivity extends AppCompatActivity implements KURCQrCodeProcessor.MemberProcessObserver,
        MemberAuthHandler,
        View.OnClickListener {

    private CameraSource cameraSource;

    private AuthManager authManager;

    private SurfaceView qrCameraView;

    private Button toggleFlashBtn;

    private KURCQrCodeProcessor qrProcessor;

    private boolean isFlashTurnedOn;

    private Camera qrCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authManager = new AuthManager(this);

        qrCameraView = (SurfaceView) findViewById(R.id.qrCameraView);
        toggleFlashBtn = (Button) findViewById(R.id.toggleFlashBtn);

        Button guestLoginBtn = (Button) findViewById(R.id.loginAsGuestBtn);

        guestLoginBtn.setOnClickListener(this);
        toggleFlashBtn.setOnClickListener(this);

        initializeQrCodeScanner();
    }

    @Override
    protected void onStart() {
        super.onStart();

        openQrCodeScanner();
    }

    /**
     * Initializes Qr Code Scanner.
     */
    private void initializeQrCodeScanner() {
        BarcodeDetector qrCodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, qrCodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        qrProcessor = new KURCQrCodeProcessor(this);

        qrCodeDetector.setProcessor(qrProcessor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginAsGuestBtn:
                handleLogin(Member.getGuestMember());
                break;
            case R.id.toggleFlashBtn:
                toggleCameraFlash();
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
        qrCameraView.post(new Runnable() {

            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA}, Const.PERMISSION_REQUEST_CAMERA);
                        return;
                    }
                    cameraSource.start(qrCameraView.getHolder());
                    syncCameraViewWithSource();

                    if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                        toggleFlashBtn.setVisibility(View.VISIBLE);
                    }

                    resizeCameraView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
     * Resize Camera View.
     */
    private void resizeCameraView() {
        if(cameraSource.getPreviewSize() != null) {
            qrCameraView.post(new Runnable() {

                @Override
                public void run() {
                    Size size = cameraSource.getPreviewSize();

                    float ratio = ((float) size.getWidth()) / ((float) size.getHeight());

                    int height = qrCameraView.getHeight();

                    int width = (int) (ratio * height);

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) qrCameraView.getLayoutParams();
                    params.width = width;
                    params.height = height;

                    qrCameraView.setLayoutParams(params);
                }
            });
        }
    }

    /**
     * Toggles Camera Flash
     */
    private void toggleCameraFlash() {
        Camera camera = getCamera();

        if(camera != null) {
            Camera.Parameters params = camera.getParameters();
            isFlashTurnedOn = !isFlashTurnedOn;

            String btnText = getResources().getString((!isFlashTurnedOn) ? R.string.btn_text_flash_on : R.string.btn_text_flash_off);
            Drawable drawable = ContextCompat.getDrawable(this,(!isFlashTurnedOn) ? R.drawable.ic_flash_on_white_24dp: R.drawable.ic_flash_off_white_24dp);
            String mode = (isFlashTurnedOn) ? Camera.Parameters.FLASH_MODE_TORCH: Camera.Parameters.FLASH_MODE_OFF;

            params.setFlashMode(mode);
            camera.setParameters(params);

            toggleFlashBtn.setText(btnText);
            toggleFlashBtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    /**
     * Returns Camera object from camera source.
     *
     * @return Camera object of camera source.
     */
    private Camera getCamera() {
        if(qrCamera == null) {
            Field[] declaredFields = CameraSource.class.getDeclaredFields();

            if(declaredFields != null) {
                for(Field field: declaredFields) {
                    if(field.getType() == Camera.class) {
                        field.setAccessible(true);

                        try {
                            Camera camera = (Camera) field.get(this.cameraSource);
                            if(camera != null) {
                                this.qrCamera = camera;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return qrCamera;
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
            Toast.makeText(this,"You have invalid credentials",Toast.LENGTH_LONG).show();
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
