package yayauheny.entity;

import java.time.LocalDateTime;

public record Receipt(Account account,
                      Player player,
                      LocalDateTime from,
                      LocalDateTime to) {
}
