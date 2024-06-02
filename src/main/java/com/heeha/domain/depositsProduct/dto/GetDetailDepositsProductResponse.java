package com.heeha.domain.depositsProduct.dto;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor()
@ToString
@Builder
public class GetDetailDepositsProductResponse {
    private Long id;
    private String type;
    private String finPrdtNm; // 금융상품명
    private String mtrtInt; // 만기 후 이자율
    private String spclCnd; // 우대조건
    private String joinDeny; // 가입제한
    private String joinMember; // 가입대상
    private String etcNote; // 기타 유의사항
    private String maxLimit; // 최고한도
    private String promotionalText; // 홍보문구
    private String explanatoryText; // 설명문구
}
