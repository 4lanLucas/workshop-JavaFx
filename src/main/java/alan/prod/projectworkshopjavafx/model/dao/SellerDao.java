package alan.prod.projectworkshopjavafx.model.dao;

import alan.prod.projectworkshopjavafx.model.entities.Department;
import alan.prod.projectworkshopjavafx.model.entities.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller obj);
    void update(Seller obj);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}
