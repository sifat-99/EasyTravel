package dream_team.easy_travel.mainApp;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {
    public static Font loadLobsterFont() {
        try (InputStream is = FontLoader.class.getResourceAsStream("/fonts/Lobster-Regular.ttf")) {
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 16);
        }
    }
}