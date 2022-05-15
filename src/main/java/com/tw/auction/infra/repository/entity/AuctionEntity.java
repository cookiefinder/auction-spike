package com.tw.auction.infra.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "auction")
@NoArgsConstructor
@AllArgsConstructor
public class AuctionEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String CN;
    private String name;
    private String bailmentStatus;
}
