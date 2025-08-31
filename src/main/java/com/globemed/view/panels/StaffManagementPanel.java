package com.globemed.view.panels;

import com.globemed.dao.*;
import com.globemed.model.*;
import com.globemed.service.StaffService;
import com.globemed.view.components.StaffTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.regex.Pattern;

public class StaffManagementPanel extends JPanel {

    private JTextField txtFullname, txtContact, txtEmail, txtNic, txtSearch;
    private JPasswordField txtPassword;
    private JComboBox<Role> cmbRole;
    private JComboBox<Department> cmbDepartment;
    private JComboBox<Status> cmbStatus;
    private JRadioButton rbMale, rbFemale;
    private ButtonGroup genderGroup;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable staffTable;
    private StaffTableModel staffTableModel;

    private StaffService staffService;
    private RoleDAO roleDAO;
    private DepartmentDAO departmentDAO;
    private GenderDAO genderDAO;
    private StatusDAO statusDAO;
    private Staff selectedStaff = null;

    public StaffManagementPanel() {
        // DAOs
        StaffDAO staffDAO = new StaffDAO();
        this.roleDAO = new RoleDAO();
        this.departmentDAO = new DepartmentDAO();
        this.genderDAO = new GenderDAO();
        this.statusDAO = new StatusDAO();
        // Service
        this.staffService = new StaffService(staffDAO);

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        setupLayout();
        addListeners();
        loadInitialData();
    }

    private void initComponents() {
        txtFullname = new JTextField(20);
        txtContact = new JTextField(15);
        txtEmail = new JTextField(20);
        txtNic = new JTextField(15);
        txtPassword = new JPasswordField(20);
        txtSearch = new JTextField(20);

        cmbRole = new JComboBox<>();
        cmbDepartment = new JComboBox<>();
        cmbStatus = new JComboBox<>();

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

        staffTableModel = new StaffTableModel();
        staffTable = new JTable(staffTableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.setRowHeight(25);
    }

    private void setupLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Staff Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; formPanel.add(txtFullname, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 3; formPanel.add(txtContact, gbc);

        // Row 1
        gbc.gridy = 1; gbc.gridx = 0; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; formPanel.add(txtEmail, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("NIC:"), gbc);
        gbc.gridx = 3; formPanel.add(txtNic, gbc);

        // Row 2
        gbc.gridy = 2; gbc.gridx = 0; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; formPanel.add(txtPassword, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 3; formPanel.add(cmbRole, gbc);

        // Row 3
        gbc.gridy = 3; gbc.gridx = 0; formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; formPanel.add(cmbDepartment, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3; formPanel.add(cmbStatus, gbc);

        // Row 4
        gbc.gridy = 4; gbc.gridx = 0; formPanel.add(new JLabel("Gender:"), gbc);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.add(rbMale); genderPanel.add(rbFemale);
        gbc.gridx = 1; formPanel.add(genderPanel, gbc);

        // Action & Search panels... (similar to Patient panel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(new TitledBorder("Find Staff"));
        searchPanel.add(new JLabel("Search by Name/NIC/Email:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);
        add(new JScrollPane(staffTable), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        staffTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = staffTable.getSelectedRow();
                if (row != -1) {
                    selectedStaff = staffTableModel.getStaffAt(row);
                    displayStaffInForm(selectedStaff);
                    setFormState(false);
                }
            }
        });

        btnAdd.addActionListener(e -> addStaff());
        btnUpdate.addActionListener(e -> updateStaff());
        btnDelete.addActionListener(e -> deleteStaff());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchStaff());
    }

    private void loadInitialData() {
        // Load data for ComboBoxes, creating defaults if empty
        if (roleDAO.findAll().isEmpty()) {
            roleDAO.save(new Role("Admin", "System Administrator"));
            roleDAO.save(new Role("Doctor", "Medical Doctor"));
            roleDAO.save(new Role("Nurse", "Registered Nurse"));
            roleDAO.save(new Role("Receptionist", "Front Desk Staff"));
        }
        roleDAO.findAll().forEach(cmbRole::addItem);

        if (departmentDAO.findAll().isEmpty()) {
            departmentDAO.save(new Department("Administration", "General Admin"));
            departmentDAO.save(new Department("Cardiology", "Heart Department"));
            departmentDAO.save(new Department("General Medicine", "OPD"));
        }
        departmentDAO.findAll().forEach(cmbDepartment::addItem);

        if (statusDAO.findAll().isEmpty()) {
            statusDAO.save(new Status("Active", "Employee is currently active"));
            statusDAO.save(new Status("Inactive", "Employee is not active"));
            statusDAO.save(new Status("On Leave", "Employee is on leave"));
        }
        statusDAO.findAll().forEach(cmbStatus::addItem);

        loadStaffToTable();
        clearForm();
    }

    private void loadStaffToTable() {
        staffTableModel.setStaffMembers(staffService.getAllStaff());
    }

    private void displayStaffInForm(Staff s) {
        txtFullname.setText(s.getFullname());
        txtContact.setText(s.getContact());
        txtEmail.setText(s.getEmail());
        txtNic.setText(s.getNic());
        txtPassword.setText(""); // Never display password
        cmbRole.setSelectedItem(s.getRole());
        cmbDepartment.setSelectedItem(s.getDepartment());
        cmbStatus.setSelectedItem(s.getStatus());
        if ("Male".equalsIgnoreCase(s.getGender().getName())) {
            rbMale.setSelected(true);
        } else {
            rbFemale.setSelected(true);
        }
    }

    private void clearForm() {
        selectedStaff = null;
        txtFullname.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtNic.setText("");
        txtPassword.setText("");
        txtSearch.setText("");
        cmbRole.setSelectedIndex(0);
        cmbDepartment.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        genderGroup.clearSelection();
        staffTable.clearSelection();
        setFormState(true);
        loadStaffToTable();
    }

    private void setFormState(boolean isAdding) {
        btnAdd.setEnabled(isAdding);
        btnUpdate.setEnabled(!isAdding);
        btnDelete.setEnabled(!isAdding);
        txtPassword.setEnabled(isAdding); // Only enable password on add
        if (!isAdding) {
            txtPassword.setToolTipText("Leave blank to keep current password");
        } else {
            txtPassword.setToolTipText(null);
        }
    }

    private boolean validateInput(boolean isAdding) {
        if (txtFullname.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (isAdding && new String(txtPassword.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required for new staff.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Email validation
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Other validations from Patient panel...
        return true;
    }

    private void addStaff() {
        if (!validateInput(true)) return;

        Staff newStaff = new Staff();
        newStaff.setFullname(txtFullname.getText());
        newStaff.setContact(txtContact.getText());
        newStaff.setEmail(txtEmail.getText());
        newStaff.setNic(txtNic.getText());
        newStaff.setRole((Role) cmbRole.getSelectedItem());
        newStaff.setDepartment((Department) cmbDepartment.getSelectedItem());
        newStaff.setStatus((Status) cmbStatus.getSelectedItem());
        newStaff.setGender(genderDAO.getGenderByName(rbMale.isSelected() ? "Male" : "Female"));

        staffService.addStaff(newStaff, new String(txtPassword.getPassword()));
        JOptionPane.showMessageDialog(this, "Staff added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void updateStaff() {
        if (selectedStaff == null) return;
        if (!validateInput(false)) return;

        selectedStaff.setFullname(txtFullname.getText());
        selectedStaff.setContact(txtContact.getText());
        selectedStaff.setEmail(txtEmail.getText());
        selectedStaff.setNic(txtNic.getText());
        selectedStaff.setRole((Role) cmbRole.getSelectedItem());
        selectedStaff.setDepartment((Department) cmbDepartment.getSelectedItem());
        selectedStaff.setStatus((Status) cmbStatus.getSelectedItem());
        selectedStaff.setGender(genderDAO.getGenderByName(rbMale.isSelected() ? "Male" : "Female"));

        staffService.updateStaff(selectedStaff, new String(txtPassword.getPassword()));
        JOptionPane.showMessageDialog(this, "Staff updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void deleteStaff() {
        if (selectedStaff == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete " + selectedStaff.getFullname() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            staffService.deleteStaff(selectedStaff.getId());
            JOptionPane.showMessageDialog(this, "Staff deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        }
    }

    private void searchStaff() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadStaffToTable();
        } else {
            staffTableModel.setStaffMembers(staffService.searchStaff(searchTerm));
        }
    }
}