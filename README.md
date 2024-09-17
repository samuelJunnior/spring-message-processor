# Mensageria RabbitMQ e Spring Boot

>Uma API REST em Spring Boot que implementa um fluxo de mensageria utilizando RabbitMQ.

**Sumário**
- [Flow](#-flow)
- [Tecnologias](#-tecnologias)
    - [Ambiente](#ambiente)
    - [Desenvolvimento](#desenvolvimento)
- [Pré-requisitos](#-pr-requisitos)
- [Build e Execução local](#-build-e-execuo-local)
- [Usando a API](#-usando-a-api)
- [Colaboradores](#-colaboradores)

## 🔁 Flow
![Spring Messsage Processor - Flow](src/main/resources/static/images/spring-message-processor.svg)


## 💻 Tecnologias

### Ambiente
* Java 21
* Maven
* Docker

### Desenvolvimento
* Spring Boot
* Versionamento e hospedágem de código com `Git` / [`Github`](https://github.com/samuelJunnior/spring-message-processor)
* Documentação com OpenApi/Swagger.
* Mensageria com RabbitMQ

## 💻 Pré-requisitos

* Você precisa ter o JAVA instalado e configurado.
* Você precisa ter o Docker instalado e configurado.
* Você precisa ter o Maven instalado e configurado.

Caso tenho sua instância RabbitMQ já configurada, apenas ajustar as configurações de `spring.rabbitmq` no arquivo [`application.yaml`](/src/main/resources/application.yaml)

Caso não tenha um broker Rabbit em sua máquina, execute o arquivo `docker-compose.yml` dentro do diretório [deployments](/deployment/docker-compose.yml) com o comando:
```bash
docker compose up -d
```
será criado container RabbitMQ para utilização.


## 🚀 Build e Execução local

Para gerar a versão executável do projeto com a extensão .jar é necessário executar o comando abaixo no diretório raiz:
```bash
mvn clean package
```

Execute o comando abaixo para iniciar o projeto
```bash
java -jar target\nome-do-seu-projeto.jar
```

## ☕ Usando a API

>Após a inicialização do projeto, acessar pelo endereço:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🤝 Colaboradores

Agradecemos às seguintes pessoas que contribuíram para este projeto:

<table>
  <tr>
    <td align="center">
      <a href="#">
         <img src="https://avatars.githubusercontent.com/u/33516411?v=4" width="100px;" alt="Foto do Samuel Junior no GitHub"/><br>
        <sub>
          <b>Samuel Junior</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="https://s2.glbimg.com/FUcw2usZfSTL6yCCGj3L3v3SpJ8=/smart/e.glbimg.com/og/ed/f/original/2019/04/25/zuckerberg_podcast.jpg" width="100px;" alt="Foto do Mark Zuckerberg"/><br>
        <sub>
          <b>Mark Zuckerberg</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="https://miro.medium.com/max/360/0*1SkS3mSorArvY9kS.jpg" width="100px;" alt="Foto do Steve Jobs"/><br>
        <sub>
          <b>Steve Jobs</b>
        </sub>
      </a>
    </td>
  </tr>
</table>
