package jogoDaVelha;

public enum Jogador {
    X, O, NENHUM;

    @Override
    public String toString() {
        if (this == NENHUM) {
            return "";
        }
        return super.toString();
    }

    public Jogador oponente() {
        // Retorna o jogador oposto
        switch (this) {
            case X:
                return O;
            case O:
                return X;
            case NENHUM:
                return NENHUM;
            default:
                throw new IllegalArgumentException("Jogador desconhecido: " + this);
        }
    }
}
