package br.ufba.engc50.bluelock.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.client.methods.HttpGet;

import java.net.URI;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.local.Global;
import br.ufba.engc50.bluelock.model.Usuario;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;
import br.ufba.engc50.bluelock.remote.RestClient;

public class LoginActivity extends AppCompatActivity {
    //private UserLoginTask mAuthTask = null;
    private EditText etitTextMatricula;
    private EditText editTextPassword;
    private View mProgressView;
    private View mLoginFormView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etitTextMatricula = (EditText) findViewById(R.id.matricula);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button buttonLogin = (Button) findViewById(R.id.email_sign_in_button);
        buttonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if (Global.getUsuarioLogado() != null) {
            activateMainActivity();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void activateMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        etitTextMatricula.setError(null);
        editTextPassword.setError(null);

        String matricula = etitTextMatricula.getText().toString();
        String password = editTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(matricula)) {
            etitTextMatricula.setError(getString(R.string.error_field_required));
            focusView = etitTextMatricula;
            cancel = true;
        } else if (!isMatriculaValid(matricula)) {
            etitTextMatricula.setError(getString(R.string.error_invalid_matricula));
            focusView = etitTextMatricula;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            RestClient client = new RestClient(this, Global.LOGIN_CALLBACK);
            HttpGet httpGet = new HttpGet(URI.create(Global.ENDPOINT_LOGIN.
                    replace("{matricula}", etitTextMatricula.getText()).
                    replace("{senha}", editTextPassword.getText())
            ));
            client.execute(httpGet);
//            mAuthTask = new UserLoginTask(matricula, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showProgress(false);
            String response = intent.getStringExtra(RestClient.HTTP_RESPONSE);
            if(response == null || response.isEmpty()) {
                editTextPassword.setError(getString(R.string.error_no_service));
                editTextPassword.requestFocus();
            } else if (response.equals("null")) {
                editTextPassword.setError(getString(R.string.error_incorrect_password));
                editTextPassword.requestFocus();
            } else {
                Global.logIn(new Usuario(response));
                activateMainActivity();
                finish();
            }
            Log.i("Login", "RESPONSE = " + response);
        }
    };

    private boolean isMatriculaValid(String matricula) {
        return matricula.length() >= 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(Global.LOGIN_CALLBACK));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client2, getIndexApiAction());
        client2.disconnect();
    }

//    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {
//
//        private final String matricula;
//        private final String password;
//
//        UserLoginTask(String matricula, String password) {
//            this.matricula = matricula;
//            this.password = password;
//        }
//
//        @Override
//        protected Integer doInBackground(Void... params) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return -9;
//            }
//
//            MicrocontrollerActions lc = new MicrocontrollerActions();
//            return lc.autenticar(matricula, password);
//        }
//
//        @Override
//        protected void onPostExecute(final Integer success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success >= 0) {
//                //activateMainActivity(success);
//                finish();
//            } else {
//                editTextPassword.setError(getString(R.string.error_incorrect_password));
//                editTextPassword.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

