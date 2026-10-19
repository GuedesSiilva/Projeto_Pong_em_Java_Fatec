public class TestesPong {
    private static int verificacoes;
    private static void conferir(boolean condicao, String descricao) {
        verificacoes++;
        if (!condicao) throw new AssertionError(descricao);
    }
    public static void main(String[] args) {
        Raquete r = new Raquete(30, 250, 14, 100, 7);
        for (int i = 0; i < 200; i++) r.moverParaCima(600);
        conferir(r.getY() == 0, "Limite superior");
        for (int i = 0; i < 200; i++) r.moverParaBaixo(600);
        conferir(r.getY() == 500, "Limite inferior");
        r.centralizar(600);
        conferir(r.getY() == 250, "Centralização da raquete");
        Bola b = new Bola(40, 0, 20, -6, -4);
        b.mover(); b.tratarLimitesVerticais(600);
        conferir(b.getY() == 0 && b.getVelocidadeY() == 4, "Rebote no teto");
        b.tratarLimitesVerticais(600);
        conferir(b.getVelocidadeY() == 4, "Sem rebote repetido no teto");
        b = new Bola(100, 579, 20, 6, 4);
        b.mover(); b.tratarLimitesVerticais(600);
        conferir(b.getY() == 580 && b.getVelocidadeY() == -4, "Rebote no chão");
        b = new Bola(40, 270, 20, -6, 4);
        b.rebater(r, true);
        conferir(b.getX() == 44 && b.getVelocidadeX() == 7, "Rebote esquerdo e reposicionamento");
        b.rebater(r, true);
        conferir(b.getVelocidadeX() == 7, "Sem segundo rebote na esquerda");
        b = new Bola(40, 270, 20, 6, 4);
        b.rebater(r, true);
        conferir(b.getVelocidadeX() == 6, "Contato em afastamento não rebate");
        Raquete direita = new Raquete(756, 250, 14, 100, 3);
        b = new Bola(740, 270, 20, 6, 4);
        b.rebater(direita, false);
        conferir(b.getX() == 736 && b.getVelocidadeX() == -7, "Rebote direito");
        b = new Bola(40, 50, 20, -6, 4);
        b.rebater(r, true);
        conferir(b.getVelocidadeX() == -6, "Sem colisão fora da raquete");
        b.reiniciar(800, 600, -1);
        conferir(b.getX() == 390 && b.getY() == 290 && b.getVelocidadeX() == -6,
            "Saque centralizado e direção");

        Partida p = new Partida();
        p.atualizar(true, true);
        conferir(p.getJogador().getY() == 250, "Teclas opostas anulam movimento");
        p.atualizar(true, false);
        conferir(p.getJogador().getY() == 243, "Comando de subir");
        p.atualizar(false, false);
        conferir(p.getJogador().getY() == 243, "Soltar tecla interrompe movimento");
        p.novaPartida(Partida.Dificuldade.FACIL);
        p.atualizar(false, false);
        conferir(p.getComputador().getY() == 250, "Tolerância impede oscilação");
        for (int i = 0; i < 10; i++) p.getBola().mover();
        int anterior = p.getComputador().getY();
        p.atualizar(false, false);
        conferir(p.getComputador().getY() - anterior == 3, "Computador acompanha com velocidade limitada");
        marcarPonto(p, false);
        conferir(p.getPlacar().getComputador() == 1 && p.getPlacar().getJogador() == 0,
            "Saída esquerda marca para computador");
        conferir(p.getBola().getX() == 390 && p.getBola().getY() == 290, "Reinício após ponto");
        p.atualizar(false, false);
        conferir(p.getPlacar().getComputador() == 1, "Mesmo ponto não é contado novamente");
        p.novaPartida(Partida.Dificuldade.DIFICIL);
        conferir(p.getComputador().getVelocidade() == 6, "Dificuldade altera velocidade");
        for (int i = 0; i < Partida.META; i++) marcarPonto(p, true);
        conferir(p.isEncerrada() && p.getVencedor().equals("Jogador"), "Vitória do jogador");
        int x = p.getBola().getX();
        p.atualizar(true, false);
        conferir(p.getPlacar().getJogador() == Partida.META && p.getBola().getX() == x,
            "Fim congela bola e placar");
        for (int repeticao = 0; repeticao < 2; repeticao++) {
            p.novaPartida(Partida.Dificuldade.FACIL);
            conferir(!p.isEncerrada() && p.getPlacar().getJogador() == 0
                && p.getPlacar().getComputador() == 0 && p.getJogador().getY() == 250
                && p.getComputador().getY() == 250 && p.getBola().getX() == 390,
                "Nova partida restaura estado");
        }
        for (int i = 0; i < Partida.META; i++) marcarPonto(p, false);
        conferir(p.isEncerrada() && p.getVencedor().equals("Computador"), "Vitória do computador");
        p.novaPartida(Partida.Dificuldade.FACIL);
        for (int i = 0; i < 20000; i++) {
            if (p.isEncerrada()) p.novaPartida(Partida.Dificuldade.FACIL);
            p.atualizar(i % 200 < 100, i % 200 >= 100);
            conferir(p.getJogador().getY() >= 0 && p.getJogador().getY() <= 500
                && p.getComputador().getY() >= 0 && p.getComputador().getY() <= 500,
                "Raquetes dentro do campo durante simulação");
            conferir(p.getBola().getY() >= 0 && p.getBola().getY() <= 580,
                "Bola respeita limites verticais durante simulação");
        }
        System.out.println("OK: " + verificacoes + " verificações passaram.");
    }
    private static void marcarPonto(Partida p, boolean jogador) {
        // Avança a bola até a saída, isolando a regra de pontuação das colisões.
        p.getBola().reiniciar(800, 600, jogador ? 1 : -1);
        while (jogador ? p.getBola().getX() < 800 : p.getBola().getX() + 20 > 0)
            p.getBola().mover();
        p.atualizar(false, false);
    }
}
