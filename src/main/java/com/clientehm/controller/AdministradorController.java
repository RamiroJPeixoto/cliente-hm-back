package com.clientehm.controller;

import com.clientehm.model.Administrador;
import com.clientehm.repository.AdministradorRepository;
import com.clientehm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

    @Autowired
    private AdministradorRepository administradorRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public AdministradorController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private ResponseEntity<Map<String, Object>> resposta(int status, String mensagem) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("mensagem", mensagem);
        corpo.put("codigo", status);
        return ResponseEntity.status(status).body(corpo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Administrador administrador) {
        Optional<Administrador> administradorOpt = administradorRepository.findByEmail(administrador.getEmail());
        if (administradorOpt.isPresent()) {
            if (administradorOpt.get().getSenha().equals(administrador.getSenha())) {
                String token = jwtUtil.gerarToken(administradorOpt.get().getEmail());

                Map<String, Object> resposta = new HashMap<>();
                resposta.put("mensagem", "Login realizado com sucesso");
                resposta.put("token", token);
                resposta.put("nome", administradorOpt.get().getNome());
                resposta.put("email", administradorOpt.get().getEmail());
                resposta.put("codigo", 200);
                return ResponseEntity.ok(resposta);
            }
        }
        return resposta(401, "Credenciais inválidas");
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Administrador administrador) {
        if (administrador.getNome() == null || administrador.getNome().length() < 3) {
            return resposta(400, "Nome muito curto (mínimo 3 caracteres)");
        }

        if (administrador.getEmail() == null || !administrador.getEmail().matches("^[\\w.-]+@hm\\.com$")) {
            return resposta(400, "Email deve ser do domínio @hm.com");
        }

        if (administrador.getSenha() == null || !isSenhaForte(administrador.getSenha())) {
            return resposta(400, "Senha fraca! Use letras maiúsculas, minúsculas, número e símbolo.");
        }

        if (administrador.getPalavraChave() == null || administrador.getPalavraChave().length() < 4) {
            return resposta(400, "Palavra-chave muito curta (mínimo 4 caracteres)");
        }

        if (administradorRepository.findByEmail(administrador.getEmail()).isPresent()) {
            return resposta(400, "Email já cadastrado");
        }

        administradorRepository.save(administrador);
        return resposta(201, "Administrador registrado com sucesso");
    }

    @PostMapping("/verificar-palavra-chave")
    public ResponseEntity<?> verificarPalavraChave(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String palavraChave = dados.get("palavraChave");

        if (email == null || palavraChave == null) {
            return resposta(400, "Email e palavra-chave são obrigatórios");
        }

        Optional<Administrador> administradorOpt = administradorRepository.findByEmail(email);
        if (administradorOpt.isPresent()) {
            Administrador admin = administradorOpt.get();
            if (palavraChave.trim().equalsIgnoreCase(admin.getPalavraChave().trim())) {
                return resposta(200, "Palavra-chave correta");
            }
        }

        return resposta(401, "Email ou palavra-chave incorretos");
    }

    @PutMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String novaSenha = dados.get("novaSenha");

        if (email == null || novaSenha == null) {
            return resposta(400, "Email e nova senha são obrigatórios");
        }

        Optional<Administrador> administradorOpt = administradorRepository.findByEmail(email);
        if (administradorOpt.isPresent()) {
            if (!isSenhaForte(novaSenha)) {
                return resposta(400, "Senha fraca! Use letras maiúsculas, minúsculas, número e símbolo.");
            }

            Administrador admin = administradorOpt.get();
            admin.setSenha(novaSenha);
            administradorRepository.save(admin);
            return resposta(200, "Senha alterada com sucesso");
        }

        return resposta(404, "Administrador não encontrado");
    }

    private boolean isSenhaForte(String senha) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{6,}$";
        return senha.matches(regex);
    }
}
