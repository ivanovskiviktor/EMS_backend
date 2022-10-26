package graduatethesis.performancemonitoringsystem.service.implementation;

import graduatethesis.performancemonitoringsystem.model.users.Token;
import graduatethesis.performancemonitoringsystem.repository.users.TokenRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void deleteToken(Long id) {
        this.tokenRepository.deleteById(id);
    }

    @Override
    public Token saveToken(Token confirmationToken) {
        return this.tokenRepository.save(confirmationToken);
    }

    @Override
    public Token findByConfirmationToken(String token) {
        return this.tokenRepository.findByConfirmationToken(token);
    }




}
