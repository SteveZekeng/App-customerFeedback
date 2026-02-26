package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.RegisterUserDto;
import com.ccaBank.feedback.entities.*;
import com.ccaBank.feedback.repositories.AgenceRepository;
import com.ccaBank.feedback.repositories.ClientRepository;
import com.ccaBank.feedback.repositories.StaffRepository;
import com.ccaBank.feedback.repositories.UserRepository;
import com.ccaBank.feedback.security.JWTResponse;
import com.ccaBank.feedback.util.JWTUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;
    private final AgenceRepository agenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;

    public AuthService(UserRepository userRepository, ClientRepository clientRepository,
                       StaffRepository staffRepository, AgenceRepository agenceRepository,
                       PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService,
                       JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.staffRepository = staffRepository;
        this.agenceRepository = agenceRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    public JWTResponse register(RegisterUserDto dto) {

        // Création de l’utilisateur
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Déterminons le rôle
        switch (dto.getTypeUser().toUpperCase()) {
            case "STAFF":
                user.setRole(Role.STAFF);
                break;
            case "ADMIN":
                user.setRole(Role.ADMIN);
                break;
            default:
                user.setRole(Role.CLIENT);
        }

        userRepository.save(user);

        // Création des entités spécifiques
        if (user.getRole() == Role.CLIENT) {
            Client client = new Client();
            client.setFirstName(dto.getFirstName());
            client.setPhone(dto.getPhone());
            client.setVille(dto.getVille());
            client.setNumeroCompte(dto.getNumeroCompte());
            client.setEmail(dto.getEmail());

            client.setUser(user);
            user.setClient(client);

        } else if (user.getRole() == Role.STAFF || user.getRole() == Role.ADMIN) { // staff ou admin
            Staff staff = new Staff();
            staff.setStaffName(dto.getStaffName());
            staff.setStaffPhone(dto.getStaffPhone());
            staff.setStaffEmail(dto.getStaffEmail());
            staff.setMatricule(dto.getMatricule());


            if (dto.getAgenceId() != null &&
                    dto.getAgenceId().getAgenceLocation() != null) {

                Agence agence = agenceRepository
                        .findByAgenceLocation(dto.getAgenceId().getAgenceLocation())
                        .orElseThrow(() -> new RuntimeException("Agence introuvable"));

                staff.setAgence(agence);
            }

            user.setStaff(staff);
            staff.setUser(user);

        }

        userRepository.save(user);

        // Génération JWT et refresh token
        String jwt = jwtUtil.generateToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new JWTResponse(jwt, "Bearer", refreshToken.getToken(), user.getId(), user.getUsername(), user.getRole());
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getUsername());
    }

    public RefreshToken createRefreshToken(User user) {
        return refreshTokenService.createRefreshToken(user);
    }
}
