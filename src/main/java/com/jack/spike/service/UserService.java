package com.jack.spike.service;

import com.jack.spike.dao.IUserDao;
import com.jack.spike.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Jack
 * @Date 2019/4/24 16:22
 */
@Service
public class UserService {

    @Autowired
    private IUserDao userDao;

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean testTransaction() {
        User user = new User();
        user.setId(2);
        user.setName("Rose");
        userDao.insert(user);
        User user1 = new User();
        user1.setId(1);
        user1.setName("Jack01");
        userDao.insert(user1);
        return true;
    }

}
