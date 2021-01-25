package jp.co.rakuten.travel.framework.configuration;

import java.util.Map;
import java.util.Set;

import jp.co.rakuten.travel.framework.configuration.Controller.ControllerType;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.parameter.Parameters;
import jp.co.rakuten.travel.framework.parameter.TestObjects;

public interface ConfigurationI
{
    /**
     * 
     * @param type {@link EquipmentType}
     * @return {@link EquipmentType} of the specified type. If the equipment is not initialized then this will return <code>null</code>
     */
    Equipment equipment( EquipmentType type );

    /**
     * 
     * @param type {@link ControllerType}
     * @return {@link ControllerType}of the specified type. If the controller is not initialized then this will return <code>null</code>
     */
    Controller controller( ControllerType type );

    // FIXME think of better way to handle parameters, currently serviceparameters is already taken by service objects

    /**
     * Holds the raw map of parameters from suite and test input
     * <br> Adds or updates the raw map
     * @param map
     */
    void add( Map< String, Object > paramMap );

    /**
     * Holds the raw map of parameters from suite and test input
     * <br> Clears the map
     * @param map
     */
    void clear();

    /**
     * Generates a new test object based on the already set parameters in configuration
     * @param clazz implementation class based on {@link TestObjects}
     * @param set set of {@link Parameters} in {@link Enum} designated form. Overrides the previously set keys
     * @return new instance of {@link TestObjects}
     * @throws InstantiationException
     */
    < T extends TestObjects > T generateTestObject( Class< T > clazz, Set< Class< ? extends Parameters > > set ) throws InstantiationException;

    /**
     * Adds a parameter to the set which will be used for generating test objects
     * @param clazz {@link Parameters} class to set 
     */
    < T extends Parameters > void addTestParameters( Class< T > clazz );

}
