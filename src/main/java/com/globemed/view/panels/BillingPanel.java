package com.globemed.view.panels;

import com.globemed.dao.*;
import com.globemed.model.Bill;
import com.globemed.model.InsuranceClaim;
import com.globemed.model.InsuranceProvider;
import com.globemed.model.Patient;
import com.globemed.service.BillingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillingPanel extends JPanel {

    // UI Components
    private JTextField txtPatientId;
    private JButton btnFetchPatient;
    private JLabel lblPatientInfo;
    private JTextArea txtDescription;
    private JTextField txtAmount;
    private JComboBox<String> cmbPaymentMethod;
    private JLabel lblBalanceDue;
    private JButton btnGenerateBill, btnClear;

    // Insurance sub-panel
    private JPanel insurancePanel;
    private JComboBox<InsuranceProvider> cmbInsuranceProvider;
    private JTextField txtPolicyNumber;
    private JTextField txtClaimAmount;
    private JTextArea txtRemarks;

    // Backend
    private PatientDAO patientDAO;
    private InsuranceProviderDAO insuranceProviderDAO;
    private BillingService billingService;
    private Patient currentPatient = null;
    private Bill lastGeneratedBill = null;

    public BillingPanel() {
        // Init backend
        this.patientDAO = new PatientDAO();
        this.insuranceProviderDAO = new InsuranceProviderDAO();
        BillDAO billDAO = new BillDAO();
        InsuranceClaimDAO claimDAO = new InsuranceClaimDAO();
        this.billingService = new BillingService(billDAO);

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        setupLayout();
        addListeners();
        loadInitialData();
    }

    private void initComponents() {
        txtPatientId = new JTextField(10);
        btnFetchPatient = new JButton("Fetch Patient");
        lblPatientInfo = new JLabel("Patient details will appear here.");
        txtDescription = new JTextArea(3, 20);
        txtAmount = new JTextField(15);
        cmbPaymentMethod = new JComboBox<>(new String[]{"Cash", "Card", "Insurance"});
        lblBalanceDue = new JLabel("LKR 0.00");
        btnGenerateBill = new JButton("Generate Bill & Process");
        btnClear = new JButton("Clear Form");

        // Insurance components
        insurancePanel = new JPanel(new GridBagLayout());
        insurancePanel.setBorder(new TitledBorder("Insurance Claim Details"));
        cmbInsuranceProvider = new JComboBox<>();
        txtPolicyNumber = new JTextField(15);
        txtClaimAmount = new JTextField(15);
        txtRemarks = new JTextArea(2, 20);
    }

    private void setupLayout() {
        JPanel mainFormPanel = new JPanel();
        mainFormPanel.setLayout(new BoxLayout(mainFormPanel, BoxLayout.Y_AXIS));

        // Patient Panel
        JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        patientPanel.setBorder(new TitledBorder("1. Patient Information"));
        patientPanel.add(new JLabel("Patient ID:"));
        patientPanel.add(txtPatientId);
        patientPanel.add(btnFetchPatient);
        patientPanel.add(lblPatientInfo);
        mainFormPanel.add(patientPanel);

        // Billing Details Panel
        JPanel billingDetailsPanel = new JPanel(new GridBagLayout());
        billingDetailsPanel.setBorder(new TitledBorder("2. Billing Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; billingDetailsPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; billingDetailsPanel.add(new JScrollPane(txtDescription), gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.0; billingDetailsPanel.add(new JLabel("Total Amount (LKR):"), gbc);
        gbc.gridx = 1; billingDetailsPanel.add(txtAmount, gbc);
        gbc.gridy++; gbc.gridx = 0; billingDetailsPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1; billingDetailsPanel.add(cmbPaymentMethod, gbc);
        mainFormPanel.add(billingDetailsPanel);

        // Insurance Panel (initially hidden)
        GridBagConstraints insGbc = (GridBagConstraints) gbc.clone();
        insGbc.gridy = 0; insurancePanel.add(new JLabel("Provider:"), insGbc);
        insGbc.gridx = 1; insurancePanel.add(cmbInsuranceProvider, insGbc);
        insGbc.gridy++; insGbc.gridx = 0; insurancePanel.add(new JLabel("Policy Number:"), insGbc);
        insGbc.gridx = 1; insurancePanel.add(txtPolicyNumber, insGbc);
        insGbc.gridy++; insGbc.gridx = 0; insurancePanel.add(new JLabel("Claim Amount:"), insGbc);
        insGbc.gridx = 1; insurancePanel.add(txtClaimAmount, insGbc);
        insGbc.gridy++; insGbc.gridx = 0; insurancePanel.add(new JLabel("Remarks:"), insGbc);
        insGbc.gridx = 1; insurancePanel.add(new JScrollPane(txtRemarks), insGbc);
        mainFormPanel.add(insurancePanel);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBorder(new TitledBorder("3. Summary"));
        summaryPanel.add(new JLabel("Balance Due by Patient:"));
        lblBalanceDue.setFont(new Font("SansSerif", Font.BOLD, 16));
        summaryPanel.add(lblBalanceDue);
        mainFormPanel.add(summaryPanel);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.add(btnGenerateBill);
        actionPanel.add(btnClear);

        add(mainFormPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        btnFetchPatient.addActionListener(e -> fetchPatient());
        btnGenerateBill.addActionListener(e -> generateBill());
        btnClear.addActionListener(e -> clearForm());

        cmbPaymentMethod.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                insurancePanel.setVisible("Insurance".equals(e.getItem()));
                calculateBalance();
            }
        });

        DocumentListener balanceCalculator = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateBalance(); }
            public void removeUpdate(DocumentEvent e) { calculateBalance(); }
            public void changedUpdate(DocumentEvent e) { calculateBalance(); }
        };
        txtAmount.getDocument().addDocumentListener(balanceCalculator);
        txtClaimAmount.getDocument().addDocumentListener(balanceCalculator);
    }

    private void loadInitialData() {
        if (insuranceProviderDAO.findAll().isEmpty()) {
            insuranceProviderDAO.save(new InsuranceProvider("Ceylinco Life"));
            insuranceProviderDAO.save(new InsuranceProvider("AIA Insurance"));
        }
        insuranceProviderDAO.findAll().forEach(cmbInsuranceProvider::addItem);
        clearForm();
    }

    private void clearForm() {
        currentPatient = null;
        lastGeneratedBill = null;
        txtPatientId.setText("");
        lblPatientInfo.setText("Patient details will appear here.");
        lblPatientInfo.setForeground(Color.DARK_GRAY);
        txtDescription.setText("");
        txtAmount.setText("");
        cmbPaymentMethod.setSelectedIndex(0);
        insurancePanel.setVisible(false);
        txtPolicyNumber.setText("");
        txtClaimAmount.setText("");
        txtRemarks.setText("");
        cmbInsuranceProvider.setSelectedIndex(0);
        calculateBalance();
    }

    private void calculateBalance() {
        try {
            double totalAmount = Double.parseDouble(txtAmount.getText().isEmpty() ? "0" : txtAmount.getText());
            double claimAmount = 0;
            if (insurancePanel.isVisible()) {
                claimAmount = Double.parseDouble(txtClaimAmount.getText().isEmpty() ? "0" : txtClaimAmount.getText());
            }
            double balance = totalAmount - claimAmount;
            lblBalanceDue.setText(String.format("LKR %.2f", balance));
        } catch (NumberFormatException e) {
            lblBalanceDue.setText("Invalid Amount");
        }
    }

    private void fetchPatient() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText().trim());
            currentPatient = patientDAO.findById(patientId);
            if (currentPatient != null) {
                lblPatientInfo.setText("Name: " + currentPatient.getFullname());
                lblPatientInfo.setForeground(Color.BLUE);
            } else {
                lblPatientInfo.setText("Patient not found.");
                lblPatientInfo.setForeground(Color.RED);
            }
        } catch (NumberFormatException e) {
            lblPatientInfo.setText("Invalid ID.");
            lblPatientInfo.setForeground(Color.RED);
        }
    }

    private boolean validateInput() {
        if (currentPatient == null) {
            JOptionPane.showMessageDialog(this, "A valid patient must be fetched.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            if (Double.parseDouble(txtAmount.getText()) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Total Amount must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if ("Insurance".equals(cmbPaymentMethod.getSelectedItem())) {
            try {
                if (Double.parseDouble(txtClaimAmount.getText()) <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Claim Amount must be a positive number for insurance.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void generateBill() {
        if (!validateInput()) return;

        Bill bill = new Bill();
        bill.setPatient(currentPatient);
        bill.setDescription(txtDescription.getText());
        bill.setAmount(Double.parseDouble(txtAmount.getText()));
        bill.setPaymentMethod((String) cmbPaymentMethod.getSelectedItem());

        InsuranceClaim claim = null;
        if ("Insurance".equals(bill.getPaymentMethod())) {
            claim = new InsuranceClaim();
            claim.setProvider((InsuranceProvider) cmbInsuranceProvider.getSelectedItem());
            claim.setPolicyNumber(txtPolicyNumber.getText());
            claim.setClaimAmount(Double.parseDouble(txtClaimAmount.getText()));
            claim.setRemarks(txtRemarks.getText());
        }

        billingService.generateBill(bill, claim);
        lastGeneratedBill = bill;

        // Display summary of processing
        String summary;
        if (claim != null) {
            summary = String.format("Bill #%d generated.\nInsurance claim status: %s\nPatient Balance: LKR %.2f",
                    bill.getId(), claim.getClaimStatus(), bill.getBalanceDue());
        } else {
            summary = String.format("Bill #%d generated.\nPaid in full by %s.",
                    bill.getId(), bill.getPaymentMethod());
        }
        JOptionPane.showMessageDialog(this, summary, "Processing Complete", JOptionPane.INFORMATION_MESSAGE);

        clearForm();
    }
}