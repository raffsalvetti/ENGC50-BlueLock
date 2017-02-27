package br.ufba.engc50.bluelock.view.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.apache.http.client.methods.HttpPost;

import java.net.URI;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.local.Global;
import br.ufba.engc50.bluelock.model.Usuario;
import br.ufba.engc50.bluelock.remote.RestClient;
import br.ufba.engc50.bluelock.remote.TipoUsuario;

public class PerfilFragment extends Fragment {

    private BaseFragmentPerfil baseFragmentPerfil;
    private BaseFragmentPerfil.BFEventListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.baseFragmentPerfil = new BaseFragmentPerfil(this);
        this.baseFragmentPerfil.setBFEventListener(this.listener);
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
