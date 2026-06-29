package fu.swt301.sms.servlet;

import fu.swt301.sms.dao.StaffDAO;
import fu.swt301.sms.entity.Staff;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validate input chinh sửa
if (email == null || email.trim().isEmpty()
        || password == null || password.trim().isEmpty()) {

    request.setAttribute("error", "Email and password are required.");
    request.getRequestDispatcher("login.jsp").forward(request, response);
    return;
}

email = email.trim();
        
        
        StaffDAO staffDAO = new StaffDAO();
        try {
            // === CHỨC NĂNG FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
            Staff staff = staffDAO.checkLogin(email, password);
            if (staff != null) {
                HttpSession session = request.getSession();
                request.changeSessionId(); //đổi session đề không bị lợi dụng để né bộ đếm 
                session.setAttribute("user", staff);
                response.sendRedirect("staff-list");
            } else {
                // === CHỨC NĂNG FR-02: Thông báo lỗi đăng nhập chung - DO TÔI TÊN CƯỜNG CODE ===
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (RuntimeException e) {
            // === CHỨC NĂNG FR-03: Khóa tài khoản tạm thời - DO TÔI TÊN CƯỜNG CODE ===
            if ("lock".equals(e.getMessage())) {
                request.setAttribute("error", "Tài khoản của bạn đã bị khóa tạm thời 5 phút do đăng nhập sai quá 5 lần.");
            } else {
                request.setAttribute("error", "Invalid email or password");
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}