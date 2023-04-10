package shop.mtcoding.securityapp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.dto.UserResponse;
import shop.mtcoding.securityapp.model.User;
import shop.mtcoding.securityapp.model.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 1. 트랜잭션 관리 서비스시작 할때부터 끝날때까지
     * 2. 영속성 객체 변경감지
     * 3. RequestDTO 요청받기
     * 4. 비지니스 로직 처리하기
     * 5. ResponseDTO 응답하기
     */

    @Transactional // 행단 관심사
    public UserResponse.JoinDto 회원가입(UserRequest.JoinDto joinDto) { // Transactional 시작과 엔티티가 붙은애들을 변경감지 한다
        String rawPassword = joinDto.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword); // 60Byte
        joinDto.setPassword(encPassword);
        User userPS = userRepository.save(joinDto.toEntity());
        return new UserResponse.JoinDto(userPS);
    }
}
