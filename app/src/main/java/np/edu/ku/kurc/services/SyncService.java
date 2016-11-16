package np.edu.ku.kurc.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;
import retrofit2.Call;
import retrofit2.Response;

public abstract class SyncService<T extends BaseModel,C extends BaseCollection<T>> extends IntentService {

    protected static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID + ".extra.";
    protected static final String ACTION_PREFIX = BuildConfig.APPLICATION_ID + ".action.";

    private LocalBroadcastManager localBroadcastManager;

    public SyncService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Handles Model Sync Behaviour.
     *
     * @param call      Call Object from which the request is to be executed.
     * @param action    Action Name.
     */
    protected void handleModelSync(Call<T> call, String action) {
        try {
            Response<T> response = call.execute();
            if(response.isSuccessful()) {
                response.body().save(getApplicationContext());
                sendSuccessResponseBroadcast(action, Const.SERVICE_RESPONSE_SUCCESS);
            } else {
                sendErrorResponse(action, response.message());
            }
        } catch (IOException e) {
            sendNetworkErrorResponse(action, e.getMessage());
            e.printStackTrace();
        } catch (DatabaseErrorException e) {
            sendDatabaseErrorResponse(action, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles Collection Sync Behaviour.
     *
     * @param call      Call Object from which the request is to be executed.
     * @param action    Action Name.
     */
    protected void handleCollectionSync(Call<C> call, String action) {
        try {
            Response<C> response = call.execute();
            if(response.isSuccessful()) {
                response.body().saveAllSync(getApplicationContext());
                sendSuccessResponseBroadcast(action, Const.SERVICE_RESPONSE_SUCCESS);
            } else {
                sendErrorResponse(action,response.message());
            }
        } catch (IOException e) {
            sendNetworkErrorResponse(action, e.getMessage());
            e.printStackTrace();
        } catch (DatabaseErrorException e) {
            sendDatabaseErrorResponse(action, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends Success Response Broadcast.
     *
     * @param action    Action Name.
     * @param message   Message of broadcast.
     */
    protected void sendSuccessResponseBroadcast(String action, String message) {
        sendResponseBroadcast(action, Const.SERVICE_RESULT_OK, message);
    }

    /**
     * Sends Error Response Broadcast.
     *
     * @param action    Action Name.
     * @param message   Message of broadcast.
     */
    protected void sendErrorResponse(String action, String message) {
        sendResponseBroadcast(action, Const.SERVICE_RESULT_FAILURE, message);
    }

    /**
     * Sends Network Error Response Broadcast.
     *
     * @param action    Action Name.
     * @param message   Message of broadcast.
     */
    protected void sendNetworkErrorResponse(String action, String message) {
        sendResponseBroadcast(action, Const.SERVICE_RESULT_NETWORK_ERROR , message);
    }

    /**
     * Sends Database Error Response Broadcast.
     *
     * @param action    Action Name.
     * @param message   Message of broadcast.
     */
    protected void sendDatabaseErrorResponse(String action, String message) {
        sendResponseBroadcast(action, Const.SERVICE_RESULT_DATABASE_ERROR , message);
    }

    /**
     * Sends Response Broadcast.
     *
     * @param action        Action Name.
     * @param resultCode    Result Code of response.
     * @param message       Message of broadcast.
     */
    private void sendResponseBroadcast(String action, int resultCode, String message) {
        Intent i = new Intent(action);

        i.putExtra(Const.SERVICE_KEY_RESULT_CODE, resultCode);
        i.putExtra(Const.SERVICE_KEY_RESULT_VALUE,message);

        localBroadcastManager.sendBroadcast(i);

        onBroadcast(action, resultCode, message);
    }

    /**
     * Callback called when a message is broadcast.
     *
     * @param action        Action Name.
     * @param resultCode    Result Code.
     * @param message       Message of broadcast.
     */
    protected abstract void onBroadcast(String action, int resultCode, String message);
}
