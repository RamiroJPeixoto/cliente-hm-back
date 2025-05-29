package com.clientehm.controller;

import com.clientehm.entity.Administrador; // Import da entidade no novo pacote
import com.clientehm.model.AdministradorLoginDTO;
import com.clientehm.model.AdministradorRegistroDTO;
import com.clientehm.model.RedefinirSenhaDTO;
import com.clientehm.model.VerificarPalavraChaveDTO;
import com.clientehm.repository.AdministradorRepository;
import com.clientehm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*") // Considere restringir para domínios específicos em produção
public class AdministradorController {

    @Autowired
    private AdministradorRepository administradorRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public AdministradorController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private ResponseEntity<Map<String, Object>> criarResposta(int status, String mensagem) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("mensagem", mensagem);
        corpo.put("codigo", status);
        return ResponseEntity.status(status).body(corpo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdministradorLoginDTO loginDTO) {
        Optional<Administrador> administradorOpt = administradorRepository.findByEmail(loginDTO.getEmail());
        if (administradorOpt.isPresent()) {
            Administrador admin = administradorOpt.get();
            // IMPORTANTE: Substitua esta comparação de senha em texto plano
            // por uma verificação usando BCryptPasswordEncoder ou similar.
            if (admin.getSenha().equals(loginDTO.getSenha())) {
                String token = jwtUtil.gerarToken(admin.getEmail());
                Map<String, Object> resposta = new HashMap<>();
                resposta.put("mensagem", "Login realizado com sucesso");
                resposta.put("token", token);
                resposta.put("nome", admin.getNome());
                resposta.put("email", admin.getEmail());
                resposta.put("codigo", 200);
                return ResponseEntity.ok(resposta);
            }
        }
        return criarResposta(401, "Credenciais inválidas");
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody AdministradorRegistroDTO registroDTO) {
        if (registroDTO.getNome() == null || registroDTO.getNome().length() < 3) {
            return criarResposta(400, "Nome muito curto (mínimo 3 caracteres)");
        }
        if (registroDTO.getEmail() == null || !registroDTO.getEmail().matches("^[\\w.-]+@hm\\.com$")) {
            return criarResposta(400, "Email deve ser do domínio @hm.com");
        }
        if (registroDTO.getSenha() == null || !isSenhaForte(registroDTO.getSenha())) {
            return criarResposta(400, "Senha fraca! Use letras maiúsculas, minúsculas, número e símbolo.");
        }
        if (registroDTO.getPalavraChave() == null || registroDTO.getPalavraChave().length() < 4) {
            return criarResposta(400, "Palavra-chave muito curta (mínimo 4 caracteres)");
        }
        if (administradorRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            return criarResposta(400, "Email já cadastrado");
        }

        // IMPORTANTE: Codifique a senha ANTES de salvar usando BCryptPasswordEncoder ou similar.
        // Exemplo: String senhaCodificada = passwordEncoder.encode(registroDTO.getSenha());
        Administrador novoAdministrador = new Administrador(
                registroDTO.getNome(),
                registroDTO.getEmail(),
                registroDTO.getSenha(), // Substitua pela senha codificada
                registroDTO.getPalavraChave()
        );
        administradorRepository.save(novoAdministrador);
        return criarResposta(201, "Administrador registrado com sucesso");
    }

    @PostMapping("/verificar-palavra-chave")
    public ResponseEntity<?> verificarPalavraChave(@RequestBody VerificarPalavraChaveDTO verificarDTO) {
        if (verificarDTO.getEmail() == null || verificarDTO.getPalavraChave() == null) {
            return criarResposta(400, "Email e palavra-chave são obrigatórios");
        }
        Optional<Administrador> administradorOpt = administradorRepository.findByEmail(verificarDTO.getEmail());
        if (administradorOpt.isPresent()) {
            Administrador admin = administradorOpt.get();
            if (verificarDTO.getPalavraChave().trim().equalsIgnoreCase(admin.getPalavraChave().trim())) {
                return criarResposta(200, "Palavra-chave correta");
            }
        }
        return criarResposta(401, "Email ou palavra-chave incorretos");
    }

    @PutMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody RedefinirSenhaDTO redefinirDTO) {
        if (redefinirDTO.getEmail() == null || redefinirDTO.getNovaSenha() == null) {
            return criarResposta(400, "Email e nova senha são obrigatórios");
        }
        Optional<Administrador> administradorOpt = administradorRepository.findByEmail(redefinirDTO.getEmail());
        if (administradorOpt.isPresent()) {
            if (!isSenhaForte(redefinirDTO.getNovaSenha())) {
                return criarResposta(400, "Senha fraca! Use letras maiúsculas, minúsculas, número e símbolo.");
            }
            Administrador admin = administradorOpt.get();
            // IMPORTANTE: Codifique a nova senha ANTES de salvar.
            // Exemplo: admin.setSenha(passwordEncoder.encode(redefinirDTO.getNovaSenha()));
            admin.setSenha(redefinirDTO.getNovaSenha()); // Substitua pela senha codificada
            administradorRepository.save(admin);
            return criarResposta(200, "Senha alterada com sucesso");
        }
        return criarResposta(404, "Administrador não encontrado");
    }

    private boolean isSenhaForte(String senha) {
        // Pelo menos 6 caracteres, 1 maiúscula, 1 minúscula, 1 número, 1 símbolo
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{6,}$";
        return senha.matches(regex);
    }
}