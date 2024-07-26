package jogoDaVelha;

import java.util.Random;

public class JogoDaVelhaSupremo extends Jogo {
    protected Tabuleiro[][] tabuleiros;
    protected Tabuleiro tabuleiroPrincipal;
    protected int proximoTabuleiroLinha;
    protected int proximoTabuleiroColuna;
    private Dificuldade dificuldade;
    private Random random;

    public JogoDaVelhaSupremo(Dificuldade dificuldade) {
        super();
        this.tabuleiros = new Tabuleiro[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiros[i][j] = new Tabuleiro();
            }
        }
        this.tabuleiroPrincipal = new Tabuleiro();
        this.proximoTabuleiroLinha = -1;
        this.proximoTabuleiroColuna = -1;
        this.random = new Random();
        this.dificuldade = dificuldade;
    }

    @Override
    public void jogar() {
        // Lógica de jogo fica na interface gráfica, não precisa de implementação aqui
    }

    public boolean fazerJogada(int linhaTabuleiroPrincipal, int colunaTabuleiroPrincipal, int linhaCelula, int colunaCelula, Jogador jogador) {
        if (isJogadaInvalida(linhaTabuleiroPrincipal, colunaTabuleiroPrincipal, linhaCelula, colunaCelula)) {
            System.out.println("Jogada inválida. Célula já ocupada ou fora dos limites.");
            return false;
        }

        if (tabuleiros[linhaTabuleiroPrincipal][colunaTabuleiroPrincipal].fazerJogada(linhaCelula, colunaCelula, jogador)) {
            if (tabuleiros[linhaTabuleiroPrincipal][colunaTabuleiroPrincipal].getVencedor() == jogador) {
                tabuleiroPrincipal.fazerJogada(linhaTabuleiroPrincipal, colunaTabuleiroPrincipal, jogador);
            }
            atualizarProximoTabuleiro(linhaCelula, colunaCelula);
            alternarJogador();
            return true;
        }

        return false;
    }

    private boolean isJogadaInvalida(int linhaTabuleiroPrincipal, int colunaTabuleiroPrincipal, int linhaCelula, int colunaCelula) {
        return linhaTabuleiroPrincipal < 0 || linhaTabuleiroPrincipal >= 3 ||
                colunaTabuleiroPrincipal < 0 || colunaTabuleiroPrincipal >= 3 ||
                linhaCelula < 0 || linhaCelula >= 3 || colunaCelula < 0 || colunaCelula >= 3 ||
                tabuleiros[linhaTabuleiroPrincipal][colunaTabuleiroPrincipal].getEstadoCelula(linhaCelula, colunaCelula) != Jogador.NENHUM;
    }

    private void atualizarProximoTabuleiro(int linhaCelula, int colunaCelula) {
        proximoTabuleiroLinha = linhaCelula;
        proximoTabuleiroColuna = colunaCelula;

        if (tabuleiros[proximoTabuleiroLinha][proximoTabuleiroColuna].estaCheio()) {
            proximoTabuleiroLinha = -1;
            proximoTabuleiroColuna = -1;
        }
    }

    public void jogarMaquina() {
        if (isProximoTabuleiroValido()) {
            realizarJogadaIA(proximoTabuleiroLinha, proximoTabuleiroColuna);
        } else {
            jogarEmQualquerTabuleiro();
        }
    }

    private boolean isProximoTabuleiroValido() {
        return proximoTabuleiroLinha >= 0 && proximoTabuleiroColuna >= 0 &&
                proximoTabuleiroLinha < 3 && proximoTabuleiroColuna < 3 &&
                !tabuleiros[proximoTabuleiroLinha][proximoTabuleiroColuna].estaCheio();
    }

    private void jogarEmQualquerTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!tabuleiros[i][j].estaCheio()) {
                    realizarJogadaIA(i, j);
                    return;
                }
            }
        }
    }

    private void realizarJogadaIA(int linhaTabuleiro, int colunaTabuleiro) {
        boolean jogadaFeita = false;
        switch (dificuldade) {
            case FACIL:
                jogadaFeita = fazerJogadaAleatoria(linhaTabuleiro, colunaTabuleiro);
                break;
            case MEDIO:
                jogadaFeita = fazerJogadaMedia(linhaTabuleiro, colunaTabuleiro);
                break;
            case DIFICIL:
                jogadaFeita = fazerJogadaDificil(linhaTabuleiro, colunaTabuleiro);
                break;
            default:
                break;
        }

        if (jogadaFeita) {
            System.out.println("Jogada da máquina concluída.");
            atualizarProximoTabuleiroDepoisDeJogadaIA(linhaTabuleiro, colunaTabuleiro);
        } else {
            System.out.println("Não foi possível realizar a jogada.");
        }
    }

    private void atualizarProximoTabuleiroDepoisDeJogadaIA(int linhaTabuleiro, int colunaTabuleiro) {
        if (tabuleiros[linhaTabuleiro][colunaTabuleiro].estaCheio()) {
            proximoTabuleiroLinha = -1;
            proximoTabuleiroColuna = -1;
        } else {
            int[] ultimaJogada = encontrarUltimaJogada(tabuleiros[linhaTabuleiro][colunaTabuleiro]);
            proximoTabuleiroLinha = ultimaJogada[0];
            proximoTabuleiroColuna = ultimaJogada[1];
        }
        alternarJogador();
    }

    private int[] encontrarUltimaJogada(Tabuleiro tabuleiro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro.getEstadoCelula(i, j) != Jogador.NENHUM) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    private boolean fazerJogadaAleatoria(int linhaTabuleiro, int colunaTabuleiro) {
        if (!isTabuleiroValido(linhaTabuleiro, colunaTabuleiro)) {
            return false;
        }

        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                if (tabuleiros[linhaTabuleiro][colunaTabuleiro].getEstadoCelula(linha, coluna) == Jogador.NENHUM) {
                    boolean jogadaFeita = tabuleiros[linhaTabuleiro][colunaTabuleiro].fazerJogada(linha, coluna, Jogador.O);
                    if (jogadaFeita) {
                        System.out.println("Máquina fez uma jogada aleatória em [" + linha + ", " + coluna + "] no tabuleiro [" + linhaTabuleiro + ", " + colunaTabuleiro + "].");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean fazerJogadaMedia(int linhaTabuleiro, int colunaTabuleiro) {
        if (!isTabuleiroValido(linhaTabuleiro, colunaTabuleiro)) {
            return false;
        }

        if (tentarJogadaVencedora(linhaTabuleiro, colunaTabuleiro, Jogador.O) ||
                tentarBloquearOponente(linhaTabuleiro, colunaTabuleiro)) {
            return true;
        }

        return fazerJogadaAleatoria(linhaTabuleiro, colunaTabuleiro);
    }

    private boolean tentarJogadaVencedora(int linhaTabuleiro, int colunaTabuleiro, Jogador jogador) {
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                if (tabuleiros[linhaTabuleiro][colunaTabuleiro].getEstadoCelula(linha, coluna) == Jogador.NENHUM) {
                    if (podeVencer(linhaTabuleiro, colunaTabuleiro, linha, coluna, jogador)) {
                        tabuleiros[linhaTabuleiro][colunaTabuleiro].fazerJogada(linha, coluna, jogador);
                        System.out.println("Máquina venceu com a jogada em [" + linha + ", " + coluna + "] no tabuleiro [" + linhaTabuleiro + ", " + colunaTabuleiro + "].");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean tentarBloquearOponente(int linhaTabuleiro, int colunaTabuleiro) {
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                if (tabuleiros[linhaTabuleiro][colunaTabuleiro].getEstadoCelula(linha, coluna) == Jogador.NENHUM) {
                    if (podeVencer(linhaTabuleiro, colunaTabuleiro, linha, coluna, Jogador.X)) {
                        tabuleiros[linhaTabuleiro][colunaTabuleiro].fazerJogada(linha, coluna, Jogador.O);
                        System.out.println("Máquina bloqueou o oponente com a jogada em [" + linha + ", " + coluna + "] no tabuleiro [" + linhaTabuleiro + ", " + colunaTabuleiro + "].");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean fazerJogadaDificil(int linhaTabuleiro, int colunaTabuleiro) {
        int[] melhorJogada = encontrarMelhorJogadaMinimax(linhaTabuleiro, colunaTabuleiro);
        if (melhorJogada != null) {
            return tabuleiros[linhaTabuleiro][colunaTabuleiro].fazerJogada(melhorJogada[0], melhorJogada[1], Jogador.O);
        }
        return false;
    }

    private int[] encontrarMelhorJogadaMinimax(int linhaTabuleiro, int colunaTabuleiro) {
        int melhorPontuacao = Integer.MIN_VALUE;
        int[] melhorJogada = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiros[linhaTabuleiro][colunaTabuleiro].getEstadoCelula(i, j) == Jogador.NENHUM) {
                    tabuleiros[linhaTabuleiro][colunaTabuleiro].fazerJogada(i, j, Jogador.O);
                    int pontuacao = minimax(linhaTabuleiro, colunaTabuleiro, false);
                    tabuleiros[linhaTabuleiro][colunaTabuleiro].desfazerJogada(i, j);

                    if (pontuacao > melhorPontuacao) {
                        melhorPontuacao = pontuacao;
                        melhorJogada[0] = i;
                        melhorJogada[1] = j;
                    }
                }
            }
        }

        return melhorPontuacao == Integer.MIN_VALUE ? null : melhorJogada;
    }

    private int minimax(int linhaTabuleiro, int colunaTabuleiro, boolean ehMaximizador) {
        Jogador vencedor = tabuleiros[linhaTabuleiro][colunaTabuleiro].getVencedor();
        if (vencedor == Jogador.O) {
            return 10;
        } else if (vencedor == Jogador.X) {
            return -10;
        } else if (tabuleiros[linhaTabuleiro][colunaTabuleiro].estaCheio()) {
            return 0;
        }

        int melhorPontuacao = ehMaximizador ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiros[linhaTabuleiro][colunaTabuleiro].getEstadoCelula(i, j) == Jogador.NENHUM) {
                    tabuleiros[linhaTabuleiro][colunaTabuleiro].fazerJogada(i, j, ehMaximizador ? Jogador.O : Jogador.X);
                    int pontuacao = minimax(linhaTabuleiro, colunaTabuleiro, !ehMaximizador);
                    tabuleiros[linhaTabuleiro][colunaTabuleiro].desfazerJogada(i, j);

                    melhorPontuacao = ehMaximizador ? Math.max(melhorPontuacao, pontuacao) : Math.min(melhorPontuacao, pontuacao);
                }
            }
        }

        return melhorPontuacao;
    }

    private boolean podeVencer(int linhaTabuleiro, int colunaTabuleiro, int linha, int coluna, Jogador jogador) {
        tabuleiros[linhaTabuleiro][colunaTabuleiro].fazerJogada(linha, coluna, jogador);
        boolean vitoria = tabuleiros[linhaTabuleiro][colunaTabuleiro].getVencedor() == jogador;
        tabuleiros[linhaTabuleiro][colunaTabuleiro].desfazerJogada(linha, coluna);
        return vitoria;
    }

    private boolean isTabuleiroValido(int linhaTabuleiro, int colunaTabuleiro) {
        if (linhaTabuleiro < 0 || linhaTabuleiro >= 3 || colunaTabuleiro < 0 || colunaTabuleiro >= 3) {
            System.out.println("Tabuleiro indicado é inválido.");
            return false;
        }
        return true;
    }
}
