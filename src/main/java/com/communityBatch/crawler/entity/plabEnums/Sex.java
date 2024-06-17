package com.communityBatch.crawler.entity.plabEnums;

public enum Sex {
    
    BOTH(0),
    MAN(1),
    WOMAN(-1);

    private final int value;

    Sex(int value){
        this.value = value;
    }
}
