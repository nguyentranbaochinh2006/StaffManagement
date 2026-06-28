<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Staff Details</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h2 class="text-center mt-5">Staff Details</h2>
    <div class="card mt-4 mx-auto" style="max-width: 600px;">
        <div class="card-body">
            <table class="table table-striped">
                <tr>
                    <th>Staff ID</th>
                    <td>${staff.staffID}</td>
                </tr>
                <tr>
                    <th>Full Name</th>
                    <td>${staff.fullName}</td>
                </tr>
                <tr>
                    <th>Gender</th>
                    <td>${staff.gender ? 'Male' : 'Female'}</td>
                </tr>
                <tr>
                    <th>Phone Number</th>
                    <td>${staff.phoneNumber}</td>
                </tr>
                <tr>
                    <th>Email Address</th>
                    <td>${staff.email}</td>
                </tr>
                <tr>
                    <th>Role</th>
                    <td>${staff.role.roleName}</td>
                </tr>
                <tr>
                    <th>Status</th>
                    <td>
                        <c:choose>
                            <c:when test="${staff.isActive}">
                                <span class="badge badge-success">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Inactive</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
            <div class="text-center mt-4">
                <a href="staff-crud?action=edit&id=${staff.staffID}" class="btn btn-warning">Edit Info</a>
                <a href="staff-list" class="btn btn-secondary">Back to List</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>