package br.ufba.engc50.bluelock.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.ufba.engc50.bluelock.PerfilFragment;
import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.SobreFragment;
import br.ufba.engc50.bluelock.TravaFragment;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

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
        menu.setGroupVisible(R.id.grupoAdministracao, getIntent().getIntExtra("tipoUsuario", 1) == MicrocontrollerActions.TIPO_USUARIO_ADMIN);
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

        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_relatorio) {

        } else if (id == R.id.nav_usuarios) {
            //content = new Intent(this, UsuariosActivity.class);
        } else if (id == R.id.nav_perfil) {
            Bundle args = new Bundle();
            args.putChar(PerfilFragment.EXTRA_TIPO_ACAO, 'e');
            PerfilFragment perfilFragment = new PerfilFragment();
            perfilFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutFragmentMainActivity, perfilFragment)
                    .commit();
        } else if (id == R.id.nav_trava) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutFragmentMainActivity, new TravaFragment())
                    .commit();
        } else if (id == R.id.nav_sobre) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutFragmentMainActivity, new SobreFragment())
                    .commit();
        } else if (id == R.id.nav_sair) {
            MicrocontrollerActions.logOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
