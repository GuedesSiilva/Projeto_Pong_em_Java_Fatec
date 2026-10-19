import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

public class CampoJogo extends JPanel {
    private final Partida partida = new Partida();
    private final Set<String> teclas = new HashSet<>();
    private final Timer timer = new Timer(16, e -> atualizarJogo());

    public CampoJogo() {
        setPreferredSize(new Dimension(Partida.LARGURA, Partida.ALTURA));
        setBackground(new Color(12, 18, 30));
        for (String tecla : new String[]{"W", "S", "UP", "DOWN"}) {
            associar("pressed " + tecla, () -> teclas.add(tecla));
            associar("released " + tecla, () -> teclas.remove(tecla));
        }
        associar("pressed N", this::iniciar);
    }
    private void associar(String comando, Runnable acao) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(comando), comando);
        getActionMap().put(comando, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { acao.run(); }
        });
    }
    public void limparComandos() { teclas.clear(); }
    public void iniciar() {
        timer.stop(); limparComandos();
        Partida.Dificuldade nivel = (Partida.Dificuldade) JOptionPane.showInputDialog(
            this, "Escolha a dificuldade", "Nova partida", JOptionPane.QUESTION_MESSAGE,
            null, Partida.Dificuldade.values(), partida.getDificuldade());
        limparComandos();
        if (nivel != null) partida.novaPartida(nivel);
        if (!partida.isEncerrada()) timer.start();
        repaint();
    }
    private void atualizarJogo() {
        partida.atualizar(teclas.contains("W") || teclas.contains("UP"),
                          teclas.contains("S") || teclas.contains("DOWN"));
        repaint();
        if (partida.isEncerrada()) {
            timer.stop(); limparComandos();
            int resposta = JOptionPane.showConfirmDialog(this,
                partida.getVencedor() + " venceu! Jogar novamente?", "Fim da partida",
                JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) iniciar();
        }
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D desenho = (Graphics2D) g.create();
        try {
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            desenho.setColor(new Color(60, 74, 96));
            for (int y = 0; y < Partida.ALTURA; y += 30) desenho.fillRect(398, y, 4, 16);
            desenho.setColor(new Color(71, 230, 165)); partida.getJogador().desenhar(desenho);
            desenho.setColor(new Color(83, 175, 255)); partida.getComputador().desenhar(desenho);
            desenho.setColor(Color.WHITE); partida.getBola().desenhar(desenho);
            desenho.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
            desenho.drawString(String.valueOf(partida.getPlacar().getJogador()), 310, 60);
            desenho.drawString(String.valueOf(partida.getPlacar().getComputador()), 460, 60);
            desenho.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            desenho.drawString("W/S ou ↑/↓   •   N: nova partida   •   " + partida.getDificuldade()
                + "   •   Vitória: " + Partida.META + " pontos", 130, 580);
            if (partida.isEncerrada()) {
                desenho.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
                desenho.drawString(partida.getVencedor() + " venceu! Pressione N.", 150, 300);
            }
        } finally { desenho.dispose(); }
    }
}
