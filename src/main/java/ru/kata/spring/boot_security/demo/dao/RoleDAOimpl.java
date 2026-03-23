package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Repository
public class RoleDAOimpl implements RoleDAO {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Set<Role> allRoles() {
        TypedQuery<Role> query = entityManager.createQuery("select r from Role r",Role.class);
        List<Role> roleList = query.getResultList();
        return new HashSet<>(roleList);
    }
}
