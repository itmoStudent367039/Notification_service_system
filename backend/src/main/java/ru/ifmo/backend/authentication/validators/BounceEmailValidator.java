package ru.ifmo.backend.authentication.validators;

import com.zerobounce.ZBValidateStatus;
import com.zerobounce.ZeroBounceSDK;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ifmo.backend.authentication.exceptions.BounceMessageException;
import ru.ifmo.backend.authentication.exceptions.InvalidEmailException;

import java.util.Objects;

@Component
public class BounceEmailValidator {
  @Value("${zero-bounce.key}")
  private String key;

  @Value("${zero-bounce.ip}")
  // TODO: need VPN
  private String ip;

  private ZeroBounceSDK zeroBounceSDK;

  public BounceEmailValidator() {
    ZeroBounceSDK.getInstance().initialize(key);
  }

  @PostConstruct
  public void init() {
    this.zeroBounceSDK = ZeroBounceSDK.getInstance();
    this.zeroBounceSDK.initialize(key);
  }

  public void validateEmail(String email) throws BounceMessageException {
    this.zeroBounceSDK.validate(
        email,
        ip,
        (response -> {
          System.out.println(response.getStatus());
          if (Objects.requireNonNull(response.getStatus()) != ZBValidateStatus.VALID
              && response.getStatus() != ZBValidateStatus.CATCH_ALL) {
            throw new InvalidEmailException("Can't send letter to this email");
          }
        }),
        (errorResponse) -> {
          throw new BounceMessageException(errorResponse.getErrors());
        });
  }
}
