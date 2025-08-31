package com.globemed.model;

import com.globemed.dp.visitor.IVisitableElement;
import com.globemed.dp.visitor.IReportVisitor;
import javax.persistence.*;

@Entity
@Table(name = "patient")
public class Patient implements IVisitableElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname", nullable = false, length = 255)
    private String fullname;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "contact", nullable = false, unique = true, length = 10)
    private String contact;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "nic", nullable = false, unique = true, length = 15)
    private String nic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    public Patient() {}

    public Patient(String fullname, int age, String contact, String address, String nic, Gender gender) {
        this.fullname = fullname;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.nic = nic;
        this.gender = gender;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    @Override
    public void accept(IReportVisitor visitor) {
        visitor.visit(this);
    }
}