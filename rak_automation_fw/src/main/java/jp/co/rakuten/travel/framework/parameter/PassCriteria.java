package jp.co.rakuten.travel.framework.parameter;

import jp.co.rakuten.travel.framework.utility.Utility;

import org.apache.log4j.Logger;

public enum PassCriteria
{
    /**
     * Single test base criteria
     */
    TYPE_1,
    /**
     * Iteration Based criteria
     * <br> 80% pass rate
     * <br> 20% fail rate
     * <br> 20% skip rate
     */
    TYPE_2,
    /**
     * Minimum Stability based criteria
     * <br> PASS            -> PASS
     * <br> PASS PASS       -> PASS (removed)
     * <br> FAIL FAIL       -> FAIL
     * <br> SKIP SKIP       -> SKIP
     * <br> FAIL PASS PASS -> PASS
     * <br> SKIP PASS PASS -> PASS
     * <br> FAIL PASS FAIL -> FAIL
     * <br> SKIP PASS SKIP -> SKIP
     * <br> FAIL PASS SKIP -> FAIL
     * <br> SKIP PASS FAIL -> FAIL
     * <br> FAIL SKIP -> FAIL
     * <br> SKIP FAIL -> FAIL
     * <br> this will have a maximum of three (3) iterations
     */
    TYPE_3,

    /**
     * 
     */
    UNKNOWN;

    Logger LOG = Logger.getLogger( PassCriteria.class );

    public static PassCriteria get( String s )
    {
        for( PassCriteria p : values() )
        {
            if( p.name().equalsIgnoreCase( s ) )
            {
                return p;
            }
        }

        return UNKNOWN;
    }

    public Result checkCriteria( float pass, float fail, float skip, float total )
    {
        float totalTest = (pass + fail + skip);
        if( totalTest == 0 )
        {
            LOG.warn( "no partial results yet" );
            return Result.UNKNOWN;
        }

        Result criteriaFlag = Result.UNKNOWN;

        switch( this )
        {
        case TYPE_2:
            if( (pass / total) * 100 >= 80 )
            {
                LOG.info( "PASS Criteria of >=80% met." );
                criteriaFlag = Result.PASS;
            }
            else if( (fail / total) * 100 > 20 )
            {
                LOG.info( "FAIL Criteria of >20% met." );
                criteriaFlag = Result.FAIL;
            }
            else if( (skip / total) * 100 > 20 )
            {
                LOG.info( "SKIP Criteria of >20% met." );
                criteriaFlag = Result.SKIP;
            }
            break;

        case TYPE_3:
            if( Math.abs( pass - totalTest ) < Utility.EPSILON )
            {
                LOG.info( "PASS Criteria of first PASS met" );
                criteriaFlag = Result.PASS;
            }
            else if( fail == 2 || (fail == 1 && skip == 1) )
            {
                LOG.info( "FAIL Criteria of 2 consecutive FAIL/SKIP met" );
                criteriaFlag = Result.FAIL;
            }
            else if( skip == 2 )
            {
                criteriaFlag = Result.SKIP;
            }
            break;

        case TYPE_1:
            if( pass > 0 )
            {
                criteriaFlag = Result.PASS;
            }
            else if( fail > 0 )
            {
                criteriaFlag = Result.FAIL;
            }
            else if( skip > 0 )
            {
                criteriaFlag = Result.SKIP;
            }
            break;

        default:
            break;
        }

        return criteriaFlag;
    }
}
