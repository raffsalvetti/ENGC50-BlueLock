package br.ufba.engc50.bluelock.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.URI;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.local.Global;
import br.ufba.engc50.bluelock.model.Registro;
import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;
import br.ufba.engc50.bluelock.remote.RestClient;

public class TravaFragment extends Fragment implements MicrocontrollerActions.MicrocontrollerActionsEvent {

    private MicrocontrollerActions microcontrollerActions;
    private TextView textViewStatusTranca;
    private ImageButton abrir, fechar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        microcontrollerActions = new MicrocontrollerActions(getActivity());
        microcontrollerActions.addListener(this);
        microcontrollerActions.findBT();

        View v = inflater.inflate(R.layout.fragment_trava, container, false);
        textViewStatusTranca = (TextView)v.findViewById(R.id.textViewTrancaStatus);
        fechar = (ImageButton)v.findViewById(R.id.imageButtonFechar);
        abrir = (ImageButton)v.findViewById(R.id.imageButtonAbrir);
        abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarAcao(new Registro(0, null, "Abriu a tranca."));
            }
        });

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarAcao(new Registro(0, null, "Fechou a tranca."));
            }
        });

        updateButtons();
        return v;
    }

    private void registrarAcao(Registro registro) {
        RestClient client = new RestClient(getActivity(), Global.ACAO_REGISTRAR_CALLBACK);
        HttpPost httpPost = new HttpPost(URI.create(Global.ENDPOINT_ACAO_REGISTRAR));
        httpPost.setEntity(registro.getPostParameters());
        client.execute(httpPost);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(RestClient.HTTP_RESPONSE);
            if(intent.getAction().equals(Global.ACAO_REGISTRAR_CALLBACK)) {
                if(response != null && !response.isEmpty() && response.equals("\"OK\"")) {
                    microcontrollerActions.toggleTranca();
                    updateButtons();
                    Snackbar.make(getActivity().getCurrentFocus(), "Agora a tranca está " + (microcontrollerActions.getStatusTranca() ? "fechada" : "aberta") + "!", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Global.ACAO_REGISTRAR_CALLBACK));
        microcontrollerActions.openBT();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
        microcontrollerActions.closeBT();
    }

    private void updateButtons() {
        textViewStatusTranca.setText("Use o botão para " + (microcontrollerActions.getStatusTranca() ? "abrir" : "fechar") + " a tranca!");
        if(microcontrollerActions.getStatusTranca()){
            fechar.setVisibility(View.GONE);
            abrir.setVisibility(View.VISIBLE);
        } else {
            fechar.setVisibility(View.VISIBLE);
            abrir.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBluetoothEvent(String event) {
        Log.i("TravaFragment", "onBluetoothEvent: " + event);
    }

    @Override
    public void onBluetoothInput(String data) {
        Log.i("TravaFragment", "onBluetoothInput: " + data);
    }

    @Override
    public void onBluetoothError(Exception ex) {
        Log.i("TravaFragment", "onBluetoothError: " + ex.getMessage());
    }
}
