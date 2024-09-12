package dev.samueljunnior.module.message.controller;

import dev.samueljunnior.module.message.dto.NewMessageDTO;
import dev.samueljunnior.module.message.service.MessageService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Tag(
        name = "Message",
        description = "Operações de processamento de mensageria.",
        externalDocs = @ExternalDocumentation(url = "https://samueljunnior.github.io/about-me/")
)
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(
            description = "Cria uma nova mensagem para processamento.",
            responses = @ApiResponse(
                    responseCode = "200"
            )
    )
    public ResponseEntity<Void> newMessage(@RequestBody NewMessageDTO dto) {
        messageService.newMessge(dto);
        return ResponseEntity.ok().build();
    }

}
