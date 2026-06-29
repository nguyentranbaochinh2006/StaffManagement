/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fu.swt301.sms.servlet;

/**
 *
 * @author Lenovo
 */


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    @Test
    public void testDoGet() throws Exception {

        // Arrange
        LoginServlet servlet = new LoginServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("login.jsp"))
                .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).getRequestDispatcher("login.jsp");
        verify(dispatcher).forward(request, response);
    }
    
    
    @Test
public void testDoPost_EmptyEmail() throws Exception {

    LoginServlet servlet = new LoginServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    when(request.getParameter("email")).thenReturn("");
    when(request.getParameter("password")).thenReturn("123456");

    when(request.getRequestDispatcher("login.jsp"))
            .thenReturn(dispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(
            "error",
            "Email and password are required.");

    verify(dispatcher).forward(request, response);
}


@Test
public void testDoPost_EmptyPassword() throws Exception {

    LoginServlet servlet = new LoginServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    when(request.getParameter("email"))
            .thenReturn("admin@gmail.com");

    when(request.getParameter("password"))
            .thenReturn("");

    when(request.getRequestDispatcher("login.jsp"))
            .thenReturn(dispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(
            "error",
            "Email and password are required.");

    verify(dispatcher).forward(request, response);
}



}
