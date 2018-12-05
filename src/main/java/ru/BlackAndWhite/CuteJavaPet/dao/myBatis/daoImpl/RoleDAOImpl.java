package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.daoImpl;


import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.RoleDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.RoleMapper;
import ru.BlackAndWhite.CuteJavaPet.model.Role;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public class RoleDAOImpl implements RoleDAO {//extends JpaRepository<Role, Long> {
    private static String DEFAULT_ROLE = "user";
//    @Autowired
//    JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlSession sqlSession;



    public Integer findRoleIdByName(String name) {
        List<Role> roles = sqlSession.getMapper(RoleMapper.class).selectRolesByName(name);
        sqlSession.commit();
        Role role = roles.get(0);
        return role.getId();
    }
    public List<Role> selectRolesByUserId(Integer id){
        List<Role> roles = sqlSession.getMapper(RoleMapper.class).selectRolesByUserId(id);
        sqlSession.commit();
        return roles;
    }

    public List<User> selectUsersByRoleId(Integer id){
        List<User> users = sqlSession.getMapper(RoleMapper.class).selectUsersByRoleId(id);
        sqlSession.commit();
        return users;
    };
    public void addDefaultUserRole(Integer idUser) {
        addUserRole(idUser, findRoleIdByName(DEFAULT_ROLE));
    }

    public void addUserRole(Integer idUser, Integer idRole) {
        sqlSession.getMapper(RoleMapper.class).addUserRole(idUser, idRole);
        sqlSession.commit();
    }
}
