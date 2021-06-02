package com.khanhpham.api;

public interface ISpeedUpgrade extends IUpgradeable {
    byte tier();

    /**
     * @return the tick speed tick
     */
    int speedEffect();
}
