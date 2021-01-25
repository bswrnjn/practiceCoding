package jp.co.rakuten.travel.framework.property.pageaction.singularity;

public interface Initialized extends WebPageAction
{
	/**
     * To check if all settings are default value and if all options are present in a web page.
     * <br>Checking string values in edit boxes, presence of buttons, cheking choices; etc.
     * @return TRUE if all parameters are confirmed correct and complete, FALSE otherwise
     */
    boolean checkDefaults();
}
