package np.edu.ku.kurc.auth;

import np.edu.ku.kurc.models.Member;

public interface MemberAuthHandler {

    /**
     * Handles Member login.
     *
     * @param member Member to be logged in.
     */
    void handleLogin(Member member);

    /**
     * Handles canceling of member login.
     *
     * @param member Member found to be cancelled.
     */
    void cancelLogin(Member member);
}