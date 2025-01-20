package Jump.Service;

import Jump.Config.DataNotFoundException;
import Jump.Entity.SiteUser;
import Jump.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password){
        SiteUser siteUser =new SiteUser();
        siteUser.setUsername(username);
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser=userRepository.findByUsername(username);
        if(siteUser.isPresent()){
            return siteUser.get();
        }else{
            throw new DataNotFoundException("siteUser not found");
        }

    }



}
