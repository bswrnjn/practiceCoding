package jp.co.rakuten.travel.framework.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class TestTransformer implements IAnnotationTransformer
{
    @Override
    public void transform( ITestAnnotation annotation, @SuppressWarnings( "rawtypes" ) Class testClass, @SuppressWarnings( "rawtypes" ) Constructor testConstructor, Method testMethod )
    {
        // String method = testMethod.getName();
        // if( "test".equals( testMethod.getName() ) )
        // {
        // annotation.setInvocationCount( Integer.valueOf( System.getProperty( "api_iteration" ) ) );
        // }
    }
}
