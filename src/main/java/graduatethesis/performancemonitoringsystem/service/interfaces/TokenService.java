package graduatethesis.performancemonitoringsystem.service.interfaces;

import graduatethesis.performancemonitoringsystem.model.users.Token;

public interface TokenService {
    void deleteToken(Long id);

    Token saveToken(Token confirmationToken);

    Token findByConfirmationToken(String confirmationToken);




}
