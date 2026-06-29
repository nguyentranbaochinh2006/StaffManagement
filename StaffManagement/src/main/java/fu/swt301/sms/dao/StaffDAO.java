package fu.swt301.sms.dao;

import fu.swt301.sms.entity.Role;
import fu.swt301.sms.entity.Staff;
import fu.swt301.sms.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Staff entity. This class provides all the
 * necessary methods to interact with the 'Staff' table in the database. It
 * handles all CRUD (Create, Read, Update, Delete) operations as well as other
 * specific queries.
 */
public class StaffDAO {

    /**
     * A private helper method to map a row from the ResultSet to a Staff
     * object. This avoids code duplication in methods that retrieve staff data.
     * It performs a JOIN with the Role table to populate the nested Role
     * object.
     *
     * @param rs The ResultSet cursor, positioned at the row to be mapped.
     * @return A fully populated Staff object.
     * @throws SQLException if a database access error occurs.
     */
    
    //được sauwr để phù hợp với fr1 và 7
    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
    Staff staff = new Staff();

    staff.setStaffID(rs.getInt("StaffID"));
    staff.setStaffCode(rs.getString("StaffCode"));
    staff.setFullName(rs.getString("FullName"));
    staff.setDateOfBirth(rs.getDate("DateOfBirth"));
    staff.setGender(rs.getBoolean("Gender"));
    staff.setPhoneNumber(rs.getString("PhoneNumber"));
    staff.setEmail(rs.getString("Email"));
    staff.setPassword(rs.getString("Password"));
    staff.setDepartment(rs.getString("Department"));
    staff.setPosition(rs.getString("Position"));
    staff.setSalary(rs.getBigDecimal("Salary"));
    staff.setHireDate(rs.getDate("HireDate"));
    staff.setIsActive(rs.getBoolean("IsActive"));

    Role role = new Role();
    role.setRoleID(rs.getInt("Role_ID"));
    role.setRoleName(rs.getString("Role_Name"));
    staff.setRole(role);

    staff.setFailedAttempts(rs.getInt("FailedAttempts"));
    staff.setLockTime(rs.getTimestamp("LockTime"));

    
    
    return staff;
}

    /**
     * Checks if a given email already exists in the Staff table, excluding a
     * specific staff member. This is crucial for validation during updates to
     * prevent a user from taking another user's email.
     *
     * @param email The email to check for existence.
     * @param currentStaffId The ID of the staff member being updated. Use 0
     * when creating a new staff member.
     * @return true if the email exists for another staff member, false
     * otherwise.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver is not found.
     */
    public boolean isEmailExists(String email, int currentStaffId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Staff WHERE Email = ? AND StaffID != ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, currentStaffId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Checks if a given full name already exists in the Staff table, excluding
     * a specific staff member.
     *
     * @param fullName The full name to check.
     * @param currentStaffId The ID of the staff member being updated. Use 0 for
     * new staff.
     * @return true if the name exists for another staff member, false
     * otherwise.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver is not found.
     */
    public boolean isFullNameExists(String fullName, int currentStaffId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Staff WHERE FullName = ? AND StaffID != ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setInt(2, currentStaffId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Checks if a given phone number already exists in the Staff table,
     * excluding a specific staff member.
     *
     * @param phoneNumber The phone number to check.
     * @param currentStaffId The ID of the staff member being updated. Use 0 for
     * new staff.
     * @return true if the phone number exists for another staff member, false
     * otherwise.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver is not found.
     */
    public boolean isPhoneNumberExists(String phoneNumber, int currentStaffId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Staff WHERE PhoneNumber = ? AND StaffID != ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            ps.setInt(2, currentStaffId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    
    //===Chức năng fr6 7===
    public boolean isStaffCodeExists(String staffCode, int currentStaffId)
        throws SQLException, ClassNotFoundException {

    String sql =
        "SELECT COUNT(*) FROM Staff WHERE StaffCode = ? AND StaffID <> ?";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, staffCode);
        ps.setInt(2, currentStaffId);

        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}

    /**
     * Authenticates a user by checking their email and password against the
     * database.
     *
     * @param email The user's email.
     * @param password The user's plain text password.
     * @return A populated Staff object if authentication is successful, null
     * otherwise.
     */
    // === CHỨC NĂNG FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
    private void resetLock(int staffID) {
        String sql = "UPDATE Staff SET FailedAttempts = 0, LockTime = NULL WHERE StaffID = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === CHỨC NĂNG FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
    private void updateLock(int staffID, int failedAttempts, java.sql.Timestamp lockTime) {
        String sql = "UPDATE Staff SET FailedAttempts = ?, LockTime = ? WHERE StaffID = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, failedAttempts);
            ps.setTimestamp(2, lockTime);
            ps.setInt(3, staffID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Staff checkLogin(String email, String password) {
        // === CHỨC NĂNG FR-01: Đăng nhập bằng BCrypt & FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
        String sql = "SELECT s.*, r.Role_Name FROM Staff s JOIN Role r ON s.Role_ID = r.Role_ID WHERE s.Email = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Staff staff = extractStaffFromResultSet(rs);

                    // Check account locking
                    java.sql.Timestamp lockTime = staff.getLockTime();
                    if (lockTime != null) {
                        long lockTimeMs = lockTime.getTime();
                        long currentTimeMs = System.currentTimeMillis();
                        if (currentTimeMs - lockTimeMs < 5 * 60 * 1000) { // 5 minutes
                            throw new RuntimeException("lock");
                        } else {
                            // Lock has expired, reset in DB and memory
                            resetLock(staff.getStaffID());
                            staff.setFailedAttempts(0);
                            staff.setLockTime(null);
                        }
                    }

                    // Verify hashed password
                    if (org.mindrot.jbcrypt.BCrypt.checkpw(password, staff.getPassword())) {
                        // Check account status chinh sửa
                        if (!staff.isIsActive()) {
                            return null;
                        }

                        if (staff.getFailedAttempts() > 0) {
                            resetLock(staff.getStaffID());
                        }

                        return staff;
                    } else {
                        // Increment failed attempts
                        int newAttempts = staff.getFailedAttempts() + 1;
                        java.sql.Timestamp newLockTime = null;
                        if (newAttempts >= 5) {
                            newLockTime = new java.sql.Timestamp(System.currentTimeMillis());
                        }
                        updateLock(staff.getStaffID(), newAttempts, newLockTime);

                        if (newAttempts >= 5) {
                            throw new RuntimeException("lock");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of staff members based on optional filter criteria.
     *
     * @param name A string to search for in the full name (case-insensitive).
     * Can be null or empty.
     * @param status The active status to filter by ("true" or "false"). Can be
     * null or empty.
     * @return A list of Staff objects matching the criteria.
     */
    public List<Staff> getStaffByFilter(String name, String staffCode, String department) {
    List<Staff> staffList = new ArrayList<>();

    StringBuilder sql = new StringBuilder(
            "SELECT s.*, r.Role_Name " +
            "FROM Staff s " +
            "JOIN Role r ON s.Role_ID = r.Role_ID " +
            "WHERE 1=1");

    if (name != null && !name.trim().isEmpty()) {
        sql.append(" AND s.FullName LIKE ?");
    }

    if (staffCode != null && !staffCode.trim().isEmpty()) {
        sql.append(" AND s.StaffCode LIKE ?");
    }

    if (department != null && !department.trim().isEmpty()) {
        sql.append(" AND s.Department LIKE ?");
    }

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int index = 1;

        if (name != null && !name.trim().isEmpty()) {
            ps.setString(index++, "%" + name + "%");
        }

        if (staffCode != null && !staffCode.trim().isEmpty()) {
            ps.setString(index++, "%" + staffCode + "%");
        }

        if (department != null && !department.trim().isEmpty()) {
            ps.setString(index++, "%" + department + "%");
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                staffList.add(extractStaffFromResultSet(rs));
            }
        }

    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }

    return staffList;
}

    /**
     * Inserts a new staff member into the database.
     *
     * @param staff The Staff object containing the data to be inserted.
     */
   
    // đã được chỉnh sửa để phù hợp với cả fr1 và 7
    public void createStaff(Staff staff) {

    String sql =
            "INSERT INTO Staff(" +
            "StaffCode,FullName,DateOfBirth,Gender," +
            "PhoneNumber,Email,Password," +
            "Department,Position,Salary,HireDate," +
            "Role_ID,IsActive)" +
            " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        String hashedPassword =
                org.mindrot.jbcrypt.BCrypt.hashpw(
                        staff.getPassword(),
                        org.mindrot.jbcrypt.BCrypt.gensalt());

        ps.setString(1, staff.getStaffCode());
        ps.setString(2, staff.getFullName());
        ps.setDate(3, staff.getDateOfBirth());
        ps.setBoolean(4, staff.isGender());
        ps.setString(5, staff.getPhoneNumber());
        ps.setString(6, staff.getEmail());
        ps.setString(7, hashedPassword);
        ps.setString(8, staff.getDepartment());
        ps.setString(9, staff.getPosition());
        ps.setBigDecimal(10, staff.getSalary());
        ps.setDate(11, staff.getHireDate());
        ps.setInt(12, staff.getRole().getRoleID());
        ps.setBoolean(13, staff.isIsActive());

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /**
     * Updates an existing staff member's information in the database. The
     * password is not updated via this method.
     *
     * @param staff The Staff object containing the updated data. The StaffID
     * must be set.
     */
    public void updateStaff(Staff staff) {

    String sql =
            "UPDATE Staff SET " +
            "StaffCode=?, " +
            "FullName=?, " +
            "DateOfBirth=?, " +
            "Gender=?, " +
            "PhoneNumber=?, " +
            "Email=?, " +
            "Department=?, " +
            "Position=?, " +
            "Salary=?, " +
            "HireDate=?, " +
            "Role_ID=?, " +
            "IsActive=? " +
            "WHERE StaffID=?";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, staff.getStaffCode());
        ps.setString(2, staff.getFullName());
        ps.setDate(3, staff.getDateOfBirth());
        ps.setBoolean(4, staff.isGender());
        ps.setString(5, staff.getPhoneNumber());
        ps.setString(6, staff.getEmail());
        ps.setString(7, staff.getDepartment());
        ps.setString(8, staff.getPosition());
        ps.setBigDecimal(9, staff.getSalary());
        ps.setDate(10, staff.getHireDate());
        ps.setInt(11, staff.getRole().getRoleID());
        ps.setBoolean(12, staff.isIsActive());
        ps.setInt(13, staff.getStaffID());

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /**
     * Deletes a staff member from the database by their ID.
     *
     * @param staffId The ID of the staff member to delete.
     */
    public void deleteStaff(int staffId) {
        String sql = "DELETE FROM Staff WHERE StaffID = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a single staff member by their unique ID.
     *
     * @param staffId The ID of the staff member to retrieve.
     * @return A populated Staff object if found, null otherwise.
     */
    public Staff getStaffById(int staffId) {
        String sql = "SELECT s.*, r.Role_Name FROM Staff s JOIN Role r ON s.Role_ID = r.Role_ID WHERE s.StaffID = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractStaffFromResultSet(rs);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
