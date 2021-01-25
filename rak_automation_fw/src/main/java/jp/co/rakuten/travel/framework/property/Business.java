package jp.co.rakuten.travel.framework.property;

import java.util.Date;
import java.util.Map;

import jp.co.rakuten.travel.framework.parameter.TestObjects;

/**
 * Handles business logic and to be implemented individually by each service
 *
 */
public interface Business< T extends TestObjects >
{

    int totalPrice();

    int cancellationFee();

    int cancellationFeePerDay( Date date );

    int totalPriceBeforeTax();

    int totalPriceAfterTax();

    int pointsEarned( int deductions );

    int totalOptionPrice();

    Map< Integer, Integer > cancellationPolicy();

}
