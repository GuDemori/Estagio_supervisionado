package builders;

import entities.Cliente;

public class ClienteBuilder {

    private String nomeEstabelecimento;
    private String cidade;
    private String endereco;

    public ClienteBuilder setNomeEstabelecimento(String nomeEstabelecimento){
        this.nomeEstabelecimento = nomeEstabelecimento;
        return this;
    }

    public ClienteBuilder setCidade(String cidade){
        this.cidade = cidade;
        return this;
    }

    public ClienteBuilder setEndereco(String endereco){
        this.endereco = endereco;
        return this;
    }

    public Cliente build(){
        if (nomeEstabelecimento.isEmpty()){
            System.out.println("O nome do estabelecimento é obrigatório");
        }
        if (cidade.isEmpty()){
            System.out.println("A cidade é um campo obrigatório");
        }
        return new Cliente(nomeEstabelecimento, cidade, endereco);
    }

}
