package edu.rutmiit.demo.grpcverification.service;

import edu.rutmiit.demo.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.regex.Pattern;

/**
 * Реализация gRPC-сервиса верификации пользователей.
 */
public class UserVerificationServiceImpl extends UserVerificationGrpc.UserVerificationImplBase {

    private static final Logger log = LoggerFactory.getLogger(UserVerificationServiceImpl.class);

    // Регулярные выражения для валидации
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{1,14}$"
    );

    // Список временных email-доменов
    private static final String[] DISPOSABLE_DOMAINS = {
            "tempmail.com", "throwaway.com", "mailinator.com",
            "guerrillamail.com", "10minutemail.com"
    };

    // Список подозрительных слов в имени
    private static final String[] BLOCKED_WORDS = {
            "admin", "moderator", "support", "system",
            "test", "guest", "anonymous"
    };

    @Override
    public void verifyUser(VerifyUserRequest request,
                           StreamObserver<VerifyUserResponse> responseObserver) {

        log.info("gRPC запрос: верификация пользователя id={}, username={}, email={}",
                request.getUserId(), request.getUsername(), request.getEmail());

        try {
            // Проверяем все аспекты
            EmailVerification emailCheck = verifyEmail(request.getEmail());
            PhoneVerification phoneCheck = verifyPhone(request.getPhone());
            IdentityVerification identityCheck = verifyIdentity(request.getUsername());
            GeoVerification geoCheck = verifyGeo(request.getIpAddress());

            // Собираем флаги
            java.util.List<String> flags = new java.util.ArrayList<>();

            if (!emailCheck.getValid()) {
                flags.add("INVALID_EMAIL");
            }
            if (emailCheck.getDisposable()) {
                flags.add("DISPOSABLE_EMAIL");
            }
            if (emailCheck.getSuspicious()) {
                flags.add("SUSPICIOUS_EMAIL");
            }
            if (phoneCheck.getSuspicious()) {
                flags.add("SUSPICIOUS_PHONE");
            }
            if (identityCheck.getUsernameSuspicious()) {
                flags.add("SUSPICIOUS_USERNAME");
            }
            if (geoCheck.getVpnDetected()) {
                flags.add("VPN_DETECTED");
            }
            if (geoCheck.getProxyDetected()) {
                flags.add("PROXY_DETECTED");
            }
            if (geoCheck.getTorDetected()) {
                flags.add("TOR_DETECTED");
            }

            // Определяем уровень риска
            RiskLevel riskLevel;
            if (flags.isEmpty()) {
                riskLevel = RiskLevel.LOW;
            } else if (flags.size() <= 2) {
                riskLevel = RiskLevel.MEDIUM;
            } else {
                riskLevel = RiskLevel.HIGH;
            }

            // Определяем уровень верификации
            VerificationLevel level;
            if (emailCheck.getValid() && !emailCheck.getDisposable()) {
                if (phoneCheck.getValid() && phoneCheck.getIsMobile()) {
                    level = VerificationLevel.FULL;
                } else {
                    level = VerificationLevel.BASIC;
                }
            } else {
                level = VerificationLevel.NONE;
            }

            // Финальный статус
            boolean verified = riskLevel != RiskLevel.HIGH && level != VerificationLevel.NONE;

            // Рекомендация
            String recommendedAction;
            if (verified) {
                recommendedAction = "ALLOW";
            } else if (riskLevel == RiskLevel.HIGH) {
                recommendedAction = "BLOCK";
            } else {
                recommendedAction = "MANUAL_CHECK";
            }

            // Формируем ответ
            VerifyUserResponse response = VerifyUserResponse.newBuilder()
                    .setUserId(request.getUserId())
                    .setVerified(verified)
                    .setLevel(level)
                    .setRiskLevel(riskLevel)
                    .addAllFlags(flags)
                    .setMessage(buildMessage(verified, riskLevel, flags))
                    .setRecommendedAction(recommendedAction)
                    .setConfidenceScore(calculateConfidence(flags, level))
                    .setDetails(VerificationDetails.newBuilder()
                            .setEmail(emailCheck)
                            .setPhone(phoneCheck)
                            .setIdentity(identityCheck)
                            .setGeo(geoCheck)
                            .build())
                    .build();

            log.info("gRPC ответ: user_id={}, verified={}, risk={}, flags={}",
                    response.getUserId(), response.getVerified(),
                    response.getRiskLevel(), response.getFlagsList());

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Ошибка верификации пользователя: {}", e.getMessage(), e);
            responseObserver.onError(e);
        }
    }

    // ─── Методы проверки ──────────────────────────────────────────────────

    private EmailVerification verifyEmail(String email) {
        if (email == null || email.isBlank()) {
            return EmailVerification.newBuilder()
                    .setValid(false)
                    .setDomainExists(false)
                    .setDisposable(false)
                    .setSuspicious(true)
                    .setProvider("unknown")
                    .build();
        }

        boolean valid = EMAIL_PATTERN.matcher(email).matches();
        String domain = email.substring(email.indexOf('@') + 1);
        boolean domainExists = true; // В реальном проекте — проверка DNS
        boolean disposable = java.util.Arrays.asList(DISPOSABLE_DOMAINS).contains(domain);
        boolean suspicious = disposable || domain.isBlank();

        String provider = domain.split("\\.")[0];

        return EmailVerification.newBuilder()
                .setValid(valid)
                .setDomainExists(domainExists)
                .setDisposable(disposable)
                .setSuspicious(suspicious)
                .setProvider(provider)
                .build();
    }

    private PhoneVerification verifyPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return PhoneVerification.newBuilder()
                    .setValid(false)
                    .setCountryCode("")
                    .setCarrier("unknown")
                    .setIsMobile(false)
                    .setSuspicious(true)
                    .build();
        }

        boolean valid = PHONE_PATTERN.matcher(phone).matches();
        String countryCode = phone.startsWith("+") ? phone.substring(1, 3) : "unknown";
        boolean isMobile = valid && (phone.startsWith("+7") || phone.startsWith("+1")); // Упрощенно
        boolean suspicious = !valid || phone.length() < 7;

        return PhoneVerification.newBuilder()
                .setValid(valid)
                .setCountryCode(countryCode)
                .setCarrier("unknown")
                .setIsMobile(isMobile)
                .setSuspicious(suspicious)
                .build();
    }

    private IdentityVerification verifyIdentity(String username) {
        if (username == null || username.isBlank()) {
            return IdentityVerification.newBuilder()
                    .setUsernameAllowed(false)
                    .setUsernameSuspicious(true)
                    .addBlockedWords("empty_username")
                    .setEmailVerified(false)
                    .setPhoneVerified(false)
                    .build();
        }

        String lowerUsername = username.toLowerCase();
        boolean blocked = false;
        java.util.List<String> blockedWords = new java.util.ArrayList<>();

        for (String word : BLOCKED_WORDS) {
            if (lowerUsername.contains(word)) {
                blocked = true;
                blockedWords.add(word);
            }
        }

        boolean usernameAllowed = !blocked && username.length() >= 3;
        boolean usernameSuspicious = blocked || username.length() < 3;

        return IdentityVerification.newBuilder()
                .setUsernameAllowed(usernameAllowed)
                .setUsernameSuspicious(usernameSuspicious)
                .addAllBlockedWords(blockedWords)
                .setEmailVerified(false)
                .setPhoneVerified(false)
                .build();
    }

    private GeoVerification verifyGeo(String ipAddress) {
        // В реальном проекте — вызов GeoIP сервиса
        // Для демо используем заглушку
        boolean vpnDetected = ipAddress != null && ipAddress.startsWith("192.168.");
        boolean proxyDetected = false;
        boolean torDetected = false;

        return GeoVerification.newBuilder()
                .setCountry("RU")
                .setCity("Moscow")
                .setLatitude(55.7558)
                .setLongitude(37.6173)
                .setVpnDetected(vpnDetected)
                .setProxyDetected(proxyDetected)
                .setTorDetected(torDetected)
                .build();
    }

    private String buildMessage(boolean verified, RiskLevel risk, java.util.List<String> flags) {
        if (verified) {
            return "Пользователь успешно верифицирован";
        }
        if (risk == RiskLevel.HIGH) {
            return "Пользователь заблокирован из-за высокого уровня риска. Причины: " + String.join(", ", flags);
        }
        return "Требуется ручная проверка. Причины: " + String.join(", ", flags);
    }

    private int calculateConfidence(java.util.List<String> flags, VerificationLevel level) {
        int base = 100;
        base -= flags.size() * 10;
        if (level == VerificationLevel.NONE) base -= 20;
        if (level == VerificationLevel.BASIC) base += 10;
        if (level == VerificationLevel.FULL) base += 20;
        return Math.max(0, Math.min(100, base));
    }
}