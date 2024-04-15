package alan.prod.projectworkshopjavafx.model.dao;


import alan.prod.projectworkshopjavafx.db.DB;
import alan.prod.projectworkshopjavafx.model.dao.impl.DepartmentDaoJDBC;
import alan.prod.projectworkshopjavafx.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }
    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
