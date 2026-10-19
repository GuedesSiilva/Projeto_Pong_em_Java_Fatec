import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class JanelaJogo extends JFrame {
    public JanelaJogo() {
        super("Pong — Java");
        CampoJogo campo = new CampoJogo();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(campo); pack(); setResizable(false); setLocationRelativeTo(null);
        addWindowFocusListener(new WindowAdapter() {
            @Override public void windowLostFocus(WindowEvent e) { campo.limparComandos(); }
        });
        setVisible(true); campo.iniciar();
    }
}
