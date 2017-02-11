package br.ufba.engc50.bluelock;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import br.ufba.engc50.bluelock.model.Usuario;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;
import br.ufba.engc50.bluelock.view.MainActivity;

public class PerfilFragment extends Fragment {

    public static String EXTRA_TIPO_ACAO = "tipoAcao";

    private TextView textViewMatricula, textViewMensagemErro;
    private EditText editTextMatricula, editTextPassword, editTextPasswordConfirmacao;
    private ToggleButton toggleButtonAdministrador;
    private Button buttonAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        textViewMatricula = (TextView)v.findViewById(R.id.textViewMatricula);
        editTextMatricula = (EditText)v.findViewById(R.id.editTextMatricula);
        editTextPassword = (EditText)v.findViewById(R.id.editTextPassword);
        editTextPasswordConfirmacao = (EditText)v.findViewById(R.id.editTextPasswordConfirmacao);
        toggleButtonAdministrador = (ToggleButton)v.findViewById(R.id.toggleButtonAdministrador);
        textViewMensagemErro = (TextView)v.findViewById(R.id.textViewMensagemErro);
        buttonAction = (Button)v.findViewById(R.id.buttonSalvarPerfil);

        char c = getArguments().getChar(PerfilFragment.EXTRA_TIPO_ACAO, 'e');
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
                        Usuario u = new Usuario(editTextMatricula.getText().toString(), editTextPassword.getText().toString(), (toggleButtonAdministrador.isChecked()? MicrocontrollerActions.TIPO_USUARIO_ADMIN:MicrocontrollerActions.TIPO_USUARIO_USER));
                        new MicrocontrollerActions().criarUsuario(u);
                    } else {
                        textViewMensagemErro.setText(erro);
                        textViewMensagemErro.setVisibility(View.VISIBLE);
                    }
                    fimDeModificacao(view);
                }
            });
        } else if (c == 'e') { //editar
            textViewMatricula.setVisibility(View.VISIBLE);
            editTextMatricula.setVisibility(View.GONE);
            textViewMatricula.setText(String.valueOf(MicrocontrollerActions.getUsuarioLogado().getMatricula()));
            toggleButtonAdministrador.setEnabled(MicrocontrollerActions.getUsuarioLogado().isAdministrador());
            buttonAction.setText("Salvar");
            buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //salvar usuario
                    String erro = null;
                    if( (erro = validarPassword()) == null) {
                        Usuario u = MicrocontrollerActions.getUsuarioLogado();
                        u.setSenha(editTextPassword.getText().toString());
                        u.setTipoUsuario((toggleButtonAdministrador.isChecked()?MicrocontrollerActions.TIPO_USUARIO_ADMIN:MicrocontrollerActions.TIPO_USUARIO_USER));
                        new MicrocontrollerActions().modificarUsuario(u);
                    } else {
                        textViewMensagemErro.setText(erro);
                        textViewMensagemErro.setVisibility(View.VISIBLE);
                    }
                    fimDeModificacao(view);
                }
            });
        }

        return v;
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

    private void fimDeModificacao(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(getArguments().getChar(PerfilFragment.EXTRA_TIPO_ACAO, 'e') == 'c'){
            Snackbar.make(view, "Usuário criado com sucesso!", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view, "Usuário modificado com sucesso!", Snackbar.LENGTH_LONG).show();
        }
    }

}
