package np.edu.ku.kurc.common;

public interface AsyncCallback<A,P,R> {

    void onSetup();

    void onSuccess(R result);

    void onFailure();
}
