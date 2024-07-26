package jogoAtual;

import java.util.Random;

public class JogoDaVelha {
    public enum Dificuldade {
        FACIL, MEDIO, DIFICIL, IMPOSSIVEL
    }

    private Jogador[][] tabuleiro;
    private Jogador jogadorAtual;
    private Dificuldade dificuldade;
    private Random random;

    public JogoDaVelha(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
        tabuleiro = new Jogador[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = Jogador.NENHUM;
            }
        }
        jogadorAtual = Jogador.X; // O jogador X começa o jogo
        random = new Random();
    }

    public boolean fazerJogada(int linha, int coluna) {
        if (linha < 0 || linha >= 3 || coluna < 0 || coluna >= 3 || tabuleiro[linha][coluna] != Jogador.NENHUM) {
            return false;
        }
        tabuleiro[linha][coluna] = jogadorAtual;
        if (verificarVencedor()) {
            return true;
        }
        alternarJogador();
        return true;
    }

    public void jogarMaquina() {
        switch (dificuldade) {
            case FACIL:
                jogarAleatorio();
                break;
            case MEDIO:
                jogarMedio();
                break;
            case DIFICIL:
                jogarDificil();
                break;
            case IMPOSSIVEL:
                jogarImpossivel();
                break;
        }
    }

    private void jogarAleatorio() {
        while (true) {
            int linha = random.nextInt(3);
            int coluna = random.nextInt(3);
            if (fazerJogada(linha, coluna)) {
                break;
            }
        }
    }

    private void jogarMedio() {
        // Lógica de estratégia simples para bloquear ou vencer
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == Jogador.NENHUM) {
                    if (podeVencer(i, j)) {
                        fazerJogada(i, j);
                        return;
                    }
                }
            }
        }
        // Se não conseguiu vencer, bloqueia o oponente
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == Jogador.NENHUM) {
                    if (podeVencer(i, j, Jogador.X)) {
                        fazerJogada(i, j);
                        return;
                    }
                }
            }
        }
        // Se não há vitórias ou bloqueios, joga aleatoriamente
        jogarAleatorio();
    }

    private boolean podeVencer(int linha, int coluna) {
        return podeVencer(linha, coluna, jogadorAtual);
    }

    private boolean podeVencer(int linha, int coluna, Jogador jogador) {
        // Verifica se colocando a peça em (linha, coluna) o jogador pode vencer
        tabuleiro[linha][coluna] = jogador;
        boolean vitoria = verificarVencedor();
        tabuleiro[linha][coluna] = Jogador.NENHUM;
        return vitoria;
    }

    private void jogarDificil() {
        // Lógica do Minimax
        int[] melhorJogada = encontrarMelhorJogadaMinimax();
        fazerJogada(melhorJogada[0], melhorJogada[1]);
    }

    private int[] encontrarMelhorJogadaMinimax() {
        int melhorPontuacao = Integer.MIN_VALUE;
        int[] melhorJogada = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == Jogador.NENHUM) {
                    tabuleiro[i][j] = jogadorAtual;
                    int pontuacao = minimax(false);
                    tabuleiro[i][j] = Jogador.NENHUM;

                    if (pontuacao > melhorPontuacao) {
                        melhorPontuacao = pontuacao;
                        melhorJogada[0] = i;
                        melhorJogada[1] = j;
                    }
                }
            }
        }
        return melhorJogada;
    }

    private int minimax(boolean ehMaximizador) {
        if (verificarVencedor()) {
            return (ehMaximizador ? -1 : 1);
        }
        if (estaCheio()) {
            return 0;
        }

        int melhorPontuacao = ehMaximizador ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == Jogador.NENHUM) {
                    tabuleiro[i][j] = ehMaximizador ? jogadorAtual : jogadorAtual.oponente();
                    int pontuacao = minimax(!ehMaximizador);
                    tabuleiro[i][j] = Jogador.NENHUM;

                    if (ehMaximizador) {
                        melhorPontuacao = Math.max(melhorPontuacao, pontuacao);
                    } else {
                        melhorPontuacao = Math.min(melhorPontuacao, pontuacao);
                    }
                }
            }
        }
        return melhorPontuacao;
    }

    private void jogarImpossivel() {
        jogarDificil();
    }

    public boolean verificarVencedor() {
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] != Jogador.NENHUM && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
                return true;
            }
            if (tabuleiro[0][i] != Jogador.NENHUM && tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
                return true;
            }
        }
        if (tabuleiro[0][0] != Jogador.NENHUM && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            return true;
        }
        if (tabuleiro[0][2] != Jogador.NENHUM && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            return true;
        }
        return false;
    }

    public boolean estaCheio() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == Jogador.NENHUM) {
                    return false;
                }
            }
        }
        return true;
    }

    public void alternarJogador() {
        jogadorAtual = (jogadorAtual == Jogador.X) ? Jogador.O : Jogador.X;
    }

    public String getJogadorAtual() {
        return jogadorAtual.toString();
    }

    public Jogador getEstadoCelula(int linha, int coluna) {
        return tabuleiro[linha][coluna];
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }
}
