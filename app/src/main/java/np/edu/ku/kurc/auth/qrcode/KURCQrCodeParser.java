package np.edu.ku.kurc.auth.qrcode;

import com.google.android.gms.vision.barcode.Barcode;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Member;

class KURCQrCodeParser {

    /**
     * Parses a QrCode to give a member.
     * @param barcode   Barcode to parse.
     * @return          Member object in the barcode.
     * @throws Exception
     */
    Member parse(Barcode barcode) throws Exception {
        String[] parsedQrCode = barcode.displayValue.split("\\r?\\n");

        if(parsedQrCode.length != Const.KURC_QR_NUM_LINES) {
            throw new Exception("Invalid QR Code");
        }

        return new Member(getId(parsedQrCode),parsedQrCode[1],parsedQrCode[2],parsedQrCode[3],parsedQrCode[4]);
    }

    /**
     * Returns id of member from parsed code.
     *
     * @param parsedQrCode  Parsed string array.
     * @return              Id of the member.
     * @throws Exception
     */
    private int getId(String[] parsedQrCode) throws Exception {
        String idLine = parsedQrCode[0];

        String[] splitLines = idLine.split(Const.KURC_ID_PREFIX);

        if(splitLines.length != 2) {
            throw new Exception("Invalid QR Code ID Field");
        }

        return Integer.parseInt(splitLines[1]);
    }
}
