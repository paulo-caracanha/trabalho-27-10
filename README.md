# Produtos API

API REST com Spring Boot, Spring Data JPA e H2 (em memória) para o CRUD de `Produto`, com logger Singleton.

## Estrutura

```
produtos-api/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/exemplo/produtos/
    │   │   ├── ProdutosApplication.java
    │   │   ├── model/Produto.java
    │   │   ├── repository/ProdutoRepository.java
    │   │   ├── service/ProdutoService.java
    │   │   ├── controller/ProdutoController.java
    │   │   └── logger/
    │   │       ├── AppLogger.java
    │   │       └── AppLoggerComponent.java
    │   └── resources/application.properties
    └── test/java/com/exemplo/produtos/logger/AppLoggerTest.java
```

## Como executar

Requisitos: Java 21 e Maven.

```
mvn clean test
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. O arquivo `app.log` é criado na pasta em que a aplicação é executada, na primeira requisição.

## Exemplos de requisições

```
curl -i -X POST http://localhost:8080/produtos -H "Content-Type: application/json" -d '{"nome":"Teclado","descricao":"Teclado USB","preco":89.9,"quantidade":10}'
curl -i http://localhost:8080/produtos
curl -i http://localhost:8080/produtos/1
curl -i -X PUT http://localhost:8080/produtos/1 -H "Content-Type: application/json" -d '{"nome":"Teclado Mecanico","descricao":"Teclado USB","preco":149.9,"quantidade":8}'
curl -i -X DELETE http://localhost:8080/produtos/1
curl -i http://localhost:8080/produtos/999
```

Status esperados: POST 201, GET lista 200, GET por id 200 ou 404, PUT 200 ou 404, DELETE 204 ou 404.

## Perguntas teóricas

**1. Quem garante a instância única em cada versão?**

No Singleton clássico, é a própria classe: o construtor é privado e `getInstance()` cria o objeto uma única vez, com `volatile` e dupla checagem para ser thread-safe. Na versão com `@Component`, é o contêiner do Spring: ele cria um único bean (escopo `singleton` por padrão) e injeta essa mesma instância onde ela é pedida.

**2. Quando o Singleton clássico ainda faz sentido?**

Quando o código roda fora de um contêiner de injeção de dependência, como em uma aplicação Java simples, uma biblioteca ou uma classe utilitária que precisa ser acessada de qualquer lugar. Dentro de uma aplicação Spring ele faz menos sentido, porque o próprio Spring já resolve o problema e ainda facilita testes e troca de implementação.

**Diferença resumida:** no Singleton manual, a classe controla a própria criação e quem usa chama `getInstance()`, o que cria dependência direta entre as classes. No Spring, a classe é comum e quem controla a criação e o ciclo de vida é o contêiner, que entrega o objeto pronto pelo construtor.

## Logger escolhido na versão final

O `ProdutoService` e o `ProdutoController` usam `AppLoggerComponent` (versão Spring), injetado pelo construtor. Motivos:

- A aplicação já é gerenciada pelo Spring, então a injeção de dependência é o caminho natural e evita acoplamento direto com uma chamada estática.
- Fica mais fácil de testar, pois o logger pode ser substituído por um mock.
- Usar só uma versão evita registros duplicados no `app.log`.

O `AppLogger` (Singleton clássico) continua no projeto como a primeira versão pedida na atividade e para a comparação, mas não é chamado pela aplicação. Para usá-lo no lugar, basta trocar o tipo do campo por `AppLogger` e atribuir `AppLogger.getInstance()` no construtor do Service e do Controller.

## Publicar no GitHub

```
git init
git add .
git commit -m "CRUD de produtos com logger Singleton"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/produtos-api.git
git push -u origin main
```

Crie antes o repositório vazio `produtos-api` no GitHub. Depois de rodar a aplicação e testar as rotas, inclua o `app.log` gerado no commit, já que a entrega pede um exemplo.
