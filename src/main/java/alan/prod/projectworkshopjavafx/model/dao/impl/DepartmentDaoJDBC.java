package alan.prod.projectworkshopjavafx.model.dao.impl;



import alan.prod.projectworkshopjavafx.db.DB;
import alan.prod.projectworkshopjavafx.db.DbException;
import alan.prod.projectworkshopjavafx.db.DbIntegrityException;
import alan.prod.projectworkshopjavafx.model.dao.DepartmentDao;
import alan.prod.projectworkshopjavafx.model.entities.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private final Connection conn;
    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "INSERT INTO department (Name) VALUES (?) ",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            st.setString(1, obj.getName());

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                    System.out.println("Done! new Department added with id: " + id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No Rows Affected.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE department "
                    + "SET Name = ? "
                    + "WHERE Id = ? "
            );
            st.setString(1,obj.getName());
            st.setInt(2,obj.getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Update done on DepartmentId: " + obj.getId());
            } else {
                throw new DbException("Unexpected error! no rows affected.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "DELETE FROM department WHERE Id = ? "
            );
            st.setInt(1,id);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Done! department deleted with id: " + id);
            } else {
                throw new DbException("Error: id don't found");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
        DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                    "SELECT department.* FROM department "
                        + "WHERE department.id = ? "
            );
            st.setInt(1,id);
            rs = st.executeQuery();
            if(rs.next()){
                int departmentId = rs.getInt(1);
                String departmentName = rs.getString(2);
                return new Department(departmentId,departmentName);
            } else {
                throw new DbException("Error: id don't found.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();

        try{
            st = conn.prepareStatement(
                    "SELECT department.* FROM department "
            );
            rs = st.executeQuery();

            while (rs.next()){
                int departmentId = rs.getInt(1);
                String departmentName = rs.getString(2);

                Department department = new Department(departmentId,departmentName);
                list.add(department);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
