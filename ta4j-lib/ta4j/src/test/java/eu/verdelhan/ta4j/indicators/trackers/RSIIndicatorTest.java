/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.indicators.trackers;

import eu.verdelhan.ta4j.Decimal;
import static eu.verdelhan.ta4j.TATestsUtils.assertDecimalEquals;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RSIIndicatorTest {

    private TimeSeries data;

    @Before
    public void setUp() {
        data = new MockTimeSeries(
                50.45, 50.30, 50.20,
                50.15, 50.05, 50.06,
                50.10, 50.08, 50.03,
                50.07, 50.01, 50.14,
                50.22, 50.43, 50.50,
                50.56, 50.52, 50.70,
                50.55, 50.62, 50.90,
                50.82, 50.86, 51.20, 51.30, 51.10);
    }

    @Test
    public void RSIUsingTimeFrame14UsingClosePrice() {
        RSIIndicator rsi = new RSIIndicator(new ClosePriceIndicator(data), 14);

        assertDecimalEquals(rsi.getValue(15), 62.7451);
        assertDecimalEquals(rsi.getValue(16), 66.6667);
        assertDecimalEquals(rsi.getValue(17), 75.2294);
        assertDecimalEquals(rsi.getValue(18), 71.9298);
        assertDecimalEquals(rsi.getValue(19), 73.3333);
        assertDecimalEquals(rsi.getValue(20), 77.7778);
        assertDecimalEquals(rsi.getValue(21), 74.6667);
        assertDecimalEquals(rsi.getValue(22), 77.8523);
        assertDecimalEquals(rsi.getValue(23), 81.5642);
        assertDecimalEquals(rsi.getValue(24), 85.2459);
    }

    @Test
    public void RSIFirstValueShouldBeZero() {
        RSIIndicator rsi = new RSIIndicator(new ClosePriceIndicator(data), 14);

        assertEquals(Decimal.ZERO, rsi.getValue(0));
    }
}
