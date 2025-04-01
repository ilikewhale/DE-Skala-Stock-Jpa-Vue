package com.sk.skala.myapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 플레이어 정보를 저장하는 엔티티 클래스
 * JPA를 사용하여 데이터베이스와 매핑됨
 */
@Data
@Entity
@NoArgsConstructor
public class Player {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 ID 생성 전략 사용
    private Long id; // JPA 엔티티의 기본 키 (내부 관리용)

    @Column(name = "player_id", nullable = false, unique = true) // NULL이 될 수 없고 중복될 수 없음
    private String playerId; // 플레이어 식별 ID (사용자 입력용)

    @Column(name = "player_password")
    private String playerPassword; // 플레이어 식별 패스워드 (사용자 입력용)

    @Column(name = "player_money")
    private int playerMoney; // 플레이어가 보유한 돈


    // PlayerStock과 1:N 관계 설정, 플레이어가 삭제될 때 관련 주식 정보도 함께 삭제됨
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "player_id") // 외래 키 설정
    private List<PlayerStock> playerStocks = new ArrayList<>();

    /**
     * 플레이어 생성자
     * @param playerId 플레이어 ID
     * @param playerPassword  플레이어 비밀번호
     * @param playerMoney 플레이어가 보유한 돈
     */
    public Player(String playerId, String playerPassword, int playerMoney) {
        this.playerId = playerId;
        this.playerPassword = playerPassword;
        this.playerMoney = playerMoney;
    }
}