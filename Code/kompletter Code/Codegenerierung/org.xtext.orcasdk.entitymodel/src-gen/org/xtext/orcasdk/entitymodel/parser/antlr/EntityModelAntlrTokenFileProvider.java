/*
* generated by Xtext
*/
package org.xtext.orcasdk.entitymodel.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class EntityModelAntlrTokenFileProvider implements IAntlrTokenFileProvider {
	
	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
    	return classLoader.getResourceAsStream("org/xtext/orcasdk/entitymodel/parser/antlr/internal/InternalEntityModel.tokens");
	}
}