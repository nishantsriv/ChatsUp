package com.self.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.self.chatapp.LoginmethodClass.callbackManager;
import static com.self.chatapp.LoginmethodClass.firebaseAuthwithgoogle;
import static com.self.chatapp.LoginmethodClass.mGoogleApiClient;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private SignInButton signInButton;

    private static int RC_SIGN_IN = 1;
    public static FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;
    private TextView signuptxt;
    private EditText email, password;
    private Button loginbutton;
    private TextView textView;
    private Button invite;
    public static LoginButton loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        invite = (Button) findViewById(R.id.invitebtn);
        signuptxt = (TextView) findViewById(R.id.gotoregister);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        loginbutton = (Button) findViewById(R.id.login_button);
        signInButton = (SignInButton) findViewById(R.id.signin);
        textView = (TextView) findViewById(R.id.forgot);
        loginbtn = (LoginButton) findViewById(R.id.fblogin);
        auth = FirebaseAuth.getInstance();

        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(LoginActivity.this, UserlistActivity.class);
                    i.putExtra("username", user.getEmail());
                    startActivity(i);
                    finish();
                }
            }
        };
        LoginmethodClass.loginmethod(this);
        auth.addAuthStateListener(authlistener);
        invite.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        signuptxt.setOnClickListener(this);
        loginbutton.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                firebaseAuthwithgoogle(account, this);
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("TAG", "onActivityResult: sent invitation " + id);
                }
            } else {
                Toast.makeText(this, "Unable to send invite/Cancelled by User", Toast.LENGTH_SHORT).show();
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (authlistener != null) {
            auth.removeAuthStateListener(authlistener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == invite) {
            onInviteClicked();
        } else if (v == signInButton) {
            signIn();
        } else if (v == signuptxt) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        } else if (v == loginbutton) {
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Successfully Signed in", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Could not Sign in due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (v == textView) {
            if (email.equals("")) {
                Toast.makeText(LoginActivity.this, "Type your email ", Toast.LENGTH_SHORT).show();
                return;
            } else
                auth.sendPasswordResetEmail(email.getText().toString());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse("https://www.edwisor.com"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, 0);
    }

}
