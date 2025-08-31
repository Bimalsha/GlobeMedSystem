package com.globemed.view;

import com.globemed.MainApplicationFrame;
import com.globemed.dao.StaffDAO;
import com.globemed.model.Staff;
import com.globemed.util.PasswordUtil;
import com.globemed.view.custom.LineTextField;
import com.globemed.view.custom.RoundedButton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class LoginFrame extends JFrame {

    private final StaffDAO staffDAO;

    public LoginFrame() {
        this.staffDAO = new StaffDAO();
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Make JFrame background transparent
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        // The main content pane is a custom JPanel with rounded corners
        JPanel mainPanel = new JPanel(new GridLayout(1, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // This creates the rounded corners for the whole window
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        // Add the two main panels
        mainPanel.add(createBrandingPanel());
        mainPanel.add(createLoginFormPanel());
    }

    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Paint the background image, scaled to fit
                URL imgUrl = getClass().getResource("/icons/login_background.png");
                if (imgUrl != null) {
                    Image image = new ImageIcon(imgUrl).getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("<html>Sign in to continue your<br>session with GlobeMed.</html>");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(220, 220, 240));
        panel.add(subtitleLabel, gbc);

        return panel;
    }

    private JPanel createLoginFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false); // Transparent to show the main panel's white bg
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // "Login" Title
        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        loginTitle.setForeground(new Color(50, 50, 50));
        gbc.gridy = 0;
        panel.add(loginTitle, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Glad you're back!");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 10);
        panel.add(subtitle, gbc);
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email Field
        ImageIcon userIcon = new ImageIcon(getClass().getResource("/icons/email_icon.png"));
        LineTextField emailField = new LineTextField("EMAIL", userIcon, false);
        gbc.gridy = 2;
        panel.add(emailField, gbc);

        // Password Field
        ImageIcon lockIcon = new ImageIcon(getClass().getResource("/icons/password_icon.png"));
        LineTextField passwordField = new LineTextField("PASSWORD", lockIcon, true);
        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        // "Forgot Password?" Link
        JLabel forgotPassword = new JLabel("Forgot Password?");
        forgotPassword.setFont(new Font("SansSerif", Font.BOLD, 12));
        forgotPassword.setForeground(new Color(74, 144, 226));
        forgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(forgotPassword, gbc);

        // Login Button
        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setPreferredSize(new Dimension(0, 50)); // Set height
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(loginButton, gbc);

        // Status Label
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 6;
        panel.add(statusLabel, gbc);

        // "Sign Up" Link
        JLabel signUpLabel = new JLabel("<html>Don't have an account? <font color='#4A90E2'><u>Sign Up</u></font></html>");
        signUpLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        gbc.weighty = 1.0; // Pushes it to the bottom
        gbc.anchor = GridBagConstraints.SOUTH;
        panel.add(signUpLabel, gbc);

        // Login Action Logic
        ActionListener loginAction = ae -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            Staff staff = staffDAO.findOneByProperty("email", email);

            if (staff != null && "Active".equals(staff.getStatus().getName()) && PasswordUtil.verifyPassword(password, staff.getPasswordHash())) {
                statusLabel.setText("Login Successful!");
                statusLabel.setForeground(new Color(0, 150, 0));

                // Use a short timer to show the success message before switching frames
                Timer timer = new Timer(500, e -> {
                    dispose();
                    new MainApplicationFrame(staff).setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                statusLabel.setText("Invalid email or password.");
            }
        };
        loginButton.addActionListener(loginAction);
        // Also allow login by pressing Enter in the password field
        ((JTextField)passwordField.getComponent(2)).addActionListener(loginAction);

        return panel;
    }
}