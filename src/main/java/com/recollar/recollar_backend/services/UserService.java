package com.recollar.recollar_backend.services;
import com.recollar.recollar_backend.dto.UserRequest;
import com.recollar.recollar_backend.models.CollectorModel;
import com.recollar.recollar_backend.models.PersonModel;
import com.recollar.recollar_backend.models.Transaction;
import com.recollar.recollar_backend.models.UserModel;
import com.recollar.recollar_backend.repository.CollectorRepository;
import com.recollar.recollar_backend.repository.PersonRepository;
import com.recollar.recollar_backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional

public class UserService  implements UserDetailsService {


    @Resource
    private UserRepository userRepository;

    @Resource
    private PersonRepository personRepository;

    @Resource
    private CollectorRepository collectorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        UserModel user = userRepository.findByEmail(email);
        if(user != null){
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        } else {
            throw new UsernameNotFoundException("User '"+email+"' not found!");
        }
    }
    public void signUp(UserRequest userRequest, Transaction transaction) throws Exception {
        UserModel userModel=new UserModel();
        PersonModel personModel=new PersonModel();

        userModel.setEmail(userRequest.getEmail());
        String passEncode=new BCryptPasswordEncoder().encode(userRequest.getPassword());
        userModel.setPassword(passEncode);
        userModel.setStatus(1);
        userModel.setTransaction(transaction);

        personModel.setFirstName(userRequest.getFirstName());
        personModel.setLastName(userRequest.getLastName());
        personModel.setPhoneNumber(userRequest.getPhoneNumber());
        personModel.setStatus(1);
        personModel.setTransaction(transaction);

        userRepository.save(userModel);
        personRepository.save(personModel);

        CollectorModel collectorModel=new CollectorModel();
        collectorModel.setIdPerson(personModel.getIdPerson());
        collectorModel.setIdUser(userModel.getIdUser());
        collectorModel.setImagePath("");
        collectorModel.setStatus(1);
        collectorModel.setTransaction(transaction);

        collectorRepository.save(collectorModel);

    }

}