package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;

public class UserService {

    public UserService() {
    }

    public void createUser(User user) {
         //TODO: write to db
        DBAccess.getManager().getTransaction().begin();
        DBAccess.getManager().persist(user);
        DBAccess.getManager().getTransaction().commit();
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        //TODO: read from db
        User user =   DBAccess.getManager().find(User.class, email);
        if(user == null) {
            throw new UserNotFoundException();
        }
        System.out.println("get User by email");
        return user;
    }
}
