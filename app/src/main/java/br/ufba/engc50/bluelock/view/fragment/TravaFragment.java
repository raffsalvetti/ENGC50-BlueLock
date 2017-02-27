package br.ufba.engc50.bluelock.view.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;

public class TravaFragment extends Fragment {

    private TextView textViewStatusTranca;
    private ImageButton abrir, fechar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trava, container, false);
        textViewStatusTranca = (TextView)v.findViewById(R.id.textViewTrancaStatus);
        fechar = (ImageButton)v.findViewById(R.id.imageButtonFechar);
        abrir = (ImageButton)v.findViewById(R.id.imageButtonAbrir);
        abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MicrocontrollerActions.togglrTranca();
                updateButtons();
            }
        });

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MicrocontrollerActions.togglrTranca();
                updateButtons();
            }
        });

        updateButtons();
        return v;
    }

    private void updateButtons() {
        textViewStatusTranca.setText("A tranca está " + (MicrocontrollerActions.getStatusTranca() ? "fechada" : "aberta"));
        if(MicrocontrollerActions.getStatusTranca()){
            fechar.setVisibility(View.GONE);
            abrir.setVisibility(View.VISIBLE);
        } else {
            fechar.setVisibility(View.VISIBLE);
            abrir.setVisibility(View.GONE);
        }
    }

}
