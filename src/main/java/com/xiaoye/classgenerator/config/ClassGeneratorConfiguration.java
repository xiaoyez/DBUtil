package com.xiaoye.classgenerator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xiaoye
 * @create 2021-03-16 1:40
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClassGeneratorConfiguration {

    private boolean noArgsConstructor = true;
    private boolean allArgsConstructor = false;
    private boolean getter = true;
    private boolean setter = true;
    private boolean toString = true;


}
