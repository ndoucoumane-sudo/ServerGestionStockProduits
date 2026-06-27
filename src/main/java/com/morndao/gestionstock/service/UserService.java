package com.morndao.gestionstock.service;

import com.morndao.gestionstock.model.User;
import com.morndao.gestionstock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id : " + id));
    }

    @Transactional
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Nom d'utilisateur déjà pris : " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé : " + user.getEmail());
        }
        if (user.getRole() == null) {
            user.setRole(User.Role.VIEWER);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User updated) {
        User existing = findById(id);
        existing.setUsername(updated.getUsername());
        existing.setEmail(updated.getEmail());
        existing.setRole(updated.getRole());
        return userRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        userRepository.deleteById(id);
    }
}
