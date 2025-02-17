/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.julia;

import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.lang.LanguagePropertyBundle;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.impl.CpdOnlyLanguageModuleBase;
import net.sourceforge.pmd.lang.julia.cpd.JuliaTokenizer;

/**
 * Language implementation for Julia.
 */
public class JuliaLanguageModule extends CpdOnlyLanguageModuleBase {
    private static final String ID = "julia";

    /**
     * Creates a new Julia Language instance.
     */
    public JuliaLanguageModule() {
        super(LanguageMetadata.withId(ID).name("Julia").extensions("jl"));
    }

    public static JuliaLanguageModule getInstance() {
        return (JuliaLanguageModule) LanguageRegistry.CPD.getLanguageById(ID);
    }

    @Override
    public Tokenizer createCpdTokenizer(LanguagePropertyBundle bundle) {
        return new JuliaTokenizer();
    }
}
