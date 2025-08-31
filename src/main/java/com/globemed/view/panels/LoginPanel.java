package com.globemed.view;

import com.globemed.dao.StaffDAO;
import com.globemed.model.Staff;
import com.globemed.util.PasswordUtil;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {
    private final JTextField txtEmail;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;
    private final JLabel lblStatus;
    private final StaffDAO staffDAO;
    private final Consumer<Staff> onLoginSuccess;

    public LoginPanel(Consumer<Staff> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        this.staffDAO = new StaffDAO();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Components
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Login");
        lblStatus = new JLabel("Please enter your credentials.", SwingConstants.CENTER);

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; add(txtEmail, gbc);
        gbc.gridy = 1; gbc.gridx = 0; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(txtPassword, gbc);
        gbc.gridy = 2; gbc.gridx = 1; gbc.anchor = GridBagConstraints.CENTER; add(btnLogin, gbc);
        gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; add(lblStatus, gbc);

        // Listeners
        btnLogin.addActionListener(e -> performLogin());
        txtPassword.addActionListener(e -> performLogin()); // Allow login with Enter key
    }

    private void performLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Email and password cannot be empty.");
            lblStatus.setForeground(Color.RED);
            return;
        }

        Staff staff = staffDAO.findOneByProperty("email", email);

        if (staff != null && "Active".equals(staff.getStatus().getName()) && PasswordUtil.verifyPassword(password, staff.getPasswordHash())) {
            lblStatus.setText("Login Successful!");
            lblStatus.setForeground(new Color(0, 128, 0)); // Green
            onLoginSuccess.accept(staff); // Notify the main frame of success
        } else {
            lblStatus.setText("Invalid credentials or inactive account.");
            lblStatus.setForeground(Color.RED);
        }
    }
}