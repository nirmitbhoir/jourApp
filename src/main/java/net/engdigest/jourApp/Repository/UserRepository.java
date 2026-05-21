package net.engdigest.jourApp.Repository;

import net.engdigest.jourApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
User findByUserName(String username);   // It automatically generates query to search by username
                                        // db.user.find({ "userName": username })
    // in findby UserName caseshould be same as user entity content which is also UserName
    void deleteByUserName(String username);
}
