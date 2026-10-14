package com.ezpay.dao;

import com.ezpay.entity.User;
import com.ezpay.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    @Override
    public Optional<User> findByEmailOrMobile(String emailOrMobile) {

        Optional<User> user =
                userRepository.findByEmail(emailOrMobile);

        if (user.isPresent()) {
            return user;
        }

        return userRepository.findByMobile(emailOrMobile);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByMobile(String mobile) {
        return userRepository.existsByMobile(mobile);
    }
}