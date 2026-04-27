package org.zerock.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder // 상속해서 검색 조건 추가 가능하도록
@AllArgsConstructor
@NoArgsConstructor
public class PageReqeusetDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}
