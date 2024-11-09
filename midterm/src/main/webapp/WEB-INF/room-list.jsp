<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Room List</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
            padding: 8px;
        }
        th {
            background-color: #f2f2f2;
            text-align: left;
        }
        a {
            text-decoration: none;
            color: blue;
        }
        .actions {
            display: flex;
            gap: 10px;
        }
    </style>
</head>
<body>
<h2>Room List</h2>
<a href="room?action=new">Add New Room</a>
<table>
    <thead>
        <tr>
            <th>Room Code</th>
            <th>Room Name</th>
            <th>Capacity</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="room" items="${listRooms}">
            <tr>
                <td>${room.roomCode}</td>
                <td>${room.roomName}</td>
                <td>${room.capacity}</td>
                <td class="actions">
                    <a href="room?action=edit&roomId=${room.id}">Edit</a>
                    <a href="room?action=delete&roomId=${room.id}" onclick="return confirm('Are you sure?')">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>
