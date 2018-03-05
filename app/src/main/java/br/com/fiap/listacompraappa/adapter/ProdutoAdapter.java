package br.com.fiap.listacompraappa.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.fiap.listacompraappa.R;
import br.com.fiap.listacompraappa.model.Produto;

/**
 * Created by Cecilia_2 on 11/02/2018.
 */

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolderProduto> {

    private List<Produto> produtos;
    public static int item_selecionado =0;

    public ProdutoAdapter(List<Produto> produtos) {
        this.produtos = produtos;

        System.out.println("entrou produtoAdapter");

        if ((produtos != null) && (produtos.size() > 0)) {
            System.out.println("Tentando pegar produto na produtoAdapter: " + produtos.get(0).getNome());
        } else   System.out.println("produtoAdapter esta vazia: " );

    }

    @Override
    public ProdutoAdapter.ViewHolderProduto onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.linha_produtos, parent, false);

        ViewHolderProduto holderProduto = new ViewHolderProduto((view));

        System.out.println("entrou produtoAdapter Viewholder");


        return holderProduto;

    }

    @Override
    public void onBindViewHolder(ProdutoAdapter.ViewHolderProduto holder, int position) {

        System.out.println("entrou inbindviewholder");

        System.out.println("position onbind: " + position);

        int i = 0;
        for (i = 0 ; i < produtos.size();i++) {
            System.out.println("position onbind: " + position);
            System.out.println("position i: " + i);

            System.out.println("Produto onbind: " + produtos.get(i).getNome());

            if (i == position ) {
                Produto produto = produtos.get(position);
                System.out.println("Produto da position");
                System.out.println(produto.getNome());

            }
        }

        if (holder == null) System.out.println("Holder esta null");


        if ((produtos != null) && (produtos.size() > 0)) {
            Produto produto = produtos.get(position);
            System.out.println("dentro if onbindholder");

            System.out.println("produto depois de criar a classe: ");
            System.out.println("produto: " + produto.getNome());

            holder.txNome.setText(produto.getNome());
            holder.txQtde.setText(produto.getQtde());
            System.out.println("dentro if onbindholder2");

        }

        if (position == item_selecionado) {
            holder.txNome.setTextColor(Color.RED);
        }
        else {
            holder.txNome.setTextColor(Color.BLACK);
        }

        System.out.println("saiu onbindviewholder");
    }

    @Override
    public int getItemCount() {
        System.out.println("entrou count");
        return produtos.size();
    }

    public static class ViewHolderProduto extends RecyclerView.ViewHolder {

        public TextView txNome;
        public TextView txQtde;

        public ViewHolderProduto(View v) {
            super(v);

            System.out.println("entrou viewholderproduto");

            txNome = (TextView) v.findViewById(R.id.txNome);
            txNome = (TextView) v.findViewById(R.id.txQtde);


        }
    }

}
