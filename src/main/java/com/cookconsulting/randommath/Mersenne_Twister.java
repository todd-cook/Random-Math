/*
 * Copyright (c) 2011, Todd Cook.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the <ORGANIZATION> nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.cookconsulting.randommath;

import java.io.Serializable;

/**
 * Wrapper class for Mersenne_Twister
 *
 * @author todd
 * @since 9/6/11 10:03 PM
 */
public class Mersenne_Twister implements RandomNumberGenerator, Serializable {

    private static final long serialVersionUID = 2677482389850748000L;

    private ec.util.MersenneTwister mersenneTwister = new ec.util.MersenneTwister();

    public void setSeed(long u, long v) {
        setSeed(u);
    }

    public void setSeed(long u) {
        mersenneTwister.setSeed(u);
    }

    public void setSeedFromSystemTime() {
        mersenneTwister.setSeed(System.currentTimeMillis());
    }

    public long getLong() {
        return mersenneTwister.nextLong();
    }

    public long getUInt() {
        return Math.abs(mersenneTwister.nextLong(UInt.MAX_VALUE));
    }

    @Override
    public int nextInt() {
        return mersenneTwister.nextInt();
    }

    @Override
    public int nextInt(int limit) {
        return mersenneTwister.nextInt(limit);
    }

}
