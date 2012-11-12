/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsim;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author MAEN
 */
public class AboutDialog extends JDialog {
    // A copyright statement
    private String copyright = "JSim  Copyright (C) 2007  Maen Artimy\n"
            + "This program comes with ABSOLUTELY NO WARRANTY; see http://www.gnu.org/licenses/ for details.\n"
            + "This is free software, and you are welcome to redistribute it\n"
            + "under certain conditions; see http://www.gnu.org/licenses/ for details.\n";

    public AboutDialog(JFrame parent) {
        super(parent, "About JSim");

        Box b = Box.createVerticalBox();
        b.add(Box.createGlue());
        b.add(new JLabel("JSim v0.2"));
        b.add(new JLabel("Copyright (C) 2007-2012  Maen Artimy"));
        b.add(new JLabel("This program comes with ABSOLUTELY NO WARRANTY."));
        b.add(new JLabel("This is free software, and you are welcome to redistribute it"));
        b.add(new JLabel("under certain conditions;"));
        b.add(new JLabel("see http://www.gnu.org/licenses/ for details."));
        b.add(Box.createGlue());
        getContentPane().add(b, BorderLayout.CENTER);

        JPanel p2 = new JPanel();
        JButton ok = new JButton("Ok");
        p2.add(ok);
        getContentPane().add(p2, BorderLayout.PAGE_END);

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
    }

}
