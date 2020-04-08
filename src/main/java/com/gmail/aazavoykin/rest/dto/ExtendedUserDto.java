package com.gmail.aazavoykin.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
public class ExtendedUserDto extends UserDto {

    private LocalDateTime registered;
    private boolean enabled;
}
