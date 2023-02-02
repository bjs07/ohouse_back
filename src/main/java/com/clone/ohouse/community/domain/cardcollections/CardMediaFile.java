package com.clone.ohouse.community.domain.cardcollections;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class CardMediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_media_file_id")
    private Long id;

    @Column
    private String keyName;
    @Column(length = 512)
    private String s3Url;

    @OneToOne(mappedBy = "cardMediaFile", fetch = FetchType.LAZY)
    private CardContent cardContent;

    public CardMediaFile(String keyName, String s3Url) {
        this.keyName = keyName;
        this.s3Url = s3Url;
    }

    public void update(String keyName, String s3Url){
        if(keyName != null) this.keyName = keyName;
        if(s3Url != null) this.s3Url = s3Url;
    }
}
