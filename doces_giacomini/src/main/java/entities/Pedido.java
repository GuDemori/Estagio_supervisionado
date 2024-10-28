package entities;

import entities.Cliente;
import entities.Estoque;
import entities.Produto;

import java.util.ArrayList;

public class Pedido {

    private Cliente cliente;
    private ArrayList<Produto> produtos;

    public Pedido(Cliente cliente){
        this.cliente = cliente;
        this.produtos = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto){
        produtos.add(produto);
    }

    public double calcularValorTotal(){
        return produtos.stream().mapToDouble(Produto::getPreco).sum();
    }

    public void finalizarPedido(Estoque estoque){
        for (Produto produto : produtos){
            estoque.atualizarEstoque(produto, 1);
        }
        System.out.println("Pedido finalizado para " + cliente.getNomeEstabelecimento());
    }

}

