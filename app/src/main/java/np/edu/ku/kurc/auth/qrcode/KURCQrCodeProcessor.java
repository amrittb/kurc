package np.edu.ku.kurc.auth.qrcode;

import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

import np.edu.ku.kurc.models.Member;

public class KURCQrCodeProcessor implements Detector.Processor<Barcode> {

    /**
     * Parsed Member object.
     */
    private Member member;

    /**
     * Member process observer instance.
     */
    private MemberProcessObserver observer;

    /**
     * KURC QrCode Parser object.
     */
    private KURCQrCodeParser parser = new KURCQrCodeParser();

    /**
     * Create KURC QrCode Processor object.
     *
     * @param observer  Observer to observe code processing.
     */
    public KURCQrCodeProcessor(MemberProcessObserver observer) {
        this.observer = observer;
    }

    @Override
    public void release() {}

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        if(member == null) {
            final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

            if (qrCodes.size() != 0) {
                Barcode barcode = qrCodes.valueAt(0);

                try {
                    member = parser.parse(barcode);
                    observer.processMember(member);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Resets parsed member.
     */
    public void reset() {
        member = null;
    }

    /**
     * Implementation should process the parsed member.
     */
    public interface MemberProcessObserver {

        /**
         * Processes member.
         *
         * @param member Member to be processed.
         */
        void processMember(Member member);
    }
}
