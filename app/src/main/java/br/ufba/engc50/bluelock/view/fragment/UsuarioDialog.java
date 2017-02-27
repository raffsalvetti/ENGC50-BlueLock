package br.ufba.engc50.bluelock.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.model.Usuario;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;

/**
 * Created by raffaello.salvetti on 13/02/2017.
 */

public class UsuarioDialog extends DialogFragment {

    private BaseFragmentPerfil baseFragmentPerfil;
    private BaseFragmentPerfil.BFEventListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseFragmentPerfil = new BaseFragmentPerfil(this);
        baseFragmentPerfil.setBFEventListener(listener);
        if(getArguments().getChar(BaseFragmentPerfil.EXTRA_TIPO_ACAO, 'e') == 'e') {
            getDialog().setTitle("Editar Usuário");
        } else {
            getDialog().setTitle("Novo Usuário");
        }
        return baseFragmentPerfil.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (BaseFragmentPerfil.BFEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        this.baseFragmentPerfil.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.baseFragmentPerfil.onPause();
    }

    public BaseFragmentPerfil getBaseFragmentPerfil(){
        return this.baseFragmentPerfil;
    }
}
