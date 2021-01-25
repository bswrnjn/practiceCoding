package jp.co.rakuten.travel.framework.listeners;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.sun.management.OperatingSystemMXBean;

import jp.co.rakuten.travel.framework.logger.TestLogger;

@SuppressWarnings( "restriction" )
public class ProfilerListener implements ITestListeners
{
    protected Logger    LOG = TestLogger.getLogger( this.getClass() );

    protected final Jvm m_jvm;

    /**
     * 
     */
    public ProfilerListener()
    {
        m_jvm = new Jvm( Jvm.MB );
    }

    /**
     * 
     */
    @Override
    public void onExecutionFinish()
    {

    }

    /**
     * 
     */
    @Override
    public void onExecutionStart()
    {

    }

    /**
     * 
     */
    @Override
    public void onFinish( ISuite arg0 )
    {
        LOG.info( m_jvm.getJvmStats() );
    }

    /**
     * 
     */
    @Override
    public void onStart( ISuite arg0 )
    {
        LOG.info( m_jvm.getJvmStats() );
    }

    /**
     * 
     */
    @Override
    public void onFinish( ITestContext arg0 )
    {

    }

    /**
     * 
     */
    @Override
    public void onStart( ITestContext arg0 )
    {

    }

    /**
     * 
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage( ITestResult arg0 )
    {
        LOG.info( m_jvm.getJvmStats() );
    }

    /**
     * 
     */
    @Override
    public void onTestFailure( ITestResult arg0 )
    {
        LOG.info( m_jvm.getJvmStats() );
    }

    /**
     * 
     */
    @Override
    public void onTestSkipped( ITestResult arg0 )
    {
        LOG.info( m_jvm.getJvmStats() );
    }

    /**
     * 
     */
    @Override
    public void onTestStart( ITestResult arg0 )
    {

    }

    /**
     * 
     */
    @Override
    public void onTestSuccess( ITestResult arg0 )
    {
        LOG.info( m_jvm.getJvmStats() );
    }

    private class Jvm
    {
        private static final int MB       = 1024 * 1024;
        private static final int KB       = 1024;
        private static final int BYTE     = 1;

        // Getting the runtime reference from system
        private Runtime          runtime;
        private int              unit;
        private String           unitName = "";

        /**
         * set instance of runtime object and set unit
         * @param unit
         */
        public Jvm( int unit )
        {
            runtime = Runtime.getRuntime();
            this.unit = unit;

            switch( unit )
            {
            case MB:
                unitName = unitName.concat( " MB " );
                break;
            case KB:
                unitName = unitName.concat( " KB " );
                break;
            case BYTE:
            default:
                unitName = unitName.concat( " Bytes " );
                break;
            }
        }

        /**
         * ############ Jvm Memory/CPU utilization statistics ############
         * Current system load :0.0 %
         * Total Memory in JVM :220 MB 
         * Max Memory [JVM attempt to use] :1812 MB 
         * Used Memory in JVM :132 MB 
         * Free Memory in JVM :87 MB 
         * ###############################################################
         * @return
         */
        public String getJvmStats()
        {
            return new StringBuilder()//
                    .append( System.lineSeparator() )//
                    .append( m_jvm.getHeader() )//
                    .append( System.lineSeparator() )//
                    .append( m_jvm.getSystemCpuLoad() )//
                    .append( System.lineSeparator() )//
                    .append( m_jvm.getTotalMemory() )//
                    .append( System.lineSeparator() )//
                    .append( m_jvm.getMaxMemory() )//
                    .append( System.lineSeparator() )//
                    .append( m_jvm.getUsedMemory() )//
                    .append( System.lineSeparator() )//
                    .append( m_jvm.getFreeMemory() )//
                    .append( System.lineSeparator() )//
                    .append( m_jvm.getFooter() )//
                    .toString();//
        }

        /**
         * returns jvm stat report header
         * @return
         */
        private String getHeader()
        {
            return "############ Jvm Memory/CPU utilization statistics ############";
        }

        /**
         * getTotalMemory() - getFreeMemory()
         * @return
         */
        private String getUsedMemory()
        {
            return "Used Memory in JVM : " + ( (runtime.totalMemory() - runtime.freeMemory()) / unit) + unitName;
        }

        /**
         * Returns the amount of free memory in the Java Virtual Machine.<br>
         * Calling the gc method may result in increasing the value returned by freeMemory.
         * @return
         */
        private String getFreeMemory()
        {
            return "Free Memory in JVM : " + (runtime.freeMemory() / unit) + unitName;
        }

        /**
         * Returns the total amount of memory in the Java virtual machine.<br> 
         * The value returned by this method may vary over time, depending on the host environment.
         * @return
         */
        private String getTotalMemory()
        {
            return "Total Memory in JVM : " + (runtime.totalMemory() / unit) + unitName;
        }

        /**
         * Returns the maximum amount of memory that the Java virtual machine will attempt to use.<br>
         * If there is no inherent limit then the value Long.MAX_VALUE will be returned.
         * @return
         */
        private String getMaxMemory()
        {
            return "Max Memory [JVM attempt to use] : " + (runtime.maxMemory() / unit) + unitName;
        }

        /**
         * Returns the "recent cpu usage" for the whole system. This value is a double in the [0.0,1.0] interval.<br>
         * A value of 0.0 means that all CPUs were idle during the recent period of time observed,<br> 
         * while a value of 1.0 means that all CPUs were actively running 100% of the time during the recent period being observed.<br>
         * All values betweens 0.0 and 1.0 are possible depending of the activities going on in the system. If the system recent cpu usage is not available, the method returns a negative value.
         * @return
         */
        private String getSystemCpuLoad()
        {
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
            double load = operatingSystemMXBean.getSystemCpuLoad() * 100;
            DecimalFormat numberFormat = new DecimalFormat( "0.00" );
            return "Current system load : " + numberFormat.format( load ) + " %";
        }

        /**
         * returns jvm stat report footer
         * @return
         */
        private String getFooter()
        {
            return "###############################################################";
        }
    }

}
