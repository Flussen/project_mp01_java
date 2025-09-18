package com.modding.mp.domain.port.out;

import java.time.Instant;

public interface IClockPort {
    Instant now();
 }
