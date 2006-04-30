/***********************************************************************
 * Copyright (c) 1999-2006 The Apache Software Foundation.             *
 * All rights reserved.                                                *
 * ------------------------------------------------------------------- *
 * Licensed under the Apache License, Version 2.0 (the "License"); you *
 * may not use this file except in compliance with the License. You    *
 * may obtain a copy of the License at:                                *
 *                                                                     *
 *     http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                     *
 * Unless required by applicable law or agreed to in writing, software *
 * distributed under the License is distributed on an "AS IS" BASIS,   *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or     *
 * implied.  See the License for the specific language governing       *
 * permissions and limitations under the License.                      *
 ***********************************************************************/

package org.apache.spf.modifier;

import org.apache.spf.MacroExpand;
import org.apache.spf.SPF1Data;
import org.apache.spf.SPF1Parser;

/**
 * This class represent the exp modifier
 * 
 * @author Norman Maurer <nm@byteaction.de>
 * 
 */
public class ExpModifier extends GenericModifier {


    /**
     * ABNF: "exp"
     */
    public static final String NAME_REGEX = "[eE][xX][pP]";

    /**
     * ABNF: domain-spec
     */
    public static final String VALUE_REGEX = "\\=" + SPF1Parser.DOMAIN_SPEC_REGEX;

    /**
     * ABNF: explanation = "exp" "=" domain-spec
     */
    public static final String REGEX = NAME_REGEX + VALUE_REGEX;
    

    /**
     * Generate the explanation and set it in SPF1Data so it can be accessed
     * easy later if needed
     * 
     * @param spfData
     *            The SPF1Data which should used
     * 
     */
    public String run(SPF1Data spfData) {
        String exp = null;
        String host = this.host;
        try {
            host = new MacroExpand(spfData).expandDomain(host);
            exp = spfData.getDnsProbe().getTxtCatType(host);

        } catch (Exception e) {
        }

        if ((exp == null) || (exp.equals(""))) {
            spfData.setExplanation(spfData.getDefaultExplanation());
        } else {
            try {
                spfData.setExplanation(new MacroExpand(spfData)
                        .expandExplanation(exp));
            } catch (Exception e) {
                spfData.setExplanation("");
            }
        }
        return null;

    }

    /**
     * @see org.apache.spf.modifier.Modifier#enforceSingleInstance()
     */
    public boolean enforceSingleInstance() {
        return true;
    }

}