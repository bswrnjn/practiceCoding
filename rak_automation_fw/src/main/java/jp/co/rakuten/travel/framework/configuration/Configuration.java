package jp.co.rakuten.travel.framework.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import jp.co.rakuten.travel.framework.configuration.Controller.ControllerType;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.Parameters;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.parameter.TestObjects;
import jp.co.rakuten.travel.framework.utility.IniParser;
import jp.co.rakuten.travel.framework.utility.Utility;

/**
 * Singleton Configuration handler to take care of all equipments and controllers
 * <br>Equipments take care of all the connection and API's
 * <br>Controllers take care of the API's for a specific Equipment
 * <br>Equipments have at least one controller, thus controllers are created based solely on equipments
 *
 */
public final class Configuration implements Equipment, ConfigurationI
{
    private Logger                               LOG              = TestLogger.getLogger( this.getClass() );

    public static String                         SUITE_NAME;

    private static Configuration                 s_instance;

    private Map< EquipmentType, Equipment >      m_equipments     = new ConcurrentHashMap<>();
    private Map< ControllerType, Controller >    m_controllers    = new ConcurrentHashMap<>();

    private Map< String, Object >                m_parameters     = new HashMap<>();
    private Set< Class< ? extends Parameters > > m_testParameters = new ConcurrentHashSet<>();

    private ConfigurationIni                     m_ini;

    public static String                         CONFIG           = "configuration";

    private Configuration()
    {}

    public static Configuration instance()
    {
        if( s_instance == null )
        {
            s_instance = new Configuration();
        }
        return s_instance;
    }

    boolean hasEquipment( EquipmentType type )
    {
        return m_equipments.containsKey( type );
    }

    boolean hasController( ControllerType type )
    {
        return m_controllers.containsKey( type );
    }

    @Override
    public final Equipment equipment( EquipmentType type )
    {
        return m_equipments.get( type );
    }

    @Override
    public final Controller controller( ControllerType type )
    {
        return m_controllers.get( type );
    }

    @Override
    public void init()
    {
        LOG.info( "init" );

        if( !initEquipments() )
        {
            return;
        }
        if( !initControllers() )
        {
            return;
        }
        if( !initConfigIni() )
        {
            return;
        }
    }

    private boolean initEquipments()
    {
        if( StringUtils.isEmpty( TestApiObject.instance().get( TestApiParameters.API_EQUIPMENTS ) ) )
        {
            LOG.warn( "No equipment specified" );
            return false;
        }

        String [] configs = TestApiObject.instance().get( TestApiParameters.API_EQUIPMENTS ).split( "," );
        for( String config : configs )
        {
            EquipmentType type = Utility.getEnum( config.trim(), EquipmentType.class );
            if( type.equals( EquipmentType.UNKNOWN ) )
            {
                continue;
            }
            Equipment equipment = ConfigurationFactory.equipment( type );

            equipment.init();
            m_equipments.put( type, equipment );
        }

        return true;
    }

    private boolean initControllers()
    {
        if( StringUtils.isEmpty( TestApiObject.instance().get( TestApiParameters.API_CONTROLLERS ) ) )
        {
            LOG.warn( "No controller specified" );
            return false;
        }

        String [] configs = TestApiObject.instance().get( TestApiParameters.API_CONTROLLERS ).split( "," );
        for( String config : configs )
        {
            ControllerType type = Utility.getEnum( config.trim(), ControllerType.class );
            if( type.equals( ControllerType.UNKNOWN ) )
            {
                continue;
            }
            Controller controller = ConfigurationFactory.controller( type );

            m_controllers.put( type, controller );
        }

        return true;
    }

    private boolean initConfigIni()
    {
        m_ini = new ConfigurationIni()
        {
            IniParser m_ini;

            @Override
            public void load()
            {
                InputStream m_iniFileStream = Configuration.class.getClassLoader().getResourceAsStream( TestApiObject.instance().get( TestApiParameters.API_INI_PATH ) );
                if( m_iniFileStream == null )
                {
                    LOG.error( "Error occurred while reading ini file." );
                }
                try
                {
                    m_ini = new IniParser( m_iniFileStream );
                }
                catch( IOException exception )
                {
                    LOG.error( "Error occurred while loading ini file with " + exception );
                }
            }

            @Override
            public IniParser getIniParser()
            {
                return this.m_ini;
            }
        };

        m_ini.load();

        return true;
    }

    @Override
    public void release()
    {
        LOG.info( "release" );
        for( Equipment equipment : m_equipments.values() )
        {
            equipment.release();
        }
    }

    @Override
    public void recover()
    {
        LOG.info( "recover" );
        for( Equipment equipment : m_equipments.values() )
        {
            equipment.recover();
        }
    }

    @Override
    public void refresh()
    {
        LOG.info( "refresh" );
        for( Equipment equipment : m_equipments.values() )
        {
            equipment.refresh();
        }
    }

    @Override
    public void errorInfo()
    {
        LOG.info( "errorInfo" );
        for( Equipment equipment : m_equipments.values() )
        {
            equipment.errorInfo();
        }
    }

    /**
     * Test Object related methods
     */

    /**
     * add parameters for test object usage
     * @param paramMap
     */
    @Override
    public void add( Map< String, Object > paramMap )
    {
        m_parameters.putAll( paramMap );
    }

    @Override
    public void clear()
    {
        m_parameters.clear();
    }

    @Override
    public < T extends TestObjects > T generateTestObject( Class< T > clazz, Set< Class< ? extends Parameters > > set ) throws InstantiationException
    {
        // this needs to be exactly the same as clazz constructor
        Map< Parameters, Object > testMap = new HashMap<>();

        m_testParameters.addAll( set );

        for( Class< ? extends Parameters > enumClass : m_testParameters )
        {
            for( Parameters param : enumClass.getEnumConstants() )
            {
                // default values
                testMap.put( param, param.val() );

                // parameter values
                if( m_parameters.containsKey( param.toString().toLowerCase() ) )
                {
                    LOG.debug( param.toString() + " : " + m_parameters.get( param.toString().toLowerCase() ) );
                    testMap.put( param, m_parameters.get( param.toString().toLowerCase() ) );
                }
            }
        }

        Object obj = null;
        try
        {
            Constructor< ? > ctor = clazz.getConstructor( Map.class );
            obj = ctor.newInstance( new Object []
            { testMap } );
        }
        catch( NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e )
        {
            String msg = "Can not instantiate " + clazz.getName() + " due to " + e.getClass().getSimpleName();
            LOG.error( msg, e );
            throw new InstantiationException( msg );
        }

        return clazz.cast( obj );
    }

    @Override
    public < T extends Parameters > void addTestParameters( Class< T > clazz )
    {
        m_testParameters.add( clazz );
    }

    public final ConfigurationIni getIni()
    {
        if( m_ini.getIniParser() == null )
        {
            m_ini.load();
        }
        return m_ini;
    }
}
