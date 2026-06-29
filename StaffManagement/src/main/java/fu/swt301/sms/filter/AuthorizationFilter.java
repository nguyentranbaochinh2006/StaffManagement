package fu.swt301.sms.filter;

import fu.swt301.sms.entity.Staff;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// === CHỨC NĂNG FR-05: Phân quyền ADMIN/USER bằng Filter - DO TÔI TÊN CƯỜNG CODE ===
@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());
        
        // Exclude static resources if any
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/") || path.endsWith(".css") || path.endsWith(".js") || path.equals("/favicon.ico")) {
            chain.doFilter(req, res);
            return;
        }

        boolean isLoginAction = path.equals("/login") || path.equals("/login.jsp") || path.equals("/");
        
        HttpSession session = request.getSession(false);
        Staff user = (session != null) ? (Staff) session.getAttribute("user") : null;
        
        if (user == null) {
            // User is guest
            if (isLoginAction) {
                chain.doFilter(req, res);
            } else {
                response.sendRedirect(contextPath + "/login");
            }
        } else {
            // User is logged in
            if (isLoginAction) {
                // If logged in, redirect away from login page to the staff list
                response.sendRedirect(contextPath + "/staff-list");
            } else if (path.equals("/staff-crud")) {
                // Check if user is Admin (Role ID = 1)
                if (user.getRole() != null && user.getRole().getRoleID() == 1) {
                    chain.doFilter(req, res);
                } else {
                    // Non-admin tries to perform write action -> redirect to staff-list with access denied
                    response.sendRedirect(contextPath + "/staff-list?accessDenied=true");
                }
            } else {
                chain.doFilter(req, res);
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}