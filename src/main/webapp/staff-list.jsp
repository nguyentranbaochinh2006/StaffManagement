<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Staff List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h2 class="text-center mt-5">Staff Management</h2>
    
    <!-- === CHỨC NĂNG FR-04: Đăng xuất - DO TÔI TÊN CƯỜNG CODE === -->
    <div class="row mb-3">
        <div class="col-md-12 text-right">
            Xin chào, <strong>${sessionScope.user.fullName}</strong> (${sessionScope.user.role.roleName}) | 
            <a href="logout" class="btn btn-sm btn-outline-danger">Đăng xuất</a>
        </div>
    </div>

    <!-- === CHỨC NĂNG FR-05: Phân quyền ADMIN/USER bằng Filter - DO TÔI TÊN CƯỜNG CODE === -->
    <c:if test="${param.accessDenied == 'true'}">
        <div class="alert alert-danger text-center">
            Bạn không có quyền thực hiện hành động này! Chỉ có Admin mới được thực hiện.
        </div>
    </c:if>

    <div class="row mb-3">
        <div class="col-md-6">
            <form class="form-inline" action="staff-list" method="get">
                <div class="form-group mr-2">
                    <input type="text" class="form-control" name="searchName" placeholder="Search by name" value="${param.searchName}">
                </div>
                <div class="form-group mr-2">
                    <select class="form-control" name="searchStatus">
                        <option value="">All Statuses</option>
                        <option value="true" ${param.searchStatus == 'true' ? 'selected' : ''}>Active</option>
                        <option value="false" ${param.searchStatus == 'false' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Filter</button>
            </form>
        </div>
        <div class="col-md-6 text-right">
            <!-- === CHỨC NĂNG FR-05: Phân quyền ADMIN/USER bằng Filter - DO TÔI TÊN CƯỜNG CODE === -->
            <c:if test="${sessionScope.user.role.roleID == 1}">
                <a href="staff-crud?action=create" class="btn btn-success">Add New Staff</a>
            </c:if>
        </div>
    </div>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Gender</th>
            <th>Phone Number</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <!-- === CHỨC NĂNG FR-05: Phân quyền ADMIN/USER bằng Filter - DO TÔI TÊN CƯỜNG CODE === -->
            <c:if test="${sessionScope.user.role.roleID == 1}">
                <th>Actions</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="staff" items="${staffList}">
            <tr>
                <td>${staff.staffID}</td>
                <td>${staff.fullName}</td>
                <td>${staff.gender ? 'Male' : 'Female'}</td>
                <td>${staff.phoneNumber}</td>
                <td>${staff.email}</td>
                <td>${staff.role.roleName}</td>
                <td>${staff.isActive ? 'Active' : 'Inactive'}</td>
                <!-- === CHỨC NĂNG FR-05: Phân quyền ADMIN/USER bằng Filter - DO TÔI TÊN CƯỜNG CODE === -->
                <c:if test="${sessionScope.user.role.roleID == 1}">
                    <td>
                        <a href="staff-crud?action=edit&id=${staff.staffID}" class="btn btn-sm btn-warning">Edit</a>
                        <a href="staff-crud?action=delete&id=${staff.staffID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
