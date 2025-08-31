package com.globemed.view.panels;

import com.globemed.dp.mediator.AppointmentMediator;
import com.globemed.dp.mediator.IAppointmentMediator;
import com.globemed.model.Department;
import com.globemed.model.Staff;
import com.globemed.view.components.AppointmentTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * The main UI Panel for scheduling appointments. This class acts as the
 * container for all "Colleague" components. It creates the components
 * and the Mediator, registers them with each other, and then delegates
 * all event handling to the Mediator. The panel itself contains no
 * business logic.
 */
public class AppointmentSchedulingPanel extends JPanel {

    // UI Components (Colleagues)
    private JTextField txtPatientId;
    private JLabel lblPatientName;
    private JComboBox<Department> cmbDepartment;
    private JComboBox<Staff> cmbDoctor;
    private JComboBox<LocalTime> cmbTimeSlot;
    private JComboBox<String> cmbPaymentType;
    private JSpinner dateSpinner;
    private JButton btnBookAppointment, btnCancelAppointment, btnCheckPatient;
    private JTable appointmentTable;
    private AppointmentTableModel appointmentTableModel;

    // The Mediator
    private IAppointmentMediator mediator;

    public AppointmentSchedulingPanel() {
        // 1. Create the Mediator that will manage this panel's logic
        this.mediator = new AppointmentMediator();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 2. Initialize all the UI components (Colleagues)
        initComponents();

        // 3. Register the components with the Mediator
        registerColleagues();

        // 4. Arrange the components on the panel
        setupLayout();

        // 5. Connect the components' events to the Mediator
        addListeners();

        // 6. Tell the Mediator to load its initial data
        mediator.initialize();
    }

    private void initComponents() {
        txtPatientId = new JTextField(10);
        lblPatientName = new JLabel("Patient details will appear here.");
        lblPatientName.setForeground(Color.BLUE);

        cmbDepartment = new JComboBox<>();
        cmbDoctor = new JComboBox<>();
        cmbTimeSlot = new JComboBox<>();
        cmbPaymentType = new JComboBox<>(new String[]{"Cash", "Card", "Insurance"});

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        btnBookAppointment = new JButton("Book Appointment");
        btnCancelAppointment = new JButton("Cancel Selected Appointment");
        btnCheckPatient = new JButton("Check Patient");

        appointmentTableModel = new AppointmentTableModel();
        appointmentTable = new JTable(appointmentTableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.setRowHeight(25);
    }

    private void registerColleagues() {
        // The panel tells the Mediator about all the components it needs to manage.
        // This is a crucial step in the Mediator pattern setup.
        if (mediator instanceof AppointmentMediator) {
            AppointmentMediator concreteMediator = (AppointmentMediator) mediator;
            concreteMediator.registerPatientIdField(txtPatientId);
            concreteMediator.registerPatientNameLabel(lblPatientName);
            concreteMediator.registerDepartmentComboBox(cmbDepartment);
            concreteMediator.registerDoctorComboBox(cmbDoctor);
            concreteMediator.registerTimeSlotComboBox(cmbTimeSlot);
            concreteMediator.registerPaymentTypeComboBox(cmbPaymentType);
            concreteMediator.registerDateSpinner(dateSpinner);
            concreteMediator.registerAppointmentTable(appointmentTable);
            concreteMediator.registerCancelButton(btnCancelAppointment);
        }
    }

    private void addListeners() {
        // Event listeners simply delegate to the Mediator.
        // The UI components have no knowledge of each other.
        cmbDepartment.addActionListener(e -> mediator.departmentChanged());
        cmbDoctor.addActionListener(e -> mediator.doctorOrDateChanged());
        dateSpinner.addChangeListener(e -> mediator.doctorOrDateChanged());

        btnCheckPatient.addActionListener(e -> mediator.checkPatient());
        btnBookAppointment.addActionListener(e -> mediator.bookAppointment());
        btnCancelAppointment.addActionListener(e -> mediator.cancelAppointment());

        // Some minor UI logic can remain in the panel
        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnCancelAppointment.setEnabled(appointmentTable.getSelectedRow() != -1);
            }
        });
    }

    private void setupLayout() {
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Book New Appointment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Patient Info
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Patient ID:"), gbc);
        JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        patientPanel.add(txtPatientId);
        patientPanel.add(btnCheckPatient);
        gbc.gridx = 1; formPanel.add(patientPanel, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2; formPanel.add(lblPatientName, gbc);

        // Row 1: Department & Doctor
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 1; formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; formPanel.add(cmbDepartment, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 3; formPanel.add(cmbDoctor, gbc);

        // Row 2: Date & Time
        gbc.gridy = 2; gbc.gridx = 0; formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1; formPanel.add(dateSpinner, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Time Slot:"), gbc);
        gbc.gridx = 3; formPanel.add(cmbTimeSlot, gbc);

        // Row 3: Payment
        gbc.gridy = 3; gbc.gridx = 0; formPanel.add(new JLabel("Payment Type:"), gbc);
        gbc.gridx = 1; formPanel.add(cmbPaymentType, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnBookAppointment);
        buttonPanel.add(btnCancelAppointment);

        // Top Container
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        // Main Layout
        add(topContainer, BorderLayout.NORTH);
        add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
    }
}