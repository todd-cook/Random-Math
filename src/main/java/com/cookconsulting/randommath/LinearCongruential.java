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
import java.util.Random;

/**
 * The linear congruential formula. (See Donald Knuth,
 * <i>The Art of Computer Programming, Volume 3</i>, Section 3.2.1.)
 * is used by java.util.Random.
 *
 * @author Todd Cook
 * @since 9/4/11
 */
public class LinearCongruential implements RandomNumberGenerator, Serializable {

    private static final long serialVersionUID = 4044287744212583436L;

    private Random random = new Random();

    @Override
    public void setSeed(long u, long v) {
        random.setSeed(u);
    }

    @Override
    public void setSeed(long u) {
        random.setSeed(u);
    }

    @Override
    public void setSeedFromSystemTime() {
        random.setSeed(System.currentTimeMillis());
    }

    @Override
    public long getLong() {
        return random.nextLong();
    }

    @Override
    public long getUInt() {
        long tmp = Math.abs(random.nextLong());
        if (Long.toBinaryString(tmp).length() > 32) {
            tmp = Long.parseLong(Long.toBinaryString(tmp).substring(0, 31), 2);
        }
        return tmp;
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int limit) {
        return random.nextInt(limit);
    }
}
