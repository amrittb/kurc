package np.edu.ku.kurc.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Member;

public class AuthManager {

    /**
     * Gson Instance.
     */
    private Gson gson = new Gson();

    /**
     * Auth Shared Preferences.
     */
    private SharedPreferences prefs;

    /**
     * Create an AuthManager instance.
     *
     * @param context Application context.
     */
    public AuthManager(Context context) {
        prefs = context.getSharedPreferences(Const.AUTH_PREFS,Context.MODE_PRIVATE);
    }

    /**
     * Attempts to authenticate member.
     *
     * @param member Member to be authenticated.
     */
    public boolean attemptLogin(Member member) {
        // Currently no mechanism of validating the user so directly storing the member.
        storeMember(member);

        // If it member is logged in return true.
        return true;
    }

    /**
     * Stores a member to shared preferences.
     *
     * @param member Member to be stored.
     */
    private void storeMember(Member member) {
        String memberJson = gson.toJson(member);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Const.AUTH_PREFS_MEMBER_KEY,memberJson);
        editor.apply();
    }

    /**
     * Returns the authenticated member.
     *
     * @return Authenticated Member.
     */
    public Member member() {
        String memberJson = prefs.getString(Const.AUTH_PREFS_MEMBER_KEY,null);

        if(memberJson == null) {
            return null;
        }

        return gson.fromJson(memberJson,Member.class);
    }
}
