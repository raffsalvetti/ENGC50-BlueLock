package br.ufba.engc50.bluelock.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.ufba.engc50.bluelock.PerfilFragment;
import br.ufba.engc50.bluelock.R;

public class UsuariosActivity extends AppCompatActivity {

    private Button buttonAdicionarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        buttonAdicionarUsuario = (Button)findViewById(R.id.buttonAdicionarUsuario);
        buttonAdicionarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UsuariosActivity.this, PerfilFragment.class).putExtra(PerfilFragment.EXTRA_TIPO_ACAO, 'c'));
            }
        });
    }

}
