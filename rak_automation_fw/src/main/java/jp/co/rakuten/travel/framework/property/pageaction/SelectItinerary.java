package jp.co.rakuten.travel.framework.property.pageaction;

import jp.co.rakuten.travel.framework.parameter.Parameters;
import jp.co.rakuten.travel.framework.property.OptionElement;
import jp.co.rakuten.travel.framework.property.ServiceOutputParameters;
import jp.co.rakuten.travel.framework.property.pageaction.singularity.Optionable;
import jp.co.rakuten.travel.framework.property.pageaction.singularity.Submittable;

public interface SelectItinerary< S extends Parameters, T extends ServiceOutputParameters< S >, U extends OptionElement > extends
        MultipleOutput< S, T >, Submittable, Optionable< U >
{

}
