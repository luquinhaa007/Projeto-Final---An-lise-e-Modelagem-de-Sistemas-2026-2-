import java.util.*;

// Uma variação do sinal (padrão ou regional), com vídeo e consentimento
class Sinal {
    static final String RASCUNHO = "Rascunho", PENDENTE = "Pendente de revisão",
            DEVOLVIDO = "Devolvido", REJEITADO = "Rejeitado", PUBLICADO = "Publicado";
    int id;
    Termo termo;
    String tipo, regiao = "";   // tipo: "Padrão" ou "Regional"
    String arquivoVideo;        // nome do vídeo enviado (simulado)
    boolean consentimento;
    String status = RASCUNHO;
    String comentario = "";     // motivo ou ajustes pedidos pelo revisor

    String descricao() {
        return regiao.isEmpty() ? tipo : tipo + " - " + regiao;
    }
}

// Um termo do dicionário, com uma ou mais variações (sinais)
class Termo {
    int id;
    String palavra, assunto, acepcao, exemplo, classeGramatical, origem;
    ArrayList<Sinal> sinais = new ArrayList<>();

    boolean temSinalPublicado() {
        for (Sinal s : sinais) {
            if (s.status.equals(Sinal.PUBLICADO)) return true;
        }
        return false;
    }

    // soPublicados = true mostra só o que o visitante pode ver
    void mostrar(boolean soPublicados) {
        System.out.println("\n[" + id + "] " + palavra + " (" + classeGramatical + ") - " + assunto);
        System.out.println("    Acepção: " + acepcao + " | Exemplo: " + exemplo + " | Origem: " + origem);
        for (Sinal s : sinais) {
            if (soPublicados && !s.status.equals(Sinal.PUBLICADO)) continue;
            System.out.println("    Sinal " + s.id + ": " + s.descricao() + " | vídeo: "
                    + s.arquivoVideo + " | status: " + s.status);
            if (!soPublicados && !s.comentario.isEmpty()) {
                System.out.println("      Comentário do revisor: " + s.comentario);
            }
        }
    }
}

public class DicionarioLibras {
    static ArrayList<Termo> termos = new ArrayList<>(); // "banco de dados" na memória
    static int proximoIdTermo = 1, proximoIdSinal = 1;
    static Scanner teclado = new Scanner(System.in);

    // ---------- Leitura do teclado ----------
    static String lerObrigatorio(String pergunta) {
        String texto;
        do {
            System.out.print(pergunta);
            texto = teclado.nextLine().trim();
        } while (texto.isEmpty());
        return texto;
    }

    // Enter vazio mantém o valor atual (usado na alteração)
    static String lerOpcional(String pergunta, String atual) {
        System.out.print(pergunta + " [" + atual + "]: ");
        String texto = teclado.nextLine().trim();
        return texto.isEmpty() ? atual : texto;
    }

    static boolean lerSimNao(String pergunta) {
        System.out.print(pergunta + " (s/n): ");
        return teclado.nextLine().trim().equalsIgnoreCase("s");
    }

    static int lerNumero(String pergunta) {
        System.out.print(pergunta);
        try {
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ---------- Buscas internas ----------
    static Termo acharTermo(int id) {
        for (Termo t : termos) {
            if (t.id == id) return t;
        }
        return null;
    }

    static Sinal acharSinal(int id) {
        for (Termo t : termos) {
            for (Sinal s : t.sinais) {
                if (s.id == id) return s;
            }
        }
        return null;
    }

    // ---------- UPLOAD de vídeo (simulado) ----------
    // Não procura arquivo no computador: só pede o nome e confere o formato.
    static String enviarVideo() {
        System.out.print("Nome do vídeo (ex.: pai.mp4): ");
        String nome = teclado.nextLine().trim();
        if (!nome.toLowerCase().endsWith(".mp4") && !nome.toLowerCase().endsWith(".webm")) {
            System.out.println("Formato inválido. Use um nome terminado em .mp4 ou .webm.");
            return null;
        }
        System.out.println("Enviando... 100%. Upload concluído (simulado): " + nome);
        return nome;
    }

    // ---------- CREATE ----------
    static void cadastrarTermo() {
        System.out.println("\n--- Novo termo ---");
        String palavra = lerObrigatorio("Palavra: ");
        for (Termo t : termos) {
            if (t.palavra.equalsIgnoreCase(palavra)) {
                System.out.println("Essa palavra já existe (id " + t.id + "). Use a opção 2 para adicionar uma variação.");
                return;
            }
        }
        Termo novo = new Termo();
        novo.id = proximoIdTermo++;
        novo.palavra = palavra;
        novo.assunto = lerObrigatorio("Assunto: ");
        novo.acepcao = lerObrigatorio("Acepção: ");
        novo.exemplo = lerObrigatorio("Exemplo: ");
        novo.classeGramatical = lerObrigatorio("Classe gramatical: ");
        novo.origem = lerObrigatorio("Origem: ");
        termos.add(novo);
        System.out.println("Termo cadastrado com id " + novo.id + ". Agora adicione um sinal com vídeo (opção 2).");
    }

    // Adiciona uma variação com vídeo e consentimento a um termo existente
    static void adicionarSinal() {
        System.out.println("\n--- Novo sinal com vídeo ---");
        Termo t = acharTermo(lerNumero("Id do termo: "));
        if (t == null) { System.out.println("Termo não encontrado."); return; }

        Sinal s = new Sinal();
        s.id = proximoIdSinal;
        s.termo = t;
        if (lerSimNao("É um sinal regional?")) {
            s.tipo = "Regional";
            s.regiao = lerObrigatorio("Região (ex.: Rio Grande do Sul): ");
        } else {
            s.tipo = "Padrão";
        }
        s.arquivoVideo = enviarVideo();
        if (s.arquivoVideo == null) return; // sem vídeo válido, o sinal não é criado

        boolean menor = lerSimNao("O participante do vídeo tem menos de 18 anos?");
        s.consentimento = lerSimNao((menor ? "O responsável legal" : "O participante")
                + " aceitou publicar o vídeo no dicionário aberto?");
        t.sinais.add(s);
        proximoIdSinal++;
        System.out.println("Sinal " + s.id + " salvo como rascunho.");
        if (!s.consentimento) System.out.println("Sem consentimento, ele não pode ser enviado para revisão.");
    }

    // ---------- READ ----------
    // Visitante: só vê sinais publicados
    static void buscar() {
        String busca = lerObrigatorio("Buscar por palavra, acepção, exemplo ou assunto: ").toLowerCase();
        boolean achou = false;
        for (Termo t : termos) {
            String texto = (t.palavra + " " + t.acepcao + " " + t.exemplo + " " + t.assunto).toLowerCase();
            if (texto.contains(busca) && t.temSinalPublicado()) {
                t.mostrar(true);
                achou = true;
            }
        }
        if (!achou) System.out.println("Nenhum sinal publicado encontrado.");
    }

    // ---------- UPDATE ----------
    static void alterarTermo() {
        Termo t = acharTermo(lerNumero("Id do termo a alterar: "));
        if (t == null) { System.out.println("Termo não encontrado."); return; }
        System.out.println("Enter mantém o valor atual.");
        t.palavra = lerOpcional("Palavra", t.palavra);
        t.assunto = lerOpcional("Assunto", t.assunto);
        t.acepcao = lerOpcional("Acepção", t.acepcao);
        t.exemplo = lerOpcional("Exemplo", t.exemplo);
        t.classeGramatical = lerOpcional("Classe gramatical", t.classeGramatical);
        t.origem = lerOpcional("Origem", t.origem);
        System.out.println("Termo alterado.");
    }

    // ---------- DELETE ----------
    static void excluirTermo() {
        Termo t = acharTermo(lerNumero("Id do termo a excluir: "));
        if (t == null) { System.out.println("Termo não encontrado."); return; }
        if (!lerSimNao("Excluir '" + t.palavra + "' e seus " + t.sinais.size() + " sinal(is)?")) return;
        termos.remove(t);
        System.out.println("Termo excluído.");
    }

    // ---------- Fluxo de revisão ----------
    static void enviarParaRevisao() {
        Sinal s = acharSinal(lerNumero("Id do sinal: "));
        if (s == null) { System.out.println("Sinal não encontrado."); return; }
        if (!s.status.equals(Sinal.RASCUNHO) && !s.status.equals(Sinal.DEVOLVIDO)) {
            System.out.println("Só rascunhos ou sinais devolvidos podem ser enviados. Status: " + s.status);
            return;
        }
        if (!s.consentimento) {
            s.consentimento = lerSimNao("Sem consentimento registrado. O aceite foi dado agora?");
            if (!s.consentimento) { System.out.println("Envio bloqueado: falta o consentimento."); return; }
        }
        s.status = Sinal.PENDENTE;
        s.comentario = "";
        System.out.println("Sinal enviado. Os revisores foram avisados.");
    }

    static void revisarSinais() {
        System.out.println("\n--- Fila de revisão ---");
        boolean achou = false;
        for (Termo t : termos) {
            for (Sinal s : t.sinais) {
                if (s.status.equals(Sinal.PENDENTE)) {
                    System.out.println("Sinal " + s.id + ": " + t.palavra + " (" + s.descricao() + ")");
                    achou = true;
                }
            }
        }
        if (!achou) { System.out.println("Nenhum sinal pendente."); return; }

        Sinal s = acharSinal(lerNumero("Id do sinal para revisar: "));
        if (s == null || !s.status.equals(Sinal.PENDENTE)) { System.out.println("Esse sinal não está na fila."); return; }
        s.termo.mostrar(false);

        int opcao = lerNumero("\n1 - Aprovar   2 - Rejeitar   3 - Pedir ajustes\nDecisão: ");
        if (opcao == 1) {
            if (!s.consentimento) { // regra da LGPD: sem consentimento, nada é publicado
                System.out.println("Aprovação bloqueada: consentimento ausente ou revogado.");
                return;
            }
            s.status = Sinal.PUBLICADO;
        } else if (opcao == 2) {
            s.comentario = lerObrigatorio("Motivo da rejeição: ");
            s.status = Sinal.REJEITADO;
        } else if (opcao == 3) {
            s.comentario = lerObrigatorio("Ajustes necessários: ");
            s.status = Sinal.DEVOLVIDO;
        } else {
            System.out.println("Opção inválida.");
            return;
        }
        System.out.println("Editor notificado: o sinal " + s.id + " agora está '" + s.status + "'.");
    }

    // ---------- Menu ----------
    public static void main(String[] args) {
        String opcao = "";
        while (!opcao.equals("0")) {
            System.out.println("\n=== Dicionário Colaborativo de Libras ===\n"
                    + "Editor:    1 Cadastrar termo | 2 Adicionar sinal com vídeo (upload) | 3 Alterar termo\n"
                    + "           4 Excluir termo | 5 Enviar sinal para revisão | 6 Listar tudo\n"
                    + "Revisor:   7 Revisar sinais pendentes\n"
                    + "Visitante: 8 Buscar sinais publicados\n"
                    + "0 Sair");
            System.out.print("Escolha: ");
            opcao = teclado.nextLine().trim();
            switch (opcao) {
                case "1": cadastrarTermo(); break;
                case "2": adicionarSinal(); break;
                case "3": alterarTermo(); break;
                case "4": excluirTermo(); break;
                case "5": enviarParaRevisao(); break;
                case "6": for (Termo t : termos) t.mostrar(false); break;
                case "7": revisarSinais(); break;
                case "8": buscar(); break;
                case "0": System.out.println("Até logo!"); break;
                default: System.out.println("Opção inválida.");
            }
        }
    }
}
