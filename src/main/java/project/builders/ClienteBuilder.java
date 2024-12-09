package project.builders;

import project.entities.Client;

public class ClienteBuilder {

    private String nomeEstabelecimento;
    private String cidade;
    private String endereco;
    private String login;
    private String senha;
    private String documentoIdentificacao;

    public ClienteBuilder setNomeEstabelecimento(String nomeEstabelecimento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
        return this;
    }

    public ClienteBuilder setCidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public ClienteBuilder setEndereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public ClienteBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public ClienteBuilder setSenha(String senha) {
        this.senha = senha;
        return this;
    }

    public ClienteBuilder setDocumentoIdentificacao(String documentoIdentificacao) {
        this.documentoIdentificacao = documentoIdentificacao;
        return this;
    }

    public Client build() {
        if (nomeEstabelecimento == null || nomeEstabelecimento.isEmpty()) {
            throw new IllegalArgumentException("O nome do estabelecimento é obrigatório.");
        }
        if (cidade == null || cidade.isEmpty()) {
            throw new IllegalArgumentException("A cidade é um campo obrigatório.");
        }
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("O login é obrigatório.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        if (documentoIdentificacao == null || documentoIdentificacao.isEmpty()) {
            throw new IllegalArgumentException("O documento de identificação é obrigatório.");
        }

        return new Client(
                nomeEstabelecimento,
                cidade,
                endereco,
                login,
                senha,
                documentoIdentificacao
        );
    }
}
