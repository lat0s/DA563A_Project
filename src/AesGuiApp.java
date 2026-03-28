import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class AesGuiApp {
    private static final String DEFAULT_MESSAGE = "Introduction to Computer Security";
    private static final Dimension CAPTURE_SIZE = new Dimension(920, 700);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color SUCCESS_COLOR = new Color(24, 110, 57);
    private static final Color ERROR_COLOR = new Color(176, 39, 39);
    private static final Color TEXT_COLOR = new Color(28, 37, 48);

    private final AesCryptoService cryptoService;
    private final JTextArea inputArea;
    private final JTextArea outputArea;
    private final JTextField keyField;
    private final JLabel statusLabel;

    public AesGuiApp() throws GeneralSecurityException {
        cryptoService = new AesCryptoService();
        inputArea = createTextArea(true);
        outputArea = createTextArea(false);
        keyField = new JTextField(cryptoService.getKeyBase64());
        statusLabel = new JLabel("Ready. Press Encrypt to encrypt the text.");

        inputArea.setText(DEFAULT_MESSAGE);
        outputArea.setText("");
        keyField.setEditable(false);
        keyField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        keyField.setBackground(new Color(248, 250, 252));
        keyField.setForeground(TEXT_COLOR);
        keyField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(208, 214, 222)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        statusLabel.setForeground(new Color(82, 89, 98));
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
    }

    public static void main(String[] args) throws Exception {
        if (args.length >= 2 && "--capture".equals(args[0])) {
            Path outputDirectory = Path.of(args[1]);
            SwingUtilities.invokeAndWait(() -> {
                try {
                    captureScreenshots(outputDirectory);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                AesGuiApp app = new AesGuiApp();
                app.show();
            } catch (GeneralSecurityException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    private static void captureScreenshots(Path outputDirectory) throws Exception {
        Files.createDirectories(outputDirectory);

        // Each screenshot uses a fresh app instance so the state is predictable.
        AesGuiApp initialApp = new AesGuiApp();
        saveComponentImage(initialApp.createRootPanel(), outputDirectory.resolve("gui-launch.png"));

        AesGuiApp encryptedApp = new AesGuiApp();
        encryptedApp.encryptCurrentInput();
        saveComponentImage(encryptedApp.createRootPanel(), outputDirectory.resolve("gui-encrypted.png"));

        AesGuiApp decryptedApp = new AesGuiApp();
        decryptedApp.encryptCurrentInput();
        decryptedApp.decryptCurrentOutput();
        saveComponentImage(decryptedApp.createRootPanel(), outputDirectory.resolve("gui-decrypted.png"));
    }

    private static void saveComponentImage(JComponent component, Path outputPath) throws IOException {
        component.setSize(CAPTURE_SIZE);
        component.doLayout();
        layoutRecursively(component);

        // Render the Swing component directly into an image file for the report.
        BufferedImage image = new BufferedImage(
                CAPTURE_SIZE.width,
                CAPTURE_SIZE.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        component.paint(graphics);
        graphics.dispose();

        ImageIO.write(image, "png", outputPath.toFile());
    }

    private static void layoutRecursively(JComponent component) {
        for (java.awt.Component child : component.getComponents()) {
            child.doLayout();
            if (child instanceof JComponent childComponent) {
                layoutRecursively(childComponent);
            }
        }
    }

    private JTextArea createTextArea(boolean editable) {
        JTextArea area = new JTextArea(6, 34);
        area.setEditable(editable);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        area.setForeground(TEXT_COLOR);
        area.setBackground(new Color(248, 250, 252));
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return area;
    }

    private void show() {
        JFrame frame = new JFrame("AES Encryption and Decryption");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(createRootPanel());
        frame.pack();
        frame.setMinimumSize(new Dimension(860, 660));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JComponent createRootPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout(0, 18));
        rootPanel.setPreferredSize(CAPTURE_SIZE);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        rootPanel.setBackground(BACKGROUND_COLOR);
        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createContentPanel(), BorderLayout.CENTER);
        return rootPanel;
    }

    private JComponent createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("AES Implementation");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Encrypt and decrypt text using Java Cryptography Architecture.");
        subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(88, 96, 105));

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(subtitleLabel);
        return headerPanel;
    }

    private JComponent createContentPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(PANEL_COLOR);
        outerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 220, 226)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(PANEL_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 8, 0);
        contentPanel.add(createSectionLabel("Generated AES Key (Base64)"), constraints);

        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 18, 0);
        contentPanel.add(keyField, constraints);

        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 8, 0);
        contentPanel.add(createSectionLabel("Enter Text"), constraints);

        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 0.45;
        constraints.insets = new Insets(0, 0, 18, 0);
        contentPanel.add(wrapInScrollPane(inputArea), constraints);

        constraints.gridy++;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 8, 0);
        contentPanel.add(createSectionLabel("Encrypted / Decrypted Text"), constraints);

        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 0.45;
        constraints.insets = new Insets(0, 0, 18, 0);
        contentPanel.add(wrapInScrollPane(outputArea), constraints);

        constraints.gridy++;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 14, 0);
        contentPanel.add(createButtonPanel(), constraints);

        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(statusLabel, constraints);

        outerPanel.add(contentPanel, BorderLayout.CENTER);
        return outerPanel;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JScrollPane wrapInScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(208, 214, 222)));
        scrollPane.setBackground(new Color(248, 250, 252));
        return scrollPane;
    }

    private JComponent createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(event -> encryptCurrentInput());

        JButton decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(event -> decryptCurrentOutput());

        buttonPanel.add(encryptButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(decryptButton);
        buttonPanel.add(Box.createHorizontalGlue());
        return buttonPanel;
    }

    private void encryptCurrentInput() {
        try {
            String plaintext = inputArea.getText();
            if (plaintext.isBlank()) {
                showError("Please enter text before pressing Encrypt.");
                return;
            }

            String encryptedText = cryptoService.encrypt(plaintext);
            outputArea.setText(encryptedText);
            showSuccess("Encryption completed successfully.");
        } catch (GeneralSecurityException exception) {
            showError("Encryption failed: " + exception.getMessage());
        }
    }

    private void decryptCurrentOutput() {
        try {
            String ciphertext = outputArea.getText().trim();
            if (ciphertext.isEmpty()) {
                showError("Please encrypt text first or paste Base64 ciphertext in the output box.");
                return;
            }

            String decryptedText = cryptoService.decrypt(ciphertext);
            outputArea.setText(decryptedText);
            showSuccess("Decryption completed successfully.");
        } catch (Exception exception) {
            showError("Decryption failed. Make sure the text is valid Base64 ciphertext.");
        }
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(SUCCESS_COLOR);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(ERROR_COLOR);
    }
}
