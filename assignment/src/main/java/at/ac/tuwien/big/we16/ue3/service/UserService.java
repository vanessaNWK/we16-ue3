package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;
import org.apache.log4j.*;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserService {

    public UserService() {
    }

    /**
     * Precondition: user != null, first time user with the given email is persisted
     * Postcondition: The methode persists the given user in the database.
     * @param user user to be saved
     */
    public void createUser(User user) {
         //TODO: write to db
        DBAccess.getManager().getTransaction().begin();
        DBAccess.getManager().persist(user);
        DBAccess.getManager().getTransaction().commit();
        System.out.println("Id:" + user.getId());
    }

    /**
     * Precondition: email != null
     * Postcondition: The methode retrieves the user with the given email address.
     * @param email email to be searched for
     * @return User with the given email address
     * @throws UserNotFoundException throws if there is no user with the given email
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        //TODO: read from db
        TypedQuery<User> query = DBAccess.getManager().createQuery("Select u FROM User u WHERE u.email = :email ", User.class);
        query.setParameter("email", email);
        List<User> user = query.getResultList();
        if(user.size() == 0) {
            throw new UserNotFoundException();
        }
        return user.get(0);
    }

    /**
     * Precondition: email != null
     * Postcondition: The methode reads the user with the given email from the database to check if a
     * user with this email address already exists.
     * @param email email address of the searched user
     * @return true user exists
     *         false user does not exist
     */
    public boolean doesUserExist(String email) {
        try {
            this.getUserByEmail(email);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
