package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.daoImpl;


import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.UserDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.UserMapper;
import ru.BlackAndWhite.CuteJavaPet.model.User;


@Repository
@Log4j
public class UserDAOImpl implements UserDAO {
    @Autowired
    private SqlSession sqlSession;


    public User findByUsername(String userName) {
        User findUser = sqlSession.getMapper(UserMapper.class).findByUsername(userName);
        sqlSession.commit();
        return findUser;
    }
    public User findByUserEmail(String email) {
        User findUser = sqlSession.getMapper(UserMapper.class).findByUserEmail(email);
        sqlSession.commit();
        return findUser;
    }

    public void insertUser(User user) {
        sqlSession.getMapper(UserMapper.class).insertUser(user);
        sqlSession.commit();
    }

    public int getIdUser(String userName) {
        return findByUsername(userName).getId();
    }


    public User selectUserByID(int id){
        User findUser = sqlSession.getMapper(UserMapper.class).selectUserByID(id);
        sqlSession.commit();
        return findUser;

    }
}
