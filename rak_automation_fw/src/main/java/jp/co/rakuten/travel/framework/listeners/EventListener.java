package jp.co.rakuten.travel.framework.listeners;

/**
 * Interface to be used for call backs and events
 *
 */
public interface EventListener
{
    void callback( EventType type );

    void callback();

    void callback( Object ... objs );

    boolean validate();

    String CALLBACKS      = "callbacks";
    String CALLBACK_TYPE  = "callback_type";
    String CALLBACK_CLASS = "callback_class";
}
