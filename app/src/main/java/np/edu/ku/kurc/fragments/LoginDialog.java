package np.edu.ku.kurc.fragments;

import android.os.Bundle;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import com.google.gson.Gson;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Member;

import np.edu.ku.kurc.auth.MemberAuthHandler;

public class LoginDialog extends AppCompatDialogFragment {

    /**
     * Member to be logged in.
     */
    private Member member;

    /**
     * Check if the member is to be logged in.
     */
    private boolean isToBeLoggedIn;

    /**
     * Member Authentication handler.
     */
    private MemberAuthHandler authHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String message = "";

        if(getArguments() != null) {
            member  = new Gson().fromJson(getArguments().getString("member"),Member.class);
            message = "KURC ID: " + Integer.toString(member.id) + "\nName: " + member.name;
        }

        authHandler = (MemberAuthHandler) getActivity();

        return builder
                .setTitle("Is that you?")
                .setMessage(message)
                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        authHandler.handleLogin(member);
                        isToBeLoggedIn = true;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Nah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        authHandler.cancelLogin(member);
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if( ! isToBeLoggedIn) {
            authHandler.cancelLogin(member);
        }
    }
}
