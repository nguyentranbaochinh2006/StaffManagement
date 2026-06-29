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
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String searchName = request.getParameter("searchName");
        String searchStaffCode = request.getParameter("searchStaffCode");
        String searchDepartment = request.getParameter("searchDepartment");

        StaffDAO dao = new StaffDAO();

        List<Staff> list = dao.getStaffByFilter(
                searchName,
                searchStaffCode,
                searchDepartment);

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=staff.csv");

        PrintWriter out = response.getWriter();

        out.write('\ufeff');

        out.println("ID,Staff Code,Full Name,Department,Position,Phone,Email,Role,Status");

        for (Staff s : list) {

            out.println(
                    s.getStaffID() + ","
                    + csv(s.getStaffCode()) + ","
                    + csv(s.getFullName()) + ","
                    + csv(s.getDepartment()) + ","
                    + csv(s.getPosition()) + ","
                    + csv(s.getPhoneNumber()) + ","
                    + csv(s.getEmail()) + ","
                    + csv(s.getRole().getRoleName()) + ","
                    + (s.isIsActive() ? "Active" : "Inactive")
            );
        }

        out.flush();
    }

    private String csv(String text) {

        if (text == null)
            return "";

        if (text.contains(",")
                || text.contains("\"")) {

            return "\"" + text.replace("\"", "\"\"") + "\"";
    }

    return text;
}
}
