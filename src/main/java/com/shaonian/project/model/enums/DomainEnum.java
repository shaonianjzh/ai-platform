package com.shaonian.project.model.enums;

/**
 * @author 少年
 */
public enum DomainEnum {


    /**
     * 指定访问的领域,general指向V1.5版本
     * generalv2指向V2版本。
     * 注意：不同的取值对应的url也不一样！
     */
    GENERAL("general"),
    GENERALV2("generalv2");

    private String name;

    DomainEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
