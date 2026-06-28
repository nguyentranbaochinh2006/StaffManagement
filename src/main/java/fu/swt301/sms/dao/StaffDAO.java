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

public class StaffDAO {

    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffID(rs.getInt("StaffID"));
        staff.setFullName(rs.getString("FullName"));
        staff.setGender(rs.getBoolean("Gender"));
        staff.setPhoneNumber(rs.getString("PhoneNumber"));
        staff.setEmail(rs.getString("Email"));
        staff.setPassword(rs.getString("Password"));
        
        Role role = new Role();
        role.setRoleID(rs.getInt("Role_ID"));
        role.setRoleName(rs.getString("Role_Name"));
        staff.setRole(role);
        
        staff.setIsActive(rs.getBoolean("IsActive"));
        return staff;
    }

    public List<Staff> getStaffByFilter(String searchName, String searchStatus) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT s.*, r.Role_Name FROM Staff s JOIN Role r ON s.Role_ID = r.Role_ID WHERE 1=1";
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql += " AND s.FullName LIKE ?";
        }
        if (searchStatus != null && !searchStatus.trim().isEmpty()) {
            sql += " AND s.IsActive = ?";
        }
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchName + "%");
            }
            if (searchStatus != null && !searchStatus.trim().isEmpty()) {
                ps.setInt(paramIndex++, Boolean.parseBoolean(searchStatus) ? 1 : 0);
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

    public Staff getStaffById(int staffId) {
        String sql = "SELECT s.*, r.Role_Name FROM Staff s JOIN Role r ON s.Role_ID = r.Role_ID WHERE s.StaffID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public void insertStaff(Staff staff) {
        String sql = "INSERT INTO Staff (FullName, Gender, PhoneNumber, Email, Password, Role_ID, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getFullName());
            ps.setInt(2, staff.isGender() ? 1 : 0);
            ps.setString(3, staff.getPhoneNumber());
            ps.setString(4, staff.getEmail());
            ps.setString(5, staff.getPassword());
            ps.setInt(6, staff.getRole().getRoleID());
            ps.setInt(7, staff.isIsActive() ? 1 : 0);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStaff(Staff staff) {
        String sql = "UPDATE Staff SET FullName = ?, Gender = ?, PhoneNumber = ?, Email = ?, Role_ID = ?, IsActive = ? WHERE StaffID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getFullName());
            ps.setInt(2, staff.isGender() ? 1 : 0);
            ps.setString(3, staff.getPhoneNumber());
            ps.setString(4, staff.getEmail());
            ps.setInt(5, staff.getRole().getRoleID());
            ps.setInt(6, staff.isIsActive() ? 1 : 0);
            ps.setInt(7, staff.getStaffID());
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void softDeleteStaff(int staffId) {
        String sql = "UPDATE Staff SET IsActive = 0 WHERE StaffID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStaff(int staffId) {
        String sql = "DELETE FROM Staff WHERE StaffID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetIdentity() {
        String sql = "DECLARE @maxID INT; " +
                     "SELECT @maxID = ISNULL(MAX(StaffID), 0) FROM Staff; " +
                     "DBCC CHECKIDENT ('Staff', RESEED, @maxID);";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Staff checkLogin(String email, String password) {
        String sql = "SELECT s.*, r.Role_Name FROM Staff s JOIN Role r ON s.Role_ID = r.Role_ID WHERE s.Email = ? AND s.Password = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
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

    public boolean isEmailExists(String email, int currentStaffId) {
        String sql = "SELECT 1 FROM Staff WHERE Email = ? AND StaffID != ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, currentStaffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneNumberExists(String phoneNumber, int currentStaffId) {
        String sql = "SELECT 1 FROM Staff WHERE PhoneNumber = ? AND StaffID != ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            ps.setInt(2, currentStaffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}