package com.globemed.view.components;

import com.globemed.model.Staff;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StaffTableModel extends AbstractTableModel {
    private List<Staff> staffMembers;
    private final String[] columnNames = {"ID", "Full Name", "Contact", "Email", "NIC", "Role", "Department", "Status"};

    public StaffTableModel() {
        this.staffMembers = new ArrayList<>();
    }

    public void setStaffMembers(List<Staff> staffMembers) {
        this.staffMembers = staffMembers;
        fireTableDataChanged();
    }

    public Staff getStaffAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < staffMembers.size()) {
            return staffMembers.get(rowIndex);
        }
        return null;
    }

    @Override public int getRowCount() { return staffMembers.size(); }
    @Override public int getColumnCount() { return columnNames.length; }
    @Override public String getColumnName(int column) { return columnNames[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Staff staff = staffMembers.get(rowIndex);
        switch (columnIndex) {
            case 0: return staff.getId();
            case 1: return staff.getFullname();
            case 2: return staff.getContact();
            case 3: return staff.getEmail();
            case 4: return staff.getNic();
            case 5: return staff.getRole() != null ? staff.getRole().getName() : "N/A";
            case 6: return staff.getDepartment() != null ? staff.getDepartment().getName() : "N/A";
            case 7: return staff.getStatus() != null ? staff.getStatus().getName() : "N/A";
            default: return null;
        }
    }
}