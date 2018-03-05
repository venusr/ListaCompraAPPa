package br.com.fiap.listacompraappa.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.listacompraappa.MainActivity;
import br.com.fiap.listacompraappa.R;
import br.com.fiap.listacompraappa.model.CursoItem;
import br.com.fiap.listacompraappa.model.Produto;

public class ProdutoRecyclerAdapter extends RecyclerView.Adapter<ProdutoViewHolder> {
    private List<Produto> mLista;
    public static int selected_item = -1;

    public static SparseBooleanArray selectedItems;

    public ProdutoRecyclerAdapter(List<Produto> lista) {
        mLista = lista;
    }

    public List<Produto> getListaProdutos() {
       return mLista;
    }

    public ProdutoRecyclerAdapter() {

    }


    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_produtos, parent, false);
        return new ProdutoViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ProdutoViewHolder holder, int position) {
        Produto produto = mLista.get(position);
        holder.txViewNome.setText(produto.getNome());
        holder.txViewQtde.setText(produto.getQtde());
    //    selectedItems.get(position, false);
    }

    @Override
    public int getItemCount() {
        return mLista != null ? mLista.size() : 0;
    }

    public void setfilter(List<Produto> produtoItem) {
        System.out.println("Passou aqui no setFilter");
        List<Produto> mListaSearch = new ArrayList<>();
        mListaSearch.addAll(produtoItem);
        notifyDataSetChanged();
    }

    public Produto getItem(int position) {
        return mLista.get(position);
    }

    public void updateList(Produto produto) {
        insertItem(produto);
    }

    private void insertItem(Produto produto) {
     System.out.println("entrou no insertitem : " + produto.getNome());
        mLista.add(produto);
        notifyItemInserted(getItemCount());
    }


    //para atualizar alguem j[a existente na lista
    public void updateItem(int position, Produto produtoAlterado) {
        Produto produto = mLista.get(position);
        produto.setNome(produtoAlterado.getNome());
        produto.setQtde(produtoAlterado.getQtde());
        notifyItemChanged(position);
    }

    //para remover objetos da lista
    public void deleteItem(int position) {
        mLista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mLista.size());
    }

}