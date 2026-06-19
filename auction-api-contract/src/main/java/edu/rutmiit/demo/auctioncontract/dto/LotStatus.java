package edu.rutmiit.demo.auctioncontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус лота в системе")
public enum LotStatus {
    @Schema(description = "Ожидает проверки службой безопасности")
    PENDING_VERIFICATION,

    @Schema(description = "Активен, идут торги")
    ACTIVE,

    @Schema(description = "Продан (торги завершены, есть победитель)")
    SOLD,

    @Schema(description = "Торги завершены, лот не продан (никто не сделал ставку)")
    EXPIRED,

    @Schema(description = "Отменен продавцом или модератором")
    CANCELLED
}