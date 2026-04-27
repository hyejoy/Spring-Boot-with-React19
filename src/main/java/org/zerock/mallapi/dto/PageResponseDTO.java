package org.zerock.mallapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// @Data 선언된경우 getter, setter 전부 알아서 만들어줌
@Data
public class PageResponseDTO<E> {

    private List<E> dtoList;

    private List<Integer> pageNumList;

    private PageReqeusetDTO pageReqeusetDTO;

    private boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageReqeusetDTO pageReqeusetDTO, long totalCount) {
        this.dtoList = dtoList;
        this.pageReqeusetDTO = pageReqeusetDTO;
        this.totalCount = (int)totalCount;

        int end = (int)(Math.ceil(pageReqeusetDTO.getPage() / 10.0 )) * 10;
        int start = end-9;
        int last = (int)(Math.ceil((totalCount/(double)pageReqeusetDTO.getSize())));
        end = end > last ? last : end;
        this.prev = start > 1 ;
        this.next = totalCount > end * pageReqeusetDTO.getSize();

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        if(prev)
            this.prevPage = start - 1;
        if(next)
            this.nextPage = end +1 ;

        this.totalCount = this.pageNumList.size();
        this.current = pageReqeusetDTO.getPage();

    }
}
