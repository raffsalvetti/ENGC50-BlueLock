package br.ufba.engc50.bluelock.view.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.client.methods.HttpPost;

import java.net.URI;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.local.Global;
import br.ufba.engc50.bluelock.model.Usuario;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;
import br.ufba.engc50.bluelock.remote.RestClient;
import br.ufba.engc50.bluelock.remote.TipoUsuario;

/**
 * Created by raffaello.salvetti on 13/02/2017.
 */

public class BaseFragmentPerfil {
    public static String EXTRA_TIPO_ACAO = "tipoAcao";

    private Fragment fragment;

    private TextView textViewMatricula, textViewMensagemErro;
    private EditText editTextMatricula, editTextPassword, editTextPasswordConfirmacao;
    private Spinner spinnerTipoUsuario;
    private Button buttonAction;
    private BFEventListener listener;

    public BaseFragmentPerfil(Fragment fragment, BFEventListener listener) {
        this(fragment);
        this.listener = listener;
    }

    public BaseFragmentPerfil(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setBFEventListener(BFEventListener listener){
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        textViewMatricula = (TextView)v.findViewById(R.id.textViewMatricula);
        editTextMatricula = (EditText)v.findViewById(R.id.editTextMatricula);
        editTextPassword = (EditText)v.findViewById(R.id.editTextPassword);
        editTextPasswordConfirmacao = (EditText)v.findViewById(R.id.editTextPasswordConfirmacao);
        spinnerTipoUsuario = (Spinner)v.findViewById(R.id.spinnerTipoUsuario);
        textViewMensagemErro = (TextView)v.findViewById(R.id.textViewMensagemErro);
        buttonAction = (Button)v.findViewById(R.id.buttonSalvarPerfil);

        spinnerTipoUsuario.setAdapter(new ArrayAdapter<TipoUsuario>(fragment.getActivity(), android.R.layout.simple_spinner_item, TipoUsuario.values()));

        char c = this.fragment.getArguments().getChar(BaseFragmentPerfil.EXTRA_TIPO_ACAO, 'e');
        if(c == 'c') { //criar
            textViewMatricula.setVisibility(View.GONE);
            editTextMatricula.setVisibility(View.VISIBLE);
            buttonAction.setText("Criar");
            buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //criar novo usuario
                    String erro = null;
                    if( (erro = validarMatricula()) == null && (erro = validarPassword()) == null) {
                        Usuario u = new Usuario(editTextMatricula.getText().toString(), editTextPassword.getText().toString(), ((TipoUsuario) spinnerTipoUsuario.getSelectedItem()).getCodigo());
                        RestClient client = new RestClient(fragment.getActivity(), Global.USUARIO_ADICIONAR_CALLBACK);
                        HttpPost httpPost = new HttpPost(URI.create(Global.ENDPOINT_USUARIO_ADICIONAR));
                        httpPost.setEntity(u.getPostParameters());
                        client.execute(httpPost);
                        //new MicrocontrollerActions().criarUsuario(u);
                        //fimDeModificacao(view);
                    } else {
                        setErrorMessage(erro);
                    }
                }
            });
        } else if (c == 'e' || c == 'p') { //editar ou perfil
            final Usuario u = fragment.getArguments().getParcelable("usuario");
            textViewMatricula.setVisibility(View.VISIBLE);
            editTextMatricula.setVisibility(View.GONE);
            textViewMatricula.setText(String.valueOf(u.getMatricula()));
            spinnerTipoUsuario.setSelection( TipoUsuario.getByCodigo(u.getTipoUsuario()).ordinal() );
            spinnerTipoUsuario.setEnabled(Global.getUsuarioLogado().isAdministrador());
            buttonAction.setText("Salvar");
            buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //salvar usuario
                    String erro = null;
                    if( (erro = validarPassword()) == null) {
                        u.setSenha(editTextPassword.getText().toString());
                        u.setTipoUsuario( ((TipoUsuario) spinnerTipoUsuario.getSelectedItem()).getCodigo() );
                        RestClient client = new RestClient(fragment.getActivity(), Global.USUARIO_EDITAR_CALLBACK);
                        HttpPost httpPost = new HttpPost(URI.create(Global.ENDPOINT_USUARIO_EDITAR));
                        httpPost.setEntity(u.getPostParameters());
                        client.execute(httpPost);
                    } else {
                        setErrorMessage(erro);
                    }
                }
            });
        }
        return v;
    }

    public void setErrorMessage(String errorMessage){
        textViewMensagemErro.setText(errorMessage);
        textViewMensagemErro.setVisibility(View.VISIBLE);
    }

    private String validarPassword(){
        if(editTextPassword.getText().length() < 4)
            return "A senha deve ter 4 digitos no mínimo!";
        if(!editTextPassword.getText().toString().equals(editTextPasswordConfirmacao.getText().toString()))
            return "As senhas não são iguais!";
        return null;
    }

    private String validarMatricula(){
        if(editTextMatricula.getText().length() < 4)
            return "A matrícula deve ter 4 digitos no mínimo!";
        return null;
    }

    private void fimDeModificacao(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(this.fragment.getArguments().getChar(BaseFragmentPerfil.EXTRA_TIPO_ACAO, 'e') == 'c'){
            Snackbar.make(view, "Usuário criado com sucesso!", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view, "Usuário modificado com sucesso!", Snackbar.LENGTH_LONG).show();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(RestClient.HTTP_RESPONSE);
            if(intent.getAction().equals(Global.USUARIO_ADICIONAR_CALLBACK)) {
                if(listener != null)
                    listener.onCriarUsuario(response);
            } else if(intent.getAction().equals(Global.USUARIO_EDITAR_CALLBACK)) {
                if(listener != null)
                    listener.onEditarUsuario(response);
            }
            Log.i("BaseFragmentPerfil", "RESPONSE = " + response);
        }
    };

    protected void onResume() {
        fragment.getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Global.USUARIO_EDITAR_CALLBACK));
        fragment.getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Global.USUARIO_ADICIONAR_CALLBACK));
    }

    protected void onPause() {
        fragment.getActivity().unregisterReceiver(broadcastReceiver);
    }

    public interface BFEventListener {
        void onCriarUsuario(String result);
        void onEditarUsuario(String result);
    }
}
