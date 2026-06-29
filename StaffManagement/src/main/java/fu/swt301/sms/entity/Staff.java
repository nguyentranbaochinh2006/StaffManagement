package fu.swt301.sms.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Staff {
    private int staffID;
    private String fullName;
    private boolean gender;
    private String phoneNumber;
    private String email;
    private String password;
    private Role role; // Changed from String to Role
    private boolean isActive;
    
// === CHỨC NĂNG FR-07: Thêm mới nhân viên với các trường: mã nhân viên, họ tên, ngày sinh, giới tính, email, số điện thoại, phòng ban, chức vụ, lương, ngày vào làm - DO TÔI TÊN Chinh bổ sung CODE ===
    private String staffCode;
private java.sql.Date dateOfBirth;
private String department;
private String position;
private java.math.BigDecimal salary;
private java.sql.Date hireDate;




    // === CHỨC NĂNG FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
    private int failedAttempts;
    private java.sql.Timestamp lockTime;

    
    
    
    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // === CHỨC NĂNG FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public java.sql.Timestamp getLockTime() {
        return lockTime;
    }

    public void setLockTime(java.sql.Timestamp lockTime) {
        this.lockTime = lockTime;
    }
    
    // === CHỨC NĂNG FR-07: Thêm mới nhân viên với các trường: mã nhân viên, họ tên, ngày sinh, giới tính, email, số điện thoại, phòng ban, chức vụ, lương, ngày vào làm - DO TÔI TÊN Chinh bổ sung CODE ===
     public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
    this.hireDate = hireDate;
}
}