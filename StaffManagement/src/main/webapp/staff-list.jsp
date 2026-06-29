<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
    <head>
        <title>Staff Management</title>

        <link rel="stylesheet"
              href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    </head>

    <body>

        <div class="container">

            <h2 class="text-center mt-4 mb-4">Staff Management</h2>

            <!-- Login User -->
            <div class="row mb-3">
                <div class="col-md-12 text-right">

                    Xin chào,
                    <strong>${sessionScope.user.fullName}</strong>
                    (${sessionScope.user.role.roleName})

                    <a href="logout"
                       class="btn btn-sm btn-outline-danger ml-2">
                        Logout
                    </a>

                </div>
            </div>

            <!-- Access Denied -->
            <c:if test="${param.accessDenied=='true'}">
                <div class="alert alert-danger">
                    You do not have permission.
                </div>
            </c:if>



            <!-- ================= SEARCH ================= -->

            <form action="staff-list" method="get" class="mb-4">

                <div class="form-row">

                    <div class="col">
                        <input
                            type="text"
                            class="form-control"
                            name="searchName"
                            placeholder="Full Name"
                            value="${param.searchName}">
                    </div>

                    <div class="col">
                        <input
                            type="text"
                            class="form-control"
                            name="searchStaffCode"
                            placeholder="Staff Code"
                            value="${param.searchStaffCode}">
                    </div>

                    <div class="col">
                        <input
                            type="text"
                            class="form-control"
                            name="searchDepartment"
                            placeholder="Department"
                            value="${param.searchDepartment}">
                    </div>

                    <div class="col-auto">

                        <button class="btn btn-primary">
                            Search
                        </button>

                    </div>

                </div>

            </form>



            <!-- ================= ADD STAFF ================= -->

            <div class="mb-3 text-right">

                <a href="export-staff
                   ?searchName=${param.searchName}
                   &searchStatus=
                   &searchStaffCode=${param.searchStaffCode}
                   &searchDepartment=${param.searchDepartment}"
                   class="btn btn-info">

                    Export CSV

                </a>

                <c:if test="${sessionScope.user.role.roleID==1}">

                    <a href="staff-crud?action=create"
                       class="btn btn-success">
                        Add New Staff
                    </a>

                </c:if>

            </div>




            <!-- ================= TABLE ================= -->

            <table class="table table-bordered table-hover">

                <thead class="thead-dark">

                    <tr>

                        <th>ID</th>

                        <th>Staff Code</th>

                        <th>Full Name</th>

                        <th>Date Of Birth</th>

                        <th>Gender</th>

                        <th>Phone</th>

                        <th>Email</th>

                        <th>Department</th>

                        <th>Position</th>

                        <th>Salary</th>

                        <th>Hire Date</th>

                        <th>Role</th>

                        <th>Status</th>

                        <c:if test="${sessionScope.user.role.roleID==1}">
                            <th>Action</th>
                            </c:if>

                    </tr>

                </thead>



                <tbody>

                    <c:forEach var="staff" items="${staffList}">

                        <tr>

                            <td>${staff.staffID}</td>

                            <td>${staff.staffCode}</td>

                            <td>${staff.fullName}</td>

                            <td>${staff.dateOfBirth}</td>

                            <td>

                                <c:choose>

                                    <c:when test="${staff.gender}">
                                        Male
                                    </c:when>

                                    <c:otherwise>
                                        Female
                                    </c:otherwise>

                                </c:choose>

                            </td>

                            <td>${staff.phoneNumber}</td>

                            <td>${staff.email}</td>

                            <td>${staff.department}</td>

                            <td>${staff.position}</td>

                            <td>${staff.salary}</td>

                            <td>${staff.hireDate}</td>

                            <td>${staff.role.roleName}</td>

                            <td>

                                <c:choose>

                                    <c:when test="${staff.isActive}">
                                        Active
                                    </c:when>

                                    <c:otherwise>
                                        Inactive
                                    </c:otherwise>

                                </c:choose>

                            </td>



                            <c:if test="${sessionScope.user.role.roleID==1}">

                                <td>
                                    <a href="staff-crud?action=detail&id=${staff.staffID}"
                                       class="btn btn-info btn-sm">
                                        View
                                    </a>

                                    <a href="staff-crud?action=edit&id=${staff.staffID}"
                                       class="btn btn-warning btn-sm">

                                        Edit

                                    </a>

                                    <a href="staff-crud?action=delete&id=${staff.staffID}"
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('Delete this staff?')">

                                        Delete

                                    </a>

                                </td>

                            </c:if>

                        </tr>

                    </c:forEach>

                </tbody>

            </table>

        </div>

    </body>

</html>