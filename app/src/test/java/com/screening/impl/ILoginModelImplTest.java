package com.screening.impl;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dell on 2018/4/24.
 */
public class ILoginModelImplTest {
    ILoginModelImpl iLoginPresenter = new ILoginModelImpl();

    @Test
    public void add() throws Exception {
        int add = iLoginPresenter.add(1, 2);
        Assert.assertEquals(add,3);


    }

    @Test
    public void add1() throws Exception {
        int add = iLoginPresenter.add(1, 2);
        Assert.assertEquals(add,4);


    }

}