package com.exemplo.produtos.service;

import com.exemplo.produtos.logger.AppLoggerComponent;
import com.exemplo.produtos.model.Produto;
import com.exemplo.produtos.repository.ProdutoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final AppLoggerComponent logger;

    public ProdutoService(ProdutoRepository repository, AppLoggerComponent logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public Produto criar(Produto produto) {
        logger.debug("Criando produto: nome=" + produto.getNome() + ", preco=" + produto.getPreco()
                + ", quantidade=" + produto.getQuantidade());
        produto.setId(null);
        Produto salvo = repository.save(produto);
        logger.info("Produto criado com id=" + salvo.getId());
        return salvo;
    }

    public List<Produto> listar() {
        List<Produto> produtos = repository.findAll();
        logger.debug("Consulta de produtos retornou " + produtos.size() + " registro(s)");
        logger.info("Listagem de produtos concluida");
        return produtos;
    }

    public Optional<Produto> buscarPorId(Long id) {
        logger.debug("Buscando produto com id=" + id);
        Optional<Produto> produto = repository.findById(id);
        if (produto.isPresent()) {
            logger.info("Produto encontrado com id=" + id);
        } else {
            logger.erro("Produto nao encontrado com id=" + id);
        }
        return produto;
    }

    public Optional<Produto> atualizar(Long id, Produto dados) {
        logger.debug("Atualizando produto com id=" + id + ": nome=" + dados.getNome()
                + ", preco=" + dados.getPreco() + ", quantidade=" + dados.getQuantidade());
        Optional<Produto> existente = repository.findById(id);
        if (existente.isEmpty()) {
            logger.erro("Produto nao encontrado para atualizacao com id=" + id);
            return Optional.empty();
        }
        Produto produto = existente.get();
        produto.setNome(dados.getNome());
        produto.setDescricao(dados.getDescricao());
        produto.setPreco(dados.getPreco());
        produto.setQuantidade(dados.getQuantidade());
        Produto salvo = repository.save(produto);
        logger.info("Produto atualizado com id=" + id);
        return Optional.of(salvo);
    }

    public boolean excluir(Long id) {
        logger.debug("Excluindo produto com id=" + id);
        if (!repository.existsById(id)) {
            logger.erro("Produto nao encontrado para exclusao com id=" + id);
            return false;
        }
        repository.deleteById(id);
        logger.info("Produto excluido com id=" + id);
        return true;
    }
}
