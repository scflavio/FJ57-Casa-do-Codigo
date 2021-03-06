package br.com.caelum.casadocodigo.fragment;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import br.com.caelum.casadocodigo.R;
import br.com.caelum.casadocodigo.application.CasaDoCodigoApplication;
import br.com.caelum.casadocodigo.modelo.Carrinho;
import br.com.caelum.casadocodigo.modelo.Item;
import br.com.caelum.casadocodigo.modelo.Livro;
import br.com.caelum.casadocodigo.modelo.TipoDeCompra;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetalhesLivroFragment extends Fragment {

    @BindView(R.id.detalhes_livro_nome)
    TextView nome;

    @BindView(R.id.detalhes_livro_foto)
    ImageView foto;

    @BindView(R.id.detalhes_livro_descricao)
    TextView descricao;

    @BindView(R.id.detalhes_livro_num_paginas)
    TextView numPaginas;

    @BindView(R.id.detalhes_livro_data_publicacao)
    TextView dataPublicacao;

    @BindView(R.id.detalhes_livro_isbn)
    TextView isbn;

    @Inject
    Carrinho carrinho;

    public static DetalhesLivroFragment com(Livro selecionado) {

        DetalhesLivroFragment detalhes = new DetalhesLivroFragment();

        Bundle argumentos = new Bundle();

        argumentos.putSerializable("livro", selecionado);

        detalhes.setArguments(argumentos);

        return detalhes;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Livro livro = getLivro();

        CasaDoCodigoApplication.getInstance().getComponent().injeta(this);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        actionBar.setTitle(livro.getNome());
        actionBar.setSubtitle(livro.getUrlFoto());

        actionBar.setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalhes_livro,
                container, false);

        ButterKnife.bind(this, view);

        Livro livro = getLivro();

        nome.setText(livro.getNome());

        Picasso.get().load(livro.getUrlFoto()).fit().into(foto);

        descricao.setText(livro.getDescricao());

        numPaginas.setText(livro.getNumPaginas());

        dataPublicacao.setText(livro.getDataPublicacao());

        isbn.setText(livro.getISBN());

        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().onBackPressed();
        }

        return true;
    }

    @OnClick(
            {
                    R.id.detalhes_livro_comprar_fisico,
                    R.id.detalhes_livro_comprar_ambos,
                    R.id.detalhes_livro_comprar_ebook
            }
    )
    public void compraLivro(View button) {


        carrinho.adiciona(new Item(getLivro(), defineTipoDeCompra(button)));

        Toast.makeText(getContext(), "Livro adicionado no carrinho", Toast.LENGTH_LONG).show();
    }

    private TipoDeCompra defineTipoDeCompra(View button) {
        switch (button.getId()) {

            case R.id.detalhes_livro_comprar_ambos:
                return TipoDeCompra.JUNTOS;
            case R.id.detalhes_livro_comprar_ebook:
                return TipoDeCompra.VIRTUAL;
            default:
                return TipoDeCompra.FISICO;
        }
    }


    private Livro getLivro() {
        Bundle arguments = getArguments();
        return (Livro) arguments.get("livro");
    }

}
