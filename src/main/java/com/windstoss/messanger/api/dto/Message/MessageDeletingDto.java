package com.windstoss.messanger.api.dto.Message;

import lombok.Data;

@Data
public class MessageDeletingDto {
    private boolean withText;
    private boolean withFile;
}
