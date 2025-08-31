package com.globemed.view.panels;

import com.globemed.dao.GenderDAO;
import com.globemed.dao.PatientDAO;
import com.globemed.model.Gender;
import com.globemed.model.Patient;
import com.globemed.service.PatientService;
import com.globemed.view.components.PatientTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.regex.Pattern;

public class PatientManagementPanel extends JPanel {

    private JTextField txtFullname, txtAge, txtContact, txtAddress, txtNic, txtSearch;
    private JRadioButton rbMale, rbFemale;
    private ButtonGroup genderGroup;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable patientTable;
    private PatientTableModel patientTableModel;

    private PatientService patientService;
    private PatientDAO patientDAO;
    private GenderDAO genderDAO;
    private Patient selectedPatient = null;

    public PatientManagementPanel() {
        patientDAO = new PatientDAO();
        patientService = new PatientService(patientDAO);
        genderDAO = new GenderDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        setupLayout();
        addListeners();

        loadInitialData();
    }

    private void initComponents() {
        txtFullname = new JTextField(20);
        txtAge = new JTextField(5);
        txtContact = new JTextField(15);
        txtAddress = new JTextField(30);
        txtNic = new JTextField(15);
        txtSearch = new JTextField(20);

        rbMale = new JRadioButton("Male");
        rbFemale = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbMale);
        genderGroup.add(rbFemale);

        btnAdd = new JButton("Add New");
        btnUpdate = new JButton("Update Selected");
        btnDelete = new JButton("Delete Selected");
        btnClear = new JButton("Clear Form");
        btnSearch = new JButton("Search");

        patientTableModel = new PatientTableModel();
        patientTable = new JTable(patientTableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setRowHeight(25);
    }

    private void setupLayout() {
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Patient Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Full Name
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(txtFullname, gbc);

        // Row 1: Age & Contact
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 1; formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; formPanel.add(txtAge, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 3; formPanel.add(txtContact, gbc);

        // Row 2: Address
        gbc.gridy = 2; gbc.gridx = 0; formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(txtAddress, gbc);

        // Row 3: NIC & Gender
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 1; formPanel.add(new JLabel("NIC:"), gbc);
        gbc.gridx = 1; formPanel.add(txtNic, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Gender:"), gbc);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.add(rbMale); genderPanel.add(rbFemale);
        gbc.gridx = 3; formPanel.add(genderPanel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(new TitledBorder("Find Patient"));
        searchPanel.add(new JLabel("Search by Name/NIC/Contact:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Top Container
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);
        add(new JScrollPane(patientTable), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        patientTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedPatient = patientTableModel.getPatientAt(selectedRow);
                    displayPatientInForm(selectedPatient);
                    setFormState(false); // State for editing
                }
            }
        });

        btnAdd.addActionListener(e -> addPatient());
        btnUpdate.addActionListener(e -> updatePatient());
        btnDelete.addActionListener(e -> deletePatient());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchPatients());
    }

    private void loadInitialData() {
        // Ensure default genders exist
        if (genderDAO.getGenderByName("Male") == null) genderDAO.save(new Gender("Male"));
        if (genderDAO.getGenderByName("Female") == null) genderDAO.save(new Gender("Female"));

        loadPatientsToTable();
        clearForm();
    }

    private void loadPatientsToTable() {
        List<Patient> patients = patientService.getAllPatients();
        patientTableModel.setPatients(patients);
    }

    private void displayPatientInForm(Patient p) {
        txtFullname.setText(p.getFullname());
        txtAge.setText(String.valueOf(p.getAge()));
        txtContact.setText(p.getContact());
        txtAddress.setText(p.getAddress());
        txtNic.setText(p.getNic());
        if ("Male".equalsIgnoreCase(p.getGender().getName())) {
            rbMale.setSelected(true);
        } else {
            rbFemale.setSelected(true);
        }
    }

    private void clearForm() {
        selectedPatient = null;
        txtFullname.setText("");
        txtAge.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        txtNic.setText("");
        txtSearch.setText("");
        genderGroup.clearSelection();
        patientTable.clearSelection();
        setFormState(true); // State for adding new
        loadPatientsToTable();
    }

    private void setFormState(boolean isAdding) {
        btnAdd.setEnabled(isAdding);
        btnUpdate.setEnabled(!isAdding);
        btnDelete.setEnabled(!isAdding);
    }

    private boolean validateInput() {
        if (txtFullname.getText().trim().isEmpty() || txtAddress.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Address cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int age = Integer.parseInt(txtAge.getText());
            if (age <= 0 || age > 120) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age (1-120).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!Pattern.matches("^0\\d{9}$", txtContact.getText())) {
            JOptionPane.showMessageDialog(this, "Contact number must be 10 digits and start with 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!Pattern.matches("^([0-9]{9}[vVxX]|[0-9]{12})$", txtNic.getText())) {
            JOptionPane.showMessageDialog(this, "NIC must be 9 digits with 'V' or 12 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!rbMale.isSelected() && !rbFemale.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a gender.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addPatient() {
        if (!validateInput()) return;

        String fullname = txtFullname.getText();
        int age = Integer.parseInt(txtAge.getText());
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String nic = txtNic.getText();
        Gender gender = genderDAO.getGenderByName(rbMale.isSelected() ? "Male" : "Female");

        Patient newPatient = new Patient(fullname, age, contact, address, nic, gender);
        patientService.addPatient(newPatient);

        JOptionPane.showMessageDialog(this, "Patient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void updatePatient() {
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        selectedPatient.setFullname(txtFullname.getText());
        selectedPatient.setAge(Integer.parseInt(txtAge.getText()));
        selectedPatient.setContact(txtContact.getText());
        selectedPatient.setAddress(txtAddress.getText());
        selectedPatient.setNic(txtNic.getText());
        selectedPatient.setGender(genderDAO.getGenderByName(rbMale.isSelected() ? "Male" : "Female"));

        patientService.updatePatient(selectedPatient);
        JOptionPane.showMessageDialog(this, "Patient updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void deletePatient() {
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedPatient.getFullname() + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            patientService.deletePatient(selectedPatient.getId());
            JOptionPane.showMessageDialog(this, "Patient deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        }
    }

    private void searchPatients() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadPatientsToTable();
        } else {
            patientTableModel.setPatients(patientService.searchPatients(searchTerm));
        }
    }
}