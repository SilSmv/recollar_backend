package com.recollar.recollar_backend.repository;
import com.recollar.recollar_backend.dto.SimpleUserRequest;
import com.recollar.recollar_backend.dto.UserRequest;
import com.recollar.recollar_backend.dto.UserRequestInterface;
import com.recollar.recollar_backend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;

public interface UserRepository extends JpaRepository<UserModel,String> {
    @Query(value = "select u.* from user u inner join collector c on u.id_user = c.id_user where u.email = ?1 and u.status=1", nativeQuery = true)
    UserModel findByEmail(String email);

    @Query(value = "select u.email as email, u.password as password, p.first_name as firstName, p.last_name as lastName, p.phone_number as phoneNumber, c.image_path as imagePath from collector c inner join user u on u.id_user= c.id_user inner join person p on c.id_person = p.id_person where c.id_collector=?1", nativeQuery = true)
    UserRequestInterface getUserByIdCollector(Integer idCollector);

    @Modifying
    @Transactional
    @Query("update person p SET p.firstName = :#{#profile.firstName},p.lastName = :#{#profile.lastName},p.phoneNumber=:#{#profile.phoneNumber} WHERE p.idPerson = :#{#id_person}")
    public void updateProfile(@Param("profile") SimpleUserRequest profile,@Param("id_person") Integer idPerson);

    @Modifying
    @Transactional
    @Query("update user u SET u.password = :#{#password} WHERE u.idUser = :#{#id_user}")
    public void updatePassword(@Param("password") String password,@Param("id_user") Integer idPerson);


}
