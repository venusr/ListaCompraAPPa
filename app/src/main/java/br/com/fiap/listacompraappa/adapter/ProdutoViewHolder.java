package br.com.fiap.listacompraappa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.fiap.listacompraappa.R;

public class ProdutoViewHolder extends RecyclerView.ViewHolder {
    TextView txViewNome;
    TextView txViewQtde;

    public ProdutoViewHolder(View itemView) {
        super(itemView);

        txViewNome = (TextView) itemView.findViewById(R.id.txNome);
        txViewQtde = (TextView) itemView.findViewById(R.id.txQtde);
    }
}
