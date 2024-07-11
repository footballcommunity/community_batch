package com.communityBatch.crawler.entity.plabEnums;

public enum Sex {
    
    남녀모두(0),
    남자(1),
    여자(-1);

    private final int value;

    Sex(int value){
        this.value = value;
    }
}
