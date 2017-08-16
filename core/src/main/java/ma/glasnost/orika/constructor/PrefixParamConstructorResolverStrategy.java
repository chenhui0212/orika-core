package ma.glasnost.orika.constructor;

import ma.glasnost.orika.impl.util.StringUtil;

/**
 * Finds constructor with param names that follow a prefix naming convention. For instance
 * p-prefixed param names - (pName, pAge).
 *
 * @author Stanislav Petrov
 */
public class PrefixParamConstructorResolverStrategy extends SimpleConstructorResolverStrategy {

    @Override
    protected String[] mapTargetParamNames(String[] parameterNames) {
        final String[] mappedParamNames = new String[parameterNames.length];
        for (int idx = 0; idx < parameterNames.length; idx++) {
            mappedParamNames[idx] = StringUtil.uncapitalize(parameterNames[idx].substring(1));
        }
        return mappedParamNames;
    }
}
