package fu.swt301.sms.config;

import fu.swt301.sms.utils.DBUtils;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This listener class is automatically instantiated and invoked by the web
 * container when the application starts up. Its primary purpose is to
 * initialize the database by: 1. Creating the necessary tables ('Role',
 * 'Staff') if they do not already exist. 2. Seeding the tables with default
 * data (e.g., user roles and a default admin account) if they are empty. This
 * makes the application self-contained and easier to deploy.
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    /**
     * This method is called by the container when the web application is first
     * started. It orchestrates the database initialization process.
     *
     * @param sce The event object containing the ServletContext.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection conn = DBUtils.getConnection()) {
            // Step 1: Ensure database tables are created before proceeding.
            System.out.println("Checking database schema...");
            createRoleTableIfNotExists(conn);
            createStaffTableIfNotExists(conn);

            // Step 2: Check if the 'Role' table is empty. If it is, we assume the database is new and needs seeding.
            boolean dataExists = false;
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Role"); ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    dataExists = true;
                }
            }

            // Step 3: If no data exists, insert the default roles and a default admin user.
            if (!dataExists) {
                System.out.println("No data found. Initializing default data...");
                insertDefaultData(conn);
            } else {
                System.out.println("Data already exists. Skipping initialization.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            // If any database error occurs during initialization, log it and throw a RuntimeException
            // to halt the application's startup, as it cannot function without a proper database setup.
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database.", e);
        }
    }

    /**
     * Checks if the 'Role' table exists in the database. If not, it creates the
     * table.
     *
     * @param conn The active database connection.
     * @throws SQLException if a database access error occurs.
     */
    private void createRoleTableIfNotExists(Connection conn) throws SQLException {
        String tableName = "Role";
        String checkTableSQL = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
        boolean tableExists = false;

        try (PreparedStatement ps = conn.prepareStatement(checkTableSQL)) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    tableExists = true;
                }
            }
        }

        if (!tableExists) {
            System.out.println("Table 'Role' not found. Creating table...");
            String createSQL = "CREATE TABLE Role ("
                    + "Role_ID INT PRIMARY KEY, "
                    + "Role_Name NVARCHAR(50) NOT NULL UNIQUE"
                    + ")";
            try (PreparedStatement ps = conn.prepareStatement(createSQL)) {
                ps.execute();
                System.out.println("Table 'Role' created.");
            }
        }
    }

    /**
     * Checks if the 'Staff' table exists in the database. If not, it creates
     * the table with a foreign key constraint pointing to the 'Role' table.
     *
     * @param conn The active database connection.
     * @throws SQLException if a database access error occurs.
     */
    private void createStaffTableIfNotExists(Connection conn) throws SQLException {
        String tableName = "Staff";
        String checkTableSQL = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
        boolean tableExists = false;

        try (PreparedStatement ps = conn.prepareStatement(checkTableSQL)) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    tableExists = true;
                }
            }
        }

        if (!tableExists) {
            System.out.println("Table 'Staff' not found. Creating table...");
            String createSQL = "CREATE TABLE Staff (\n"
                    + "    StaffID INT PRIMARY KEY IDENTITY(1,1),\n"
                    + "\n"
                    + "    StaffCode VARCHAR(20) NOT NULL UNIQUE,\n"
                    + "\n"
                    + "    FullName NVARCHAR(100) NOT NULL,\n"
                    + "\n"
                    + "    DateOfBirth DATE NOT NULL,\n"
                    + "\n"
                    + "    Gender BIT NOT NULL,\n"
                    + "\n"
                    + "    PhoneNumber VARCHAR(20) NOT NULL UNIQUE,\n"
                    + "\n"
                    + "    Email VARCHAR(100) NOT NULL UNIQUE,\n"
                    + "\n"
                    + "    Password VARCHAR(255) NOT NULL,\n"
                    + "\n"
                    + "    Department NVARCHAR(100) NOT NULL,\n"
                    + "\n"
                    + "    Position NVARCHAR(100) NOT NULL,\n"
                    + "\n"
                    + "    Salary DECIMAL(18,2) NOT NULL,\n"
                    + "\n"
                    + "    HireDate DATE NOT NULL,\n"
                    + "\n"
                    + "    Role_ID INT NOT NULL,\n"
                    + "\n"
                    + "    IsActive BIT NOT NULL,\n"
                    + "\n"
                    + "    FailedAttempts INT NOT NULL DEFAULT 0,\n"
                    + "\n"
                    + "    LockTime DATETIME NULL,\n"
                    + "\n"
                    + "    CONSTRAINT FK_Staff_Role\n"
                    + "        FOREIGN KEY(Role_ID)\n"
                    + "        REFERENCES Role(Role_ID)\n"
                    + ")";
            try (PreparedStatement ps = conn.prepareStatement(createSQL)) {
                ps.execute();
                System.out.println("Table 'Staff' created.");
            }
        } else {
            // === CHỨC NĂNG FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
            boolean hasFailedAttempts = false;
            String checkColSQL = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Staff' AND COLUMN_NAME = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkColSQL)) {
                ps.setString(1, "FailedAttempts");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        hasFailedAttempts = true;
                    }
                }
            }
            if (!hasFailedAttempts) {
                System.out.println("Altering table 'Staff' to add lock columns...");
                String alterSQL = "ALTER TABLE Staff ADD FailedAttempts INT NOT NULL DEFAULT 0, LockTime DATETIME NULL";
                try (PreparedStatement ps = conn.prepareStatement(alterSQL)) {
                    ps.execute();
                    System.out.println("Table 'Staff' altered successfully.");
                }
            }
        }
    }

    /**
     * Inserts a predefined set of data into the 'Role' and 'Staff' tables. This
     * includes 'Admin' and 'Staff' roles, and a default administrator account.
     *
     * @param conn The active database connection.
     * @throws SQLException if a database access error occurs.
     */
    private void insertDefaultData(Connection conn) throws SQLException {
        // === CHỨC NĂNG liên quan FR-06: DO TÔI Chinh CODE bổ sung ===

// Insert default roles using a batch operation for efficiency.
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Role (Role_ID, Role_Name) VALUES (?, ?)")) {
            ps.setInt(1, 1);
            ps.setString(2, "Admin");
            ps.addBatch();

            ps.setInt(1, 2);
            ps.setString(2, "Staff");
            ps.addBatch();

            ps.executeBatch();
            System.out.println("Default roles inserted.");
        }

        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(
                "admin123",
                org.mindrot.jbcrypt.BCrypt.gensalt());

        String sql = """
INSERT INTO Staff
(StaffCode, FullName, DateOfBirth, Gender, PhoneNumber,
 Email, Password, Department, Position,
 Salary, HireDate, Role_ID, IsActive)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
""";

// Insert a default administrator user for initial login.
        // === CHỨC NĂNG FR-01: Đăng nhập bằng BCrypt - DO TÔI TÊN CƯỜNG CODE ===

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "ADMIN001");
            ps.setString(2, "Admin User");
            ps.setDate(3, java.sql.Date.valueOf("1990-01-01"));
            ps.setBoolean(4, true);
            ps.setString(5, "0123456789");
            ps.setString(6, "admin@example.com");
            ps.setString(7, hashedPassword);
            ps.setString(8, "Administration");
            ps.setString(9, "Administrator");
            ps.setBigDecimal(10, new java.math.BigDecimal("20000000"));
            ps.setDate(11, java.sql.Date.valueOf("2024-01-01"));
            ps.setInt(12, 1);
            ps.setBoolean(13, true);

            ps.executeUpdate();
        }

    }

    /**
     * This method is called by the container when the web application is about
     * to be shut down. No cleanup action is needed in this case.
     *
     * @param sce The event object containing the ServletContext.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No action needed on shutdown.
    }
}
