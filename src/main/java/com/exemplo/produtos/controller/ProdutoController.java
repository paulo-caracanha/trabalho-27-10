package com.exemplo.produtos.controller;

import com.exemplo.produtos.logger.AppLoggerComponent;
import com.exemplo.produtos.model.Produto;
import com.exemplo.produtos.service.ProdutoService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final AppLoggerComponent logger;

    public ProdutoController(ProdutoService service, AppLoggerComponent logger) {
        this.service = service;
        this.logger = logger;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        logger.debug("Requisicao POST /produtos recebida");
        Produto criado = service.criar(produto);
        logger.info("POST /produtos respondeu 201 para id=" + criado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        logger.debug("Requisicao GET /produtos recebida");
        List<Produto> produtos = service.listar();
        logger.info("GET /produtos respondeu 200 com " + produtos.size() + " item(ns)");
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        logger.debug("Requisicao GET /produtos/" + id + " recebida");
        Optional<Produto> produto = service.buscarPorId(id);
        if (produto.isEmpty()) {
            logger.erro("GET /produtos/" + id + " respondeu 404");
            return ResponseEntity.notFound().build();
        }
        logger.info("GET /produtos/" + id + " respondeu 200");
        return ResponseEntity.ok(produto.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        logger.debug("Requisicao PUT /produtos/" + id + " recebida");
        Optional<Produto> atualizado = service.atualizar(id, produto);
        if (atualizado.isEmpty()) {
            logger.erro("PUT /produtos/" + id + " respondeu 404");
            return ResponseEntity.notFound().build();
        }
        logger.info("PUT /produtos/" + id + " respondeu 200");
        return ResponseEntity.ok(atualizado.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        logger.debug("Requisicao DELETE /produtos/" + id + " recebida");
        if (!service.excluir(id)) {
            logger.erro("DELETE /produtos/" + id + " respondeu 404");
            return ResponseEntity.notFound().build();
        }
        logger.info("DELETE /produtos/" + id + " respondeu 204");
        return ResponseEntity.noContent().build();
    }
}
