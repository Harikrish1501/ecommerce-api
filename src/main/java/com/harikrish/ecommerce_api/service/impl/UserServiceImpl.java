package com.harikrish.ecommerce_api.service.impl;

import com.harikrish.ecommerce_api.exception.ResourceNotFound;
import com.harikrish.ecommerce_api.exception.UnauthorizedException;
import com.harikrish.ecommerce_api.model.User;
import com.harikrish.ecommerce_api.model.UserPrinciple;
import com.harikrish.ecommerce_api.model.dto.response.UserResponse;
import com.harikrish.ecommerce_api.repository.UserRepo;
import com.harikrish.ecommerce_api.service.inf.IUserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor //  This is used to (final) object injection to constructor call automatically
// This is only allowed for final objects, if we want to use non-final objects then we can use @Autowired
public class UserServiceImpl implements IUserService {

//    @Autowired
    private final UserRepo userRepo;

    @Override
    public List<UserResponse> getUsers() {

//        List<UserResponse> userResponses = new ArrayList<>();
//        for(User u: userRepo.findAll()){
//            userResponses.add(mapToUserResponse(u));
//            }

        return userRepo.findAll().stream()
                .map(this:: mapToUserResponse)
                .toList();

    }

    @Override
    public UserResponse getUserById(Long id) {
        return   mapToUserResponse(userRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFound("User ID is not found"+id))) ;
    }

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).orElseThrow(()->new UnauthorizedException("Unauthorized access"));
    }

    private UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email: "+email));
        return new UserPrinciple(user);
    }
}
