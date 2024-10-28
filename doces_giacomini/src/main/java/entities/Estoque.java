package entities;

import entities.Produto;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashMap;
@Entity
@Table(name = "estoque")
public class Estoque {
    @OneToMany(mappedBy = "estoque")
    private HashMap<Produto, Integer> produtos;

    public Estoque(){
        produtos = new HashMap<>();
    }

    public void adicionarProduto(Produto produto, int quantidade){
        produtos.put(produto, quantidade);
    }

    public void atualizarEstoque(Produto produto, int quantidadeVendida){
        if(produtos.containsKey(produto)){
            produto.reduzirQuantidade(quantidadeVendida);
        }
    }

    public void exibirEstoque(){
        produtos.forEach((produto, quantidade) ->
                System.out.println(produto.getNomeProduto() + " - Estoque: " + quantidade));
    }

}
