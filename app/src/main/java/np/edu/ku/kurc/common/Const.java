package np.edu.ku.kurc.common;

public final class Const {

    // Debug Constants.
    public static final String TAG = "KURC";

    // Permission Request Codes.
    public static final int PERMISSION_REQUEST_CAMERA = 1;

    // QrCode Scanner View sizes.
    public static final int QR_VIEW_MAX_SIZE_DP = 640;
    public static final int QR_PREVIEW_SIZE = 1024;

    // KURC Qr Code constants.
    public static final int KURC_QR_NUM_LINES = 5;
    public static final String KURC_ID_PREFIX = "ID: kurc";

    // Auth Constants.
    public static final String AUTH_PREFS = "auth_prefs";
    public static final String AUTH_PREFS_MEMBER_KEY = "member";

    // Fragment tags.
    public static final String FRAGMENT_TAG_HOME = "HOME_FRAGMENT";
    public static final String FRAGMENT_TAG_ABOUT_DEVELOPERS = "ABOUT_DEVELOPERS_FRAGMENT";
    public static final String TOP_STORIES_FRAGMENT_TAG = "TOP_STORIES_FRAGMENT";
    public static final String STICKY_POST_FRAGMENT_TAG = "STICKY_POST_FRAGMENT";
    public static final String FRAGMENT_TAG_COMMITTEE = "COMMITTEE_FRAGMENT";
    public static final String FRAGMENT_TAG_DOWNLOADS = "DOWNLOADS_FRAGMENT";
    public static final String FRAGMENT_TAG_ABOUT_US = "ABOUT_US_FRAGMENT";

    // Bundle Keys.
    public static final String KEY_POST = "POST";
    public static final String KEY_FRAGMENT_TAGS = "FRAGMENT_TAGS";
    public static final String KEY_NAV_SELECTION_ID = "NAV_SELECTION_ID";

    // Service Constants.
    public static final String SERVICE_KEY_RESULT_CODE = "RESULT_CODE";
    public static final String SERVICE_KEY_RESULT_VALUE = "RESULT_VALUE";

    public static final int SERVICE_RESULT_OK = 1;
    public static final int SERVICE_RESULT_FAILURE = 0;
    public static final int SERVICE_RESULT_NETWORK_ERROR = -1;
    public static final int SERVICE_RESULT_DATABASE_ERROR = -2;

    public static final String SERVICE_RESPONSE_SUCCESS = "SYNCED";

    // Posts Count Constants.
    public static final int TOP_STORIES_POST_COUNT = 5;

    // Post View Url pattern
    public static final String KURC_POST_VIEW_URL_PATTERN = "http:\\/\\/ku.edu.np\\/kurc\\/index.php\\/(20[0-2][0-9])\\/[0-9]{2}\\/[0-9]{2}\\/([a-zA-Z0-9-]*)(\\/)?";

    // Hero Image Transaction duration
    public static final long HERO_IMAGE_TRANSACTION_DURATION = 30000;

    // Navigation orders.
    public static final int NAV_CATEGORIES_ORDER = 2;
}
