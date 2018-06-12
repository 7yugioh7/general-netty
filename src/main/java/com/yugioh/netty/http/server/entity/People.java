package com.yugioh.netty.http.server.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Create By lieber
 * @Description
 * @Date Create in 2018/6/12 14:44
 * @Modify By
 */
@Data
public class People implements Serializable {

    private int age;

    private String name;

    private float weight;

    private float height;

}
