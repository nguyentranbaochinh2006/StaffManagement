package fu.swt301.sms.servlet;

import fu.swt301.sms.dao.RoleDAO;
import fu.swt301.sms.dao.StaffDAO;
import fu.swt301.sms.entity.Role;
import fu.swt301.sms.entity.Staff;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/staff-crud")
public class StaffCrudServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        StaffDAO staffDAO = new StaffDAO();
        RoleDAO roleDAO = new RoleDAO();

        String fullName = request.getParameter("fullName");
        boolean gender = Boolean.parseBoolean(request.getParameter("gender"));
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        int roleID = Integer.parseInt(request.getParameter("roleID"));
        boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

        Role role = new Role();
        role.setRoleID(roleID);

        Staff staff = new Staff();
        staff.setFullName(fullName);
        staff.setGender(gender);
        staff.setPhoneNumber(phoneNumber);
        staff.setEmail(email);
        staff.setRole(role);
        staff.setIsActive(isActive);

        if ("create".equals(action)) {
            if (staffDAO.isEmailExists(email, 0)) {
                List<Role> roleList = roleDAO.getAllRoles();
                request.setAttribute("roleList", roleList);
                request.setAttribute("staff", staff);
                request.setAttribute("errorMessage", "Email has already been taken.");
                request.getRequestDispatcher("staff-form.jsp").forward(request, response);
                return;
            }

            if (staffDAO.isPhoneNumberExists(phoneNumber, 0)) {
                List<Role> roleList = roleDAO.getAllRoles();
                request.setAttribute("roleList", roleList);
                request.setAttribute("staff", staff);
                request.setAttribute("errorMessage", "Phone number has already been taken.");
                request.getRequestDispatcher("staff-form.jsp").forward(request, response);
                return;
            }

            String password = request.getParameter("password");
            staff.setPassword(password);
            staffDAO.insertStaff(staff);
        } else if ("update".equals(action)) {
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            staff.setStaffID(staffID);

            if (staffDAO.isEmailExists(email, staffID)) {
                List<Role> roleList = roleDAO.getAllRoles();
                request.setAttribute("roleList", roleList);
                request.setAttribute("staff", staff);
                request.setAttribute("errorMessage", "Email has already been taken.");
                request.getRequestDispatcher("staff-form.jsp").forward(request, response);
                return;
            }

            if (staffDAO.isPhoneNumberExists(phoneNumber, staffID)) {
                List<Role> roleList = roleDAO.getAllRoles();
                request.setAttribute("roleList", roleList);
                request.setAttribute("staff", staff);
                request.setAttribute("errorMessage", "Phone number has already been taken.");
                request.getRequestDispatcher("staff-form.jsp").forward(request, response);
                return;
            }

            staffDAO.updateStaff(staff);
        }

        response.sendRedirect("staff-list");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        StaffDAO staffDAO = new StaffDAO();
        RoleDAO roleDAO = new RoleDAO();

        if ("delete".equals(action)) {
            int staffId = Integer.parseInt(request.getParameter("id"));
            staffDAO.deleteStaff(staffId);
            staffDAO.resetIdentity();
            response.sendRedirect("staff-list");
            return;
        }

        if ("detail".equals(action)) {
            int staffId = Integer.parseInt(request.getParameter("id"));
            Staff staff = staffDAO.getStaffById(staffId);
            if (staff != null) {
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("staff-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("staff-list");
            }
            return;
        }

        List<Role> roleList = roleDAO.getAllRoles();
        request.setAttribute("roleList", roleList);

        if ("edit".equals(action)) {
            int staffId = Integer.parseInt(request.getParameter("id"));
            Staff staff = staffDAO.getStaffById(staffId);
            request.setAttribute("staff", staff);
        }

        request.getRequestDispatcher("staff-form.jsp").forward(request, response);
    }
}