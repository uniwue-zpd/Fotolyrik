package de.uniwue.dachs.fotolyrik_backend.utils.helper;

import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class PhotopoemHighlightPicker {
    /**
     * Generates a seed based on the current year and month.
     * @return A long seed value derived from the current year and month, like "202603"
     */
    private long getMonthlySeed() {
        YearMonth currentYearMonth = YearMonth.now();
        return currentYearMonth.getYear() * 100L + currentYearMonth.getMonthValue();
    }

    /**
     * Calculates a monthly index for highlighting a photopoem based on the current month seed.
     * The index is derived from a hash value, ensuring that the output value changes each month and is never outside the bounds of the available photopoems.
     * @param size The total number of photopoems available, used to ensure the index is within bounds.
     * @return An integer index between 0 and size-1, representing the photopoem to be highlighted for the current month.
     */
    public int calculateMonthlyIndex(int size) {
        long seed = getMonthlySeed();

        long hash = Long.rotateLeft(seed * 0x9E3779B97F4A7C15L, 17);
        return (int) (Math.abs(hash) % size);
    }
}
