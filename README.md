# Dicionário Colaborativo de Libras (protótipo)

Protótipo em Java, executado no terminal, do trabalho final da disciplina **AMS0002 – Análise e Modelagem de Sistemas** (professora Carla Diacui Medeiros Berkenbrock).

O sistema completo, descrito no documento do trabalho, é um dicionário colaborativo de sinais da Língua Brasileira de Sinais (Libras): qualquer colaborador autorizado cadastra sinais com vídeo, e um especialista em Libras confere tudo antes de publicar. Nada vai ao ar sem essa conferência e sem a autorização (termo de consentimento) de quem aparece no vídeo.

Este repositório traz o protótipo das operações essenciais: CRUD de termos, envio de vídeo (simulado), consentimento e revisão.

## Como rodar

### Pré-requisito

JDK (Java Development Kit) versão 8 ou superior. O protótipo foi testado com o Java 21. 

### Passo a passo

1. Baixe o projeto: use o botão **Code -> Download ZIP** no GitHub e extraia, ou clone com o Git:

   
   **git clone https://github.com/luquinhaa007/Projeto-Final---Anlise-e-Modelagem-de-Sistemas-2026-2-.git**
   ```

2. Abra o terminal e, na pasta onde está o arquivo, `DicionarioLibras.java`.

3. Compile:

   
   **'javac DicionarioLibras.java'**
   

4. Execute:

   
   **'java DicionarioLibras'** 
   


O menu aparece e você escolhe as opções digitando o número e apertando Enter.

> **Acentos aparecendo errados?** Rode com `java -Dfile.encoding=UTF-8 DicionarioLibras`. No Windows, se continuar errado, digite `chcp 65001` no terminal antes de executar.

## Operações disponíveis

| Opção | Perfil | O que faz |
|---|---|---|
| 1 | Editor | **Cadastrar termo** (palavra, assunto, acepção, exemplo, classe gramatical e origem). Se a palavra já existe, o programa sugere adicionar uma variação. |
| 2 | Editor | **Adicionar sinal com vídeo** a um termo: padrão ou regional, upload do vídeo e anexo do termo de consentimento. |
| 3 | Editor | **Alterar termo** (Enter mantém o valor atual). |
| 4 | Editor | **Excluir termo** e seus sinais, com confirmação. |
| 5 | Editor | **Enviar sinal para revisão** (só sinais em rascunho ou devolvidos, e com termo de consentimento). |
| 6 | Editor | **Listar tudo**, com o status de cada sinal. |
| 7 | Revisor | **Revisar sinais pendentes**: aprovar, rejeitar (com motivo) ou pedir ajustes (com comentário). |
| 8 | Visitante | **Buscar** por palavra, acepção, exemplo ou assunto. Mostra apenas sinais publicados. |
| 0 | — | Sair. |

### Sobre o upload

O envio do vídeo e do termo de consentimento é **simulado**: o programa não procura arquivos no computador. Basta digitar o nome do arquivo, e ele confere o formato:

- vídeo: `.mp4` ou `.webm` (exemplo: `pai.mp4`);
- termo de consentimento: `.pdf`, `.jpg` ou `.png` (exemplo: `termo.pdf`).

## Exemplo de uso

Um roteiro curto para ver o fluxo completo:

1. Opção **1**: cadastre o termo "pai" (assunto: família, classe: substantivo, origem: Libras).
2. Opção **2**: informe o id do termo (`1`), diga que não é regional, digite `pai.mp4`, responda que o participante não é menor de idade e digite `termo.pdf`.
3. Opção **5**: envie o sinal `1` para revisão.
4. Opção **7**: escolha o sinal `1` e aprove (decisão `1`).
5. Opção **8**: busque `pai`. O sinal publicado aparece.

Para ver o bloqueio do consentimento: na opção 2, digite um termo com formato inválido (como `termo.doc`). O sinal fica como rascunho e não pode ser enviado para revisão enquanto não houver um termo anexado.

## Regras implementadas

Baseadas nos requisitos e nos casos de uso do documento do trabalho:

- **Status do sinal** (RF04): Rascunho, Pendente de revisão, Devolvido, Rejeitado e Publicado.
- **Consentimento** (RF03): sem termo anexado, o sinal não vai para revisão. Para menores de 18 anos, o termo deve ser do responsável legal.
- **Revisão** (RF05): a aprovação é bloqueada se não houver consentimento. Rejeitar exige motivo, e pedir ajustes devolve o sinal ao editor com comentários.
- **Consulta** (RF07): o visitante só vê sinais publicados.
- **Variações** (RF01): um termo pode ter vários sinais, padrão e regionais.

## Limitações do protótipo

Este é um protótipo didático do sistema descrito no documento, então há simplificações:

- Os dados ficam **só na memória**: ao fechar o programa, tudo se perde.
- O upload de vídeo e de termo é **simulado** (confere apenas o nome e o formato).
- Não há login: o menu faz o papel dos perfis editor, revisor e visitante.
- Não estão incluídos: retirada de consentimento (RF09), histórico de ações (RF10), listas auxiliares (RF11), notificações por e-mail e os campos de configuração de mão, SignWriting, imagem e exemplo em Libras.
- É um programa de terminal em um único arquivo, e não a arquitetura web em três camadas (MVC) proposta na seção 3 do documento.
- Tais regras de código foram escolhidas pensando nos conhecimentos adquiridos até o momento pelo curso de graduação, nas disciplinas de Algoritmos, Linguagens de Programação, Estrutura de Dados e Programação Orientada a Objetos 


## Equipe

- Lucas Elzinga
- Luana Likes

Professora: Carla Diacui Medeiros Berkenbrock · Disciplina: AMS0002 – Análise e Modelagem de Sistemas 