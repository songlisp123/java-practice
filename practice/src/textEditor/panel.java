package textEditor;

import javax.swing.*;
import java.awt.*;

public class panel extends JPanel {

    private JComboBox<String> comboBox;
    private JComboBox<Integer> comboBox2;
    private JComboBox<Color> comboBox3;
    private JComboBox<Integer> comboBox4;
    private JTextArea textArea;

    public panel(JTextArea textArea) {
        super();
        this.comboBox = new JComboBox<>();
        this.comboBox2 = new JComboBox<>();
        this.comboBox3 = new JComboBox<>();
        this.comboBox4 = new JComboBox<>();
        this.textArea = textArea;

        setLayout(new GridLayout(1,3));
        addItem();
        addIntegerItem();
        addColor();
        addStyle();

        add(comboBox);
        add(comboBox2);
        add(comboBox3);
        add(comboBox4);

        comboBox.addActionListener(event->{
            textArea.setFont(new Font(
                    comboBox.getItemAt(comboBox.getSelectedIndex()),
                    comboBox4.getItemAt(comboBox4.getSelectedIndex()),
                    comboBox2.getItemAt(comboBox2.getSelectedIndex())
            ));
        });
        comboBox2.addActionListener(event->{
            textArea.setFont(new Font(
                    comboBox.getItemAt(comboBox.getSelectedIndex()),
                    comboBox4.getItemAt(comboBox4.getSelectedIndex()),
                    comboBox2.getItemAt(comboBox2.getSelectedIndex())
            ));
        });
        comboBox3.addActionListener(event->{
            textArea.setForeground(
                     comboBox3.getItemAt(comboBox3.getSelectedIndex())
            );
        });
        comboBox4.addActionListener(event->{
            textArea.setFont(new Font(
                    comboBox.getItemAt(comboBox.getSelectedIndex()),
                    comboBox4.getItemAt(comboBox4.getSelectedIndex()),
                    comboBox2.getItemAt(comboBox2.getSelectedIndex())
            ));
        });
    }
    public void addItem() {
        String[] fontNames = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        for (String s :fontNames){
            comboBox.addItem(s);
        }
    }
    public void addIntegerItem() {
        for (int i = 6; i < 40; i++) {
            if (i%2==0) comboBox2.addItem(i);
        }
    }
    public void addColor() {
        comboBox3.addItem(Color.CYAN);
        comboBox3.addItem(Color.YELLOW);
        comboBox3.addItem(Color.RED);
        comboBox3.addItem(Color.WHITE);
        comboBox3.addItem(Color.CYAN);
    }

    public void addStyle() {
        comboBox4.addItem(Font.PLAIN);
        comboBox4.addItem(Font.BOLD);
        comboBox4.addItem(Font.ITALIC);
        comboBox4.addItem(Font.BOLD + Font.ITALIC);
    }
}

