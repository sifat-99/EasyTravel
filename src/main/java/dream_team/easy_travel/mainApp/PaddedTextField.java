package dream_team.easy_travel.mainApp;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PaddedTextField extends JTextField {
    public PaddedTextField(int columns) {
        super(columns);
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Add padding
    }
}
