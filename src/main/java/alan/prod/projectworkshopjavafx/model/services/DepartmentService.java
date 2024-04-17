package alan.prod.projectworkshopjavafx.model.services;

import alan.prod.projectworkshopjavafx.model.dao.DaoFactory;
import alan.prod.projectworkshopjavafx.model.dao.DepartmentDao;
import alan.prod.projectworkshopjavafx.model.entities.Department;


import java.util.List;

public class DepartmentService {
    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj){
        if(obj.getId() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }
    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}
