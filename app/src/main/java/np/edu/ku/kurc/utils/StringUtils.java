package np.edu.ku.kurc.utils;

public final class StringUtils {

    /**
     * Replaces localhost with AVD url.
     *
     * @param url   URL in which localhost is to be replaced.
     * @return      Replaced URL.
     */
    public static String replaceLocalhost(String url) {
        if(url == null) {
            return url;
        }

        return url.replace("http://localhost/","http://10.0.2.2/");
    }
}
