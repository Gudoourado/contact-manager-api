package com.gustavo.contactmanager.exception;

import com.gustavo.contactmanager.controller.ContactController;
import com.gustavo.contactmanager.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
class GlobalExceptionHandlerTest {

    private static final String CONTATO_VALIDO =
            "{\"name\":\"Ana\",\"email\":\"ana@exemplo.com\",\"phone\":\"71999990000\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    void jsonMalFormadoDevolve400() throws Exception {
        mockMvc.perform(post("/api/contacts").contentType(MediaType.APPLICATION_JSON).content("{\"name\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Corpo da requisição inválido"));
    }

    @Test
    void idComLetraDevolve400() throws Exception {
        mockMvc.perform(get("/api/contacts/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Parâmetro inválido"))
                .andExpect(jsonPath("$.message").value(containsString("'id'")));
    }

    @Test
    void parametroObrigatorioAusenteDevolve400() throws Exception {
        mockMvc.perform(get("/api/contacts/search"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Parâmetro obrigatório ausente"))
                .andExpect(jsonPath("$.message").value(containsString("'keyword'")));
    }

    @Test
    void metodoNaoSuportadoDevolve405() throws Exception {
        mockMvc.perform(patch("/api/contacts"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.error").value("Método não permitido"));
    }

    @Test
    void corpoQueNaoEJsonDevolve415() throws Exception {
        mockMvc.perform(post("/api/contacts").contentType(MediaType.TEXT_PLAIN).content("oi"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.error").value("Tipo de conteúdo não suportado"));
    }

    @Test
    void enderecoInexistenteDevolve404() throws Exception {
        mockMvc.perform(get("/nao-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Endereço não encontrado"));
    }

    @Test
    void contatoInexistenteDevolve404() throws Exception {
        when(contactService.findById(999L)).thenThrow(new ResourceNotFoundException("Contato não encontrado com id: 999"));

        mockMvc.perform(get("/api/contacts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contato não encontrado com id: 999"));
    }

    @Test
    void contatoDuplicadoDevolve409() throws Exception {
        when(contactService.create(any())).thenThrow(new DuplicateResourceException("Email já cadastrado"));

        mockMvc.perform(post("/api/contacts").contentType(MediaType.APPLICATION_JSON).content(CONTATO_VALIDO))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email já cadastrado"));
    }

    @Test
    void camposInvalidosDevolvem400ComCadaCampo() throws Exception {
        mockMvc.perform(post("/api/contacts").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.fields.name").exists())
                .andExpect(jsonPath("$.fields.email").exists());
    }

    @Test
    void erroInesperadoDevolve500SemExporDetalheInterno() throws Exception {
        when(contactService.findAll()).thenThrow(new IllegalStateException("senha do banco: s3gredo"));

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Erro interno do servidor"))
                .andExpect(jsonPath("$.message").value(not(containsString("s3gredo"))));
    }
}
