package org.abigotado.links.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    private UUID id;
    private String longLink;
    private String shortLink;
    private UUID userId;
    private int clicksLeft;
    private LocalDateTime expirationDate;
}
