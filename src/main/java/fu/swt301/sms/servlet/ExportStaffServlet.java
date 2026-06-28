package fu.swt301.sms.servlet;

import fu.swt301.sms.dao.StaffDAO;
import fu.swt301.sms.entity.Staff;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/export-staff")
public class ExportStaffServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchName = request.getParameter("searchName");
        String searchStatus = request.getParameter("searchStatus");

        StaffDAO staffDAO = new StaffDAO();
        List<Staff> staffList = staffDAO.getStaffByFilter(searchName, searchStatus);

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"staff_list.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.write('\ufeff');
            writer.println("Staff ID,Full Name,Gender,Phone Number,Email,Role,Status");

            for (Staff staff : staffList) {
                String gender = staff.isGender() ? "Male" : "Female";
                String status = staff.isIsActive() ? "Active" : "Inactive";
                
                writer.println(String.format("%d,%s,%s,%s,%s,%s,%s",
                        staff.getStaffID(),
                        escapeCsvField(staff.getFullName()),
                        gender,
                        escapeCsvField(staff.getPhoneNumber()),
                        escapeCsvField(staff.getEmail()),
                        escapeCsvField(staff.getRole().getRoleName()),
                        status
                ));
            }
            writer.flush();
        }
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}