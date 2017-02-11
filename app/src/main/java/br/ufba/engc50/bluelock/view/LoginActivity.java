package br.ufba.engc50.bluelock.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;

public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;
    private EditText etitTextMatricula;
    private EditText editTextPassword;
    private View mProgressView;
    private View mLoginFormView;

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

        if(MicrocontrollerActions.getUsuarioLogado() != null) {
            activateMainActivity(MicrocontrollerActions.getUsuarioLogado().getTipoUsuario());
        }
    }

    private void activateMainActivity(int tipoUsuario) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("tipoUsuario", tipoUsuario);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        etitTextMatricula.setError(null);
        editTextPassword.setError(null);

        String matricula = etitTextMatricula.getText().toString();
        String password = editTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
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
            mAuthTask = new UserLoginTask(matricula, password);
            mAuthTask.execute((Void) null);
        }
    }

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

    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String matricula;
        private final String password;

        UserLoginTask(String matricula, String password) {
            this.matricula = matricula;
            this.password = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return -9;
            }

            MicrocontrollerActions lc = new MicrocontrollerActions();
            return lc.autenticar(matricula, password);
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;
            showProgress(false);

            if (success >= 0) {
                activateMainActivity(success);
                finish();
            } else {
                editTextPassword.setError(getString(R.string.error_incorrect_password));
                editTextPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

