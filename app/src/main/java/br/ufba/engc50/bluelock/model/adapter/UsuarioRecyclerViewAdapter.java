package br.ufba.engc50.bluelock.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.model.Usuario;
import br.ufba.engc50.bluelock.remote.TipoUsuario;
import br.ufba.engc50.bluelock.view.fragment.UsuarioFragment;

import java.util.List;

public class UsuarioRecyclerViewAdapter extends RecyclerView.Adapter<UsuarioRecyclerViewAdapter.ViewHolder> {

    private final List<Usuario> mValues;
    private final UsuarioFragment.OnListFragmentInteractionListener mListener;

    public UsuarioRecyclerViewAdapter(List<Usuario> items, UsuarioFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getMatricula()));
        holder.mContentView.setText(TipoUsuario.getByCodigo(mValues.get(position).getTipoUsuario()).getDescricao());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onUsuarioEdit(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Usuario mItem;
        public ImageButton imageButtonEditar, imageButtonExcluir;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.textViewListaMatricula);
            mContentView = (TextView) view.findViewById(R.id.textViewListaTipoUsuario);
            imageButtonEditar = (ImageButton) view.findViewById(R.id.imageButtonEdit);
            imageButtonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null)
                        mListener.onUsuarioEdit(mItem);
                }
            });
            imageButtonExcluir = (ImageButton) view.findViewById(R.id.imageButtonDelete);
            imageButtonExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null)
                        mListener.onUsuarioDelete(mItem);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
