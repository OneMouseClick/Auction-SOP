package edu.rutmiit.demo.auctionrest.controllers;

import edu.rutmiit.demo.auctioncontract.dto.UserRegistrationRequest;
import edu.rutmiit.demo.auctioncontract.dto.UserResponse;
import edu.rutmiit.demo.auctioncontract.endpoints.UserApi;
import edu.rutmiit.demo.auctioncontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.auctionrest.assemblers.UserModelAssembler;
import edu.rutmiit.demo.auctionrest.event.UserEventPublisher;
import edu.rutmiit.demo.auctionrest.storage.InMemoryStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController implements UserApi {

    private final InMemoryStorage storage;
    private final UserModelAssembler userModelAssembler;
    private final UserEventPublisher userEventPublisher;

    public UserController(InMemoryStorage storage,
                          UserModelAssembler userModelAssembler,
                          UserEventPublisher userEventPublisher) {
        this.storage = storage;
        this.userModelAssembler = userModelAssembler;
        this.userEventPublisher = userEventPublisher;
    }

    @Override
    public ResponseEntity<EntityModel<UserResponse>> registerUser(
            UserRegistrationRequest request,
            HttpServletRequest httpRequest) {

        // Проверяем, не существует ли пользователь с таким username или email
        boolean exists = storage.users.values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(request.username()) ||
                        u.getEmail().equalsIgnoreCase(request.email()));

        if (exists) {
            throw new RuntimeException("Пользователь с таким именем или email уже существует");
        }

        // Создаем пользователя
        long id = storage.userSequence.incrementAndGet();
        UserResponse user = UserResponse.builder()
                .id(id)
                .username(request.username())
                .email(request.email())
                .rating(100.0) // начальный рейтинг
                .verified(false)
                .riskLevel("MEDIUM")
                .verificationLevel("NONE")
                .recommendedAction("MANUAL_CHECK")
                .confidenceScore(0)
                .verificationFlags(java.util.List.of())
                .build();

        storage.users.put(id, user);

        // Публикуем событие для верификации
        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        userEventPublisher.publishUserRegistered(user, ipAddress, userAgent);

        EntityModel<UserResponse> model = userModelAssembler.toModel(user);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<UserResponse> getUserById(Long id) {
        UserResponse user = Optional.ofNullable(storage.users.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userModelAssembler.toModel(user);
    }
}