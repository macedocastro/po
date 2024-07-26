package jogoAtual;


public enum Jogador {
    X, O, NENHUM;

    public Jogador oponente() {
        return this == X ? O : X;
    }
}

