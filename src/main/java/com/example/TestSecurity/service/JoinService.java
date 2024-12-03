package com.example.TestSecurity.service;

import com.example.TestSecurity.dto.JoinDTO;
import com.example.TestSecurity.entity.UserEntity;
import com.example.TestSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {
        boolean isUser = userRepository.existsByUserName(joinDTO.getUserName());
        if(isUser) {
            return;
        }

        //db에 이미 동일한 userName을 가진 회원이 존재하는지 확인

        UserEntity data = new UserEntity();

        data.setUserName(joinDTO.getUserName());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);

    }
}
