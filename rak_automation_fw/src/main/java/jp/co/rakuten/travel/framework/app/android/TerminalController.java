package jp.co.rakuten.travel.framework.app.android;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.Connection;
import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Controller;
import jp.co.rakuten.travel.framework.page.AndroidController;

/**
 * Handle phone settings by driver
 */
// FIXME TerminalController = AndroidController, design by interface
public class TerminalController implements Controller
{
    private AndroidController m_android = (AndroidController)Configuration.instance().controller( ControllerType.ANDROID );

    public void setDataConnection()
    {
        ((AndroidDriver< ? >)m_android.driver()).setConnection( Connection.AIRPLANE );
        ((AndroidDriver< ? >)m_android.driver()).setConnection( Connection.DATA );
    }

    public void enter()
    {
        ((AndroidDriver< ? >)m_android.driver()).pressKeyCode( AndroidKeyCode.ENTER );
    }

    /**
     * Set the app in background for 1 minute
     */
    public void reopenApp( int seconds )
    {
        ((AndroidDriver< ? >)m_android.driver()).runAppInBackground( seconds );
    }
}
