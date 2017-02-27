package br.ufba.engc50.bluelock.view.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.model.adapter.UsuarioRecyclerViewAdapter;
import br.ufba.engc50.bluelock.local.Global;
import br.ufba.engc50.bluelock.model.Usuario;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UsuarioFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private View view;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UsuarioFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UsuarioFragment newInstance(int columnCount) {
        UsuarioFragment fragment = new UsuarioFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario_list, container, false);
        View recycleList = view.findViewById(R.id.recyclerListUsuario);
        final FragmentManager fm = getFragmentManager();
        Button buttonAdicionarUsuario = (Button)view.findViewById(R.id.buttonAdicionarUsuario);
        buttonAdicionarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putChar(BaseFragmentPerfil.EXTRA_TIPO_ACAO, 'c');
                UsuarioDialog nu = new UsuarioDialog();
                nu.setArguments(args);
                nu.show(fm, "NOVOUSUARIO");
            }
        });
        if (recycleList instanceof RecyclerView) {
            Context context = recycleList.getContext();
            RecyclerView recyclerView = (RecyclerView) recycleList;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ArrayList<Usuario> usuarios = getArguments().getParcelableArrayList("usuarios");
            usuarios.remove(Global.getUsuarioLogado());
            recyclerView.setAdapter(new UsuarioRecyclerViewAdapter(usuarios, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onUsuarioEdit(Usuario item);
        void onUsuarioDelete(Usuario item);
    }
}
