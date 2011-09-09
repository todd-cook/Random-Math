package com.cookconsulting.randommath;

/**
 * Enum for mapping Random Number Generations algorithms to their class implementations
 *
 * @author Todd Cook
 * @since 9/4/11
 */
public enum RandomNumberAlgorithm {

    MULTIPLY_WITH_CARRY(MultiplyWithCarry.class),
    MERSENNE_TWISTER(Mersenne_Twister.class),
    LINEAR_CONGRUENTIAL(LinearCongruential.class);

    private Class clazz;

    RandomNumberAlgorithm(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

}
