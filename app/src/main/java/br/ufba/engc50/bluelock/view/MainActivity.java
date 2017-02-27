package br.ufba.engc50.bluelock.view;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;

import br.ufba.engc50.bluelock.model.Registro;
import br.ufba.engc50.bluelock.model.Usuario;
import br.ufba.engc50.bluelock.remote.RestClient;
import br.ufba.engc50.bluelock.view.fragment.BaseFragmentPerfil;
import br.ufba.engc50.bluelock.view.fragment.RegistroFragment;
import br.ufba.engc50.bluelock.view.fragment.UsuarioDialog;
import br.ufba.engc50.bluelock.view.fragment.PerfilFragment;
import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.view.fragment.SobreFragment;
import br.ufba.engc50.bluelock.view.fragment.TravaFragment;
import br.ufba.engc50.bluelock.view.fragment.UsuarioFragment;
import br.ufba.engc50.bluelock.local.Global;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseFragmentPerfil.BFEventListener, UsuarioFragment.OnListFragmentInteractionListener {

    private DrawerLayout drawer;
    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        menu.setGroupVisible(R.id.grupoAdministracao, Global.getUsuarioLogado().isAdministrador());
        View header = navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.textViewDrawerMatricula)).setText(String.valueOf(Global.getUsuarioLogado().getMatricula()));
	    setDefaultFragment();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_relatorio) {
            setFragmentRelatorio();
        } else if (id == R.id.nav_usuarios) {
            setFragmentUsuarios();
        } else if (id == R.id.nav_perfil) {
            setTitle("Perfil");
            Bundle args = new Bundle();
            args.putChar(BaseFragmentPerfil.EXTRA_TIPO_ACAO, 'e');
            args.putParcelable("usuario", Global.getUsuarioLogado());
            PerfilFragment perfilFragment = new PerfilFragment();
            perfilFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutFragmentMainActivity, perfilFragment, "PERFIL")
                    .commit();
        } else if (id == R.id.nav_trava) {
            setDefaultFragment();
        } else if (id == R.id.nav_sobre) {
            setTitle("Sobre");
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutFragmentMainActivity, new SobreFragment())
                    .commit();
        } else if (id == R.id.nav_sair) {
            Global.logOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragmentRelatorio() {
        setTitle("Relatórios");
        RestClient client = new RestClient(this, Global.ACAO_RELATORIO_CALLBACK);
        HttpPost httpPost = new HttpPost(URI.create(Global.ENDPOINT_ACAO_RELATORIO));
        client.execute(httpPost);
    }

    private void setFragmentUsuarios() {
        setTitle("Usuários");
        RestClient client = new RestClient(this, Global.USUARIO_LISTAR_CALLBACK);
        HttpPost httpPost = new HttpPost(URI.create(Global.ENDPOINT_USUARIO_LISTAR));
        client.execute(httpPost);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(Global.USUARIO_LISTAR_CALLBACK));
        registerReceiver(broadcastReceiver, new IntentFilter(Global.USUARIO_REMOVER_CALLBACK));
        registerReceiver(broadcastReceiver, new IntentFilter(Global.ACAO_RELATORIO_CALLBACK));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //showProgress(false);
            String response = intent.getStringExtra(RestClient.HTTP_RESPONSE);
            if(intent.getAction().equals(Global.USUARIO_LISTAR_CALLBACK)) {
                if(!response.equals("null")) {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("usuarios", Usuario.buildListUsuario(response));
                    UsuarioFragment fragment = new UsuarioFragment();
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutFragmentMainActivity, fragment)
                        .commit();
                }
            } else if (intent.getAction().equals(Global.USUARIO_REMOVER_CALLBACK)) {
                if(response.equals("\"OK\"")) {
                    Snackbar.make(getCurrentFocus(), "Usuário removido!", Snackbar.LENGTH_LONG).show();
                    setFragmentUsuarios();
                } else {
                    Snackbar.make(getCurrentFocus(), response, Snackbar.LENGTH_LONG).show();
                }
            } else if(intent.getAction().equals(Global.ACAO_RELATORIO_CALLBACK)) {
                if(response != null && !response.isEmpty() && !response.equals("null")) {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("registros", Registro.buildListRegistro(response));
                    RegistroFragment frag = new RegistroFragment();
                    frag.setArguments(args);
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayoutFragmentMainActivity, frag)
                            .commit();
                }
            }
            Log.i("MainActivity", "RESPONSE = " + response);
        }
    };

    private void setDefaultFragment() {
        setTitle("Fechadura");
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentMainActivity, new TravaFragment())
                .commit();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onCriarUsuario(String result) {
        if(result.equals("\"OK\"")) {
            UsuarioDialog frag = (UsuarioDialog) fragmentManager.findFragmentByTag("NOVOUSUARIO");
            frag.getDialog().dismiss();
            hideKeyboard();
            Snackbar.make(getCurrentFocus(), "Usuário criado com sucesso!", Snackbar.LENGTH_LONG).show();
            setFragmentUsuarios();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);
                result = "";
                JSONArray jA1 = jsonObject.getJSONArray("matricula");
                for (int j =0 ; j < jA1.length(); j++) {
                    result += jA1.getString(j) + "\r\n";
                }
                //FragmentManager fm = getFragmentManager();
                UsuarioDialog frag = (UsuarioDialog) fragmentManager.findFragmentByTag("NOVOUSUARIO");
                frag.getBaseFragmentPerfil().setErrorMessage(result);
                frag.getDialog().dismiss();
            } catch (Exception ex) {
                Log.e("MainActivity", "nao foi possivel recuperar a mensagem de erro: " + ex.getMessage());
            }
        }
    }

    @Override
    public void onEditarUsuario(String result) {
        if(result.equals("\"OK\"")) {
            Snackbar.make(getCurrentFocus(), "Usuário modificado com sucesso!", Snackbar.LENGTH_LONG).show();
            hideKeyboard();
            setDefaultFragment();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);
                result = "";
                JSONArray jA1 = jsonObject.getJSONArray("matricula");
                for (int j =0 ; j < jA1.length(); j++) {
                    result += jA1.getString(j) + "\r\n";
                }
                //FragmentManager fm = getFragmentManager();
                PerfilFragment frag = (PerfilFragment) fragmentManager.findFragmentByTag("PERFIL");
                frag.getBaseFragmentPerfil().setErrorMessage(result);
            } catch (Exception ex) {
                Log.e("MainActivity", "nao foi possivel recuperar a mensagem de erro: " + ex.getMessage());
            }
        }
    }

    @Override
    public void onUsuarioEdit(Usuario item) {
        Log.i("MainActivity", "onUsuarioEdit: " + item);
        Bundle args = new Bundle();
        args.putChar(BaseFragmentPerfil.EXTRA_TIPO_ACAO, 'e');
        args.putParcelable("usuario", item);
        UsuarioDialog nu = new UsuarioDialog();
        nu.setArguments(args);
        nu.show(fragmentManager, "EDITARUSUARIO");
    }

    @Override
    public void onUsuarioDelete(final Usuario item) {
        new AlertDialog.Builder(this)
            .setTitle("Apagar Usuário!")
            .setMessage("Confirma apagar o usuário " + item.getMatricula() + "?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    RestClient client = new RestClient(MainActivity.this, Global.USUARIO_REMOVER_CALLBACK);
                    HttpPost httpPost = new HttpPost(URI.create(Global.ENDPOINT_USUARIO_REMOVER));
                    httpPost.setEntity(item.getPostParameters());
                    client.execute(httpPost);
                }})
            .setNegativeButton(android.R.string.no, null).show();
        Log.i("MainActivity", "onUsuarioDelete: " + item);
    }
}
