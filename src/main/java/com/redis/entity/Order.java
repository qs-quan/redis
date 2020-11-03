package com.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-03 9:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    private String id;

    private String remark;
}
